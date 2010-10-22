package horizon.aether.utilities;

import horizon.aether.R;
import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Class to provide static methods for easy access if the user preferences
 * from anywhere in the code.
 */
public class PrefsUtils {
    /**
     * Returns a boolean static whether the logging is switched on or off.
     * @param context
     * @return The logging status (on/off).
     */
    public static boolean getLoggingStatus(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(context.getString(R.string.Preferences_loggingOn), true);
    }
    
    /**
     * Gets the logs directory path.
     * @param context
     * @return The absolute path of the logs directory.
     */
    public static String getLogsDirPath(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.Preferences_logsDir), "/sdcard/aether/");
    }

    /**
     * Gets the log file path. 
     * @param context
     * @return The absolute path of the log file.
     */
    public static String getLogFilePath(Context context) {
        return getLogFilePath(getLogsDirPath(context)); 
    }
    
    /**
     * Gets the log file path given the directory
     * @param dir
     * @return The log file path.
     */
    public static String getLogFilePath(String dir) {
        return FileUtils.fixDirPath(dir) + "sensorservice.log";
    }
    
    /**
     * Gets the maximum file size.
     * @param context
     * @return The maximum file size (in bytes).
     */
    public static long getMaxFileSize(Context context) {
        String s = PreferenceManager.getDefaultSharedPreferences(context)
                        .getString(context.getString(R.string.Preferences_maxFileSize), "10");
        return (long)(Integer.parseInt(s) * 1024 * 1024);
    }
    
    /**
     * Gets the maximum total capacity of the log files.
     * @param context
     * @return The maximum total capacity (in bytes).
     */
    public static long getMaxTotalCapacity(Context context) {
        String s = PreferenceManager.getDefaultSharedPreferences(context)
                        .getString(context.getString(R.string.Preferences_maxTotalCapacity), "100");
        return (long)(Integer.parseInt(s) * 1024 * 1024);
    }

    /**
     * Gets the archives directory path.
     * @param context
     * @return The archives directory path.
     */
    public static String getArchivesDir(Context context) {
        return getArchivesDir(getLogsDirPath(context));
    }
    
    /**
     * Gets the archives directory path given the logs directory path.
     * @param dir
     * @return The archives directory path.
     */
    public static String getArchivesDir(String dir) {
        return FileUtils.fixDirPath(dir);
    }
    
    /**
     * Gets the storage cleanup strategy.
     * @param context
     * @return The number of the storage cleanup strategy.
     */
    public static int getStorageCleanupStrategy(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(context.getString(R.string.Preferences_storageCleanupStrategy), "0"));
    }
    
    /**
     * Gets the status of a logger.
     * @param context
     * @param logger
     * @return The logger's status (on/off)
     */
    public static boolean getLoggerStatus(Context context, String logger) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(logger, false);        
    }
    
    /**
     * Gets the start on boot flag.
     * @param context
     * @return The boot flag.
     */
    public static boolean getStartOnBootFlag(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(context.getString(R.string.Preferences_startOnBootFlag), false);
    }

    /**
     * Gets the preferred uploading times.
     * @param context
     * @return
     */
    public static int getUploadTimes(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(context.getString(R.string.Preferences_uploadWhen), "0"));
    }
    
    /**
     * Gets the preferred connection type.
     * @param context
     * @return
     */
    public static int getUploadConnection(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.Preferences_uploadUsing), "0"));
    }
}
