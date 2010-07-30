package horizon.aether.gaeserver.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Blob {

    public static final String CELL_LOCATION_BLOB = "CELL_LOCATION";
    public static final String DATA_CONNECTION_STATE_BLOB = "DATA_CONNECTION_STATE";
    public static final String SERVICE_STATE_BLOB = "SERVICE_STATE";
    public static final String SIGNAL_STRENGTH_BLOB = "SIGNAL_STRENGTH";
    public static final String TELEPHONY_STATE_BLOB = "TELEPHONY_STATE";
    public static final String AP_SITINGS_BLOB = "AP_SITINGS";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Key key;

    /**
     * Gets the key.
     * @return The key.
     */
    public Key getKey() {
        if (this.key == null) {
            this.key = createKey();
        }

        return this.key;
    }

    /**
     * Sets the key.
     * @param key
     */
    public void setKey(Key key) { 
        this.key = key; 
    }
    
    /**
     * Creates the key.
     * @return
     */
    protected abstract Key createKey();
    
    /**
     * Returns a JSON string representation of the object.
     * @return a JSON string.
     */
    protected abstract String toJSONString();
}
