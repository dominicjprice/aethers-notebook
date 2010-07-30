package horizon.aether.sensors;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

/**
 * Service to be extended by potential logging listeners. Child classes should
 * override the log() method to manipulate the LogEntries as they wish.
 * 
 * The class also implements Parcelable so that LoggingListenerService objects 
 * can be send from one process to another through an AIDL interface.
 */
public class LoggingListenerService
extends Service 
implements Parcelable
{
    private SensorServiceLogger dataLogger;
    
    public LoggingListenerService() { }
    
    /**
     * Method to be implemented by child classes to handle
     * the LogEntries.
     * @param entry: Log entry.
     */
    public void log(LogEntry entry) { }

    /**
     * Called by the system when clients want to bind to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * Called by the system when the service is created.
     */
    @Override
    public void onCreate() { 
        super.onCreate();
    }
    
    private LoggingListenerService(Parcel in) { }
    
    /**
     * Flattens this object in to a Parcel.
     */
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
    
        /**
         * Describes the kinds of special objects contained in this Parcelable's 
         * marshalled representation.
         */
        @Override
        public int describeContents() {
            return 0;
        }
    
    /**
     * Service connection for registering to the list of listeners. 
     */
    protected class LoggingListenerServiceConnection implements ServiceConnection {
        private LoggingListenerService loggingService;
        
        public LoggingListenerServiceConnection(LoggingListenerService loggingService) {
            this.loggingService = loggingService;
        }
        
        /**
         * When the service has connected, the listener is registered to the logging service. 
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { 
            dataLogger = (SensorServiceLogger)service;

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
