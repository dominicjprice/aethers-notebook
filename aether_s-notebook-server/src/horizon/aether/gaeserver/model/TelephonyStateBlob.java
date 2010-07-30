package horizon.aether.gaeserver.model;

/**
 * Class that represents a blob for telephony state entries. 
 * Such a blob is described by:
 *   - A list of neighbouring cells (ArrayList<NeighbouringCell>).
 *   - The network type (String). 
 */

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;

@PersistenceCapable
public class TelephonyStateBlob extends Blob {

    public static final String NETWORK_TYPE_EDGE = "NETWORK_TYPE_EDGE";
    public static final String NETWORK_TYPE_GPRS = "NETWORK_TYPE_GPRS";
    public static final String NETWORK_TYPE_UMTS = "NETWORK_TYPE_UMTS";
    public static final String NETWORK_TYPE_UNKNOWN = "NETWORK_TYPE_UNKNOWN";
    
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
     * Creates the blob's key.
     */
    @Override
    protected Key createKey() {
        return KeyFactory.createKey(TelephonyStateBlob.class.getSimpleName(), this.toString());
    }    
    
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
     * Returns a JSON string representation of the object.
     * @return A JSON string.
     */
    @Override
    public String toJSONString() {
        JSONStringer data = new JSONStringer();
        try
        {
            data.object();
            data.key("neighbouringCells");
            data.array();
            for (NeighbouringCell info : this.neighbouringCells) {
                data.object();
                data.key("cid");
                data.value(info.getCid());
                data.key("rssi");
                data.value(info.getRssi());
                data.endObject();
            }
            data.endArray();
            data.key("networkType");
            data.value(this.networkType);
            data.endObject();
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        
        return data.toString();
    }
}
