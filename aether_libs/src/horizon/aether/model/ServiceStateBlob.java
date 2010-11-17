package horizon.aether.model;

/**
 * Class that represents a blob for service state entries. 
 * Such a blob is described by the following:
 *   - operatorAlphaLong (String)
 *   - operatorAlphaShort (String)
 *   - operatorNumeric (String)
 *   - roaming (boolean)
 *   - state (String)
 */
public class ServiceStateBlob {

    public static final String STATE_EMERGENCY_ONLY = "STATE_EMERGENCY_ONLY";
    public static final String STATE_IN_SERVICE = "STATE_IN_SERVICE";
    public static final String STATE_OUT_OF_SERVICE = "STATE_OUT_OF_SERVICE";
    public static final String STATE_POWER_OFF = "STATE_POWER_OFF";
    public static final String STATE_UNKNOWN = "STATE_UNKNOWN";
    
    private String operatorAlphaLong;
    
    private String operatorAlphaShort;
    
    private String operatorNumeric;
    
    private boolean roaming;
 
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


}
