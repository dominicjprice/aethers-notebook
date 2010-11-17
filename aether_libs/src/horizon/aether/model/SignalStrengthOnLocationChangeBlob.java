package horizon.aether.model;

/**
 * Class that represents a Signal Strength blob. 
 */
public class SignalStrengthOnLocationChangeBlob {

    public static final String NETWORK_TYPE_EDGE = "NETWORK_TYPE_EDGE";
    public static final String NETWORK_TYPE_GPRS = "NETWORK_TYPE_GPRS";
    public static final String NETWORK_TYPE_UMTS = "NETWORK_TYPE_UMTS";
    public static final String NETWORK_TYPE_UNKNOWN = "NETWORK_TYPE_UNKNOWN";

    private float accuracy;
    
    private double altitude;
    
    private float bearing;
    
    private double latitude;
    
    private double longitude;
    
    private String provider;
    
    private float speed;
    
    private String extras;
    
    private int signalStrength;

    private String networkType;

    /**
     * Gets the accuracy.
     * @return
     */
    public float getAccuracy() { return this.accuracy; }

    /**
     * Gets the altitude.
     * @return
     */
    public double getAltitude() { return this.altitude; }

    /**
     * Gets the bearing.
     * @return
     */
    public float getBearing() { return this.bearing; }

    /**
     * Gets the latitude.
     * @return
     */
    public double getLatitude() { return this.latitude; }

    /**
     * Gets the longitude.
     * @return
     */
    public double getLongitude() { return this.longitude; }

    /**
     * Gets the provider.
     * @return
     */
    public String getProvider() { return this.provider; }

    /**
     * Gets the speed.
     * @return
     */
    public float getSpeed() { return this.speed; }

    /**
     * Gets the extras.
     * @return
     */
    public String getExtras() { return this.extras; }

    /**
     * Sets the accuracy.
     * @param accuracy
     */
    public void setAccuracy(float accuracy) { this.accuracy = accuracy; }

    /**
     * Sets the altitude.
     * @param altitude
     */
    public void setAltitude(double altitude) { this.altitude = altitude; }

    /**
     * Sets the bearing.
     * @param bearing
     */
    public void setBearing(float bearing) { this.bearing = bearing; }

    /**
     * Sets the latitude.
     * @param latitude
     */
    public void setLatitude(double latitude) { this.latitude = latitude;  }

    /**
     * Sets the longitude.
     * @param longitude
     */
    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**
     * Sets the provider.
     * @param provider
     */
    public void setProvider(String provider) { this.provider = provider; }

    /**
     * Sets the speed.
     * @param speed
     */
    public void setSpeed(float speed) { this.speed = speed; }

    /**
     * Sets the extras.
     * @param extras
     */
    public void setExtras(String extras) { this.extras = extras; }
    
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

    public SignalStrengthOnLocationChangeBlob(float accuracy, double altitude, float bearing,
            double latitude, double longitude, String provider,
            float speed, String extras, int signalStrength, String networkType) {
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.bearing = bearing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
        this.speed = speed;
        this.extras = extras;
        this.signalStrength = signalStrength;
        this.networkType = networkType;
    }

    
    
    /**
     * Default constructor.
     */
    public SignalStrengthOnLocationChangeBlob() { }

}
