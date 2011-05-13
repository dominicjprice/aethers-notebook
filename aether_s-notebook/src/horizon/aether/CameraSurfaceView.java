package horizon.aether;

import horizon.android.logging.Logger;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView 
extends SurfaceView
implements SurfaceHolder.Callback
{
	private static final Logger logger = Logger.getLogger(CameraSurfaceView.class);
	
	private Camera camera;
	
	public CameraSurfaceView(Camera camera, Context context) 
	{
		super(context);
		this.camera = camera;
		SurfaceHolder holder = getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);
	}

	@Override
	public void surfaceChanged(
			SurfaceHolder holder, int format, int width, int height)
	{
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		try 
		{
			camera.setPreviewDisplay(getHolder());
		}
		catch(IOException e) 
		{
			throw new RuntimeException(e);
		}
		camera.startPreview();
		camera.autoFocus(new Camera.AutoFocusCallback() 
		{	
			@Override
			public void onAutoFocus(boolean success, Camera camera) 
			{
				logger.debug("Camera is focused");
			}
		});
		logger.debug("Camera preview started");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		camera.stopPreview();
		camera.release();
	}
}
