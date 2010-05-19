package horizon.aether.sensors;

import horizon.android.logging.Logger;
import horizon.aether.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SensorServiceConfigurator 
extends Activity
{
	private static Logger logger = Logger.getLogger(SensorServiceConfigurator.class);
	
	private View mainview;
	
	private ServiceConnection serviceConnection = new ServiceConnection() 
	{
		@Override
		public void onServiceDisconnected(ComponentName name) 
		{
			logger.verbose("SensorServiceConfigurator.ServiceConnection.onServiceDisconnected()");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) 
		{
			logger.verbose("SensorServiceConfigurator.ServiceConnection.onServiceConnected()");
			final SensorServiceControl control = (SensorServiceControl)service;
			LinearLayout layout = (LinearLayout)mainview;
			Context context = SensorServiceConfigurator.this;
			ScrollView sv = new ScrollView(context);
			sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			layout.addView(sv);
			LinearLayout more = new LinearLayout(context);
			more.setOrientation(LinearLayout.VERTICAL);
			sv.addView(more);
			try 
			{
				for(final LoggingServiceDescriptor desc: control.getLoggers())
				{
					LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					LinearLayout ll = new LinearLayout(context);
					ll.setLayoutParams(p);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					TextView tv = new TextView(SensorServiceConfigurator.this);
					tv.setText(desc.name);
					tv.setGravity(Gravity.LEFT);
					ll.addView(tv);
					final ToggleButton bu = new ToggleButton(context);
					bu.setGravity(Gravity.RIGHT);
					bu.setChecked(control.getLoggerStatus(desc));
					LinearLayout bull = new LinearLayout(context);
					bull.setGravity(Gravity.RIGHT);
					bull.setLayoutParams(p);
					bull.addView(bu);
					bu.setOnClickListener(new View.OnClickListener() 
					{	
						@Override
						public void onClick(View v) 
						{
							try
							{
								if(bu.isChecked())
									control.startLogger(desc);
								else
									control.stopLogger(desc);
							}
							catch(RemoteException e)
							{
								throw new RuntimeException(e);
							}
						}
					});
					ll.addView(bull);
					more.addView(ll);
				}
			} 
			catch(Exception e) 
			{
				throw new RuntimeException(e);
			}
		}
	};
	
	private Intent serviceIntent;
		
    @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		logger.verbose("SensorServiceConfigurator.onDestroy()");
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		logger.verbose("SensorServiceConfigurator.onPause()");
		unbindService(serviceConnection);
	}

	@Override
	protected void onRestart() 
	{
		super.onRestart();
		logger.verbose("SensorServiceConfigurator.onRestart()");
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		logger.verbose("SensorServiceConfigurator.onResume()");
		bindService(serviceIntent, serviceConnection , 0);
	}

	@Override
	protected void onStart() 
	{
		super.onStart();
		logger.verbose("SensorServiceConfigurator.onStart()");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		logger.verbose("SensorServiceConfigurator.onStop()");
		//stopService(serviceIntent);
	}

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        mainview = getLayoutInflater().inflate(R.layout.sensor_service_configurator, null);
        setContentView(mainview);
        logger.verbose("SensorServiceConfigurator.onCreate()");
        serviceIntent = new Intent(this, SensorService.class);
        serviceIntent.setAction(SensorService.SENSOR_SERVICE_CONTROL_ACTION);
        bindService(serviceIntent, serviceConnection, 0);
        startService(serviceIntent);
    }
}