package horizon.aether.sensors;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public class LoggingListenerService
extends Service 
implements Parcelable
{
    private SensorServiceLogger dataLogger;
    
    public LoggingListenerService() { }
    
    public void log(LogEntry entry) { 
        // child classes need to override this  
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() { 
        super.onCreate();
    }
    
    @Override
    public String toString() {
        return "LoggingListenerService";
    }

    private LoggingListenerService(Parcel in) { }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) { }
    
    public static final Parcelable.Creator<LoggingListenerService> CREATOR = 
        new Parcelable.Creator<LoggingListenerService>() {
            public LoggingListenerService createFromParcel(Parcel in) {
                return new LoggingListenerService(in);
            }
            
            public LoggingListenerService[] newArray(int size) {
                return new LoggingListenerService[size];
            }
        };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    protected class LoggingListenerServiceConnection implements ServiceConnection {
        private LoggingListenerService loggingService;
        
        public LoggingListenerServiceConnection(LoggingListenerService loggingService) {
            this.loggingService = loggingService;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { 
            dataLogger = (SensorServiceLogger)service;

            // register
            try {
                dataLogger.registerListener(loggingService);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }                           

        @Override
        public void onServiceDisconnected(ComponentName name) { }
    }
}
