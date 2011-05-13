package horizon.aether.sensors;

import horizon.android.logging.Logger;
import android.content.Context;
import android.content.Intent;

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
        
        Intent sensorsIntent = new Intent(context, SensorService.class);
        context.startService(sensorsIntent);
    }
    
    /**
     * Finalises the application by stopping the sensor services and the logging.
     * @param context
     */
    public static void finalize(Context context) {
        logger.verbose("AppHelper.finalize()");

        Intent sensorsIntent = new Intent(context, SensorService.class);
        context.stopService(sensorsIntent);
    }
}
