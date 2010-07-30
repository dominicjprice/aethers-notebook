package horizon.aether.utilities;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that provides static helper methods to deal with files.
 */
public class FileUtils {

    /**
     * Returns the size of a directory.
     * 
     * @param dir
     * @return The directory size in byte.
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
     * Checks if a directory exists (and is indeed a directory) or,
     * otherwise, if it can be created.
     * 
     * @param dirPath: path to the directory.
     * @return whether the directory exists or can be created.
     */
    public static boolean checkDirExistsOrCanBeCreated(String dirPath) {
        File dir = new File(dirPath);
        return (dir.exists() && dir.isDirectory()) ||
        (!dir.exists() && dir.mkdir());
    }

    /**
     * Makes sure a directory path has a "/" in the end.
     * 
     * @param dirPath: path to the directory
     * @return the correct directory path
     */
    public static String fixDirPath(String dirPath) {
        if (dirPath.charAt(dirPath.length()-1) != '/') {
            dirPath += "/";
        }

        return dirPath;
    }

    /**
     * Uploads a file to a server.
     * 
     * @param filePath: The path of the file to upload.
     * @param serverUrl: The server address.
     */
    public static boolean uploadFile(String filePath, String serverUrl) {
        // unique random number
        String boundary = Long.toHexString(System.currentTimeMillis()); 
        
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024*1024;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));

            URL url = new URL(serverUrl);
            connection = (HttpURLConnection) url.openConnection();

            // enable POST method
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes("--" + boundary + "\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + filePath +"\"" + "\r\n");
            outputStream.writeBytes("\r\n");

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("--" + boundary + "--\r\n");

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            // retrieve the response from server
            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            return (responseCode == 200);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
