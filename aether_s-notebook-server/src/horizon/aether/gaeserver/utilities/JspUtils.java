package horizon.aether.gaeserver.utilities;

import horizon.aether.gaeserver.model.DataConnectionStateEntry;
import horizon.aether.gaeserver.model.ServiceStateEntry;
import horizon.aether.gaeserver.model.SignalStrengthEntry;
import horizon.aether.gaeserver.model.TelephonyStateEntry;

import java.util.List;

/**
 * Class that contains static helper methods for drawing maps. These
 * methods create a script string that, when executed, it draws circles
 * on a map according to the given entries.
 */
public class JspUtils {

    /**
     * Draws a Data Connection State map.
     */
    public static String drawDataConnectionStateMap(List<DataConnectionStateEntry> entries) {
        StringBuffer r = new StringBuffer();

        if (entries.size() > 0) {
            for (DataConnectionStateEntry entry : entries) {
                if (entry.getLocation() != null) {
                    double lat = entry.getLocation().getLatitude();
                    double lon = entry.getLocation().getLongitude();
                    int acc = (int) (entry.getLocation().getAccuracy());
                    String colour = getDataConnectionStateColour(entry.getState());
                    if (colour != null) {
                        r.append(drawTheCircle(lat, lon, acc, colour));
                    }
                }
            }
        }

        return r.toString();
    }

    /**
     * Draws a Service State map.
     */
    public static String drawServiceStateMap(List<ServiceStateEntry> entries) {
        StringBuffer r = new StringBuffer();

        if (entries.size() > 0) {
            for (ServiceStateEntry entry : entries) {
                if (entry.getLocation() != null) {
                    double lat = entry.getLocation().getLatitude();
                    double lon = entry.getLocation().getLongitude();
                    int acc = (int) (entry.getLocation().getAccuracy());
                    String colour = getServiceStateColour(entry.getState());
                    if (colour != null) {
                        r.append(drawTheCircle(lat, lon, acc, colour));
                    }
                }
            }
        }

        return r.toString();
    }

    /**
     * Draws a Signal Strength map.
     */
    public static String drawSignalStrengthMap(List<SignalStrengthEntry> entries) {
        StringBuffer r = new StringBuffer();

        if (entries.size() > 0) {
            for (SignalStrengthEntry entry : entries) {
                if (entry.getLocation() != null) {
                    double lat = entry.getLocation().getLatitude();
                    double lon = entry.getLocation().getLongitude();
                    int acc = (int) (entry.getLocation().getAccuracy());
                    int strength = entry.getSignalStrength();
                    String colour = getSignalColour(strength);
                    if (colour != null) {
                        r.append(drawTheCircle(lat, lon, acc/10, colour));
                    }
                }
            }
        }

        return r.toString();
    }

    /**
     * Draws a Telephony State map.
     */
    public static String drawTelephonyStateMap(List<TelephonyStateEntry> entries) {
        StringBuffer r = new StringBuffer();

        if (entries.size() > 0) {
            for (TelephonyStateEntry entry : entries) {
                if (entry.getLocation() != null) {
                    double lat = entry.getLocation().getLatitude();
                    double lon = entry.getLocation().getLongitude();
                    int acc = (int) (entry.getLocation().getAccuracy());
                    String colour = getTelephonyStateColour(entry.getNetworkType());
                    if (colour != null) {
                        r.append(drawTheCircle(lat, lon, acc, colour));
                    }
                }
            }
        }

        return r.toString();
    }

    /////////////////////////////////////////////////////////////////

    private static String drawTheCircle(double lat, double lon, int acc, String colour) {
        if (colour != null) {
            return "\ndrawCircle(map, " + lat + ", " + lon + ", "  + acc + ", \"#" + colour + "\");";
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////

    // COLOUR METHODS

    private static String getSignalColour(int strength) {       
        String[] colours = new String[] {
                "F7F7FF", "EFEFFF", "E6E6FF", "DEDEFF", "D6D6FF", 
                "CECEFF", "C5C5FF", "BDBDFF", "B5B5FF", "ADADFF", 
                "A5A5FF", "9C9CFF", "9494FF", "8C8CFF", "8484FF", 
                "7B7BFF", "7373FF", "6B6BFF", "6363FF", "5A5AFF", 
                "5252FF", "4A4AFF", "4242FF", "3A3AFF", "3131FF", 
                "2929FF", "2121FF", "1919FF", "1010FF", "0808FF", "0000FF", 
        };

        if (strength >= 0 && strength < 32) {
            return colours[strength];
        }
        else {
            return null;
        }
    }

    private static String getDataConnectionStateColour(String state) {
        if (state.equals(DataConnectionStateEntry.DATA_CONNECTED))
            return "00BC16";    // green
        else if (state.equals(DataConnectionStateEntry.DATA_CONNECTING))
            return "0026FF";    // blue
        else if (state.equals(DataConnectionStateEntry.DATA_DISCONNECTED))
            return "FF0000";    // red
        else if (state.equals(DataConnectionStateEntry.DATA_SUSPENDED))
            return "FFD800";    // yellow

        return null;
    }

    private static String getServiceStateColour(String state) {
        if (state.equals(ServiceStateEntry.STATE_IN_SERVICE))
            return "00BC16";    // green
        else if (state.equals(ServiceStateEntry.STATE_EMERGENCY_ONLY))
            return "0026FF";    // blue
        else if (state.equals(ServiceStateEntry.STATE_POWER_OFF))
            return "FF0000";    // red
        else if (state.equals(ServiceStateEntry.STATE_OUT_OF_SERVICE))
            return "FFD800";    // yellow

        return null;
    }

    private static String getTelephonyStateColour(String networkType) {
        if (networkType.equals(TelephonyStateEntry.NETWORK_TYPE_EDGE))
            return "00BC16";    // green
        else if (networkType.equals(TelephonyStateEntry.NETWORK_TYPE_GPRS))
            return "0026FF";    // blue
        else if (networkType.equals(TelephonyStateEntry.NETWORK_TYPE_UMTS))
            return "FF0000";    // red
        else if (networkType.equals(TelephonyStateEntry.NETWORK_TYPE_UNKNOWN))
            return "FFD800";    // yellow

        return null;
    }

}
