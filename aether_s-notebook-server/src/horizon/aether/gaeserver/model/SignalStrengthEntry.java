package horizon.aether.gaeserver.model;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class SignalStrengthEntry {
    
    public static final String IDENTIFIER = "SIGNAL_STRENGTH";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long timestamp;
    
    @Persistent(defaultFetchGroup = "true")
    private Location location;

    @Persistent
    private int strength;

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
     * Gets the strength.
     * @return The strength.
     */
    public int getStrength() { return this.strength; }

    /**
     * Sets the strength.
     * @param strength
     */
    public void setStrength(int strength) { this.strength = strength; }
    
    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param strength
     */
    public SignalStrengthEntry(long timestamp, Location location, int strength) {
        this.timestamp = timestamp;
        this.location = location;        
        this.strength = strength;
    }
    
    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param blob
     */
    public SignalStrengthEntry(long timestamp, Location location, SignalStrengthBlob blob) {
        this.timestamp = timestamp;
        this.location = location;
        this.strength = blob.getStrength();
    }
    
    /**
     * Default constructor.
     */
    public SignalStrengthEntry() { 
        super();
    }
}
