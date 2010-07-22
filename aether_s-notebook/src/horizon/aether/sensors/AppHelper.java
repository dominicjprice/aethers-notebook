package horizon.aether.sensors;

import horizon.android.logging.Logger;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;

public class AppHelper {
    private static Logger logger = Logger.getLogger(AppHelper.class);
    
    private static Intent sensorsIntent;
    
    public static void initialize(Context context) {
        logger.verbose("AppHelper.initialize()");
        
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // start the sensors service
        sensorsIntent = new Intent(context, SensorService.class);
        
        context.startService(sensorsIntent);
        
        // bind to the sensors service to initialize loggers
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
                        } 
                        catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
        },
        0);
    }
    
    public static void finalize(Context context) {
        context.stopService(sensorsIntent);
    }
}
