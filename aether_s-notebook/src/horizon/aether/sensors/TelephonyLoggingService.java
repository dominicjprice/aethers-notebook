package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

public class TelephonyLoggingService 
extends SensorLoggingService 
{
	private static final long SCAN_INTERVAL = 5000;
	
	private TelephonyManager telephonyManager;

	@Override
	protected long doScan() 
	{
		JSONStringer data = new JSONStringer();
		try
		{
			data.object();
			
			// Get neighbouring cell info
			data.key("neighbouringCells");
			data.array();
			for(NeighboringCellInfo info : telephonyManager.getNeighboringCellInfo())
			{
				data.object();
				data.key("cid");
				data.value(info.getCid());
				data.key("rssi");
				data.value(info.getRssi());
				data.endObject();
			}
			data.endArray();
			data.key("networkType");
			switch(telephonyManager.getNetworkType())
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
		return "TELEPHONY_STATE";
	}

	@Override
	protected void onCreateInternal() 
	{
		telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
	}

	@Override
	protected void onDestroyInternal(){}

	@Override
	protected void onStartInternal(){}
}
