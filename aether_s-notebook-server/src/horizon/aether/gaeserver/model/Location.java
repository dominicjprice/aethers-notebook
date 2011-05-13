package horizon.aether.gaeserver.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Class that represents a location. A Location object is described
 * by the following:
 *   - accuracy (float)
 *   - altitude (double)
 *   - bearing (float)
 *   - latitude (double)
 *   - longitude (double)
 *   - provider (String)
 *   - speed (float)
 *   - extras (String)
 */
@PersistenceCapable
public class Location {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private float accuracy;
    
    @Persistent
    private double altitude;
    
    @Persistent
    private float bearing;
    
    @Persistent
    private double latitude;
    
    @Persistent
    private double longitude;
    
    @Persistent
    private String provider;
    
    @Persistent
    private float speed;
    
    @Persistent
    private String extras;
    
    /**
     * Gets the key.
     * @return The key.
     */
    public Key getKey() {
        return this.key;
    }
    
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
     * Constructor.
     */
    public Location(float accuracy, double altitude, float bearing,
                    double latitude, double longitude, String provider,
                    float speed, String extras) {
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.bearing = bearing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
        this.speed = speed;
        this.extras = extras;
    }
    
    /**
     * Default constructor.
     */
    public Location() { }
    
    /**
     * Returns a string representation of the object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(altitude);
        sb.append("_");
        sb.append(bearing);
        sb.append("_");
        sb.append(latitude);
        sb.append("_");
        sb.append(longitude);
        sb.append("_");
        sb.append(provider);
        sb.append("_");
        sb.append(speed);
        sb.append("_");
        sb.append(extras);
        return sb.toString();
    }
}