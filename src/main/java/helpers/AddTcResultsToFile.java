package helpers;

import java.io.*;

import static helpers.TCLogger.*;
import static helpers.propertyFile.DataProvider.*;

;

public class AddTcResultsToFile {

    public static void addTcResultToFile_failed() {

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m"; // Red color

        String folderPath = "evidence";
        String outputPath = "results_failed.txt";

        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {

                    String suiteNameFix = TEST_RUN;
                    if (!TEST_RUN.equals("Regression") && !TEST_RUN.equals("Smoke")) {suiteNameFix = "Partial Regression";}
                    String msg = "Test Execution Results - " + suiteNameFix +  " -- "  + "> " + DEV_TEAM + " " + APP_NAME + " " + ENVIRONMENT + "_" + SERVER;
                    writer.println(msg);
                    writer.println("");
                    writer.println("-->>FAILED TEST CASES<<--");

                    int sequenceNumber = 1;

                    for (File file : files) {
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                            String fileNameWithSequence = (sequenceNumber + ". " + file.getName()).replace(".txt", "").replace("_", " "). replace(" log", "");
                            if (fileNameWithSequence.toLowerCase().contains("failed")) {
                                String coloredFileName = ANSI_RED + fileNameWithSequence + ANSI_RESET;
                                loggerTCResults(coloredFileName);
                                writer.println(fileNameWithSequence);
                            }
                            sequenceNumber++;
                        }
                    }
                } catch (IOException ex) {
                    loggerStep_Failed("Unable to add TC Result To File: ", ex.getMessage(), false);
                }

            } else {
                loggerInformation("No files found in the folder.");
            }
        } else {
            loggerInformation("The specified path is not a directory.");
        }
    }

    public static void addTcResultToFile_skipped() {

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_ORANGE = "\u001B[38;5;208m"; // Orange color

        String folderPath = "evidence";
        String outputPath = "results_skipped.txt";

        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {

                    writer.println("");
                    writer.println("-->>SKIPPED TEST CASES<<--");

                    int sequenceNumber = 1;

                    for (File file : files) {
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                            String fileNameWithSequence = (sequenceNumber + ". " + file.getName()).replace(".txt", "").replace("_", " "). replace(" log", "");
                            if (fileNameWithSequence.toLowerCase().contains("skipped")) {
                                String coloredFileName = ANSI_ORANGE + fileNameWithSequence + ANSI_RESET;
                                loggerTCResults(coloredFileName);
                                writer.println(fileNameWithSequence.replace("", ""));
                            }
                            sequenceNumber++;
                        }
                    }
                } catch (IOException ex) {
                    loggerStep_Failed("Unable to add TC Result To File: ", ex.getMessage(), false);
                }

            } else {
                loggerInformation("No files found in the folder.");
            }
        } else {
            loggerInformation("The specified path is not a directory.");
        }
    }
    public static void addTcResultToFile_passed() {

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_GREEN = "\u001B[32m"; // Green color

        String folderPath = "evidence";
        String outputPath = "results_passed.txt";

        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {

                    writer.println("");
                    writer.println("-->>PASSED TEST CASES<<--");

                    int sequenceNumber = 1;
                    for (File file : files) {
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                            String fileNameWithSequence = (sequenceNumber + ". " + file.getName()).replace(".txt", "").replace("_", " "). replace(" log", "");
                            if (fileNameWithSequence.toLowerCase().contains("passed")) {
                                String coloredFileName = ANSI_GREEN + fileNameWithSequence + ANSI_RESET;
                                loggerTCResults(coloredFileName);
                                writer.println(fileNameWithSequence);
                            }
                            sequenceNumber++;
                        }
                    }
                } catch (IOException ex) {
                    loggerStep_Failed("Unable to add TC Result To File: ", ex.getMessage(), false);
                }

            } else {
                loggerInformation("No files found in the folder.");
            }
        } else {
            loggerInformation("The specified path is not a directory.");
        }
    }
    public static void mergeTxtFiles(String file1, String file2, String file3, String mergedFileName) {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2));
             BufferedReader reader3 = new BufferedReader(new FileReader(file3));
             BufferedWriter writer = new BufferedWriter(new FileWriter(mergedFileName))) {

            String line;
            while ((line = reader1.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            while ((line = reader2.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            while ((line = reader3.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            loggerInformation("Files merged successfully.");

        } catch (IOException ex) {
            loggerStep_Failed("Unable to Merge TXT Files: ", ex.getMessage(), false);
        }
    }
    public static void generateTestResults(){
        addTcResultToFile_failed();
        addTcResultToFile_skipped();
        addTcResultToFile_passed();
        mergeTxtFiles("results_failed.txt", "results_skipped.txt", "results_passed.txt", "results.txt");
    }
}
