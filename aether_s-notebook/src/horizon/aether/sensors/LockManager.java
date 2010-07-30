package horizon.aether.sensors;

import java.util.Hashtable;

/**
 * Centralises the locking of files, specifically logging files.
 */
public class LockManager {

    /**
     * Lock for the main log file.
     */
    public static final Object logFileLock = new Object();
    
    /**
     * Lock for the logs directory.
     */
    public static final Object logDirLock = new Object();
    
    /**
     * Lock for the log archives directory.
     */
    public static final Object logArchivesDirLock = new Object();

    private static Hashtable<String, Object> archivesLocks = new Hashtable<String, Object>();

    /**
     * Gets the lock for an archived file.
     * @param filePath: The absolute path of the file.
     * @return The lock object. 
     */
    public static Object getArchivedFileLock(String filePath) {
        if (archivesLocks.get(filePath) == null) {
            archivesLocks.put(filePath, new Object());
        }

        return archivesLocks.get(filePath);
    }
}
