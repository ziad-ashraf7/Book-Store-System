package ziad.bookstoresystem.Controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) throws IOException {
        File testTxtOld = new File("test.txt");
        File testTxtNew = new File("test New.txt");
        Path path = Files.createTempFile("test" , ".txt");
        System.out.println("Creating file: " + testTxtOld.getAbsolutePath());
        try {
            if (testTxtOld.createNewFile()) {
                System.out.println("File created: " + testTxtOld.getName());
            } else {
                System.out.println("File already exists.");
            }
            if (!Files.exists(path)) {
                System.out.println("File Temp created: " + path.toAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        // Open the Old Text File in Notepad
        try {
            System.out.println("Opening file in Notepad.");
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", testTxtOld.getAbsolutePath());
            pb.start();
            System.out.println("File opened in Notepad.");
        } catch (Exception e) {
            System.out.println("An error occurred. Open the file manually." + e.getMessage());
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Replace old file with new file
        try {
            Files.move( path.toAbsolutePath() , testTxtOld.toPath().toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File replaced.");
        } catch (Exception e) {
            System.out.println("An error occurred. " + e.getMessage());
            e.printStackTrace();
        }
    }
}