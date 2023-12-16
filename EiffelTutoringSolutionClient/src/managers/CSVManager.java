package managers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import gui.Main;

public class CSVManager {
	
	// Even thought the data are all registered on the service's host, some data
	// are gathered on the client's computer and loaded from it, in order to make
	// them appear on the html file which is loaded by the CalendarView class.
	// Actually, the html calendar files cannot load the data from the server
	// but locally only. This is the reason why this class, CSVManager, exists
	// on the client side.
	
	// ============================================
    // ====== GET THE HEADERS OF A DATA FILE ======
	// ============================================
	
	private static String[] getFileHeaders(String fileName) {
		String headers[];
		if(fileName.toLowerCase().contains("student")) {
			headers = new String[]{"name", "surname", "email", "password", "is_gustave", "balance", "student_id"};
		} else if(fileName.toLowerCase().contains("tutor")) {
			headers = new String[]{"name", "surname", "email", "password", "tutor_id"};
		} else if(fileName.toLowerCase().contains("lesson")) {
			headers = new String[]{"date", "start_time", "end_time", "is_online", "type", "cost", "student_id", "tutor_id", "lesson_id"};
		} else { // messages.csv
			headers = new String[]{"waiting_for_answer", "answer", "recipient_id", "student_id", "tutor_id", "lesson_id", "message_id"};
		}
		return headers;
	}
	
	// =============================================================================
    // ====== GET THE DATA TYPE OF A FILE RELATIVELY TO THE NAME OF THIS FILE ======
	// =============================================================================
	
	public static String getDataType(String dataPath, boolean firstUpperCase) {
		String dataName = null;
		if(dataPath.contains("lessons")) {
			dataName = "lesson";
		} else { // if(dataPath.contains("messages"))
			dataName = "message";
		}
		if(firstUpperCase) {
			dataName = dataName.substring(0, 1).toUpperCase() + dataName.substring(1);
		}
		return dataName;
	}
	
	
	// =====================================================
    // ====== GET THE COLUMN ID OF AN ACCOUNT ACCOUNT ======
	// =====================================================
	
    public static String getAccountId(String accType, String[] accInfos) {
    	if(accType.equals("student")) {
    		return accInfos[6];
    	} else {
    		return accInfos[4];
    	}
    }
    

	// ==============================================
    // ====== CREATE A  NON EXISTING DATA FILE ======
	// ==============================================
    
	public static void dataFileCreation(String fileToCreate) {
		String headers[] = getFileHeaders(fileToCreate);
    	// Trying to create the file requested
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileToCreate))) {
            writer.println(String.join(",", headers));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	// =====================================================================
    // ====== CHECK IF A LESSON IS ALREADY IN THE ACCOUNTLESSONS LIST ======
	// =====================================================================
	
	private static boolean lessonAlreadyThere(List<String[]> accountLessons, String value) {
	    for (String[] lesson : accountLessons) {
	        // 8 is the index relative to the id column of a lesson
	        if (lesson.length > 8 && lesson[8] != null && lesson[8].equals(value)) { // ùùùùù
	            return true; // The lesson already exist
	        }
	    }
	    // The lesson doesn't exist
	    return false;
	}
	
	// =============================================
    // ====== LOAD THE LESSONS OF THE ACCOUNT ======
	// =============================================
	
    public static void loadLesson() throws RemoteException {
    	String accType = Main.getAccountType(); 								// account type
    	String[] accInfos = Main.getAccountInformations(); 						// informations of the account
    	String id = getAccountId(accType, accInfos); 	// id of the account
    	List<String[]> accountLessons = new ArrayList<>(); 						// list used to store lessons 
    	String filePath = "src\\gui\\calendar\\lessons.csv"; 					// location of the stored data
    	
    	// Retrieving lessons of the account
    	String[][] rows = Main.getClientManager().findRowsUser(id, "lessons", accType);
    	
    	// Each lesson retrieved is added to the list accountLessons, which manages the lessons of the connected account
        for (String[] row : rows) {
            if (!lessonAlreadyThere(accountLessons, row[8])) {
                accountLessons.add(row);
            }
        }
        
    	// Creating a file that will contain lessons of the account
    	CSVManager.dataFileCreation(filePath);
    	
    	// Writing the lessons in the field
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (String[] line : accountLessons) {
                // Join the elements of the line array into a CSV-formatted string
                String lineToAdd = String.join(",", line);
                // Write the CSV-formatted line to the file
                writer.write(lineToAdd);
                // Add a new line to separate each entry
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }
    
    
	// ==========================================
    // ====== CHECK IS A STRING IS NUMERIC ======
	// ==========================================
    
	public static boolean isNumeric(String string) {
		try {
			Double.parseDouble(string);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
    
	// ====================================
    // ====== LOAD AVAILABLE LESSONS ======
	// ====================================
    
    public static String[][] loadAvailableLessons() throws RemoteException {
    	String[][] availableLessons = Main.getClientManager().findAvailableLessons();
        return availableLessons;
    }	
}
