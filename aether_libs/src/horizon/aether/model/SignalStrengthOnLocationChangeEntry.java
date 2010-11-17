package horizon.aether.model;

//import horizon.aether.gaeserver.utilities.StringUtils;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


/**
 * Class that represents a Signal Strength Entry.
 */
@PersistenceCapable
public class SignalStrengthOnLocationChangeEntry {
    
    public static final String IDENTIFIER = "SIGNAL_STRENGTH_ON_LOCATION_CHANGE";
    
    public static final String NETWORK_TYPE_EDGE = "NETWORK_TYPE_EDGE";
    public static final String NETWORK_TYPE_GPRS = "NETWORK_TYPE_GPRS";
    public static final String NETWORK_TYPE_UMTS = "NETWORK_TYPE_UMTS";
    public static final String NETWORK_TYPE_UNKNOWN = "NETWORK_TYPE_UNKNOWN";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.UUIDSTRING)
    private String key;

    @Persistent
    private long timestamp;
    
    @Persistent(defaultFetchGroup = "true")
    private Location location;

    @Persistent
    private int signalStrength;

    @Persistent
    private String networkType;

    
    /**
     * Gets the key.
     * @return
     */
    public String getKey() { return this.key; }
    
    /**
     * Gets the timestamp.
     * @return
     */
    public long getTimestamp() { return this.timestamp; }
    
    /**
     * Formats the timestamp to yyyy-mm-dd hh:mm:ss format
     * @return
     */
   /* public String getDateTimeString()
    {
    	//return StringUtils.toDateTimeString(this.timestamp);
    }*/
    
    /**
     * Gets the location.
     * @return
     */
    public Location getLocation() { return this.location; }

    /**
     * Gets the signal strength.
     * @return The signal strength.
     */
    public int getSignalStrength() { return this.signalStrength; }

    /**
     * Sets the signal strength.
     * @param signal strength
     */
    public void setSignalStrength(int strength) { this.signalStrength = strength; }

    /**
     * Gets the network type.
     * @return The network type.
     */
    public String getNetworkType() { return this.networkType; }

    /**
     * Sets the network type.
     * @param network type
     */
    public void setNetworkType(String networkType) { this.networkType = networkType; }

    
    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param strength
     */
    public SignalStrengthOnLocationChangeEntry(long timestamp, Location location, int signalStrength, String networkType) {
        this.timestamp = timestamp;
        this.location = location;        
        this.signalStrength = signalStrength;
        this.networkType = networkType;
    }
    
    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param blob
     */
    public SignalStrengthOnLocationChangeEntry(long timestamp, Location location, SignalStrengthOnLocationChangeBlob blob) {
        this.timestamp = timestamp;
        this.location = location;
        this.networkType = blob.getNetworkType();
        this.signalStrength = blob.getSignalStrength();
    }
    
    /**
     * Default constructor.
     */
    public SignalStrengthOnLocationChangeEntry() { }
    
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(networkType);
        sb.append("_");
        sb.append(signalStrength);
        return sb.toString();
    }
}
