package horizon.aether.sensors;

import horizon.android.logging.Logger;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Service class that is inherited by all the sensors. Provides a logger thread
 * that calls the sensor's doScan() method and sends the log entry to the log() 
 * method of the sensor service through AIDL.
 */
public abstract class SensorLoggingService
extends Service
{
	private static final Logger logger = Logger.getLogger(SensorLoggingService.class);
	
	/**
	 * Logger thread that makes calls to the sensor's doScan() method.
	 */
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
	
	/**
	 * Called by the system when clients want to bind to the service.
	 */
	@Override
	public final IBinder onBind(Intent intent) 
	{
		logger.verbose("SensorLoggingService.onBind()");
		return null;
	}

	/**
	 * Called by the system when the service is created.
	 * This calls the onCreateInternal() method that is implemented
	 * in the child classes.
	 */
	@Override
	public final void onCreate() 
	{
		super.onCreate();
		logger.verbose("SensorLoggingService.onCreate()");
		onCreateInternal();
	}
	
	/**
	 * Called by the system when the service is destroyed.
	 * This calls the onDestroyInternal() method that is implemented
	 * in the child classes and also unbinds from the sensor service.
	 */
	@Override
	public final void onDestroy() 
	{
		super.onDestroy();
		logger.verbose("SensorLoggingService.onDestroy()");
		onDestroyInternal();	
		unbindService(loggingServiceConnection);
		sensorLogger.stop();
	}

	/**
	 * Called by the system when the service is started. 
	 * This calls the onStartInternal() method that is implemented
	 * in the child classes and also binds to the sensor service
	 * to send the logging data.
	 */
	@Override
	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent, startId);
		logger.verbose("SensorLoggingService.onStart(" + intent + ", " + startId + ")");
		
		Intent i = new Intent(this, SensorService.class);
		i.setAction(SensorService.SENSOR_SERVICE_LOG_ACTION);
		bindService(i, loggingServiceConnection, 0);
			
		onStartInternal();
	}
	
	private ServiceConnection loggingServiceConnection = new ServiceConnection() {   
        @Override
        public void onServiceDisconnected(ComponentName name){}
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) 
        {
            dataLogger = (SensorServiceLogger)service;
            new Thread(sensorLogger = new SensorLogger()).start();
        }
    };
	
    /**
     * Sends some log data to the sensor service. 
     * @param data: Data to log.
     */
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
