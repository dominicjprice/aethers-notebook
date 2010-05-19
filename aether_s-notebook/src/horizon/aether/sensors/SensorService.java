package horizon.aether.sensors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import horizon.android.logging.Logger;
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
		loggingServices.add(new LoggingServiceDescriptor("Location", "Location", LocationLoggingService.class));
	}
	
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
		logger.verbose("SensorService.onCreate()");
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		logger.verbose("SensorService.onDestroy()");
		for(LoggingServiceDescriptor desc : loggingServices)
			stopService(desc.getIntent(this));
		logOut.close();
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
		try
		{
			logOut = new PrintWriter(new BufferedWriter(
					new FileWriter("/sdcard/sensorservice.log", true)));
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
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
		return null;
	}
	
	private static final Object LOG_SYNC = new Object();
	
	private PrintWriter logOut;
	
	private class SensorServiceLoggerStub 
	extends SensorServiceLogger.Stub
	{		
		@Override
		public void log(long timestamp, String identifier, String dataBlob) 
		throws RemoteException 
		{
			synchronized(LOG_SYNC) 
			{
				logOut.println(timestamp + ":"
						+ identifier + ":" + dataBlob);
				logOut.flush();
			}
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
