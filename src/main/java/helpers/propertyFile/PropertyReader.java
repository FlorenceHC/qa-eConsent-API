package helpers.propertyFile;

import java.io.FileInputStream;
import java.util.Properties;

import static helpers.TCLogger.*;

public class PropertyReader {


    /**
     * This method reads and extracts the value of a variable from properties file.
     * @param path - path to properties file
     * @param keyName - name of variable
     */

    public static String getProperties (String path, String keyName){
        Properties properties;

        try {
            FileInputStream input = new FileInputStream(path);
            properties = new Properties();
            properties.load(input);
            input.close();
            return properties.getProperty(keyName);
        } catch (Exception ex) {
            loggerInformation("Unable to get properties file path: " + path);
            loggerInformation("Unable to get properties key name " + keyName);
            loggerStep_Failed( "File Input Stream failed: " , ex.getMessage(), true);
        }
        return null;
    }

}
