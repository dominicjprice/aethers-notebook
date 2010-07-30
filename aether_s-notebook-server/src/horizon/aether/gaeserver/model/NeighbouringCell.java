package horizon.aether.gaeserver.model;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Represents a neighbouring cell (used in Telephony blobs).
 * A cell is described by:
 *   - cid (int)
 *   - rssi (int)
 */
@PersistenceCapable
@EmbeddedOnly
public class NeighbouringCell {
    @Persistent
    private int cid;
    
    @Persistent
    private int rssi;
    
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