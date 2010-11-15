package horizon.aether.geotag2kml;

import horizon.aether.model.SignalStrengthOnLocationChangeEntry;
import horizon.aether.utilities.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import sun.rmi.runtime.Log;

public class Geotag2kml 
{
	private static final Logger log = Logger.getLogger(Geotag2kml.class.getName());

	@SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception
	{
		Properties properties = new Properties();
		properties.setProperty("javax.jdo.PersistenceManagerFactoryClass",
		                "org.datanucleus.jdo.JDOPersistenceManagerFactory");
		properties.setProperty("javax.jdo.option.ConnectionDriverName","com.mysql.jdbc.Driver");
		properties.setProperty("javax.jdo.option.ConnectionURL","jdbc:mysql://localhost:3306/aether");
		properties.setProperty("javax.jdo.option.ConnectionUserName","aether");
		properties.setProperty("javax.jdo.option.ConnectionPassword","WU5sVPnf8Jcf2sza");
		//properties.setProperty("javax.jdo.option.NontransactionalRead","true");
		//properties.setProperty("javax.jdo.option.NontransactionalWrite","true");
		properties.setProperty("javax.jdo.option.Mapping", "mysql");
		properties.setProperty("datanucleus.autoCreateSchema","true");
		properties.setProperty("datanucleus.storeManagerType", "rdbms");
		properties.setProperty("datanucleus.rdbms.stringDefaultLength", "255");
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(properties);
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// prepare query
		Query query = pm.newQuery(SignalStrengthOnLocationChangeEntry.class);
		//query.setRange(0, 6000);

		// execute
		List<SignalStrengthOnLocationChangeEntry> entries = (List<SignalStrengthOnLocationChangeEntry>) query.execute();
		log.log(Log.BRIEF, entries.size() + "found");
		
		FileWriter fw = new FileWriter("map.kml"); 
		
		BufferedWriter bfw = new BufferedWriter(fw);
		bfw.append("<kml xmlns='http://earth.google.com/kml/2.2'>\n");
		bfw.append("	<Document>\n");
		bfw.append("		<Snippet><![CDATA[<p>Created by Horizon.Mark Paxton</p>]]></Snippet>\n");
		bfw.append("		<Style id='gv_waypoint_normal'>\n");
		bfw.append("				<BalloonStyle>\n");
		bfw.append("				<text><![CDATA[<p>Description</p>${title}]]></text>\n");
		bfw.append("			</BalloonStyle>\n");
		bfw.append("			<IconStyle>\n");
		bfw.append("				<Icon>\n");
		bfw.append("					<href>http://maps.google.ca/mapfiles/kml/pal4/icon56.png</href>\n");
		bfw.append("				</Icon>\n");
		bfw.append("				<color>FFFFFFFF</color>\n");
		bfw.append("				<hotSpot x='0.5' xunits='fraction' y='0.5' yunits='fraction' />\n");
		bfw.append("			</IconStyle>\n");
		bfw.append("			<LabelStyle>\n");
		bfw.append("				<color>FFFFFFFF</color>\n");
		bfw.append("				<scale>1</scale>\n");
		bfw.append("			</LabelStyle>\n");
		bfw.append("		</Style>\n");
		bfw.append("		 <Style id='gv_waypoint_highlight'>\n");
		bfw.append("			<BalloonStyle>\n");
		bfw.append("			  <text><![CDATA[<p align='left' style='white-space:nowrap;'><font size='+1'><b>$[name]</b></font></p> <p align='left'>$[description]</p>]]></text>\n");
		bfw.append("			</BalloonStyle>\n");
		bfw.append("			<IconStyle>\n");
		bfw.append("			  <Icon>\n");
		bfw.append("				 <href>http://maps.google.ca/mapfiles/kml/pal4/icon56.png</href>\n");
		bfw.append("			  </Icon>\n");
		bfw.append("			  <color>FFFFFFFF</color>\n");
		bfw.append("			  <hotSpot x='0.5' xunits='fraction' y='0.5' yunits='fraction' />\n");
		bfw.append("			  <scale>1.2</scale>\n");
		bfw.append("			</IconStyle>\n");
		bfw.append("			<LabelStyle>\n");
		bfw.append("			  <color>FFFFFFFF</color>\n");
		bfw.append("			  <scale>1</scale>\n");
		bfw.append("			</LabelStyle>\n");
		bfw.append("		 </Style>\n");
		bfw.append("		 <Style id='gv_trackpoint_normal'>\n");
		bfw.append("			<BalloonStyle>\n");
		bfw.append("			  <text><![CDATA[<p align='left' style='white-space:nowrap;'><font size='+1'><b>$[name]</b></font></p> <p align='left'>$[description]</p>]]></text>\n");
		bfw.append("			</BalloonStyle>\n");
		bfw.append("			<IconStyle>\n");
		bfw.append("			  <Icon>\n");
		bfw.append("				 <href>http://maps.google.ca/mapfiles/kml/pal2/icon26.png</href>\n");
		bfw.append("			  </Icon>\n");
		bfw.append("			  <scale>0.3</scale>\n");
		bfw.append("			</IconStyle>\n");
		bfw.append("			<LabelStyle>\n");
		bfw.append("			  <scale>0</scale>\n");
		bfw.append("			</LabelStyle>\n");
		bfw.append("		 </Style>\n");
		bfw.append("		 <Style id='gv_trackpoint_highlight'>\n");
		bfw.append("			<BalloonStyle>\n");
		bfw.append("			  <text><![CDATA[<p align='left' style='white-space:nowrap;'><font size='+1'><b>$[name]</b></font></p> <p align='left'>$[description]</p>]]></text>\n");
		bfw.append("			</BalloonStyle>\n");
		bfw.append("			<IconStyle>\n");
		bfw.append("			  <Icon>\n");
		bfw.append("				 <href>http://maps.google.ca/mapfiles/kml/pal2/icon26.png</href>\n");
		bfw.append("			  </Icon>\n");
		bfw.append("			  <scale>0.4</scale>\n");
		bfw.append("			</IconStyle>\n");
		bfw.append("			<LabelStyle>\n");
		bfw.append("			  <scale>1</scale>\n");
		bfw.append("			</LabelStyle>\n");
		bfw.append("		 </Style>\n");
		bfw.append("		 <StyleMap id='gv_waypoint'>\n");
		bfw.append("			<Pair>\n");
		bfw.append("			  <key>normal</key>\n");
		bfw.append("			  <styleUrl>#gv_waypoint_normal</styleUrl>\n");
		bfw.append("			</Pair>\n");
		bfw.append("			<Pair>\n");
		bfw.append("			  <key>highlight</key>\n");
		bfw.append("			  <styleUrl>#gv_waypoint_highlight</styleUrl>\n");
		bfw.append("			</Pair>\n");
		bfw.append("		</StyleMap>\n");
		bfw.append("		<StyleMap id='gv_trackpoint'>\n");
		bfw.append("			<Pair>\n");
		bfw.append("				<key>normal</key>\n");
		bfw.append("				<styleUrl>#gv_trackpoint_normal</styleUrl>\n");
		bfw.append("			</Pair>\n");
		bfw.append("			<Pair>\n");
		bfw.append("				<key>highlight</key>\n");
		bfw.append("				<styleUrl>#gv_trackpoint_highlight</styleUrl>\n");
		bfw.append("			</Pair>\n");
		bfw.append("		 </StyleMap>\n");
		bfw.append("		<name><![CDATA[Geotagger app data]]></name>\n");
		bfw.append("		<open>1</open>\n");
		bfw.append("		<visibility>1</visibility>\n");
		bfw.append("		<Placemark>\n");
		bfw.append("			<name><![CDATA[<p>Route</p>]]></name>\n");
		bfw.append("			<description><![CDATA[<p>Rough route based on joining GPS readings</p>]]></description>\n");
		bfw.append("			<MultiGeometry>\n");
		bfw.append("				<LineString>\n");
		bfw.append("					<altitudeMode>clampToGround</altitudeMode>\n");
		bfw.append("					<coordinates></coordinates>\n");
		bfw.append("					<tessellate>1</tessellate>\n");
		bfw.append("				</LineString>\n");
		bfw.append("			</MultiGeometry>\n");
		bfw.append("			<Snippet></Snippet>\n");
		bfw.append("			<Style>\n");
		bfw.append("				<LineStyle>\n");
		bfw.append("					<color>ff00008b</color>\n");
		bfw.append("					<width>4</width>\n");
		bfw.append("				</LineStyle>\n");
		bfw.append("			</Style>\n");
		bfw.append("		</Placemark>\n");
		bfw.append("		<Folder id='track 1 points'>\n");
		bfw.append("			<name>" + entries.size() + " Points</name>		");

		String entryDateTime="";
		String colour = "";
		String networkColour = "";
		for(SignalStrengthOnLocationChangeEntry entry : entries)
		{
			entryDateTime = StringUtils.toDateTimeString(entry.getTimestamp());
			bfw.append("	<Placemark>\n");
			bfw.append("				<Point>\n");
			bfw.append("					<altitudeMode>clampToGround</altitudeMode>\n");
			bfw.append("					<coordinates>" + entry.getLocation().getLongitude() + ", " + entry.getLocation().getLatitude() + "</coordinates>\n");
			bfw.append("					<extrude>1</extrude>\n");
			bfw.append("				</Point>\n");
			bfw.append("				<name>" + entryDateTime + "</name>\n");
			bfw.append("				<description><![CDATA[<b>Signal Strength Reading</b><br/>\n");
			bfw.append("				<i>Network Type:</i>" + entry.getNetworkType() + "<br/>\n");
			bfw.append("				<i>Signal Strength:</i>" + entry.getSignalStrength() + "<br/>\n");
			bfw.append("				<i>Latitude:</i>" + entry.getLocation().getLatitude() + "&#176;<br/>\n");
			bfw.append("				<i>Longitude:</i>" + entry.getLocation().getLongitude() + "&#176;<br/>\n");
			bfw.append("				<i>Elevation:</i>" + entry.getLocation().getAltitude() + "m<br/>\n");
			bfw.append("				<i>Speed:</i>" + entry.getLocation().getSpeed() + "km/h<br/>\n");
			bfw.append("				<i>Time:</i> " + entryDateTime + " ]]></description>\n");
			bfw.append("				<TimeStamp>\n");
			bfw.append("					<when>" + entryDateTime + "+01:00</when>\n");
			bfw.append("				</TimeStamp>\n");
			bfw.append("				<styleUrl>#gv_trackpoint</styleUrl>\n");
			bfw.append("				<Snippet></Snippet>\n");
			bfw.append("				<Style>\n");			
			/* Work out the colour for the signal strength icon based on network type and strength.
			 Signal strength is usually between 0 and 31, with 99 reserved for 'unknown' as follows:
			    0 -113 dBm or less
				1 -111 dBm
				2...30 -109... -53 dBm
				31 -51 dBm or greater
				99 not known or not detectable
			If it's unknown, then set colour specially otherwise go on to use network type and strength 
		   */
			if (entry.getSignalStrength()==99)
			{
				 colour = "33FFFFFF";
			}
			else if (entry.getSignalStrength()==0)
			{
				 colour = "FF000000";
			} 
			else if(entry.getSignalStrength() == 1)
			{
				networkColour = "33";
			}
			else if (entry.getSignalStrength()<5)
			{
				networkColour = "40";
			}
			else if (entry.getSignalStrength()<10)
			{
				networkColour = "50";
			}
			else if (entry.getSignalStrength()<15)
			{
				networkColour = "70";
			}
			else if (entry.getSignalStrength()<20)
			{
				networkColour = "90";
			}
			else if (entry.getSignalStrength()<25)
			{
				networkColour = "BB";
			}
			else if (entry.getSignalStrength()<31)
			{
				networkColour = "DD";
			}
			else
			{
				networkColour = "FF";
			}
			
			if (entry.getNetworkType().contentEquals("NETWORK_TYPE_UMTS"))
			{
				colour = "FF" + networkColour + "0000";
			}
			else if (entry.getNetworkType().contentEquals("NETWORK_TYPE_GPRS"))
			{
				colour = "FF00" + networkColour + "00";
			}
			else if (entry.getNetworkType().contentEquals("NETWORK_TYPE_EDGE"))
			{
				colour = "FF0000" + networkColour;
			}
			else
			{
				colour = "FF" + networkColour + networkColour + networkColour;
			}
			bfw.append("			<IconStyle>\n");
			bfw.append("						<color>" + colour + "</color>\n");
			bfw.append("				 	</IconStyle>\n");
			bfw.append("				 	<LabelStyle>\n");
			bfw.append("						<color>FF0073E6</color>\n");
			bfw.append("				 	</LabelStyle>\n");
			bfw.append("				 	<LineStyle>\n");
			bfw.append("						<color>990073E6</color>\n");
			bfw.append("						<width>2</width>\n");
			bfw.append("				 	</LineStyle>\n");
			bfw.append("				</Style>\n");
			bfw.append("			</Placemark>\n");
		}
		bfw.append("				</Folder>\n");
		bfw.append("			</Document>\n");
		bfw.append("			</kml>\n");
		
		bfw.close();
		fw.close();
	}
}

