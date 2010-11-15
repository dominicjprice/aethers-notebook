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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

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
        	BufferedReader reader = new BufferedReader(new FileReader("sensorservice.log"));
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
            log.warning(e.toString()); 
        }
        catch (JsonMappingException e) {
            log.warning(e.toString());
        }
        catch (IOException e) {
            log.warning(e.toString());
        }

        pm.close();
	}

}
