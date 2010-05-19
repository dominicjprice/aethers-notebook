import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class sensorlog2sql
{
	public enum LogType 
	{
		CELL_LOCATION,
		DATA_CONNECTION_STATE,
		LOCATION,
		SERVICE_STATE,
		SIGNAL_STRENGTH,
		TELEPHONY_STATE,
		AP_SITINGS;
	};
	
	public static void main(String[] args)
	throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/sensor", "root", "29yugbypmoI");
		BufferedReader in = new BufferedReader(
				new FileReader("sensorservice.log"));
		String line;
		while((line = in.readLine()) != null)
		{
			Date date = parseDate(line);
			LogType type = parseLogType(line);
			switch(type)
			{
			case AP_SITINGS:
				insertAPSiting(c, date, parseArray(line));
				break;
			case CELL_LOCATION:
				insertCellLocation(c, date, parseObject(line));
				break;
			case DATA_CONNECTION_STATE:
				insertDataConnectionState(c, date, parseObject(line));
				break;
			case LOCATION:
				insertLocation(c, date, parseObject(line));
				break;
			case SERVICE_STATE:
				insertServiceState(c, date, parseObject(line));
				break;
			case SIGNAL_STRENGTH:
				insertSignalStrength(c, date, parseObject(line));
				break;
			case TELEPHONY_STATE:
				insertTelephonyState(c, date, parseObject(line));
				break;
			}
		}
		in.close();
		c.close();
	}
	
	public static Date parseDate(String line)
	{
		return new Date(Long.parseLong(line.substring(0, line.indexOf(':'))));
	}
	
	public static LogType parseLogType(String line)
	{
		int start = line.indexOf(':') + 1;
		String lt = line.substring(start, line.indexOf(':', start));
		if(lt.equals("CELL_LOCATION"))
			return LogType.CELL_LOCATION;
		if(lt.equals("DATA_CONNECTION_STATE"))
			return LogType.DATA_CONNECTION_STATE;
		if(lt.equals("LOCATION"))
			return LogType.LOCATION;
		if(lt.equals("SERVICE_STATE"))
			return LogType.SERVICE_STATE;
		if(lt.equals("SIGNAL_STRENGTH"))
			return LogType.SIGNAL_STRENGTH;
		if(lt.equals("TELEPHONY_STATE"))
			return LogType.TELEPHONY_STATE;
		if(lt.equals("AP_SITINGS"))
			return LogType.AP_SITINGS;
		throw new RuntimeException("Unknown log type");
	}
	
	public static JSONObject parseObject(String line)
	throws Exception
	{
		String start = line.substring(line.indexOf(':', line.indexOf(':') + 1) + 1);
		return new JSONObject(new JSONTokener(start));
	}
	
	public static JSONArray parseArray(String line)
	throws Exception
	{
		String start = line.substring(line.indexOf(':', line.indexOf(':') + 1) + 1);
		return new JSONArray(new JSONTokener(start));
	}
	
	private static void insertCellLocation(Connection c, Date date, JSONObject obj)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO GsmCellLocation (datetime, cid, lac) values (?, ?, ?)");
		p.setTimestamp(1, new Timestamp(date.getTime()));
		p.setString(2, obj.getString("cid"));
		p.setString(3, obj.getString("lac"));
		p.execute();
	}
	
	private static void insertDataConnectionState(Connection c, Date date, JSONObject obj)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO DataConnectionState (datetime, state) values (?, ?)");
		p.setTimestamp(1, new Timestamp(date.getTime()));
		p.setString(2, obj.getString("state"));
		p.execute();
	}
	
	private static void insertLocation(Connection c, Date date, JSONObject obj)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO Location (datetime, accuracy, altitude, bearing, latitude, longitude, provider, speed, extras) " +
				"values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		p.setTimestamp(1, new Timestamp(date.getTime()));
		if(obj.isNull("accuracy"))
			p.setNull(2, Types.NULL);
		else
			p.setDouble(2, obj.getDouble("accuracy"));
		
		if(obj.isNull("altitude"))
			p.setNull(3, Types.NULL);
		else
			p.setDouble(3, obj.getDouble("altitude"));
		
		if(obj.isNull("bearing"))
			p.setNull(4, Types.NULL);
		else
			p.setDouble(4, obj.getDouble("bearing"));
		
		p.setDouble(5, obj.getDouble("latitude"));
		p.setDouble(6, obj.getDouble("longitude"));
		p.setString(7, obj.getString("provider"));
		
		if(obj.isNull("speed"))
			p.setNull(8, Types.NULL);
		else
			p.setDouble(8, obj.getDouble("speed"));
		
		if(obj.isNull("extras"))
			p.setNull(9, Types.NULL);
		else
			p.setString(9, obj.getString("extras"));
				
		p.execute();
	}
	
	private static void insertServiceState(Connection c, Date date, JSONObject obj)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO ServiceState (datetime, operatorAlphaLong, operatorAlphaShort, operatorNumeric, roaming, state)" +
				" values (?, ?, ?, ?, ?, ?)");
		p.setTimestamp(1, new Timestamp(date.getTime()));
		p.setString(2, obj.getString("operatorAlphaLong"));
		p.setString(3, obj.getString("operatorAlphaShort"));
		if(obj.isNull("operatorNumeric"))
			p.setNull(4, Types.NULL);
		else
			p.setInt(4, Integer.parseInt(obj.getString("operatorNumeric")));
		p.setBoolean(5, obj.getBoolean("roaming"));
		p.setString(6, obj.getString("state"));
		p.execute();
	}
	
	private static void insertSignalStrength(Connection c, Date date, JSONObject obj)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO SignalStrength (datetime, strength) values (?, ?)");
		p.setTimestamp(1, new Timestamp(date.getTime()));
		p.setInt(2, obj.getInt("strength"));
		p.execute();
	}
	
	private static void insertTelephonyState(Connection c, Date date, JSONObject obj)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO TelephonyState (datetime, networkType) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
		p.setTimestamp(1, new Timestamp(date.getTime()));
		p.setString(2, obj.getString("networkType"));			
		p.execute();
		ResultSet rs = p.getGeneratedKeys();
		rs.next();
		long key = rs.getLong(1);
		JSONArray arr = obj.getJSONArray("neighbouringCells");
		for(int i = 0; i < arr.length(); i++)
		{
			insertNeighbouringCell(c, date, key, arr.getJSONObject(i));
		}
	}
	
	private static void insertNeighbouringCell(Connection c, Date date, long foreignKey, JSONObject obj)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO NeighbouringCellInfo (TelephonyState_row, cid, rssi) values (?, ?, ?)");
		p.setLong(1, foreignKey);
		p.setInt(2, obj.getInt("cid"));
		p.setInt(3, obj.getInt("rssi"));
		p.execute();
	}
	
	private static void insertAPSiting(Connection c, Date date, JSONArray arr)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO AccessPointSiting (datetime) values (?)", Statement.RETURN_GENERATED_KEYS);
		p.setTimestamp(1, new Timestamp(date.getTime()));	
		p.execute();
		ResultSet rs = p.getGeneratedKeys();
		rs.next();
		long key = rs.getLong(1);
		for(int i = 0; i < arr.length(); i++)
		{
			insertAccessPoint(c, date, key, arr.getJSONObject(i));
		}
	}
	
	private static void insertAccessPoint(Connection c, Date date, long foreignKey, JSONObject obj)
	throws Exception
	{
		PreparedStatement p = c.prepareStatement("INSERT INTO AccessPoint (AccessPointSiting_row, BSSID, SSID, capabilities, frequency, level) " +
				"values (?, ?, ?, ?, ?, ?)");
		p.setLong(1, foreignKey);
		p.setString(2, obj.getString("BSSID"));
		p.setString(3, obj.getString("SSID"));
		p.setString(4, obj.getString("capabilities"));
		p.setInt(5, obj.getInt("frequency"));
		p.setInt(6, obj.getInt("level"));
		p.execute();
	}
}
