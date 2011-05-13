import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.json.JSONObject;
import org.json.JSONTokener;

public class geotag2kml 
{
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	
	public static void main(String[] args)
	throws Exception
	{
		BufferedReader in = new BufferedReader(new FileReader("geotagger.log"));
		String line;
		System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		System.out.println("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
		System.out.println("<Document>");
		System.out.println("<Style id=\"transBluePoly\">");
		System.out.println("<LineStyle>");
		System.out.println("<width>1.5</width>");
		System.out.println("</LineStyle>");
		System.out.println("<PolyStyle>");
		System.out.println("<color>7dff0000</color>");
		System.out.println("</PolyStyle>");
		System.out.println("</Style>");
		while((line = in.readLine()) != null)
		{
			String date =
					DATE_FORMAT.format(
							new Date(
									Long.parseLong(
											line.substring(0, line.indexOf(":")))));
			
			String json = line.substring(line.indexOf(':') + 1, line.lastIndexOf(':'));
			JSONObject obj = new JSONObject(new JSONTokener(json));
			System.out.println("<Placemark>");
			System.out.println("<styleUrl>#transBluePoly</styleUrl>");
			System.out.println("<name>" + date + "</name>");
			
			String last = line.substring(line.lastIndexOf(":") + 1);
			if(last.startsWith("VOICE"))
			{
				System.out.println("<description>Audio: http://www.mrl.nott.ac.uk/~djp/woolich/" 
						+ last.substring(last.indexOf('=') + 1) + "</description>");
			}
			else if(last.startsWith("IMAGE"))
			{
				System.out.println("<description>Photo: http://www.mrl.nott.ac.uk/~djp/woolich/" 
						+ last.substring(last.indexOf('=') + 1) + "</description>");
			}
			else if(last.startsWith("VIDEO"))
			{
				System.out.println("<description>Video: http://www.mrl.nott.ac.uk/~djp/woolich/" 
						+ last.substring(last.indexOf('=') + 1) + "</description>");
			}
			else
				System.out.println("<description>" + last.substring(last.indexOf('=') + 1) + "</description>");
			System.out.println("<Point>");
			System.out.println("<coordinates>" + obj.getDouble("longitude") 
					+ "," + obj.getDouble("latitude") + "," 
					+ (obj.has("altitude") && obj.get("altitude") != null ? obj.getDouble("altitude") : 0.0 ) 
					+ "</coordinates>");
			System.out.println("</Point>");
			getCircle(obj.getDouble("longitude"), obj.getDouble("latitude"), obj.getDouble("accuracy"));
			System.out.println("</Placemark>");
		}
		System.out.println("</Document>");
		System.out.println("</kml>");
		in.close();
	}
	
	public static String getCircle(double longitude, double latitude, double accuracy) 
	throws IOException, InterruptedException, ExecutionException
	{
		ProcessBuilder pb = new ProcessBuilder//("echo", "hello");
				("python", "/home/pszdp1/projects/horizon/eclipse/geotag2kml/kmlcircle.py",
				"-h", String.valueOf(longitude), "-v", String.valueOf(latitude),
				"-r", String.valueOf(accuracy), "-s", "25");
		pb.redirectErrorStream();
		Process p = pb.start();
		final BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() 
		{
			@Override
			public Void call() 
			throws Exception 
			{
				String line;
				while((line = in.readLine()) != null)
					System.out.println(line);
				in.close();
				return null;
			}
		});
		new Thread(task).start();
		p.waitFor();
		task.get();
		return "";
	}
}
