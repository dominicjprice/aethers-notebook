package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Manages the logging of signal strength data.
 */
public class SignalStrengthLoggingService
extends SensorLoggingService
implements LocationListener
{
	protected int lastSignalStrength;
	protected int lastNetworkType;
	
	@Override
	protected long doScan() 
	{
		Looper.prepare();
		
		TelephonyManager telMan = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
				
		telMan.listen(new PhoneStateListener()
		{
			@Override
			public void onSignalStrengthChanged(int asu)
			{
				lastSignalStrength = asu;
				TelephonyManager telMan = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
				lastNetworkType = telMan.getNetworkType();
			}
		}, PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
		
		LocationManager locMan = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		for(String prov : locMan.getAllProviders())
			locMan.requestLocationUpdates(prov, 1, 1, this);
		
		Looper.loop();
		return Long.MAX_VALUE;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		JSONStringer data = new JSONStringer();
		try
		{
			data.object();
			data.key("accuracy");
			if(location.hasAccuracy())
				data.value(location.getAccuracy());
			else
				data.value(null);
			
			data.key("altitude");
			if(location.hasAltitude())
				data.value(location.getAltitude());
			else
				data.value(null);
			
			data.key("bearing");
			if(location.hasBearing())
				data.value(location.getBearing());
			else
				data.value(null);
			
			data.key("latitude");
			data.value(location.getLatitude());
			
			data.key("longitude");
			data.value(location.getLongitude());
			
			data.key("provider");
			data.value(location.getProvider());
			
			data.key("speed");
			if(location.hasSpeed())
				data.value(location.getSpeed());
			else
				data.value(null);
			
			Bundle extras = location.getExtras();
			data.key("extras");
			if(extras != null)
				data.value(extras.toString());
			else
				data.value(null);
			
			data.key("signalStrength");
			data.value(this.lastSignalStrength);
			data.key("networkType");
			switch(this.lastNetworkType)
			{
				case TelephonyManager.NETWORK_TYPE_EDGE:
					data.value("NETWORK_TYPE_EDGE");
					break;
				case TelephonyManager.NETWORK_TYPE_GPRS:
					data.value("NETWORK_TYPE_GPRS");
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
					data.value("NETWORK_TYPE_UMTS");
					break;
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				default:
					data.value("NETWORK_TYPE_UNKNOWN");	
			}
			data.endObject();
		}
		catch(JSONException e)
		{
			throw new RuntimeException(e);
		}
		log(data.toString());
	}
	
	@Override
	protected String getIdentifier() 
	{
		return "SIGNAL_STRENGTH";
	}

	@Override
	protected void onCreateInternal(){}

	@Override
	protected void onDestroyInternal(){}

	@Override
	protected void onStartInternal(){}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}
	
}
