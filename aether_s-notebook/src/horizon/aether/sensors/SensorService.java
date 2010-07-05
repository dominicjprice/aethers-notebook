package horizon.aether.sensors;

import horizon.android.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;

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

    // location service
    private LocationService locationService = new LocationService();

    // logging listeners
    private List<LoggingListenerService> loggingListeners = new ArrayList<LoggingListenerService>();

    // file system logging listener service
    private FileSystemLoggingListenerService fsLoggingListener = new FileSystemLoggingListenerService();
    
    private Map<LoggingServiceDescriptor, Boolean> loggingServicesStatuses
            = new HashMap<LoggingServiceDescriptor, Boolean>();
    {
        for(LoggingServiceDescriptor desc : loggingServices)
            loggingServicesStatuses.put(desc, new Boolean(false));
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        logger.verbose("SensorService.onConfigurationChanged()");
    }

    @Override
    public void onCreate() 
    {
        super.onCreate();
        
        // start location service
        // TODO: should be on if at least one of the services is on?
        startService(new Intent(this, LocationService.class));
        
        startService(new Intent(this, FileSystemLoggingListenerService.class));
        
        logger.verbose("SensorService.onCreate()");
    }

    @Override
    public void onDestroy() 
    {
        super.onDestroy();
        logger.verbose("SensorService.onDestroy()");
        for(LoggingServiceDescriptor desc : loggingServices)
            stopService(desc.getIntent(this));

        // stop location service
        locationService.stopSelf();
        
        // stop logging listener service
        fsLoggingListener.stopSelf();
    }

    @Override
    public void onLowMemory() 
    {
        super.onLowMemory();
        logger.verbose("SensorService.onLowMemory()");
    }

    @Override
    public void onRebind(Intent intent) 
    {
        super.onRebind(intent);
        logger.verbose("SensorService.onRebind()");
    }

    @Override
    public void onStart(Intent intent, int startId) 
    {
        super.onStart(intent, startId);
        logger.verbose("SensorService.onStart()");
    }

    @Override
    public boolean onUnbind(Intent intent) 
    {
        logger.verbose("SensorService.onUnbind()");
        return super.onUnbind(intent);
    }

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
        
    private class SensorServiceLoggerStub 
    extends SensorServiceLogger.Stub
    {
        @Override
        public void log(long timestamp, String identifier, String dataBlob) 
        throws RemoteException 
        {           
            LogEntry entry = new LogEntry(timestamp, identifier, locationService.getLocation(), dataBlob);
            
            for (LoggingListenerService listener : loggingListeners) {
                listener.log(entry);
            }
        }

        @Override
        public void registerListener(LoggingListenerService listener)
        throws RemoteException {
            loggingListeners.add(listener);
            logger.verbose("SensorServiceLoggerStub.registerListener(): logging listener added.");
        }
    }
    
    public class SensorServiceControlStub
    extends SensorServiceControl.Stub
    {
        @Override
        public List<LoggingServiceDescriptor> getLoggers()
        throws RemoteException 
        {
            return loggingServices;
        }

        @Override
        public void startLogger(LoggingServiceDescriptor descriptor)
        throws RemoteException 
        {
            startService(descriptor.getIntent(SensorService.this));
            loggingServicesStatuses.put(descriptor, Boolean.TRUE);
        }

        @Override
        public void stopLogger(LoggingServiceDescriptor descriptor)
        throws RemoteException 
        {
            stopService(descriptor.getIntent(SensorService.this));
            loggingServicesStatuses.put(descriptor, Boolean.FALSE);
        }

        @Override
        public boolean getLoggerStatus(LoggingServiceDescriptor descriptor)
        throws RemoteException 
        {
            return loggingServicesStatuses.get(descriptor).booleanValue();
        }
    }
}
