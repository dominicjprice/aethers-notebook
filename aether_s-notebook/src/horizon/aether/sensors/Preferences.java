package horizon.aether.sensors;

import horizon.aether.R;
import horizon.aether.utilities.FileUtils;
import horizon.android.logging.Logger;

import java.io.File;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

/**
 * Preferences activity that provides shows a preferences screen to the user to:
 *    - Toggle logging on/off
 *    - Set path for logs directory
 *    - Set maximum file size
 *    - Set maximum total capacity used by the log files
 *    - Set strategy to be followed in case the maximum total capacity has been reached. 
 *
 * An inner class (Helper) provides some helper methods to access these preferences
 * easily from anywhere in the application.
 */
public class Preferences extends PreferenceActivity {

    /**
     * Upper and lower limits for maximum file size and maximum total capacity.
     */
    private static int MAX_MAX_FILE_SIZE = 20;
    private static int MIN_MAX_FILE_SIZE = 1;
    private static int MAX_MAX_TOTAL_CAPACITY = 200;
    private static int MIN_MAX_TOTAL_CAPACITY = 1;
        
    private static final Logger logger = Logger.getLogger(Preferences.class);
    private PreferenceCategory sensorsCategory;
    private boolean isLoggingOn;
    
    /**
     * Called by the system when the activity is created. The activity
     * is initialised to show the current user settings.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        logger.verbose("Preferences.onCreate()");
        
        // sensor services
        setupLoggingPref();
        setupSensorsPref();        
        setupLogsDirPref();
        setupMaxFileSizePref();
        setupMaxTotalCapacityPref();
    }

    /**
     * Called by the system when the activity is destroyed. The activity
     * needs to unbind from any services to avoid leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(sensorsServiceConnection);
        unbindService(toggleLoggingServiceConnection);
    }
    
    private void setupSensorsPref() {
        sensorsCategory = (PreferenceCategory)findPreference(getString(R.string.Preferences_sensors));
        
        Intent serviceIntent = new Intent(this, SensorService.class);
        serviceIntent.setAction(SensorService.SENSOR_SERVICE_CONTROL_ACTION);
        bindService(serviceIntent, sensorsServiceConnection, 0);
    }

    private void setupLoggingPref() {
        CheckBoxPreference loggingPref = (CheckBoxPreference)findPreference(getString(R.string.Preferences_loggingOn));
        loggingPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent serviceIntent = new Intent(getBaseContext(), SensorService.class);
                serviceIntent.setAction(SensorService.SENSOR_SERVICE_CONTROL_ACTION);
                isLoggingOn = ((Boolean)newValue).booleanValue();
                bindService(serviceIntent, toggleLoggingServiceConnection, 0);                
                return true;
            }
        });
    }
    
    private void setupLogsDirPref() {
        EditTextPreference logsDirPref = (EditTextPreference)findPreference(getString(R.string.Preferences_logsDir));
        logsDirPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean success = false;
                
                String newPath = FileUtils.fixDirPath(newValue.toString());
                
                // check if the directory exists or if it can be created
                if (!FileUtils.checkDirExistsOrCanBeCreated(newPath)) {
                    String msg = "Invalid directory.";
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();    
                }
                
                // move old file to new directory
                File oldFile = new File(Helper.getLogFilePath(getBaseContext()));
                File newFile = new File(Helper.getLogFilePath(newPath));

                synchronized(LockManager.logFileLock) {
                    if (oldFile.exists()) {
                        oldFile.renameTo(newFile);
                    }
                }
                    
                // move archives directory
                File oldArchivesDir = new File(Helper.getArchivesDir(getBaseContext()));
                File newArchivesDir = new File(Helper.getArchivesDir(newPath));
                synchronized (LockManager.logArchivesDirLock) {
                    if (oldArchivesDir.exists()) {
                        oldArchivesDir.renameTo(newArchivesDir);
                    }
                }
                
                return success;
            } 
        });
    }

    private void setupMaxFileSizePref() {
        EditTextPreference maxFileSizePref = (EditTextPreference)findPreference(getString(R.string.Preferences_maxFileSize));
        maxFileSizePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String msg = "Please enter a number between " + MIN_MAX_FILE_SIZE + " and " + MAX_MAX_FILE_SIZE;
                try {
                    int v = Integer.parseInt(newValue.toString().trim());
                    if (v < MIN_MAX_FILE_SIZE || v > MAX_MAX_FILE_SIZE) {
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                catch (NumberFormatException e) {
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
    }

    private void setupMaxTotalCapacityPref() {
        EditTextPreference maxTotalCapacityPref = (EditTextPreference)findPreference(getString(R.string.Preferences_maxTotalCapacity));
        maxTotalCapacityPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String msg = "Please enter a number between " + MIN_MAX_TOTAL_CAPACITY + " and " + MAX_MAX_TOTAL_CAPACITY;
                try {
                    int v = Integer.parseInt(newValue.toString().trim());
                    if (v < MIN_MAX_TOTAL_CAPACITY || v > MAX_MAX_TOTAL_CAPACITY) {
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                catch (NumberFormatException e) {
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
    }
    
    private ServiceConnection sensorsServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            logger.verbose("Preferences.ServiceConnection.onServiceDisconnected()");
            sensorsServiceConnection = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            logger.verbose("Preferences.ServiceConnection.onServiceConnected()");
            final SensorServiceControl control = (SensorServiceControl)service;

            try {
                for (final LoggingServiceDescriptor desc: control.getLoggers()) {
                    CheckBoxPreference p = new CheckBoxPreference(getBaseContext());
                    p.setKey(desc.name);
                    p.setTitle(desc.name);
                    p.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            Boolean b = (Boolean)newValue;
                            try {
                                if (b.booleanValue())
                                    control.startLogger(desc);
                                else {
                                    control.stopLogger(desc);
                                }
                            }
                            catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }
                    });

                    // add item to UI
                    sensorsCategory.addItemFromInflater(p);
                }
            } 
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }                    
    };
    
    private ServiceConnection toggleLoggingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final SensorServiceControl control = (SensorServiceControl)service;
            try {
                if (isLoggingOn)
                    control.startLogging();
                else
                    control.stopLogging();
            } 
            catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };
    
    /**
     * Inner class to provide static methods for easy access if the user preferences
     * from anywhere in the code.
     */
    public static class Helper {
        
        /**
         * Returns a boolean static whether the logging is switched on or off.
         * @param context
         * @return The logging status (on/off).
         */
        public static boolean isLoggingOn(Context context) {
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
            return FileUtils.fixDirPath(dir) + "archives/";
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
   }
}