package horizon.aether;

import java.util.UUID;

import horizon.android.logging.Logger;
import horizon.aether.R;
import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VoiceNote
extends Activity
{
	private static final Logger logger = Logger.getLogger(VoiceNote.class);
		
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicenote);
        logger.verbose("VoiceNote.onCreate()");
        final TextView gpsView = (TextView)findViewById(R.id.VoiceNoteGPS);
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
        
        final Button butt = (Button)findViewById(R.id.VoiceNoteButton);
        butt.setOnClickListener(new View.OnClickListener() 
        {
        	
        	
			@Override
			public void onClick(View v) 
			{
				if(isRecording)
				{
					recorder.stop();
					recorder.reset();
					recorder.release();
					recorder = null;
					
					Intent i = new Intent();
					i.setAction("VoiceNote");
					i.putExtra("fileName", outFileName);
					i.putExtra("location", locMan.getLastKnownLocation(prov));
					setResult(Activity.RESULT_OK, i);
					outFileName = null;
					isRecording = false;
					VoiceNote.this.finish();
				}
				else
				{
					butt.setText("Stop Recording");
					outFileName = UUID.randomUUID().toString() + ".3gp";
					recorder = new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					recorder.setOutputFile("/sdcard/geotagger/" + outFileName);
					try 
					{
						recorder.prepare();
					} 
					catch (Exception e) 
					{
						throw new RuntimeException(e);
					}
					recorder.start();
					isRecording = true;
				}
				
			}
		});
    }
	
	private String outFileName;
	
	private MediaRecorder recorder;
	
	private boolean isRecording = false;
}
