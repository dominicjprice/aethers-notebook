package horizon.aether.gaeserver.model;

import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.datastore.Key;

/**
 * Blob interface to be implemented by all types of blobs.
 */
@PersistenceCapable
public interface IBlob {
    
    /**
     * Creates a key.
     */
    public void createKey();
    
    /**
     * Gets the key.
     * @return the key.
     */
    public Key getKey();
}
