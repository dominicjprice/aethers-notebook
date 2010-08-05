package horizon.aether.sensors;

import horizon.aether.utilities.PrefsUtils;
import horizon.android.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * SensorService is the main service of the application. It holds
 * a number of logging services and their statuses and a list of 
 * logging listeners. It provides an interface for logging services
 * and logging listeners to subscribe/unsubscribe. 
 */
public class SensorService 
extends Service 
{
    public static final String SENSOR_SERVICE_CONTROL_ACTION = "GEOTAG_SENSOR_SERVICE_CONTROL_ACTION";
    
    public static final String SENSOR_SERVICE_LOG_ACTION = "GEOTAG_SENSOR_SERVICE_LOG_ACTION";
    
    public static final String SENSOR_SERVICE_LOGGER_LISTENER_REGISTER_ACTION = "SENSOR_SERVICE_LOGGER_LISTENER_REGISTER_ACTION";
    
    private static Logger logger = Logger.getLogger(SensorService.class);
    
    private List<LoggingServiceDescriptor> loggingServices 
            = new ArrayList<LoggingServiceDescriptor>();
    {
        loggingServices.add(new LoggingServiceDescriptor("WiFi", "WiFi", WifiLoggingService.class));
        loggingServices.add(new LoggingServiceDescriptor("Cell Location", "Cell Location", CellLocationLoggingService.class));
        loggingServices.add(new LoggingServiceDescriptor("Data Connection", "Data Connection", DataConnectionStateLoggingService.class));
        loggingServices.add(new LoggingServiceDescriptor("Signal Strength", "Signal Strength", SignalStrengthLoggingService.class));
        loggingServices.add(new LoggingServiceDescriptor("Service State", "Service State", ServiceStateLoggingService.class));
        loggingServices.add(new LoggingServiceDescriptor("Telephony", "Telephony", TelephonyLoggingService.class));
    }

    // logging listeners
    private List<LoggingListenerService> loggingListeners = new ArrayList<LoggingListenerService>();

    private Map<LoggingServiceDescriptor, Boolean> loggingServicesStatuses
            = new HashMap<LoggingServiceDescriptor, Boolean>();
    {
        for(LoggingServiceDescriptor desc : loggingServices)
            loggingServicesStatuses.put(desc, new Boolean(false));
    }
    
    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
    
    /**
     * Called by the system when the device configuration changes.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        logger.verbose("SensorService.onConfigurationChanged()");
    }

    /**
     * Called by the system when the service is created. It starts
     * the location service and the file system logging listener. 
     */
    @Override
    public void onCreate() 
    {
        super.onCreate();
        logger.verbose("SensorService.onCreate()");
    }

    /**
     * Called by the system when the service is destroyed. It stops
     * all the logging services, the location service and the file
     * system logging listener service.
     */
    @Override
    public void onDestroy() 
    {
        super.onDestroy();
        logger.verbose("SensorService.onDestroy()");
                
        for(LoggingServiceDescriptor desc : loggingServices)
            stopService(desc.getIntent(this));

        finaliseLogging();
    }

    /**
     * Called when the overall system is running low on memory.
     */
    @Override
    public void onLowMemory() 
    {
        super.onLowMemory();
        logger.verbose("SensorService.onLowMemory()");
    }

    /**
     * Called when new clients have connected to the service, after it had previously 
     * been notified that all had disconnected.
     */
    @Override
    public void onRebind(Intent intent) 
    {
        super.onRebind(intent);
        logger.verbose("SensorService.onRebind()");
    }

    /**
     * Called by the system when the service is started.
     */
    @Override
    public void onStart(Intent intent, int startId) 
    {
        super.onStart(intent, startId);
        logger.verbose("SensorService.onStart()");
        
        // start the loggers from configuration
        for (final LoggingServiceDescriptor desc: this.loggingServices) {
          if (PrefsUtils.getLoggerStatus(this, desc.name)) {
              startService(desc.getIntent(SensorService.this));
              loggingServicesStatuses.put(desc, Boolean.TRUE);
          }
        }
      
        // also initialise logging (if it was on by configuration)
        if (PrefsUtils.getLoggingStatus(this)) {
            initialiseLogging();
        }
    }
    
    /**
     * Called by the system when all clients have disconnected from a particular
     * interface published by the service.
     */
    @Override
    public boolean onUnbind(Intent intent) 
    {
        logger.verbose("SensorService.onUnbind()");
        return super.onUnbind(intent);
    }

    /**
     * Returns the communication channel to the service.
     */
    @Override
    public IBinder onBind(Intent intent) 
    {
        logger.verbose("SensorService.onBind()");
        if(intent.getAction() == null)
            return null;
        if(intent.getAction().equals(SENSOR_SERVICE_CONTROL_ACTION))
            return new SensorServiceControlStub();
        if(intent.getAction().equals(SENSOR_SERVICE_LOG_ACTION))
            return new SensorServiceLoggerStub();
        if(intent.getAction().equals(SENSOR_SERVICE_LOGGER_LISTENER_REGISTER_ACTION))
            return new SensorServiceLoggerStub();
        return null;
    }
    
    /**
     * Class that implements the functionality required by the SensorServiceLogger 
     * interface defined in SensorServiceLogger.aidl.
     */
    private class SensorServiceLoggerStub 
    extends SensorServiceLogger.Stub
    {
        /**
         * Sends a LogEntry to the log() method of all the logging listeners.
         */
        @Override
        public void log(long timestamp, String identifier, String dataBlob) 
        throws RemoteException 
        {
            if (PrefsUtils.getLoggingStatus(getBaseContext())) {
                LogEntry entry = new LogEntry(timestamp, identifier, LocationService.getLocation(), dataBlob);

                for (LoggingListenerService listener : loggingListeners) {
                    listener.log(entry);
                }
            }
        }

        /**
         * Called by a logging listener that wants to subscribe to the logging.
         * The listener is added to the list of listeners and is notified for 
         * any new log entry.
         */
        @Override
        public void registerListener(LoggingListenerService listener)
        throws RemoteException {
            logger.verbose("SensorServiceLoggerStub.registerListener()");
            loggingListeners.add(listener);
        }
    }
    
    /**
     * Class that implements the functionality required by the SensorServiceControl 
     * interface defined in SensorServiceControl.aidl.
     */
    public class SensorServiceControlStub
    extends SensorServiceControl.Stub
    {
        /**
         * Returns the list of loggers.
         */
        @Override
        public List<LoggingServiceDescriptor> getLoggers()
        throws RemoteException 
        {
            return loggingServices;
        }

        /**
         * Starts a specific logger given by a descriptor.
         */
        @Override
        public void startLogger(LoggingServiceDescriptor descriptor)
        throws RemoteException 
        {
            startService(descriptor.getIntent(SensorService.this));
            loggingServicesStatuses.put(descriptor, Boolean.TRUE);
        }

        /**
         * Stops a specific logger given by a descriptor.
         */
        @Override
        public void stopLogger(LoggingServiceDescriptor descriptor)
        throws RemoteException 
        {
            stopService(descriptor.getIntent(SensorService.this));            
            loggingServicesStatuses.put(descriptor, Boolean.FALSE);
        }

        /**
         * Returns the status of a specific logger given by a descriptor.
         */
        @Override
        public boolean getLoggerStatus(LoggingServiceDescriptor descriptor)
        throws RemoteException 
        {
            return loggingServicesStatuses.get(descriptor).booleanValue();
        }

        /**
         * Starts the logging.
         */
        @Override
        public void startLogging() 
        throws RemoteException {
            logger.verbose("SensorServiceControlStub.startLogging()");
            initialiseLogging();
        }

        /**
         * Stops the logging.
         */
        @Override
        public void stopLogging() 
        throws RemoteException {
            logger.verbose("SensorServiceControlStub.stopLogging()");
            finaliseLogging();
        }
    }

    private void initialiseLogging() {
        startService(new Intent(this, LocationService.class));
        startService(new Intent(this, FileSystemLoggingListenerService.class));
        registerReceiver(connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
    
    private void finaliseLogging() {
        stopService(new Intent(getBaseContext(), LocationService.class));
        stopService(new Intent(getBaseContext(), FileSystemLoggingListenerService.class));
        unregisterReceiver(connectivityReceiver);
    }
}
