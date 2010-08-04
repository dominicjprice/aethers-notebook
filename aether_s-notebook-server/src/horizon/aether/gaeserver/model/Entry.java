package horizon.aether.gaeserver.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Represents a logging entry. A logging entry has the following fields:
 *   - timestamp (long)
 *   - identifier (String)
 *   - location (Location)
 *   - dataBlob (Key).
 *   
 * The datablob is represented by a key object instead of a Blob object because 
 * the App Engine does not support polymorphic relationships.  
 */
@PersistenceCapable
public class Entry {

    public static final String CELL_LOCATION_BLOB = "CELL_LOCATION";
    public static final String DATA_CONNECTION_STATE_BLOB = "DATA_CONNECTION_STATE";
    public static final String SERVICE_STATE_BLOB = "SERVICE_STATE";
    public static final String SIGNAL_STRENGTH_BLOB = "SIGNAL_STRENGTH";
    public static final String TELEPHONY_STATE_BLOB = "TELEPHONY_STATE";
    public static final String AP_SITINGS_BLOB = "AP_SITINGS";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long timestamp;
    
    @Persistent
    private String identifier;
    
    @Persistent
    private Location location;
       
    @Persistent
    private Key dataBlob;
    
    /**
     * Gets the key.
     * @return
     */
    public Key getKey() { return this.key; }
    
    /**
     * Gets the timestamp.
     * @return
     */
    public long getTimestamp() { return this.timestamp; }
    
    /**
     * Gets the identifier.
     * @return
     */
    public String getIdentifier() { return this.identifier; }
    
    /**
     * Gets the location.
     * @return
     */
    public Location getLocation() { return this.location; }
    
    /**
     * Gets the datablob key.
     * @return
     */
    public Key getDataBlob() { return this.dataBlob; }

    /**
     * Constructor.
     * @param timestamp
     * @param identifier
     * @param location
     * @param dataBlob
     */
    public Entry(long timestamp, String identifier, Location location, Key dataBlob) {
        this.timestamp = timestamp;
        this.identifier = identifier;
        this.location = location;
        this.dataBlob = dataBlob;
    }
    
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.timestamp);
        sb.append("_");
        sb.append(this.identifier);
        sb.append("_");        
        sb.append(this.location.toString());
        sb.append("_");
        sb.append(this.dataBlob.toString());
        return sb.toString();
    }

}
