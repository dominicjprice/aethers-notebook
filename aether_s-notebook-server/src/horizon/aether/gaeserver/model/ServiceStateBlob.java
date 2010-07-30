package horizon.aether.gaeserver.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;

/**
 * Class that represents a blob for service state entries. 
 * Such a blob is described by the following:
 *   - operatorAlphaLong (String)
 *   - operatorAlphaShort (String)
 *   - operatorNumeric (String)
 *   - roaming (boolean)
 *   - state (String)
 */

@PersistenceCapable
public class ServiceStateBlob extends Blob {

    public static final String STATE_EMERGENCY_ONLY = "STATE_EMERGENCY_ONLY";
    public static final String STATE_IN_SERVICE = "STATE_IN_SERVICE";
    public static final String STATE_OUT_OF_SERVICE = "STATE_OUT_OF_SERVICE";
    public static final String STATE_POWER_OFF = "STATE_POWER_OFF";
    public static final String STATE_UNKNOWN = "STATE_UNKNOWN";
    
    @Persistent
    private String operatorAlphaLong;
    
    @Persistent
    private String operatorAlphaShort;
    
    @Persistent
    private String operatorNumeric;
    
    @Persistent
    private boolean roaming;
 
    @Persistent
    private String state;
    
    /**
     * Gets the operator (alpha long).
     * @return
     */
    public String getOperatorAlphaLong() { return this.operatorAlphaLong; }
    
    /**
     * Gets the operator (alpha short).
     * @return
     */
    public String getOperatorAlphaShort() { return this.operatorAlphaShort; }
    
    /**
     * Gets the operator (numeric).
     * @return
     */
    public String getOperatorNumeric() { return this.operatorNumeric; }
    
    /**
     * Returns the roaming flag.
     * @return The roaming flag.
     */
    public boolean isRoaming() { return this.roaming; }
    
    /**
     * Gets the state.
     * @return The state.
     */
    public String getState() { return this.state; }
    
    /**
     * Sets the operator (alpha long).
     * @param operatorAlphaLong
     */
    public void setOperatorAlphaLong(String operatorAlphaLong) { this.operatorAlphaLong = operatorAlphaLong; }
    
    /**
     * Sets the operator (alpha short).
     * @param operatorAlphaShort
     */
    public void setOperatorAlphaShort(String operatorAlphaShort) { this.operatorAlphaShort = operatorAlphaShort; }
    
    /**
     * Sets the operator (numeric).
     * @param operatorNumeric
     */
    public void setOperatorNumeric(String operatorNumeric) { this.operatorNumeric = operatorNumeric; }
    
    /**
     * Sets the roaming flag.
     * @param roaming
     */
    public void setRoaming(boolean roaming) { this.roaming = roaming; }
    
    /**
     * Sets the state.
     * @param state
     */
    public void setState(String state) { this.state = state; }
    
    /**
     * Constructor.
     * @param operatorAlphaLong
     * @param operatorAlphaShort
     * @param operatorNumeric
     * @param roaming
     * @param state
     */
    public ServiceStateBlob(String operatorAlphaLong, String operatorAlphaShort, 
                        String operatorNumeric, boolean roaming, String state) {
        this.operatorAlphaLong = operatorAlphaLong;
        this.operatorAlphaShort = operatorAlphaShort;
        this.operatorNumeric = operatorNumeric;
        this.roaming = roaming;
        this.state = state;
    }
    
    /**
     * Default constructor.
     */
    public ServiceStateBlob() { }
    
    /**
     * Creates the blob's key.
     */
    @Override
    protected Key createKey() {
        String keyString = operatorAlphaLong + operatorAlphaShort + operatorNumeric + roaming + state;
        return KeyFactory.createKey(ServiceStateBlob.class.getSimpleName(), keyString);
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
            data.key("operatorAlphaLong");
            data.value(this.getOperatorAlphaLong());
            data.key("operatorAlphaShort");
            data.value(this.getOperatorAlphaShort());
            data.key("operatorNumeric");
            data.value(this.getOperatorNumeric());
            data.key("roaming");
            data.value(this.isRoaming());
            data.key("state");
            data.value(this.state);
            data.endObject();
        }
        catch (JSONException e){}
        
        return data.toString();
    }
}
