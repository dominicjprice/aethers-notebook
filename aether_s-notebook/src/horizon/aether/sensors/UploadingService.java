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

/**
 * Service that takes care of the uploading of the archived log files.
 */
public class UploadingService 
extends Service {

    private static final Logger logger = Logger.getLogger(UploadingService.class);
    
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
        
        // new thread to upload files
        startArchivingThread();
    }
    
    /**
     * Class that implements the functionality required by the IUploadingService 
     * interface defined in IUploadingService.aidl.
     */
    public class UploadingServiceStub extends IUploadingService.Stub {
        public void uploadArchives() throws RemoteException {
            startArchivingThread();
        }
    }
    
    private void startArchivingThread() {
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
}
