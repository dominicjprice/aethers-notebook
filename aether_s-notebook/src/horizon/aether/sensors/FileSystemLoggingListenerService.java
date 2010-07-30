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

/**
 * Listens to the logging and records log entries to the file system.
 * 
 * For efficiency, a buffer is used to hold log entries before they are 
 * flushed into the log file.
 * 
 * This service also provides an archiving mechanism where log files are 
 * compressed and archived just before they reach the maximum file size 
 * (which is set by the user). This mechanism also deals with the clean-up
 * of files in the case the maximum capacity (also set by the user) is reached.
 * 
 */
public class FileSystemLoggingListenerService 
extends LoggingListenerService 
{
    private static final int MAX_BUFFER_SIZE = 2 * 1024; 
    private static final ReentrantLock bufferLock = new ReentrantLock();
    private static final Logger logger = Logger.getLogger(FileSystemLoggingListenerService.class);
    private StringBuilder buffer = new StringBuilder();
    
    /**
     * Logs a LogEntry by saving it to the buffer. If the buffer is full,
     * its contents are flushed to the file.
     */
    @Override
    public void log(LogEntry entry) {
        logger.verbose("FileSystemLoggingListenerService.log()");
        
        if (entry != null) {
            bufferLock.lock();

            buffer.append(entry.toString());
            buffer.append("\n");
            if (buffer.toString().getBytes().length > MAX_BUFFER_SIZE) {
                flushBufferToFile();
            }
            else {
                bufferLock.unlock();
            }
        }
    }

    /**
     * Called by the system when the service is created.
     * 
     * Registers this service to the logging listeners of the SensorService. 
     */
    @Override
    public void onCreate() {
        super.onCreate();
        logger.verbose("FileSystemLoggingListenerService.onCreate()");

        // bind to logging service
        Intent bindIntent = new Intent(this, SensorService.class);
        bindIntent.setAction(SensorService.SENSOR_SERVICE_LOGGER_LISTENER_REGISTER_ACTION);
        bindService(bindIntent, new LoggingListenerServiceConnection(this), Context.BIND_AUTO_CREATE);
    }

    /**
     * Called by the system when the service is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.verbose("FileSystemLoggingListenerService.onDestroy()");
        
        // flush buffer contents before destroying 
        bufferLock.lock();
        flushBufferToFile();
    }

    /**
     * Flushes the data of the buffer to the log file. Before doing that it makes sure
     * that the maximum file size and capacity (set by the user) will not be exceeded.
     */
    private void flushBufferToFile() {
        logger.verbose("FileSystemLoggingListenerService.flushBufferToFile()");
        
        // grab buffer data
        String bufData = new String(buffer);
        
        // empty buffer and unlock
        buffer.delete(0, buffer.length());
        bufferLock.unlock();

        // make sure we won't get over the max directory size
        int bufSize = 0;
        synchronized(bufferLock) {
            bufSize = bufData.getBytes().length;
            File dir = new File(Preferences.Helper.getLogsDirPath(getBaseContext()));
            long maxTotalCapacity = Preferences.Helper.getMaxTotalCapacity(getBaseContext());
            
            synchronized (LockManager.logDirLock) {
                if (FileUtils.getDirSize(dir) + bufSize >= maxTotalCapacity) {
                    cleanUp();
                }
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

    /**
     * Cleans up the archive directory according to the strategy selected by the user.
     * At the moment, only 1 strategy is provided where the oldest files are deleted to make
     * space for the new ones.
     */
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
    
    /**
     * Archives the log file. The contents of the log file are compressed and saved in a
     * archived-sensorservice.logdd-MM-yyyy@HH-mm-ss.dfl file under the /archives directory.
     * The log file is then deleted.
     */
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
                            
                            File logFile = new File(logFilePath);
                            if (logFile.exists()) {
                                // compress
                                synchronized (LockManager.getArchivedFileLock(zipPath)) {
                                    CompressionUtils.compress(logFilePath, zipPath);
                                }
                                
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