package horizon.aether.gaeserver.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
public class ServiceStateBlob implements IBlob {

    public static final String STATE_EMERGENCY_ONLY = "STATE_EMERGENCY_ONLY";
    public static final String STATE_IN_SERVICE = "STATE_IN_SERVICE";
    public static final String STATE_OUT_OF_SERVICE = "STATE_OUT_OF_SERVICE";
    public static final String STATE_POWER_OFF = "STATE_POWER_OFF";
    public static final String STATE_UNKNOWN = "STATE_UNKNOWN";
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    Key key;

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
     * Creates the key.
     */
    @Override
    public void createKey() {
        this.key = KeyFactory.createKey(ServiceStateBlob.class.getSimpleName(), this.hashCode());
    }

    /**
     * Gets teh key.
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
                + ((operatorAlphaLong == null) ? 0 : operatorAlphaLong
                        .hashCode());
        result = prime
                * result
                + ((operatorAlphaShort == null) ? 0 : operatorAlphaShort
                        .hashCode());
        result = prime * result
                + ((operatorNumeric == null) ? 0 : operatorNumeric.hashCode());
        result = prime * result + (roaming ? 1231 : 1237);
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
        if (!(obj instanceof ServiceStateBlob))
            return false;
        ServiceStateBlob other = (ServiceStateBlob) obj;
        if (operatorAlphaLong == null) {
            if (other.operatorAlphaLong != null)
                return false;
        } else if (!operatorAlphaLong.equals(other.operatorAlphaLong))
            return false;
        if (operatorAlphaShort == null) {
            if (other.operatorAlphaShort != null)
                return false;
        } else if (!operatorAlphaShort.equals(other.operatorAlphaShort))
            return false;
        if (operatorNumeric == null) {
            if (other.operatorNumeric != null)
                return false;
        } else if (!operatorNumeric.equals(other.operatorNumeric))
            return false;
        if (roaming != other.roaming)
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        return true;
    }

}
