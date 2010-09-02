package horizon.aether.sensors;

import horizon.aether.utilities.PrefsUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ConnectivityReceiver is a broadcast receiver that listens to the 
 * CONNECTIVITY_CHANGE intent and notifies the UploadingService
 * if there's a new connection established (according to the
 * user's preferences).
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    
    /**
     * Called when the BroadcastReceiver is receiving an Intent broadcast. 
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // if we've got the right connection
        if (checkConnection(context)) {
            // tell the uploading service it can upload
            Intent i = new Intent(UploadingService.UPLOADING_SERVICE_CONNECTIVITY_RECEIVER_ACTION,
                                    null, context, UploadingService.class);
            
            context.startService(i);
        }        
    }
    
    /**
     * Checks if the preferred connection (Wi-Fi/Mobile) is active and connected.
     * @param context
     * @return true if it is connected.
     */
    public static boolean checkConnection(Context context) {
        ConnectivityManager connMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMan.getActiveNetworkInfo();
        
        boolean isConnReady = false;
        if (networkInfo != null) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                // got connected connection
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                         && PrefsUtils.getUploadConnection(context) == UploadingService.UPLOAD_USING_WIFI_ONLY
                || PrefsUtils.getUploadConnection(context) == UploadingService.UPLOAD_USING_ANY_CONNECTION) {
                    // got preferred connection
                    isConnReady = true;
                }                
            }
        }
        
        return isConnReady;
    }
}
