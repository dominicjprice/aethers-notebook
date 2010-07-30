package horizon.aether.gaeserver.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Class that provides static helper methods that deal
 * with compression.
 */
public class CompressionUtils {
    public static boolean uncompress(InputStream is, OutputStream os) {
        InflaterInputStream iis = new InflaterInputStream(is);
        try {
            int oneByte;
            while ((oneByte = iis.read()) != -1) {
                os.write(oneByte);
            }
            os.close();
            iis.close();
        }
        catch (IOException e) { 
            return false;
        }
        
        return true;
    }
}
