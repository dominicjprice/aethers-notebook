package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

public class ServiceStateLoggingService
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
			public void onServiceStateChanged(ServiceState serviceState) 
			{
				JSONStringer data = new JSONStringer();
				try 
				{
					data.object();
					data.key("operatorAlphaLong");
					data.value(serviceState.getOperatorAlphaLong());
					data.key("operatorAlphaShort");
					data.value(serviceState.getOperatorAlphaShort());
					data.key("operatorNumeric");
					data.value(serviceState.getOperatorNumeric());
					data.key("roaming");
					data.value(serviceState.getRoaming());
					data.key("state");
					switch(serviceState.getState())
					{
					case ServiceState.STATE_EMERGENCY_ONLY :
						data.value("STATE_EMERGENCY_ONLY");
						break;
					case ServiceState.STATE_IN_SERVICE :
						data.value("STATE_IN_SERVICE");
						break;
					case ServiceState.STATE_OUT_OF_SERVICE :
						data.value("STATE_OUT_OF_SERVICE");
						break;
					case ServiceState.STATE_POWER_OFF :
						data.value("STATE_POWER_OFF");
						break;
					default:
						data.value("STATE_UNKNOWN");
					}
					data.endObject();
				}
				catch (JSONException e){}
				log(data.toString());
			}
		}, PhoneStateListener.LISTEN_SERVICE_STATE);
		
		Looper.loop();
		return Long.MAX_VALUE;
	}

	@Override
	protected String getIdentifier() 
	{
		return "SERVICE_STATE";
	}

	@Override
	protected void onCreateInternal(){}

	@Override
	protected void onDestroyInternal(){}

	@Override
	protected void onStartInternal(){}
}
