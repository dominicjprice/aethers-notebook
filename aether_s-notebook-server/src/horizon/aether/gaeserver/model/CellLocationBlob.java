package horizon.aether.gaeserver.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class CellLocationBlob implements IBlob {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    Key key;

    @Persistent
    private int cid;

    @Persistent
    private int lac;

    /**
     * Gets the cell id.
     * @return The cell id.
     */
    public int getCid() { return this.cid; }

    /**
     * Gets the location area code.
     * @return The location area code.
     */
    public int getLac() { return this.lac; }

    /**
     * Sets the cell id.
     * @param cid
     */
    public void setCid(int cid) { this.cid = cid; }

    /**
     * Sets the location area code.
     * @param lac
     */
    public void setLac(int lac) { this.lac = lac; }

    /**
     * Constructor.
     * @param cid
     * @param lac
     */
    public CellLocationBlob(int cid, int lac) {
        this.cid = cid;
        this.lac = lac;
    }

    /**
     * Default constructor.
     */
    public CellLocationBlob() { }

    /**
     * Creates the key.
     */
    @Override
    public void createKey() {
        this.key = KeyFactory.createKey(CellLocationBlob.class.getSimpleName(), this.hashCode());
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
        result = prime * result + cid;
        result = prime * result + lac;
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
        if (!(obj instanceof CellLocationBlob))
            return false;
        CellLocationBlob other = (CellLocationBlob) obj;
        if (cid != other.cid)
            return false;
        if (lac != other.lac)
            return false;
        return true;
    }
}
