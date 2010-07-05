package horizon.aether.sensors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.content.Context;
import android.content.Intent;

public class FileSystemLoggingListenerService 
extends LoggingListenerService 
{
    private PrintWriter logOut;
    private static final Object LOG_SYNC = new Object();
        
    @Override
    public void log(LogEntry entry) {
        synchronized(LOG_SYNC) {        
            logOut.println(entry.toString());
            logOut.flush();
        }
    }
   
    @Override
    public void onCreate() {
        super.onCreate();
        
        try {
            logOut = new PrintWriter(new BufferedWriter(
                    new FileWriter("/sdcard/sensorservice.log", true)));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        
        // bind to logging service
        Intent bindIntent = new Intent(this, SensorService.class);
        bindIntent.setAction(SensorService.SENSOR_SERVICE_LOGGER_LISTENER_REGISTER_ACTION);
        bindService(bindIntent, new LoggingListenerServiceConnection(this), Context.BIND_AUTO_CREATE);
    }
    
    @Override
    public void onDestroy() {
        logOut.close();
        super.onDestroy();
    }
    
}
