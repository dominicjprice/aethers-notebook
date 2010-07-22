package horizon.aether;

import horizon.aether.sensors.AppHelper;
import horizon.android.logging.Logger;
import android.app.Application;
import android.content.res.Configuration;

public class GeoTagger 
extends Application 
{
	private static Logger logger = Logger.getLogger(GeoTagger.class);
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		logger.verbose("GeoTagger.onConfigurationChanged()");
	}

	@Override
	public void onCreate() 
	{
		super.onCreate();
		logger.verbose("GeoTagger.onCreate()");
		
		// initialize sensors according to preferences
		AppHelper.initialize(getApplicationContext());
	}

	@Override
	public void onLowMemory() 
	{
		super.onLowMemory();
		logger.verbose("GeoTagger.onLowMemory()");
	}

	@Override
	public void onTerminate() 
	{
		super.onTerminate();
		logger.verbose("GeoTagger.onTerminate()");
		
		// finalize application
		AppHelper.finalize(getApplicationContext());
	}
}
