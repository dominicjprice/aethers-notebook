package horizon.aether.gaeserver.model;

/**
 * Class that represents a blob for wifi entries. A wifi
 * blob consists of a collection of networks.
 */

import java.util.ArrayList;

import com.google.appengine.api.datastore.Key;

public class WifiBlob {

    Key key;

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
        
}
