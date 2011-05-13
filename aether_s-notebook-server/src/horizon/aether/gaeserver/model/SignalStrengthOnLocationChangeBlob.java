package horizon.aether.gaeserver.model;

/**
 * Class that represents a Signal Strength blob. 
 */
public class SignalStrengthOnLocationChangeBlob {

    public static final String NETWORK_TYPE_EDGE = "NETWORK_TYPE_EDGE";
    public static final String NETWORK_TYPE_GPRS = "NETWORK_TYPE_GPRS";
    public static final String NETWORK_TYPE_UMTS = "NETWORK_TYPE_UMTS";
    public static final String NETWORK_TYPE_UNKNOWN = "NETWORK_TYPE_UNKNOWN";

    private int signalStrength;

    private String networkType;

    /**
     * Gets the signal strength.
     * @return The signal strength.
     */
    public int getSignalStrength() { return this.signalStrength; }

    /**
     * Gets the network type.
     * @return The network type
     */
    public String getNetworkType() { return this.networkType; }

    
    /**
     * Sets the signal strength.
     * @param signal strength
     */
    public void setSignalStrength(int signalStrength) { this.signalStrength = signalStrength; }
    
    /**
     * Sets the network type.
     * @param networkType
     */
    public void setNetworkType(String networkType) { this.networkType = networkType; }

    
    /**
     * Constructor.
     * @param strength
     */
    public SignalStrengthOnLocationChangeBlob(int signalStrength, String networkType) {
        this.signalStrength = signalStrength;
        this.networkType = networkType;
    }
    
    /**
     * Default constructor.
     */
    public SignalStrengthOnLocationChangeBlob() { }

}
