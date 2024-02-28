package helpers;

import com.github.javafaker.Faker;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.io.FileUtils;
import org.jasypt.util.text.StrongTextEncryptor;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static helpers.TCLogger.*;


public class Utils {


    /**
     * Delete screenshot
     *
     * @param filePath The path to the file
     */
    public static void deleteFile(String filePath) {

        try {
            File f = new File(filePath);
            f.delete();
            loggerInformation("File Deleted: " + filePath);
        } catch (Exception e) {
            loggerInformation("Failed to Delete File: " + filePath);
        }
    }


    /**
     * Get testNG Test Report Log
     * @param result - ITestResult, This class describes the result of a test
     */
    public static String testReport(ITestResult result) {
        try {
            Date d=new Date();
            int year=d.getYear();
            String currentYear= String.valueOf(year+1900);

            return String.valueOf(Reporter.getOutput(result))
//                    .replace("[", "")
//                    .replace("]", "")
//                    .replace("\"", "\\\"")
                    .replace("\n", "")
                    .replaceAll(" --- Test Case ", "\\\n\\\n--- Test Case")
                    .replaceAll("" + currentYear + "-", "\\\n"  + currentYear + "-");
        } catch (Exception ex) {
            loggerStep_Failed("There is no Test Report: " , ex.getMessage(), false);
        }
        return "There is No Test Report";
    }

    /**
     * Get Unix timestamp
     */
    public static int getUnixTime() {

        int unixTime = 0;
        try {
            unixTime = Integer.parseInt(String.valueOf(System.currentTimeMillis() / 1000L));
        } catch (Exception ex) {
            loggerStep_Failed("Unable to get Unix Time: " , ex.getMessage(), false);
        }
        return unixTime;
    }

    /**
     * Convert Unix timestamps to String Simple Date Format
     * @param unixTime - long variable to convert
     * @param zoneGMT - time zone, e.g. GMT+1
     * @param milliseconds - convert to milliseconds if provided timestamps is raw
     */
    public static String convertUnixTime(long unixTime, String zoneGMT, boolean milliseconds) {

        final String gmtTime;
        final long unixTimeMlSec;

        if (milliseconds) unixTimeMlSec = unixTime * 1000L;
        else unixTimeMlSec = unixTime;

        try {
            Date date = new Date(unixTimeMlSec);
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            jdf.setTimeZone(TimeZone.getTimeZone(zoneGMT));
            gmtTime = jdf.format(date);
            return gmtTime;
        } catch (Exception ex) {
            loggerStep_Failed("Unable to convert Unix Time: " , ex.getMessage(), true);
        }
        return null;
    }

