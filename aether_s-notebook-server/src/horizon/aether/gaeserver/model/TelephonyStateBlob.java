package horizon.aether.gaeserver.model;

/**
 * Class that represents a blob for telephony state entries. 
 * Such a blob is described by:
 *   - A list of neighbouring cells (ArrayList<NeighbouringCell>).
 *   - The network type (String). 
 */

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class TelephonyStateBlob implements IBlob {

    public static final String NETWORK_TYPE_EDGE = "NETWORK_TYPE_EDGE";
    public static final String NETWORK_TYPE_GPRS = "NETWORK_TYPE_GPRS";
    public static final String NETWORK_TYPE_UMTS = "NETWORK_TYPE_UMTS";
    public static final String NETWORK_TYPE_UNKNOWN = "NETWORK_TYPE_UNKNOWN";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    Key key;

    @Persistent
    private ArrayList<NeighbouringCell> neighbouringCells;
    
    @Persistent
    private String networkType;

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
     * @param neighbouringCells
     * @param networkType
     */
    public TelephonyStateBlob(ArrayList<NeighbouringCell> neighbouringCells, String networkType) {
        this.neighbouringCells = neighbouringCells;
        this.networkType = networkType;
    }
    
    /**
     * Default constructor.
     */
    public TelephonyStateBlob() { }
        
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(networkType);
        
        for (NeighbouringCell nc : this.neighbouringCells) {
            sb.append("_");
            sb.append(nc.getCid());
            sb.append("_");
            sb.append(nc.getRssi());
        }
        
        return sb.toString();
    }

    /**
     * Creates the key.
     */
    @Override
    public void createKey() {
        this.key = KeyFactory.createKey(TelephonyStateBlob.class.getSimpleName(), this.hashCode());
    }

    /**
     * Gets the key.
     */
    @Override
    public Key getKey() {
        if (this.key == null)
            createKey();
        
        return this.key;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((neighbouringCells == null) ? 0 : neighbouringCells
                        .hashCode());
        result = prime * result
                + ((networkType == null) ? 0 : networkType.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof TelephonyStateBlob))
            return false;
        TelephonyStateBlob other = (TelephonyStateBlob) obj;
        if (neighbouringCells == null) {
            if (other.neighbouringCells != null)
                return false;
        } else if (!neighbouringCells.equals(other.neighbouringCells))
            return false;
        if (networkType == null) {
            if (other.networkType != null)
                return false;
        } else if (!networkType.equals(other.networkType))
            return false;
        return true;
    }
    
}
