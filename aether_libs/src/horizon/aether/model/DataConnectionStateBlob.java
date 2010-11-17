package horizon.aether.model;

/**
 * Class that represents a data connection state blob 
 */
public class DataConnectionStateBlob {

    public static final String DATA_CONNECTED = "DATA_CONNECTED";
    public static final String DATA_CONNECTING = "DATA_CONNECTING";
    public static final String DATA_DISCONNECTED = "DATA_DISCONNECTED";
    public static final String DATA_SUSPENDED = "DATA_SUSPENDED";
    public static final String DATA_UNKNOWN = "DATA_UNKNOWN";

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

}
