package horizon.aether.gaeserver.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;

@PersistenceCapable
public class CellLocationBlob extends Blob {

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
     * Creates the blob's key.
     */
    @Override
    protected Key createKey() {
        return KeyFactory.createKey(CellLocationBlob.class.getSimpleName(), this.cid + "" + this.lac);
    }

    /**
     * Returns the JSON string representation of the object.
     */
    @Override
    protected String toJSONString() {
        JSONStringer data = new JSONStringer();
        try 
        {
            data.object();
            data.key("cid");
            data.value(this.getCid());
            data.key("lac");
            data.value(this.getLac());
            data.endObject();
        }
        catch (JSONException e) { }
        
        return data.toString();
    }
}
