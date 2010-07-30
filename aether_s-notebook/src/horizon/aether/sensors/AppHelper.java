package horizon.aether.sensors;

import horizon.aether.R;
import horizon.android.logging.Logger;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;

/**
 * The AppHelper contains some helper methods to initialise and finalise the
 * sensor's side of the application. 
 */
public class AppHelper {
    private static Logger logger = Logger.getLogger(AppHelper.class);
    
    /**
     * Initialises the application by starting the sensor services according
     * to the user preferences.
     * @param context
     */
    public static void initialize(final Context context) {
        logger.verbose("AppHelper.initialize()");
        
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // start the sensors service
        Intent sensorsIntent = new Intent(context, SensorService.class);
        context.startService(sensorsIntent);
        
        // bind to the sensors service to initialise loggers
        sensorsIntent.setAction(SensorService.SENSOR_SERVICE_CONTROL_ACTION);
        context.bindService(
                sensorsIntent, 
                new ServiceConnection() {
                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        logger.verbose("AppHelper.initialize().ServiceConnection.onServiceDisconnected()");
                    }
            
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        logger.verbose("AppHelper.initialize().ServiceConnection.onServiceConnected()");
                        final SensorServiceControl control = (SensorServiceControl)service;
                
                        try {
                            for (final LoggingServiceDescriptor desc: control.getLoggers()) {
                                if (prefs.getBoolean(desc.name, false)) {
                                    control.startLogger(desc);
                                }
                            }
                            
                            // also initialise logging
                            if (prefs.getBoolean(context.getString(R.string.Preferences_loggingOn), true)) {
                                control.startLogging();
                            }
                        } 
                        catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
        },
        0);
    }
    
    /**
     * Finalises the application by stopping the sensor services and the logging.
     * @param context
     */
    public static void finalize(Context context) {
        logger.verbose("AppHelper.finalize()");

        // stop the service
        Intent sensorsIntent = new Intent(context, SensorService.class);
        context.stopService(sensorsIntent);
        
        // bind to the sensors service to stop loggers
        sensorsIntent.setAction(SensorService.SENSOR_SERVICE_CONTROL_ACTION);
        context.bindService(
                sensorsIntent, 
                new ServiceConnection() {
                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        logger.verbose("AppHelper.finalize().ServiceConnection.onServiceDisconnected()");
                    }
            
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        logger.verbose("AppHelper.finalize().ServiceConnection.onServiceConnected()");
                        final SensorServiceControl control = (SensorServiceControl)service;
                
                        try {
                            for (final LoggingServiceDescriptor desc: control.getLoggers()) {
                                control.stopLogger(desc);
                            }
                            
                            control.stopLogging();
                        } 
                        catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
        },
        0);
    }
}
