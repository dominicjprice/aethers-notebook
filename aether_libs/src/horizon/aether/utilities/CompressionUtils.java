package horizon.aether.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Class that provides static helper methods that deal
 * with compression.
 */
public class CompressionUtils {
	public enum CompressionType {
		 NONE, GZIP, DEFAULT;
	}
	
    public static boolean uncompress(InputStream is, OutputStream os) throws Exception {
       return  uncompress(is, os, CompressionType.DEFAULT);
    }
    
    public static boolean uncompress(InputStream is, OutputStream os, CompressionType type) throws Exception {
		InputStream input = null;
    	switch(type)
        {
	        case DEFAULT:
	        	input = new InflaterInputStream(is);
	        	break;
	        case GZIP:
	        	input = new GZIPInputStream(is);
	        	break;
	        case NONE:
	        	input = is;
	        	break; 
	        default:
	        	throw new Exception("Can't determine compression type!");
        }
    	try {
            int oneByte;
            while ((oneByte = input.read()) != -1) {
                os.write(oneByte);
            }
            os.close();
            input.close();
        }
        catch (IOException e) { 
            return false;
        }
        
        return true;
    }
}

