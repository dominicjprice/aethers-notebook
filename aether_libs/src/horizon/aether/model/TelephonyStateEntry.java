package horizon.aether.model;


import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

//import com.google.appengine.api.datastore.Key;

/**
 * Class that represents a telephony state entry. 
 * Such an entry is described by:
 *   - A list of neighbouring cells (ArrayList<NeighbouringCell>).
 *   - The network type (String). 
 */
@PersistenceCapable
public class TelephonyStateEntry {
    
    public static final String IDENTIFIER = "TELEPHONY_STATE";
    
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

    @Persistent(embedded = "true")
    private ArrayList<NeighbouringCell> neighbouringCells;
    
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
     * Gets the location.
     * @return
     */
    public Location getLocation() { return this.location; }

    /**
     * Gets the network type.
     * @return The network type
     */
    public String getNetworkType() { return this.networkType; }

    /**
     * Gets the neighbouring cells.
     * @return The neighbouring cells collection.
     */
    public ArrayList<NeighbouringCell> getNeighbouringCells() { return this.neighbouringCells; }

    /**
     * Sets the network type.
     * @param networkType
     */
    public void setNetworkType(String networkType) { this.networkType = networkType; }

    /**
     * Sets the neighbouring cells collection
     * @param cells
     */
    public void setNeighbouringCells(ArrayList<NeighbouringCell> cells) { this.neighbouringCells = cells; }

    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param neighbouringCells
     * @param networkType
     */
    public TelephonyStateEntry(long timestamp, Location location, 
                                ArrayList<NeighbouringCell> neighbouringCells, String networkType) {
        this.timestamp = timestamp;
        this.location = location;
        this.neighbouringCells = neighbouringCells;
        this.networkType = networkType;
    }
    
    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param blob
     */
    public TelephonyStateEntry(long timestamp, Location location, TelephonyStateBlob blob) {
        this.timestamp = timestamp;
        this.location = location;
        this.neighbouringCells = blob.getNeighbouringCells();
        this.networkType = blob.getNetworkType();
    }
    
    /**
     * Default constructor.
     */
    public TelephonyStateEntry() { }
}
