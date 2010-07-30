package horizon.aether.gaeserver.utilities;

import horizon.aether.gaeserver.model.Blob;
import horizon.aether.gaeserver.model.CellLocationBlob;
import horizon.aether.gaeserver.model.DataConnectionStateBlob;
import horizon.aether.gaeserver.model.Entry;
import horizon.aether.gaeserver.model.EntryPack;
import horizon.aether.gaeserver.model.Location;
import horizon.aether.gaeserver.model.ServiceStateBlob;
import horizon.aether.gaeserver.model.SignalStrengthBlob;
import horizon.aether.gaeserver.model.TelephonyStateBlob;
import horizon.aether.gaeserver.model.Wifi;
import horizon.aether.gaeserver.model.WifiBlob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 * Helper class for parsing a blob of log entries using the Jackson processor.
 */
public class JacksonUtils {

    private static final Logger log = Logger.getLogger(JacksonUtils.class.getName());
    
    @SuppressWarnings("unchecked")
    public static EntryPack parseEntries(String contents) {
        EntryPack entryPack = new EntryPack();
        
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            BufferedReader reader = new BufferedReader(new StringReader(contents));            
            String strLine;
            
            while ((strLine = reader.readLine()) != null) {
                Map<String,Object> entry = mapper.readValue(strLine, Map.class);
                
                long timestamp = Long.parseLong(entry.get("timestamp").toString());
                String identifier = entry.get("identifier").toString();
                Location location = mapper.readValue(entry.get("location").toString(), Location.class);
                String dataBlob = entry.get("dataBlob").toString();
                
                // construct blob
                Blob blob = null;
                if (identifier.equals(Blob.AP_SITINGS_BLOB)) {
                    ArrayList<Wifi> networks = mapper.readValue(dataBlob, TypeFactory.collectionType(ArrayList.class, Wifi.class));                    
                    blob = new WifiBlob(networks);
                }
                else if (identifier.equals(Blob.CELL_LOCATION_BLOB)) {
                    blob = (CellLocationBlob) mapper.readValue(dataBlob, CellLocationBlob.class);
                }
                else if (identifier.equals(Blob.DATA_CONNECTION_STATE_BLOB)) {
                    blob = (DataConnectionStateBlob) mapper.readValue(dataBlob, DataConnectionStateBlob.class);
                }
                else if (identifier.equals(Blob.SERVICE_STATE_BLOB)) {
                    blob = (ServiceStateBlob) mapper.readValue(dataBlob, ServiceStateBlob.class);
                }
                else if (identifier.equals(Blob.SIGNAL_STRENGTH_BLOB)) {
                    blob = (SignalStrengthBlob) mapper.readValue(dataBlob, SignalStrengthBlob.class);
                }
                else if (identifier.equals(Blob.TELEPHONY_STATE_BLOB)) {
                    blob = (TelephonyStateBlob) mapper.readValue(dataBlob, TelephonyStateBlob.class);
                }
                
                // construct entry
                Entry e = new Entry(timestamp, identifier, location, blob.getKey());
                
                // put them in the pack
                entryPack.add(e, blob);
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
        
        return entryPack;
    }
}
