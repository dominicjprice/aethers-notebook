/**
 * 
 */
package horizon.aether.decompressLog;

import horizon.aether.utilities.CompressionUtils;
import horizon.aether.utilities.CompressionUtils.CompressionType;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * @author Mark Paxton mcp@cs.nott.ac.uk
 * A small app that decompresses cloudpad dfl log files 
 *
 */
public class DecompressLog {
	private static final Logger log = Logger.getLogger(DecompressLog.class.getName());

    public static void main(String[] args) throws Exception
	{
    	//Set up the log4j log to log to console
    	BasicConfigurator.configure();

    	if(args.length==0)
    	{
    		throw new Exception("No input file specified!");
    	}
    	String filenameArg = args[0];
    	CompressionType compressionType = CompressionType.DEFAULT;
    	if(args.length > 1)
    	{
    		String compressionTypeArg = args[1];
	    	if(compressionTypeArg.contentEquals("none"))
	    	{
	    		compressionType = CompressionType.NONE;
	    	}
	    	else if (compressionTypeArg.contentEquals("gz"))
	    	{
	    		compressionType = CompressionType.GZIP;
	    	}
    	}
    	try
    	{
	    	String sourceFileName = filenameArg;
	    	String outputFileName = sourceFileName + ".log";
	    	
	    	BufferedInputStream stream = new BufferedInputStream(new FileInputStream(sourceFileName));
	    	FileOutputStream uncompressedStream = new FileOutputStream(outputFileName);
	        CompressionUtils.uncompress(stream, uncompressedStream, compressionType);
		}
	    catch(Exception ex)
	    {
	    	log.fatal("Error somewhere when trying to decompress file!", ex);
	    }
	}
}
