package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

import horizon.android.logging.Logger;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiLoggingService 
extends SensorLoggingService 
{
	private static final long SCAN_INTERVAL = 5000;
	
	private static final Logger logger = Logger.getLogger(WifiLoggingService.class);
	
	private WifiManager wifiManager;

	@Override
	protected long doScan() 
	{
		wifiManager.startScan();
		JSONStringer data = new JSONStringer();
		try
		{
			data.array();
			if(wifiManager.getScanResults() == null)
				return SCAN_INTERVAL;
			for(ScanResult result : wifiManager.getScanResults())
			{
				data.object();
				data.key("BSSID");
				data.value(result.BSSID);
				data.key("SSID");
				data.value(result.SSID);
				data.key("capabilities");
				data.value(result.capabilities);
				data.key("frequency");
				data.value(result.frequency);
				data.key("level");
				data.value(result.level);
				data.endObject();
			}
			data.endArray();
			log(data.toString());
		}
		catch(JSONException e)
		{
			throw new RuntimeException(e);
		}
		
		return SCAN_INTERVAL;
	}

	@Override
	protected String getIdentifier() 
	{
		return "AP_SITINGS";
	}

	@Override
	protected void onCreateInternal() 
	{
		wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	}

	@Override
	protected void onDestroyInternal(){}

	@Override
	protected void onStartInternal() 
	{
		if(!wifiManager.setWifiEnabled(true))
		{
			logger.warn("Unable to enable Wifi");
			throw new RuntimeException("Unable to enable WiFi");
		}
	}
}
