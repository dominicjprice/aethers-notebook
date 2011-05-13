package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Represents a log entry that has the following fields:
 *    - timestamp
 *    - identifier
 *    - location
 *    - dataBlob
 *    
 * The string representation of a LogEntry is a JSON string. 
 */
public class LogEntry {

    private long timestamp;
    private String identifier;
    private String location;
    private String dataBlob;
    
    /**
     * Gets the entry timestmap.
     * @return The timestmap.
     */
    public long getTimestamp() { 
        return this.timestamp; 
    }

    /**
     * Gets the entry identifier.
     * @return The identifier.
     */
    public String identifier() { 
        return this.identifier; 
    }

    /**
     * Gets the entry location.
     * @return The location.
     */
    public String location() { 
        return this.location; 
    }

    /**
     * Gets the entry datablob.
     * @return The datablob.
     */
    public String dataBlob() { 
        return this.dataBlob; 
    }
    
    /**
     * Constructor.
     * @param timestamp
     * @param identifier
     * @param location
     * @param dataBlob
     */
    public LogEntry(long timestamp, String identifier, String location, String dataBlob) {
        this.timestamp = timestamp;
        this.identifier = identifier;
        this.location = location;
        this.dataBlob = dataBlob;
    }
    
    /**
     * Returns a JSON string representation of the object.
     */
    @Override
    public String toString() {
        JSONStringer data = new JSONStringer();
        try {
            data.object();
            data.key("timestamp");
            data.value(timestamp);
            data.key("identifier");
            data.value(identifier);
            data.key("location");
            data.value(location);
            data.key("dataBlob");
            data.value(dataBlob);
            data.endObject();
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        
        return data.toString();
    }
}
