package horizon.aether.gaeserver.model;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Class that represents a service state entry. Such an entry,
 * is described by the following: 
 *   - operatorAlphaLong (String)
 *   - operatorAlphaShort (String)
 *   - operatorNumeric (String)
 *   - roaming (boolean)
 *   - state (String)
 */
@PersistenceCapable
public class ServiceStateEntry {
    
    public static final String IDENTIFIER = "SERVICE_STATE";
    
    public static final String STATE_EMERGENCY_ONLY = "STATE_EMERGENCY_ONLY";
    public static final String STATE_IN_SERVICE = "STATE_IN_SERVICE";
    public static final String STATE_OUT_OF_SERVICE = "STATE_OUT_OF_SERVICE";
    public static final String STATE_POWER_OFF = "STATE_POWER_OFF";
    public static final String STATE_UNKNOWN = "STATE_UNKNOWN";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long timestamp;
    
    @Persistent(defaultFetchGroup = "true")
    private Location location;

    @Persistent
    private String operatorAlphaLong;
    
    @Persistent
    private String operatorAlphaShort;
    
    @Persistent
    private String operatorNumeric;
    
    @Persistent
    private boolean roaming;
 
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
     * Gets the operator (alpha long).
     * @return
     */
    public String getOperatorAlphaLong() { return this.operatorAlphaLong; }
    
    /**
     * Gets the operator (alpha short).
     * @return
     */
    public String getOperatorAlphaShort() { return this.operatorAlphaShort; }
    
    /**
     * Gets the operator (numeric).
     * @return
     */
    public String getOperatorNumeric() { return this.operatorNumeric; }
    
    /**
     * Returns the roaming flag.
     * @return The roaming flag.
     */
    public boolean isRoaming() { return this.roaming; }
    
    /**
     * Gets the state.
     * @return The state.
     */
    public String getState() { return this.state; }
    
    /**
     * Sets the operator (alpha long).
     * @param operatorAlphaLong
     */
    public void setOperatorAlphaLong(String operatorAlphaLong) { this.operatorAlphaLong = operatorAlphaLong; }
    
    /**
     * Sets the operator (alpha short).
     * @param operatorAlphaShort
     */
    public void setOperatorAlphaShort(String operatorAlphaShort) { this.operatorAlphaShort = operatorAlphaShort; }
    
    /**
     * Sets the operator (numeric).
     * @param operatorNumeric
     */
    public void setOperatorNumeric(String operatorNumeric) { this.operatorNumeric = operatorNumeric; }
    
    /**
     * Sets the roaming flag.
     * @param roaming
     */
    public void setRoaming(boolean roaming) { this.roaming = roaming; }
    
    /**
     * Sets the state.
     * @param state
     */
    public void setState(String state) { this.state = state; }
    
    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param operatorAlphaLong
     * @param operatorAlphaShort
     * @param operatorNumeric
     * @param roaming
     * @param state
     */
    public ServiceStateEntry(long timestamp, Location location, 
                            String operatorAlphaLong, String operatorAlphaShort, 
                            String operatorNumeric, boolean roaming, String state) {
        this.timestamp = timestamp;
        this.location = location;
        this.operatorAlphaLong = operatorAlphaLong;
        this.operatorAlphaShort = operatorAlphaShort;
        this.operatorNumeric = operatorNumeric;
        this.roaming = roaming;
        this.state = state;
    }
    
    /**
     * Constructor
     * @param timestamp
     * @param identifier
     * @param location
     * @param blob
     */
    public ServiceStateEntry(long timestamp, Location location, ServiceStateBlob blob) {
        this.timestamp = timestamp;
        this.location = location;
        this.operatorAlphaLong = blob.getOperatorAlphaLong();
        this.operatorAlphaShort = blob.getOperatorAlphaShort();
        this.operatorNumeric = blob.getOperatorNumeric();
        this.roaming = blob.isRoaming();
        this.state = blob.getState();
    }
    
    /**
     * Default constructor.
     */
    public ServiceStateEntry() { 
        super();
    }
    
}
