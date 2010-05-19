package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class SignalStrengthLoggingService
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
			public void onSignalStrengthChanged(int asu)
			{
				JSONStringer data = new JSONStringer();
				try 
				{
					data.object();
					data.key("strength");
					data.value(asu);
					data.endObject();
				}
				catch (JSONException e){}
				log(data.toString());
			}
		}, PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
		
		Looper.loop();
		return Long.MAX_VALUE;
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
}
