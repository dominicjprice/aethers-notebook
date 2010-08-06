package horizon.aether.sensors;

import horizon.aether.utilities.FileUtils;
import horizon.aether.utilities.PrefsUtils;
import horizon.aether.utilities.PropertiesUtils;
import horizon.android.logging.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Service that takes care of the uploading of the archived log files.
 */
public class UploadingService 
extends Service {

    private static final Logger logger = Logger.getLogger(UploadingService.class);
    
    public static final int UPLOAD_WHEN_USER_SAYS = 0;
    public static final int UPLOAD_WHEN_WE_HAVE_FILE = 1;
    public static final int UPLOAD_WHEN_REACHING_MAX = 2;
    
    public static final int UPLOAD_USING_ANY_CONNECTION = 0;
    public static final int UPLOAD_USING_WIFI_ONLY = 1;
    
    public static final String UPLOADING_SERVICE_USER_CLICK_ACTION = "UPLOADING_SERVICE_USER_CLICK_ACTION";
    public static final String UPLOADING_SERVICE_ARCHIVE_ACTION = "UPLOADING_SERVICE_ARCHIVE_ACTION";
    public static final String UPLOADING_SERVICE_CONNECTIVITY_RECEIVER_ACTION = "UPLOADING_SERVICE_CONNECTIVITY_RECEIVER_ACTION";
        
    /**
     * Returns the communication channel to the service. This is always null as 
     * the UploadingService does not expose any methods to be invoked by other services.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new UploadingServiceStub();
    }
    
    /**
     * Called by the system when the service is created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        logger.verbose("UploadingService.onCreate()");
    }
    
    /**
     * Called by the system when the service is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.verbose("UploadingService.onDestroy()");
    }
    
    /**
     * Called by the system when the service is started. It creates
     * a new thread that makes web requests to upload the archived log files.
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        logger.verbose("UploadingService.onStart(" + intent + ", " + startId + ")");

        checkAndUpload(intent.getAction());
    }
    
    /**
     * Class that implements the functionality required by the IUploadingService 
     * interface defined in IUploadingService.aidl.
     */
    public class UploadingServiceStub extends IUploadingService.Stub {
        public boolean uploadFile(String filePath, String serverUrl) throws RemoteException {
            return FileUtils.uploadFile(filePath, serverUrl);    
        }
    }

    private void checkAndUpload(String action) {
        boolean gotFiles = checkForArchives();
        boolean gotConnection = ConnectivityReceiver.checkConnection(this);        
        
        if (action.equals(UPLOADING_SERVICE_USER_CLICK_ACTION)) {
            if (gotFiles && gotConnection) {
                startUploadingThread();
            }
            else {
                String text = "";
                if (!gotFiles)
                    text = "No archived files to upload.";
                if (!gotConnection)
                    text = "No connection.";
                Toast.makeText(this, "Failed to upload archives. " + text, Toast.LENGTH_SHORT).show();
            }
        }
        else if (action.equals(UPLOADING_SERVICE_ARCHIVE_ACTION)
                || action.equals(UPLOADING_SERVICE_CONNECTIVITY_RECEIVER_ACTION)) {
            int uploadTime = PrefsUtils.getUploadTimes(this); 
            if (uploadTime == UPLOAD_WHEN_WE_HAVE_FILE && gotFiles && gotConnection) {
                startUploadingThread();
            }
            else if (uploadTime == UPLOAD_WHEN_REACHING_MAX && gotConnection) {
                // reaching max?
                File dir = new File(PrefsUtils.getLogsDirPath(this));
                long maxTotalCapacity = PrefsUtils.getMaxTotalCapacity(this);
                long maxFileSize = PrefsUtils.getMaxFileSize(this);
                synchronized (LockManager.logDirLock) {
                    long dirSize = FileUtils.getDirSize(dir);
                    if (dirSize + maxFileSize * 1.5 >= maxTotalCapacity) {
                        // yes, upload
                        startUploadingThread();
                    }
                }
            }
        }
    }
    
    private void startUploadingThread() {
        logger.verbose("UploadingService.startUploadingThread()");
        Runnable uploadArchivesRunnable = new Runnable() {
            @Override
            public void run() {
                
                // grab files and sort by last modified (ascending)
                File dir = new File(PrefsUtils.getArchivesDir(getBaseContext()));

                synchronized(LockManager.logArchivesDirLock) {
                    if (dir.exists()) {
                        File[] files = dir.listFiles();
                        if (files.length > 0) {
                            Arrays.sort(files, new Comparator<File>() {
                                public int compare(File f1, File f2) {
                                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                                }});

                            // now iteratively upload files (oldest first) and delete them from the sd-card
                            for (File f : files) {
                                synchronized (LockManager.getArchivedFileLock(f.getAbsolutePath())) {
                                    boolean result = FileUtils.uploadFile(f.getAbsolutePath(), PropertiesUtils.getServerUrl());
                                    if (result) {
                                        logger.verbose("File uploaded [" + f.getAbsolutePath() + "]");
                                        f.delete();
                                    }
                                    else {
                                        logger.warn("File failed to upload [" + f.getAbsolutePath() + "]");
                                    }
                                }
                            }
                        }            
                    }             
                }
            }
        };
        
        new Thread(null, uploadArchivesRunnable, "UploadingFilesThread").start();
    }
    
    private boolean checkForArchives() {
        boolean gotFiles = false;
        synchronized(LockManager.logArchivesDirLock) {
            File dir = new File(PrefsUtils.getArchivesDir(this));
        
            if (dir.exists()) {
                File[] files = dir.listFiles();
                if (files.length > 0) {
                    gotFiles = true;
                }
            }
        }
        
        return gotFiles;
    }
}
