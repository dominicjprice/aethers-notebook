package horizon.aether.SensorLog2sql;

import horizon.aether.model.CellLocationBlob;
import horizon.aether.model.CellLocationEntry;
import horizon.aether.model.DataConnectionStateBlob;
import horizon.aether.model.DataConnectionStateEntry;
import horizon.aether.model.Location;
import horizon.aether.model.ServiceStateBlob;
import horizon.aether.model.ServiceStateEntry;
import horizon.aether.model.SignalStrengthBlob;
import horizon.aether.model.SignalStrengthEntry;
import horizon.aether.model.SignalStrengthOnLocationChangeBlob;
import horizon.aether.model.SignalStrengthOnLocationChangeEntry;
import horizon.aether.model.TelephonyStateBlob;
import horizon.aether.model.TelephonyStateEntry;
import horizon.aether.model.Wifi;
import horizon.aether.model.WifiEntry;
import horizon.aether.utilities.CompressionUtils;
import horizon.aether.utilities.CompressionUtils.CompressionType;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

public class SensorLog2sql
{
    private static final Logger log = Logger.getLogger(SensorLog2sql.class.getName());

	@SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception
	{
		CompressionType compressionType = CompressionType.NONE;    	
    	/*if(args.length==0)
    	{
    		throw new Exception("No input file specified!");
    	}
    	String filenameArg = args[0];
    	if(args.length > 1)
    	{
    		String compressionTypeArg = args[1];
	    	if(compressionTypeArg.contentEquals("none"))
	    	{
	    		compressionType = CompressionType.NONE;
	    	}
	    	else if (compressionTypeArg.contentEquals("gz"))
	    	{
	    		compressionType = CompressionType.GZIP;
	    	}
	    	else if (compressionTypeArg.contentEquals("dfl"))
	    	{
	    		compressionType = CompressionType.DEFAULT;
	    	}
    	}
    	String sourceFileName = filenameArg;*/
    	String sourceFileName = "sensorservice.log";
    	//Set up the log4j log to log to console
		/* @TODO Change this to use the config file...*/
    	BasicConfigurator.configure();
		log.setLevel(Level.ERROR);
		
		Properties properties = new Properties();
		properties.setProperty("javax.jdo.PersistenceManagerFactoryClass",
		                "org.datanucleus.jdo.JDOPersistenceManagerFactory");
		properties.setProperty("javax.jdo.option.ConnectionDriverName","com.mysql.jdbc.Driver");
		properties.setProperty("javax.jdo.option.ConnectionURL","jdbc:mysql://localhost:3306/aether");
		properties.setProperty("javax.jdo.option.ConnectionUserName","aether");
		properties.setProperty("javax.jdo.option.ConnectionPassword","WU5sVPnf8Jcf2sza");
		properties.setProperty("javax.jdo.option.Mapping", "mysql");
		properties.setProperty("datanucleus.autoCreateSchema","true");
		properties.setProperty("datanucleus.storeManagerType", "rdbms");
		properties.setProperty("datanucleus.rdbms.stringDefaultLength", "255");
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(properties);
		
		PersistenceManager pm = pmf.getPersistenceManager();
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
	    	BufferedInputStream stream = new BufferedInputStream(new FileInputStream(sourceFileName));
	    	OutputStream uncompressedStream = new ByteArrayOutputStream();
            CompressionUtils.uncompress(stream, uncompressedStream, compressionType);

	        BufferedReader reader = new BufferedReader(new StringReader(uncompressedStream.toString()));            
            String strLine;
            
            while ((strLine = reader.readLine()) != null) {
                Map<String,Object> entry = mapper.readValue(strLine, Map.class);
                
                // timestamp
                long timestamp = Long.parseLong(entry.get("timestamp").toString());
                
                // identifier
                String identifier = entry.get("identifier").toString();
                
                // location
                Location location = null;
                if (entry.get("location") != null) {
                    location = mapper.readValue(entry.get("location").toString(), Location.class);
                }
                
                // blob data
                String dataBlob = entry.get("dataBlob").toString();
                if (identifier.equals(WifiEntry.IDENTIFIER)) {
                    ArrayList<Wifi> networks = mapper.readValue(dataBlob, TypeFactory.collectionType(ArrayList.class, Wifi.class));                    
                    pm.makePersistent(new WifiEntry(timestamp, location, networks));
                }
                else if (identifier.equals(CellLocationEntry.IDENTIFIER)) {
                    CellLocationBlob blob = (CellLocationBlob) mapper.readValue(dataBlob, CellLocationBlob.class);
                    pm.makePersistent(new CellLocationEntry(timestamp, location, blob));
                }
                else if (identifier.equals(DataConnectionStateEntry.IDENTIFIER)) {
                    DataConnectionStateBlob blob = (DataConnectionStateBlob) mapper.readValue(dataBlob, DataConnectionStateBlob.class);
                    pm.makePersistent(new DataConnectionStateEntry(timestamp, location, blob));
                }
                else if (identifier.equals(ServiceStateEntry.IDENTIFIER)) {
                    ServiceStateBlob blob = (ServiceStateBlob) mapper.readValue(dataBlob, ServiceStateBlob.class);
                    pm.makePersistent(new ServiceStateEntry(timestamp, location, blob));
                }
                else if (identifier.equals(SignalStrengthEntry.IDENTIFIER)) {
/*                    SignalStrengthBlob blob = (SignalStrengthBlob) mapper.readValue(dataBlob, SignalStrengthBlob.class);
                    pm.makePersistent(new SignalStrengthEntry(timestamp, location, blob));
                }
                else if (identifier.equals(SignalStrengthOnLocationChangeEntry.IDENTIFIER)) {*/
                    SignalStrengthOnLocationChangeBlob blob = (SignalStrengthOnLocationChangeBlob) mapper.readValue(dataBlob, SignalStrengthOnLocationChangeBlob.class);
                    pm.makePersistent(new SignalStrengthOnLocationChangeEntry(timestamp, location, blob));
                    SignalStrengthBlob blob2 = new SignalStrengthBlob();
                    blob2.setStrength(blob.getSignalStrength());
                    pm.makePersistent(new SignalStrengthEntry(timestamp, location, blob2));
                }
                else if (identifier.equals(TelephonyStateEntry.IDENTIFIER)) {
                    TelephonyStateBlob blob = (TelephonyStateBlob) mapper.readValue(dataBlob, TelephonyStateBlob.class);
                    pm.makePersistent(new TelephonyStateEntry(timestamp, location, blob));
                }
            }
        }
        catch (JsonParseException e) { 
            log.error(e.toString(), e); 
        }
        catch (JsonMappingException e) {
            log.error(e.toString(), e); 
        }
        catch (IOException e) {
            log.error(e.toString(), e); 
        }

        pm.close();
	}

}
