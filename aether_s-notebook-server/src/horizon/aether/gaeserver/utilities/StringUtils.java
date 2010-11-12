package horizon.aether.gaeserver.utilities;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * Class that provides static helper methods that deal
 * with Strings.
 */
public class StringUtils {

    /**
     * Checks if a String is null or empty.
     */
    public static boolean isNullOrEmpty(String str) {
        return (str == null || str == ""); 
    }
    
    /**
     * Converts a timestamp to string yyyy-mm-dd hh:mm:ss
     * @param long timestamp unix style timestamp
     */
    public static String toDateTimeString(long timestamp) {
        String DATE_FORMAT = "yyyy-MM-dd h:m:s";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date(timestamp));
      }
}
