package horizon.aether.gaeserver.model;


import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;

@PersistenceCapable
public class SignalStrengthBlob extends Blob {

    @Persistent
    private int strength;

    /**
     * Gets the strength.
     * @return The strength.
     */
    public int getStrength() { return this.strength; }

    /**
     * Sets the strength.
     * @param strength
     */
    public void setStrength(int strength) { this.strength = strength; }
    
    /**
     * Constructor.
     * @param strength
     */
    public SignalStrengthBlob(int strength) {
        this.strength = strength;
    }
    
    /**
     * Default constructor.
     */
    public SignalStrengthBlob() { }
    
    /**
     * Creates the blob's key.
     */
    public Key createKey() {
        return KeyFactory.createKey(SignalStrengthBlob.class.getSimpleName(), ""+strength);
    }

    /**
     * Returns a string representation of the object
     */
    @Override
    protected String toJSONString() {
        JSONStringer data = new JSONStringer();
        try {
            data.object();
            data.key("strength");
            data.value(this.strength);
            data.endObject();
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        
        return data.toString();
    }
}
