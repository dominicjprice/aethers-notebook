package horizon.aether.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Class that provides static helper methods that deal
 * with compression.
 */
public class CompressionUtils {

    /**
     * Compresses a given file using the DEFLATE algorithm.
     * 
     * @param pathIn: The path of the file to compress
     * @param pathOut: The path of the new compressed file
     * @throws IOException 
     */
    public static void compress(String pathIn, String pathOut) throws IOException {
        File compressedFile = new File(pathOut);
        if (!compressedFile.exists()) {
            compressedFile.createNewFile();
        }
        FileInputStream fis = new FileInputStream(pathIn);
        FileOutputStream fos = new FileOutputStream(pathOut);
        DeflaterOutputStream dos = new DeflaterOutputStream(fos);
        copyStream(fis, dos);
    }

    /**
     * Uncompresses a file that has been compressed using 
     * the DEFLATE algorithm.
     * 
     * @param pathIn: The path of the compressed file
     * @param pathOut: The path of the new, uncompressed file.
     * @throws FileNotFoundException 
     */
    public static void uncompress(String pathIn, String pathOut) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(pathIn);
        InflaterInputStream iis = new InflaterInputStream(fis);
        FileOutputStream fos = new FileOutputStream(pathOut);
        
        copyStream(iis, fos);
    }

    private static void copyStream(InputStream is, OutputStream os) {
        try {
            int oneByte;
            while ((oneByte = is.read()) != -1) {
                os.write(oneByte);
            }
            os.close();
            is.close();
        }
        catch (IOException e) { }
    }
}
