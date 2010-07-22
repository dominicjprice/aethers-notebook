package horizon.aether.sensors;

import horizon.aether.utilities.CompressionUtils;
import horizon.aether.utilities.FileUtils;
import horizon.android.logging.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.Intent;

public class FileSystemLoggingListenerService 
extends LoggingListenerService 
{
    private static final int MAX_BUFFER_SIZE = 1024; 
    private static final ReentrantLock bufferLock = new ReentrantLock();
    private static final Logger logger = Logger.getLogger(FileSystemLoggingListenerService.class);
    private String buffer;
    
    @Override
    public void log(LogEntry entry) {
        logger.verbose("FileSystemLoggingListenerService.log()");
        
        bufferLock.lock();
        
        buffer += entry.toString() + "\n";
        if (buffer.getBytes().length > MAX_BUFFER_SIZE) {
            flushBufferToFile();
        }
        else {
            bufferLock.unlock();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.verbose("FileSystemLoggingListenerService.onCreate()");

        // bind to logging service
        Intent bindIntent = new Intent(this, SensorService.class);
        bindIntent.setAction(SensorService.SENSOR_SERVICE_LOGGER_LISTENER_REGISTER_ACTION);
        bindService(bindIntent, new LoggingListenerServiceConnection(this), Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.verbose("FileSystemLoggingListenerService.onDestroy()");
    }

    private void flushBufferToFile() {
        logger.verbose("FileSystemLoggingListenerService.flushBufferToFile()");
  
        // grab buffer data
        String bufData = new String(buffer);

        // empty buffer and unlock
        buffer = "";
        bufferLock.unlock();
        
        // make sure we won't get over the max directory size
        int bufSize = 0;
        synchronized(bufferLock) {
            bufSize = buffer.getBytes().length;
            File dir = new File(Preferences.Helper.getLogsDirPath(getBaseContext()));
            long maxTotalCapacity = Preferences.Helper.getMaxTotalCapacity(getBaseContext());
            if (FileUtils.getDirSize(dir) + bufSize  >= maxTotalCapacity) {
                cleanUp();
            }
        }

        // make sure we won't get over the max file size
        synchronized(LockManager.logFileLock) {
            File logFile = new File(Preferences.Helper.getLogFilePath(getBaseContext()));
            if (logFile.length() + bufSize > Preferences.Helper.getMaxFileSize(getBaseContext())) {
                archive();
            }

            // everything OK, now write data to file
            try {
                PrintWriter logOut = new PrintWriter(new BufferedWriter(new FileWriter(
                        Preferences.Helper.getLogFilePath(getBaseContext()), true)));
                logOut.print(bufData);
                logOut.flush();
                logOut.close();            
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }        
        }        
    }

    private void cleanUp() {
        logger.verbose("FileSystemLoggingListenerService.cleanUp()");
        final int strategy = Preferences.Helper.getStorageCleanupStrategy(getBaseContext());
        
        switch (strategy) {
        case 0:
            // delete old files
            new Thread(null, deleteOldFilesRunnable, "CleanUpThread").start();
            break;
        case 1:
            // more strategies to be added
            // ...
            break;
        }
    }
    
    private void archive() {
        logger.verbose("FileSystemLoggingListenerService.archive()");
        new Thread(
                null, 
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // calculate file paths
                            String logsDirPath = Preferences.Helper.getLogsDirPath(getBaseContext());
                            String logFilePath = Preferences.Helper.getLogFilePath(getBaseContext());
                            String time = new SimpleDateFormat("dd-MM-yyyy@HH-mm-ss").format(new Date());
                            String archivesDirPath = logsDirPath + "archives/";
                            String zipPath = archivesDirPath + "archived-sensorservice.log" + time + ".dfl";
                            File archivesDir = new File(archivesDirPath);
                            if (!(archivesDir.exists() && archivesDir.isDirectory())) {
                                archivesDir.mkdir();
                            }
                            
                            // compress
                            File logFile = new File(logFilePath);
                            if (logFile.exists()) {
                                CompressionUtils.compress(logFilePath, zipPath);
        
                                // empty log file
                                logFile.delete();
                            }
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }                        
                    }
            
                },
                "ArchivingThread").run();
    }
    
    Runnable deleteOldFilesRunnable = new Runnable () {
        @Override
        public void run() {
            int howMany = 0;

            // grab files and sort by last modified (ascending)
            File dir = new File(Preferences.Helper.getArchivesDir(getBaseContext()));
            if (dir.exists()) {
                File[] files = dir.listFiles();
                if (files.length > 0) {
                    Arrays.sort(files, new Comparator<File>() {
                        public int compare(File f1, File f2) {
                            return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                        }});

                    howMany = (files.length > 2) ? 2 : 1;
                }            
                else {
                    howMany = 0;
                }

                logger.verbose("FileSystemLoggingListenerService.deleteOldFilesRunnable deleting " + howMany + " file(s)");
                
                // delete the files
                for (int i=0; i<howMany; i++) {
                    files[i].delete();
                }
            }
        }
    };    
}