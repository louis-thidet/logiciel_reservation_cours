package managers;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import gui.Main;

public class LogManager {

	// =================================================================
    // ===== GET THE PATH TO THE LOG RELATIVELY TO THE SYSTEM'S OS =====
	// =================================================================
	
	public static String[] getlogPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        String directoryPath;
        String folderName;
        String[] infos = new String[2];
        
        if (osName.contains("linux") || osName.contains("mac")) {
            directoryPath = System.getProperty("user.home") + File.separator + ".eiffel-tutoring-solution";
            folderName = ".eiffel-tutoring-solution";
        } else if (osName.contains("windows")) {
            directoryPath = System.getProperty("user.home") + File.separator + "eiffel-tutoring-solution";
            folderName = "eiffel-tutoring-solution";
        } else {
            System.err.println("Unsupported operating system: " + osName);
            return null;
        }
        infos[0] = directoryPath;
        infos[1] = folderName;
        return infos;
	}
	
	// =================================
    // ===== RETRIEVE THE LOG FILE =====
	// =================================
	
	public static String getLogFile() {
        String directoryPath = getlogPath()[0];
        String filePath;
        String folderName = getlogPath()[1];

        File directory = new File(directoryPath);

        if (!directory.exists() && directory.mkdirs()) {
            Main.infoMessage(folderName+" doesn't exist, it will be created", Color.RED, false);
            Main.infoMessage(folderName+" directory created successfully", Color.BLUE, false);
        } else if(!directory.exists() && !directory.mkdirs()) {
        	Main.infoMessage(folderName+" doesn't exist, it will be created", Color.RED, false);
        	Main.infoMessage("Failed to create the Versus directory", Color.RED, false);
        }

        filePath = directoryPath + File.separator + "logs.txt";
        return filePath;
	}
	
	// =============================
    // ===== CREATE A LOG FILE =====
	// =============================
	
	public static void createLogFile(String createdOrErased) {
		String filePath = getLogFile();
		String message = "Log file "+createdOrErased+" successfully";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
    		Main.infoMessage(message, Color.BLUE, false);
        } catch (IOException e) {
            e.printStackTrace();
            Main.infoMessage("logs.txt "+"couldn't be created", Color.RED, false);
        }
	}
	
	// =========================================
    // ===== WRITING A LOG IN THE LOG FILE =====
	// =========================================
	
	public static void writeLogFile(String message) {
		String logPath = getlogPath()[0]+File.separator+"logs.txt";
        // Create or append to the log file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logPath, true))) {
            // Write the message to the log file
            writer.write(message);
            // Add a new line to separate each log entry
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	
	
	
	
	
	
	
}
