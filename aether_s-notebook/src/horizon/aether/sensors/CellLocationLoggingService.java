package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

import android.os.Looper;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class CellLocationLoggingService
extends SensorLoggingService
{
	@Override
	protected long doScan() 
	{
		Looper.prepare();
		
		TelephonyManager man = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
				
		man.listen(new PhoneStateListener()
		{
			@Override
			public void onCellLocationChanged(CellLocation location) 
			{
				if(!(location instanceof GsmCellLocation))
					return;
				GsmCellLocation loc = (GsmCellLocation)location;
				JSONStringer data = new JSONStringer();
				try 
				{
					data.object();
					data.key("cid");
					data.value(loc.getCid());
					data.key("lac");
					data.value(loc.getLac());
					data.endObject();
				}
				catch (JSONException e){}
				log(data.toString());
			}
		}, PhoneStateListener.LISTEN_CELL_LOCATION);
		
		Looper.loop();
		return Long.MAX_VALUE;
	}

	@Override
	protected String getIdentifier() 
	{
		return "CELL_LOCATION";
	}

	@Override
	protected void onCreateInternal(){}

	@Override
	protected void onDestroyInternal(){}

	@Override
	protected void onStartInternal(){}
}
