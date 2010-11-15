package horizon.aether.model;

import java.io.Serializable;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

//import com.google.appengine.api.datastore.Key;

/**
 * Represents a neighbouring cell (used in Telephony blobs).
 * A cell is described by:
 *   - cid (int)
 *   - rssi (int)
 */
@PersistenceCapable
@EmbeddedOnly
public class NeighbouringCell implements Serializable {

    private static final long serialVersionUID = 6727056529448278660L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.UUIDSTRING)
    protected String key;
    
    @Persistent
    private int cid;
    
    @Persistent
    private int rssi;
    
    /**
     * Gets the key.
     * @return The key.
     */
    public String getKey() { return this.key; }

    /**
     * Gets the cid.
     * @return The cid.
     */
    public int getCid() { return this.cid; }
    
    /**
     * Gets the rssi.
     * @return The rssi.
     */
    public int getRssi() { return this.rssi; }
    
    /**
     * Sets the key.
     * @param key
     */
    public void setKey(String key) { this.key = key; }    

    /**
     * Sets the cid.
     * @param cid
     */
    public void setCid(int cid) { this.cid = cid; }
    
    /**
     * Sets the rssi
     * @param rssi
     */
    public void setRssi(int rssi) { this.rssi = rssi; }
    
    /**
     * Constructor.
     * @param cid
     * @param rssi
     */
    public NeighbouringCell(int cid, int rssi) {
        this.cid = cid;
        this.rssi = rssi;
    }        
    
    /**
     * Default constructor.
     */
    public NeighbouringCell() { }
}