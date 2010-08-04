package horizon.aether.gaeserver.model;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class SignalStrengthBlob implements IBlob {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    Key key;

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
     * Creates the key.
     */
    @Override
    public void createKey() {
        this.key = KeyFactory.createKey(SignalStrengthBlob.class.getSimpleName(), this.hashCode());
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
        result = prime * result + strength;
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
        if (!(obj instanceof SignalStrengthBlob))
            return false;
        SignalStrengthBlob other = (SignalStrengthBlob) obj;
        if (strength != other.strength)
            return false;
        return true;
    }
    
}
