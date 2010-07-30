package horizon.aether.gaeserver.model;

/**
 * Class that represents a blob for wifi entries. A wifi
 * blob consists of a collection of networks.
 */
import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;

@PersistenceCapable
public class WifiBlob extends Blob {

    @Persistent
    ArrayList<Wifi> networks;
    
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
     * Creates the blob's key.
     */
    @Override
    protected Key createKey() {
        // TODO: must to be unique
        return KeyFactory.createKey(WifiBlob.class.getSimpleName(), networks.toString());
    }

    /**
     * Returns the JSON string representation of the object.
     */
    @Override
    protected String toJSONString() {
        JSONStringer data = new JSONStringer();
        try {
            data.array();
            for (Wifi wf : this.networks) {
                data.object();
                data.key("bssid");
                data.value(wf.getBSSID());
                data.key("ssid");
                data.value(wf.getSSID());
                data.key("capabilities");
                data.value(wf.getCapabilities());
                data.key("frequency");
                data.value(wf.getFrequency());
                data.key("level");
                data.value(wf.getLevel());
                data.endObject();
            }

            data.endArray();
        } 
        catch (JSONException e) { }

        return data.toString();
    }
}
