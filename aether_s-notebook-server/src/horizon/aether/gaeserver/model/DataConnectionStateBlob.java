package horizon.aether.gaeserver.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class DataConnectionStateBlob implements IBlob {

    public static final String DATA_CONNECTED = "DATA_CONNECTED";
    public static final String DATA_CONNECTING = "DATA_CONNECTING";
    public static final String DATA_DISCONNECTED = "DATA_DISCONNECTED";
    public static final String DATA_SUSPENDED = "DATA_SUSPENDED";
    public static final String DATA_UNKNOWN = "DATA_UNKNOWN";

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    Key key;

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
     * Creates the key.
     */
    @Override
    public void createKey() {
        this.key = KeyFactory.createKey(DataConnectionStateBlob.class.getSimpleName(), this.hashCode());
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
        result = prime * result + ((state == null) ? 0 : state.hashCode());
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
        if (!(obj instanceof DataConnectionStateBlob))
            return false;
        DataConnectionStateBlob other = (DataConnectionStateBlob) obj;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        return true;
    }

}
