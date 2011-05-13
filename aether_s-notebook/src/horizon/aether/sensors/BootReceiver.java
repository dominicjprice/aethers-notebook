package horizon.aether.sensors;

import horizon.aether.utilities.PrefsUtils;
import horizon.android.logging.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BootReceiver is a broadcast receiver that listens to the BOOT event
 * and starts the application if the user has chosen so. 
 */
public class BootReceiver extends BroadcastReceiver {

    private static final Logger logger = Logger.getLogger(BootReceiver.class);
  
    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast. 
     * The application may or may not be started according to the user's preferences.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (PrefsUtils.getStartOnBootFlag(context)) {
                logger.verbose("BootReceiver.onReceive(): flag on");
                AppHelper.initialize(context);
            }
        }
    }
}
