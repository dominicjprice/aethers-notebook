package horizon.aether.gaeserver.model;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DataConnectionStateEntry {
    
    public static final String IDENTIFIER = "DATA_CONNECTION_STATE";
    
    public static final String DATA_CONNECTED = "DATA_CONNECTED";
    public static final String DATA_CONNECTING = "DATA_CONNECTING";
    public static final String DATA_DISCONNECTED = "DATA_DISCONNECTED";
    public static final String DATA_SUSPENDED = "DATA_SUSPENDED";
    public static final String DATA_UNKNOWN = "DATA_UNKNOWN";

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long timestamp;
    
    @Persistent(defaultFetchGroup = "true")
    private Location location;

    @Persistent
    private String state;

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
     * Gets the location.
     * @return
     */
    public Location getLocation() { return this.location; }

    /**
     * Get the state string.
     * @return
     */
    public String getState() { return this.state; }

    /**
     * Set the state string.
     * @param state
     */
    public void setState(String state) { this.state = state; }

    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param state
     */
    public DataConnectionStateEntry(long timestamp, Location location, String state) { 
        this.timestamp = timestamp;
        this.location = location;
        this.state = state; 
    }

    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param blob
     */
    public DataConnectionStateEntry(long timestamp, Location location, DataConnectionStateBlob blob) {
        this.timestamp = timestamp;
        this.location = location;
        this.state = blob.getState();
    }
    
    /**
     * Default constructor.
     */
    public DataConnectionStateEntry() { }

}
