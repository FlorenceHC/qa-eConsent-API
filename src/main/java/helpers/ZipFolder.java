package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static helpers.TCLogger.loggerInformation;
import static helpers.TCLogger.loggerStep_Failed;


public class ZipFolder {


    public static void zipFolder(String sourceFolderPath, String zipFilePath) {
        File sourceFolder = new File(sourceFolderPath);
        try (
                FileOutputStream fos = new FileOutputStream(zipFilePath);
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            zipDirectory(sourceFolder, sourceFolder.getName(), zos);
        } catch (IOException ex) {
            loggerStep_Failed("Unable to Zip Folder" , ex.getMessage(), true);

        }
    }

    private static void zipDirectory(File folder, String baseName, ZipOutputStream zos) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectory(file, baseName + File.separator + file.getName(), zos);
            } else {
                addToZip(baseName + File.separator + file.getName(), file, zos);
            }
        }
        loggerInformation("Directory: " + folder + ", successfully zipped");
    }

    private static void addToZip(String entryName, File file, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
        }
    }
}
