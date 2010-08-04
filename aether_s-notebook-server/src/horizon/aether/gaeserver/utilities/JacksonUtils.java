package horizon.aether.gaeserver.utilities;

import horizon.aether.gaeserver.model.CellLocationBlob;
import horizon.aether.gaeserver.model.DataConnectionStateBlob;
import horizon.aether.gaeserver.model.Entry;
import horizon.aether.gaeserver.model.EntryPack;
import horizon.aether.gaeserver.model.IBlob;
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
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 * Helper class for processing log entries using Jackson. 
 */
public class JacksonUtils {
   
    private static final Logger log = Logger.getLogger(JacksonUtils.class.getName());
    
    /**
     * Parses a string and creates all the Entry objects using Jackson's full data binding. 
     * @param contents
     * @return
     */
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
                IBlob blob = null;
                if (identifier.equals(Entry.AP_SITINGS_BLOB)) {
                    ArrayList<Wifi> networks = mapper.readValue(dataBlob, TypeFactory.collectionType(ArrayList.class, Wifi.class));                    
                    blob = new WifiBlob(networks);
                }
                else if (identifier.equals(Entry.CELL_LOCATION_BLOB)) {
                    blob = (CellLocationBlob) mapper.readValue(dataBlob, CellLocationBlob.class);
                }
                else if (identifier.equals(Entry.DATA_CONNECTION_STATE_BLOB)) {
                    blob = (DataConnectionStateBlob) mapper.readValue(dataBlob, DataConnectionStateBlob.class);
                }
                else if (identifier.equals(Entry.SERVICE_STATE_BLOB)) {
                    blob = (ServiceStateBlob) mapper.readValue(dataBlob, ServiceStateBlob.class);
                }
                else if (identifier.equals(Entry.SIGNAL_STRENGTH_BLOB)) {
                    blob = (SignalStrengthBlob) mapper.readValue(dataBlob, SignalStrengthBlob.class);
                }
                else if (identifier.equals(Entry.TELEPHONY_STATE_BLOB)) {
                    blob = (TelephonyStateBlob) mapper.readValue(dataBlob, TelephonyStateBlob.class);
                }
                
                // construct entry
                Entry e = new Entry(timestamp, identifier, location, blob.getKey());
                
                // add them in the pack
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
    
    /**
     * Serialises a list of entries to a list of JSON strings.
     * @param entries
     * @return
     */
    public static List<String> generateEntries(List<Entry> entries) {
        List<String> results = new ArrayList<String>();
        ObjectMapper mapper = new ObjectMapper();
        for (Entry entry : entries) {
            try {
                results.add(mapper.writeValueAsString(entry));
            } catch (JsonGenerationException e) {
                log.warning(e.toString());
            } catch (JsonMappingException e) {
                log.warning(e.toString());
            } catch (IOException e) {
                log.warning(e.toString());
            }
        }
        
        return results;
    }
}