    /**
     * Generate random String with upper case and with lower case
     * @param len string length
     */
    public static String randomString_character(int len){
        final String AB = randomString_upperCase(len/2) + randomString_upperCase(len/2).toLowerCase();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    /**
     * Generate random String with upper case
     * @param len string length
     */
    public static String randomString_upperCase(int len){
        final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    /**
     * Generate random String Numbers
     * @param len string length
     */
    public static String randomString_number(int len){
        final String AB = "0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


    /**
     * Get Current Date and Time (Fri Dec 17 16:07:15 GMT 2021)
     */
    public static Date getDate() {
        Date date = new Date();
        loggerInformation("Current Date: " + date);
        return date;

        }

    /**
     * Copy File
     * @param filePath path of the existing file
     * @param newFileName name of the new file
     * @param fileExtension file type (without the dot ".")
     * @return new file path
     */
    public static String copyFile(String filePath, String newFilePath, String newFileName, String fileExtension){

        Path pathIn = Paths.get(filePath);
        Path pathOut = Paths.get( newFilePath, newFileName + "." + fileExtension);

            try {
                Files.copy(pathIn, pathOut, StandardCopyOption.REPLACE_EXISTING);
                loggerInformation("File Copy-Rename from: " + filePath +" to: " + pathOut);
                return String.valueOf(pathOut);
        }
            catch (IOException ex) {
                loggerStep_Failed("Unable to Copy File: " , ex.getMessage(), true);
        }
        return null;
    }

    /**
     * Cut string by space between values
     * @param stringForCut the string we want to shorten
     * @param regex by which character the string will be divided
     * @param part the part of the string we want to take  (eg 0- first part; 1 - second part;...)
     * @return cut string
     */
    public static String cutString(String stringForCut, String regex, int part){

        String partOfString = null;
        try {
            String[] forCutFull = stringForCut.split(regex);
            partOfString = forCutFull[part];
//            LoggerInformation("Cut String value is: " + partOfString);
        } catch (Exception ex) {
            loggerStep_Failed("Unable to Cut String: " , ex.getMessage(), true);
        }
        return partOfString;
    }




    /**
     * Get First Name from email
     * @param email path to the image
     * @return imageTxt string
     */
    public static String firstName(String email) {
        String name;
        String regex;
        if (email.contains("@")) {
            regex = "\\.";
        } else {
            regex = " ";
        }
        String[] part = email.split(regex);
        name = part[0];

        return name;
    }

    /**
     * Get Current Method Name
     */
    public static String getMethodName() {
        String methodName= null;
        try {
            methodName = new Object(){}.getClass().getEnclosingMethod().getName();
            loggerInformation("Current Method Name is: " + methodName);
            return methodName;
        } catch (Exception ex) {
            loggerStep_Failed("Unable to get Method Name : " , ex.getMessage(), false);
            return methodName;
        }
    }

    /**
     * Get Current Class Name
     */
    public String getClassName() {
        String className= null;
        try {
            className = this.getClass().getName();
            loggerInformation("Current Class Name is: " + className);
            return className;
        } catch (Exception ex) {
            loggerStep_Failed("Unable to get Class Name : " , ex.getMessage(), false);
            return className;
        }
    }



    /**
     * Base64 Encoder
     * @param filePath path to the file to encode
     */
    public static String encodeFile(String filePath) {
        try {
            File fileForEncode = new File(filePath);
            byte[] fileContent = FileUtils.readFileToByteArray(fileForEncode);
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException ex) {
            loggerStep_Failed("Unable to convert file to Base64", ex.getMessage(), false);
        }
        return null;
    }

    /**
     * HMAC - cryptographic method
     * @param algorithm for encoding
     * @param data path string to encode
     * @param key for encoding
     */
    public static String hmacWithApacheCommons(String algorithm, String data, String key) {
        return new HmacUtils(algorithm, key).hmacHex(data);
    }

    /**
     * jasypt for Encoding Data
     * jasypt - cryptographic method
     * @param dataForEncoding path string to encode
     * @param setPassword for encoding
     */
    public static String jasyptEncodingString(String setPassword, String dataForEncoding) {
        String encodedData = null;
        try {
            StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
            textEncryptor.setPassword(setPassword);
            encodedData = textEncryptor.encrypt(dataForEncoding);
        } catch (Exception ex) {
            loggerStep_Failed("Unable to encode data: " + dataForEncoding, String.valueOf(ex.getCause()), false);
        }
        return encodedData;
    }

    /**
     * jasypt for Decoding Data
     * jasypt - cryptographic method
     * @param dataForDecoding path string to encode
     * @param setPassword for encoding
     */
    public static String jasyptDecodingString(String setPassword, String dataForDecoding) {
        String decodedData = null;
        try {
            StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
            textEncryptor.setPassword(setPassword);
            decodedData = textEncryptor.decrypt(dataForDecoding);
        } catch (Exception ex) {
            loggerStep_Failed("Unable to decode data: " + dataForDecoding, String.valueOf(ex.getCause()), false);
        }
        return decodedData;
    }


    public static void printingTheRemainingMinutes(int waitTime, int numberOfMinutes) throws InterruptedException {

         loggerInformation("The " + numberOfMinutes + "-minute countdown begins");
         while (numberOfMinutes !=0){
             loggerInformation(numberOfMinutes + " minutes left to go.");
             Thread.sleep(waitTime);
             numberOfMinutes--;
             }
        }
    /**
     * Random regex ID generator
     * For generation ID for all kind of documents
     */

    public static String randomRegexGenerator(String regex, String characters) {
        Faker faker = new Faker();
        return faker.regexify(regex + "{" + characters + "}");
    }

    /**
     * This method generates random names
     */

    public static String generateName() {
            Faker faker = new Faker();
            return "Automation Generate Name " + faker.regexify("[A-Za-z0- ]{10}");
        }

    /**
     * This method generates random description
     */
    public static String generateDescription() {
            Faker faker = new Faker();
            return "Automation Generate Description " + faker.regexify("[A-Za-z0- ]{10}");
        }


    /**
     * This method generates date and time "
     * @param offSet offsets time in seconds. If you need to set date in past just provide negative number
     * @param  timeFormat is expecting time format. example ("yyyy-MM-dd'T'HH:mm:ss'Z'")
     * @param  zoneID is expecting time zone ID. example (Europe/Berlin)
     */

    public static String generateOffSetDate(long offSet, String timeFormat,String zoneID) {
        long unixTimeMillis = System.currentTimeMillis() + offSet * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        sdf.setTimeZone(TimeZone.getTimeZone(zoneID));
        return sdf.format(new Date(unixTimeMillis));
    }

    /**
     * xRay User Selector
     * Every other day, the method chooses a different Xray user
     */
    public static String xRayUserSelector(String firstUser, String secondUser) {

        String user;
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(2023, 1, 1);  // Adjust the start date as needed
        long daysSinceStart = startDate.until(currentDate).getDays();

        if (daysSinceStart % 2 == 0) {
            user = firstUser;
        } else {
            user = secondUser;
        }
        return user;
    }

    /**
     * Create Directory In Properties File
     *
     * @param path Path to the file
     */
    public static void makeDirectory(String path){
        try {
            File theDir = new File(path);
            if (theDir.mkdirs()){
                loggerInformation("Directory has been Created: " + path);
            }else if (theDir.isDirectory()){
                loggerInformation("The Directory already Created: " + path);
            }

        } catch (Exception ex) {
            loggerStep_Failed("Unable to Make Directory" , String.valueOf(ex.getMessage()), false);
        }
    }

    /**
     * write In Properties File
     *
     * @param data data for writing
     * @param path Path to the file
     */
    public static void writeInTxtFile(String data, String path){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(data);
            writer.close();
            loggerInformation("Write Data to Txt file: " + path);
        } catch (IOException ex) {
            loggerStep_Failed("Unable to Write in Txt File" , String.valueOf(ex.getMessage()), false);
        }
    }


    /**
     * write In Properties File
     *
     * @param propertiesName Name of Properties
     * @param value Value of Properties
     * @param path Path to the Properties file
     */
    public static void writeInPropertiesFile(String propertiesName, String value, String path){
        try {
            //Instantiating the properties file
            Properties props = new Properties();
            //Populating the properties file
            props.put(propertiesName, value);
            //Storing the properties file
            props.store(new FileOutputStream(path), "This is " + propertiesName + ".properties file");

            loggerInformation("Properties file created: " + path);
            loggerInformation("Properties file value added: " + value);
        } catch (IOException ex) {
            loggerInformation("Properties file: " + path + " -Not created " + ex.getMessage() );
        }
    }

    public static String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * write In Properties File
     *
     * @param testCaseID Test Case ID
     * @param nameTC Name of Test Case
     * @param log Test Case Log
     * @param testStatus Path to the file
     *
     */
    public static void writeLogToFile(String testCaseID, String nameTC, String log, String testStatus){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long ts = timestamp.getTime();
        String dirPath = System.getProperty("user.dir") + File.separator + "evidence";
        makeDirectory(dirPath);
        String fileNameLog = testCaseID + "_" + nameTC + "_" + ts + "_log -->>" + testStatus + "<<-- .txt";
        String filePathLog = dirPath + File.separator + fileNameLog;
        writeInTxtFile(log, filePathLog);
    }
}
