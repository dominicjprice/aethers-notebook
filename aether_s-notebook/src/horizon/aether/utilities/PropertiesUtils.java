package horizon.aether.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that provides static helper methods to deal with application properties
 * which are saved in app.properties file. 
 */
public class PropertiesUtils {

    /**
     * Returns the server address.
     * @return the server's url string.
     */
    public static String getServerUrl() {
        return getAppProperty("serverUrl");
    }
    
    
    /**
     * Helper method that returns a property from the app.properties file.
     * @param property
     * @return
     */
    private static String getAppProperty(String property) {
        Properties props = new Properties();
       
        try {
            props.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("app.properties"));
        } 
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } 
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return props.getProperty(property);
    }
}
