package horizon.aether.gaeserver.model;

/**
 * Class that represents a blob for wifi entries. A wifi
 * blob consists of a collection of networks.
 */
import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class WifiBlob implements IBlob {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    Key key;

    @Persistent
    ArrayList<Wifi> networks;
    
    public ArrayList<Wifi> getNetworks() { return this.networks; }
    
    public void setNetworks(ArrayList<Wifi> networks) { this.networks = networks; }
    
    /**
     * Constructor.
     * @param networks
     */
    public WifiBlob(ArrayList<Wifi> networks) {
        this.networks = networks;
    }
    
    /**
     * Default constructor.
     */
    public WifiBlob() { 
        this.networks = new ArrayList<Wifi>();
    }

    /**
     * Creates the key.
     */
    @Override
    public void createKey() {
        this.key = KeyFactory.createKey(WifiBlob.class.getSimpleName(), this.hashCode());
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
        result = prime * result
                + ((networks == null) ? 0 : networks.hashCode());
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
        if (!(obj instanceof WifiBlob))
            return false;
        WifiBlob other = (WifiBlob) obj;
        if (networks == null) {
            if (other.networks != null)
                return false;
        } else if (!networks.equals(other.networks))
            return false;
        return true;
    }
        
}
