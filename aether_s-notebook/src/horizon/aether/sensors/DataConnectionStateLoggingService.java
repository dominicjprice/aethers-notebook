package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class DataConnectionStateLoggingService
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
			public void onDataConnectionStateChanged(int state)
			{
				JSONStringer data = new JSONStringer();
				try 
				{
					data.object();
					data.key("state");
					switch(state)
					{
					case TelephonyManager.DATA_CONNECTED :
						data.value("DATA_CONNECTED");
						break;
					case TelephonyManager.DATA_CONNECTING :
						data.value("DATA_CONNECTING");
						break;
					case TelephonyManager.DATA_DISCONNECTED :
						data.value("DATA_DISCONNECTED");
						break;
					case TelephonyManager.DATA_SUSPENDED :
						data.value("DATA_SUSPENDED");
						break;
					default:
						data.value("DATA_UNKNOWN");
					}
					data.endObject();
				}
				catch (JSONException e){}
				log(data.toString());
			}
		}, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
		
		Looper.loop();
		return Long.MAX_VALUE;
	}

	@Override
	protected String getIdentifier() 
	{
		return "DATA_CONNECTION_STATE";
	}

	@Override
	protected void onCreateInternal(){}

	@Override
	protected void onDestroyInternal(){}

	@Override
	protected void onStartInternal(){}
}
