package horizon.aether.sensors;

import horizon.android.logging.Logger;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

public abstract class SensorLoggingService
extends Service
{
	private static final Logger logger = Logger.getLogger(SensorLoggingService.class);
	
	private class SensorLogger
	implements Runnable
	{
		private boolean running = true;
		
		private Thread loggerThread;
		
		@Override
		public void run()
		{
			loggerThread = Thread.currentThread();
			logger.info("SensorLogger thread " + loggerThread.getId() + " started");
			while(running)
			{
				try
				{
					long sleepTime = doScan();
					if(sleepTime > 0)
						Thread.sleep(sleepTime);
				}
				catch(InterruptedException e)
				{
					logger.info("SensorLogger thread " + loggerThread.getId() + " interrupted");
				}
			}
			logger.info("SensorLogger thread " + loggerThread.getId() + " stopped");
		}
		
		public void stop()
		{
			running = false;
			loggerThread.interrupt();
		}
	}
	
	private SensorLogger sensorLogger;
	
	private SensorServiceLogger dataLogger; 
	
	@Override
	public final IBinder onBind(Intent intent) 
	{
		logger.verbose("SensorLoggingService.onBind()");
		return null;
	}

	@Override
	public final void onCreate() 
	{
		super.onCreate();
		logger.verbose("SensorLoggingService.onCreate()");
		onCreateInternal();
	}
	
	@Override
	public final void onDestroy() 
	{
		super.onDestroy();
		logger.verbose("SensorLoggingService.onDestroy()");
		onDestroyInternal();		
		sensorLogger.stop();
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent, startId);
		logger.verbose("SensorLoggingService.onStart(" + intent + ", " + startId + ")");
		
		Intent i = new Intent(this, SensorService.class);
		i.setAction(SensorService.SENSOR_SERVICE_LOG_ACTION);
		bindService(i, new ServiceConnection() 
		{	
			@Override
			public void onServiceDisconnected(ComponentName name){}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) 
			{
				dataLogger = (SensorServiceLogger)service;
				new Thread(sensorLogger = new SensorLogger()).start();
			}
		}, 0);
			
		onStartInternal();
	}
	
	protected final void log(String data)
	{			
		try 
		{
			if(dataLogger != null)
				dataLogger.log(System.currentTimeMillis(), getIdentifier(), data);
		}
		catch(RemoteException e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	protected abstract void onCreateInternal();
	
	protected abstract void onStartInternal();
	
	protected abstract void onDestroyInternal();
	
	protected abstract String getIdentifier();
	
	protected abstract long doScan();
}
