package horizon.aether;

import horizon.android.logging.Logger;
import horizon.aether.R;
import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TextNote
extends Activity
{
	private static final Logger logger = Logger.getLogger(TextNote.class);
	
	@Override
	public void onResume()
	{
		super.onResume();
		EditText et = (EditText)findViewById(R.id.EditText01);
		et.setText("");
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textnote);
        logger.verbose("TextNote.onCreate()");
        final TextView gpsView = (TextView)findViewById(R.id.TextNoteGPS);
        final LocationManager locMan = (LocationManager)getSystemService(LOCATION_SERVICE);
        final String prov = locMan.getBestProvider(new Criteria(), true);
        locMan.requestLocationUpdates(prov, 10000, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras){}
			
			@Override
			public void onProviderEnabled(String provider) {}
			
			@Override
			public void onProviderDisabled(String provider) {}
			
			@Override
			public void onLocationChanged(Location location) 
			{
				gpsView.setText("Longitude: " + location.getLongitude()
						+ "\nLatitude: " + location.getLatitude()
						+ "\nAccuracy: " + location.getAccuracy());
			}
		});
        
        final Button butt = (Button)findViewById(R.id.TextViewTag);
        butt.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent();
				i.setAction("TextNote");
				i.putExtra("note", ((EditText)findViewById(R.id.EditText01)).getText().toString());
				i.putExtra("location", locMan.getLastKnownLocation(prov));
				setResult(Activity.RESULT_OK, i);
				TextNote.this.finish();
			}
		});
    }
}
