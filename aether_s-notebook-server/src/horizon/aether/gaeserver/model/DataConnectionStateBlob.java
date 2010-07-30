package horizon.aether.gaeserver.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;

@PersistenceCapable
public class DataConnectionStateBlob extends Blob {

    public static final String DATA_CONNECTED = "DATA_CONNECTED";
    public static final String DATA_CONNECTING = "DATA_CONNECTING";
    public static final String DATA_DISCONNECTED = "DATA_DISCONNECTED";
    public static final String DATA_SUSPENDED = "DATA_SUSPENDED";
    public static final String DATA_UNKNOWN = "DATA_UNKNOWN";

    @Persistent
    private String state;

    /**
     * Get the state string.
     * @return
     */
    public String getState() { return this.state; }

    /**
     * Set the state string.
     * @param state
     */
    public void setState(String state) { this.state = state; }
    
    /**
     * Constructor.
     * @param state
     */
    public DataConnectionStateBlob(String state) { 
        this.state = state; 
    }

    /**
     * Default constructor.
     */
    public DataConnectionStateBlob() { }
    
    /**
     * Creates the blob's key.
     */
    @Override
    protected Key createKey() {
        return KeyFactory.createKey(DataConnectionStateBlob.class.getSimpleName(), this.state);
    }

    /**
     * Returns a JSON string representation of the object.
     */
    @Override
    protected String toJSONString() {
        JSONStringer data = new JSONStringer();
        try 
        {
            data.object();
            data.key("state");
            data.value(state);
            data.endObject();
        }
        catch (JSONException e) { }

        return data.toString();
    }
}
