package horizon.aether.gaeserver.model;

/**
 * Class that represents a Signal Strength blob. 
 */
public class SignalStrengthBlob {

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

}
