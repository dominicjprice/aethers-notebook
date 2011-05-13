package horizon.aether;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import horizon.android.logging.Logger;
import horizon.aether.R;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageNote
extends Activity
{
	private static final Logger logger = Logger.getLogger(ImageNote.class);
		
	private Camera camera;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagenote);
        logger.verbose("ImageNote.onCreate()");
        final TextView gpsView = (TextView)findViewById(R.id.ImageNoteGPS);
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
        
        final Button butt = (Button)findViewById(R.id.ImageButton);
        butt.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				camera.takePicture(null, null, new Camera.PictureCallback() 
				{
					@Override
					public void onPictureTaken(byte[] data, Camera camera) 
					{
						Intent i = new Intent();
						i.setAction("VoiceNote");
						String outFileName = UUID.randomUUID().toString() + ".jpg";
						try
						{
							BufferedOutputStream out = new BufferedOutputStream(
									new FileOutputStream("/sdcard/geotagger/" + outFileName));
							out.write(data);
							out.close();
						}
						catch(IOException e)
						{
							throw new RuntimeException(e);
						}
						i.putExtra("fileName", outFileName);
						i.putExtra("location", locMan.getLastKnownLocation(prov));
						setResult(Activity.RESULT_OK, i);
						ImageNote.this.finish();
					}
				});
			}
		});
        
        LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout02);
        CameraSurfaceView cview = new CameraSurfaceView(camera = Camera.open(), this);
        LayoutParams lp = ll.getLayoutParams();
        lp.width = 320;
        lp.height = 240;
        ll.setLayoutParams(lp);
        ll.addView(cview);
    }
}
