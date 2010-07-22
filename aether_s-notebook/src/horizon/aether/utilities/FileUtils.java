package horizon.aether.utilities;

import java.io.File;

public class FileUtils {

    /**
     * Returns the size of a directory in bytes.
     */
    public static long getDirSize(File dir) {
        long size = 0;
        if (dir.isFile()) {
            size = dir.length();
        } 
        else {
            File[] files = dir.listFiles();
            
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } 
                else {
                    size += getDirSize(file);
                }
            }
        }
        
        return size;
    }
    
    /**
     * Checks if a directory exists (and is indeed a directory) or
     * otherwise if it can be created.
     */
    public static boolean checkDirExistsOrCanBeCreated(String path) {
        File dir = new File(path);
        return (dir.exists() && dir.isDirectory()) ||
               (!dir.exists() && dir.mkdir());
    }
    
    /**
     * Makes sure a directory path has a "/" in the end.
     */
    public static String fixDirPath(String dirPath) {
        if (dirPath.charAt(dirPath.length()-1) != '/') {
            dirPath += "/";
        }
        
        return dirPath;
    }
}
