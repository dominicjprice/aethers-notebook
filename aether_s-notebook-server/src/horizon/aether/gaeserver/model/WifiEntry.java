package horizon.aether.gaeserver.model;


import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Class that represents a Wi-Fi entry. A Wi-Fi entry 
 * consists of a collection of networks.
 */
@PersistenceCapable
public class WifiEntry {

    public static final String IDENTIFIER = "AP_SITINGS";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long timestamp;
    
    @Persistent(defaultFetchGroup = "true")
    private Location location;

    @Persistent
    ArrayList<Wifi> networks;
    
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

    public ArrayList<Wifi> getNetworks() { return this.networks; }
    
    public void setNetworks(ArrayList<Wifi> networks) { this.networks = networks; }
    
    /**
     * Constructor
     * @param timestamp
     * @param location
     * @param networks
     */
    public WifiEntry(long timestamp, Location location, ArrayList<Wifi> networks) {
        this.timestamp = timestamp;
        this.location = location;
        this.networks = networks;
    }
    
    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param blob
     */
    public WifiEntry(long timestamp, Location location, WifiBlob blob) {
        this.timestamp = timestamp;
        this.location = location;
        this.networks = blob.getNetworks();
    }
    /**
     * Default constructor.
     */
    public WifiEntry() {
        super();
        this.networks = new ArrayList<Wifi>();
    }

}
