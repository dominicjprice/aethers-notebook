package horizon.aether.gaeserver.model;

/**
 * Class that represents a cell location blob.
 */
public class CellLocationBlob {

    private int cid;

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
}
