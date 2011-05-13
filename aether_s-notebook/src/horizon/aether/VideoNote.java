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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoNote
extends Activity
{
	private static final Logger logger = Logger.getLogger(VideoNote.class);
	
	private MediaRecorder mediaRecorder;
	
	private String fileName = UUID.randomUUID().toString() + ".mp4";
	
	private boolean isRecording = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videonote);
        logger.verbose("VideoNote.onCreate()");
        final TextView gpsView = (TextView)findViewById(R.id.VideoNoteGPS);
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
        
        final Button butt = (Button)findViewById(R.id.VideoButton);
        butt.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				if(!isRecording)
				{
					butt.setText("Stop Recording");
					isRecording = true;
					mediaRecorder.start();
				}
				else
				{
					mediaRecorder.stop();
					Intent i = new Intent();
					i.setAction("VideoNote");
					i.putExtra("fileName", fileName);
					i.putExtra("location", locMan.getLastKnownLocation(prov));
					setResult(Activity.RESULT_OK, i);
					isRecording = false;
					VideoNote.this.finish();
				}
			}
		});
        
        LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout02);
        SurfaceView sv = new SurfaceView(this);
        sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        mediaRecorder = new MediaRecorder();
        
        sv.getHolder().addCallback(new SurfaceHolder.Callback() 
        {	
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) 
			{
				mediaRecorder.release();
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) 
			{	
				mediaRecorder.setPreviewDisplay(holder.getSurface());
				mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				mediaRecorder.setVideoSize(320, 240);
				mediaRecorder.setVideoFrameRate(15);
				mediaRecorder.setOutputFile("/sdcard/geotagger/" + fileName);
				try 
				{
					mediaRecorder.prepare();
				}
				catch(Exception e) 
				{
					throw new RuntimeException(e);
				}
			}
			
			@Override
			public void surfaceChanged(
					SurfaceHolder holder, int format, int width, int height) 
			{	
			}
		});
        LayoutParams lp = ll.getLayoutParams();
        lp.width = 320;
        lp.height = 240;
        ll.setLayoutParams(lp);
        ll.addView(sv);
    }
}
