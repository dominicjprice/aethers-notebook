package horizon.aether.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

//import com.google.appengine.api.datastore.Key;

/**
 * Class that  represents a cell location entry.
 */
@PersistenceCapable
public class CellLocationEntry {

    public static final String IDENTIFIER = "CELL_LOCATION";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.UUIDSTRING)
    private String key;

    @Persistent
    private long timestamp;
    
    @Persistent(defaultFetchGroup = "true")
    private Location location;

    @Persistent
    private int cid;

    @Persistent
    private int lac;

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
     * Gets the cell id.
     * @return The cell id.
     */
    public int getCid() { return this.cid; }

    /**
     * Gets the location area code.
     * @return The location area code.
     */
    public int getLac() { return this.lac; }

    /**
     * Sets the cell id.
     * @param cid
     */
    public void setCid(int cid) { this.cid = cid; }

    /**
     * Sets the location area code.
     * @param lac
     */
    public void setLac(int lac) { this.lac = lac; }

    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param cid
     * @param lac
     */
    public CellLocationEntry(long timestamp, Location location, int cid, int lac) {
        this.timestamp = timestamp;
        this.location = location;
        this.cid = cid;
        this.lac = lac;
    }

    /**
     * Constructor.
     * @param timestamp
     * @param location
     * @param blob
     */
    public CellLocationEntry(long timestamp, Location location, CellLocationBlob blob) {
        this.timestamp = timestamp;
        this.location = location;
        this.cid = blob.getCid();
        this.lac = blob.getLac();
    }
    
    /**
     * Default constructor.
     */
    public CellLocationEntry() { }
}
