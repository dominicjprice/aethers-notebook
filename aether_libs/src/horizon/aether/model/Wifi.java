package horizon.aether.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

//import com.google.appengine.api.datastore.Key;

/**
 * Class that represents a wifi network. A wifi network is 
 * described by the following:
 *    - BSSID (String)
 *    - SSID (String)
 *    - capabilities (String)
 *    - frequency (int)
 *    - level (int)
 */
@PersistenceCapable
public class Wifi  {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.UUIDSTRING)
    protected String key;
    
    @Persistent
    private String BSSID;
    
    @Persistent
    private String SSID;
    
    @Persistent
    private String capabilities;
    
    @Persistent
    private int frequency;
    
    @Persistent
    private int level;
    
    /**
     * Gets the key.
     * @return The key.
     */
    public String getKey() { return this.key; }

    /**
     * Gets the BSSID.
     * @return the BSSID
     */
    public String getBSSID() { return this.BSSID; }
    
    /**
     * Gets the SSID.
     * @return the SSID.
     */
    public String getSSID() { return this.SSID; }
    
    /**
     * Gets the capabilities.
     * @return the capabilities.
     */
    public String getCapabilities() { return this.capabilities; }
                  
    /**
     * Gets the frequency.
     * @return the frequency.
     */
    public int getFrequency() { return this.frequency; }
    
    /**
     * Gets the level.
     * @return the level.
     */
    public int getLevel() { return this.level; }
    
    /**
     * Sets the key.
     * @param key
     */
    public void setKey(String key) { this.key = key; }    

    /**
     * Sets the BSSID.
     * @param BSSID
     */
    public void setBSSID(String BSSID) { this.BSSID = BSSID; }
    
    /**
     * Sets the SSID.
     * @param SSID
     */
    public void setSSID(String SSID) { this.SSID = SSID; }
    
    /**
     * Sets the capabilities.
     * @param capabilities
     */
    public void setCapabilities(String capabilities) { this.capabilities = capabilities; }
    
    /**
     * Sets the frequency.
     * @param frequency
     */
    public void setFrequency(int frequency) { this.frequency = frequency; }
    
    /**
     * Sets the level.
     * @param level
     */
    public void setLevel(int level) { this.level = level; }
    
    /**
     * Constructor.
     * @param BSSID
     * @param SSID
     * @param capabilities
     * @param frequency
     * @param level
     */
    public Wifi(String BSSID, String SSID, String capabilities, int frequency, int level) {
        this.BSSID = BSSID;
        this.SSID = SSID;
        this.capabilities = capabilities;
        this.frequency = frequency;
        this.level = level;
    }
    
    /**
     * Default constructor.
     */
    public Wifi() { }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(BSSID);
        sb.append("_");
          sb.append(SSID);
          sb.append("_");
          sb.append(capabilities);
          sb.append("_");
          sb.append(frequency);
          sb.append("_");
          sb.append(level);
          sb.append("_");
        
        return sb.toString();
    }
   
}
