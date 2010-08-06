package horizon.aether.ui;

import horizon.aether.R;
import horizon.aether.sensors.AppHelper;
import horizon.aether.sensors.LockManager;
import horizon.aether.sensors.LoggingServiceDescriptor;
import horizon.aether.sensors.SensorService;
import horizon.aether.sensors.SensorServiceControl;
import horizon.aether.sensors.UploadingService;
import horizon.aether.utilities.FileUtils;
import horizon.aether.utilities.PrefsUtils;
import horizon.android.logging.Logger;

import java.io.File;

import android.content.ComponentName;
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
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Preferences activity that provides shows a preferences screen to the user to:
 *    - Toggle logging on/off
 *    - Set start on boot flag 
 *    - Set path for logs directory
 *    - Set maximum file size
 *    - Set maximum total capacity used by the log files
 *    - Set strategy to be followed in case the maximum total capacity has been reached.
 *    - Set uploading preferences such as connection and strategy 
 *    - Turn on/off sensors
 *
 * A button is also provided to upload the archives to the server.
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
    
    private SensorServiceControl controlService = null;
    private ServiceConnection controlServiceConnection;
    
    /**
     * Called by the system when the activity is created. The activity
     * is initialised to show the current user settings.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.verbose("Preferences.onCreate()");
        addPreferencesFromResource(R.xml.preferences);

        AppHelper.initialize(this);

        connectControlService();
        
        setupLogsDirPref();
        setupMaxFileSizePref();
        setupMaxTotalCapacityPref();        
        setupUploadButtonPref();
    }
    
    /**
     * Called by the system when the activity is destroyed. The activity
     * needs to unbind from any services to avoid leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.verbose("Preferences.onDestroy()");
        unbindService(controlServiceConnection);
    }

    /**
     * Connects to the SensorsService.
     */
    public void connectControlService() {
        logger.warn("Preferences.connectControlService()");

        controlServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                logger.warn("Connected");
                controlService = (SensorServiceControl)service;
                setupLoggingPref();
                setupSensorsPref();                
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                logger.warn("Disconnected");
                controlService = null;
            }
        };

        Intent serviceIntent = new Intent(getBaseContext(), SensorService.class);
        serviceIntent.setAction(SensorService.SENSOR_SERVICE_CONTROL_ACTION);
        bindService(serviceIntent, controlServiceConnection, 0);
    }

    private void setupSensorsPref() {
        sensorsCategory = (PreferenceCategory)findPreference(getString(R.string.Preferences_sensors));
        try {
            for (final LoggingServiceDescriptor desc: controlService.getLoggers()) {
                CheckBoxPreference p = new CheckBoxPreference(getBaseContext());
                p.setKey(desc.name);
                p.setTitle(desc.name);
                p.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        Boolean b = (Boolean)newValue;
                        try {
                            if (b.booleanValue())
                                controlService.startLogger(desc);
                            else {
                                controlService.stopLogger(desc);
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

    private void setupUploadButtonPref() {
        ButtonPreference cmdUpload = (ButtonPreference)findPreference("cmdUpload");        
        cmdUpload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(UploadingService.UPLOADING_SERVICE_USER_CLICK_ACTION, 
                                            null, getBaseContext(), UploadingService.class);
                startService(uploadIntent);
            }
        });
    }

    private void setupLoggingPref() {
        CheckBoxPreference loggingPref = (CheckBoxPreference)findPreference(getString(R.string.Preferences_loggingOn));
        loggingPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                isLoggingOn = ((Boolean)newValue).booleanValue();
                logger.warn("isLoggingOn=" + isLoggingOn);
                try {
                    if (isLoggingOn) {
                        controlService.startLogging();
                    }
                    else {
                        controlService.stopLogging();
                    }
                } 
                catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
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
                File oldFile = new File(PrefsUtils.getLogFilePath(getBaseContext()));
                File newFile = new File(PrefsUtils.getLogFilePath(newPath));

                synchronized(LockManager.logFileLock) {
                    if (oldFile.exists()) {
                        oldFile.renameTo(newFile);
                    }
                }
                    
                // move archives directory
                File oldArchivesDir = new File(PrefsUtils.getArchivesDir(getBaseContext()));
                File newArchivesDir = new File(PrefsUtils.getArchivesDir(newPath));
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

}

