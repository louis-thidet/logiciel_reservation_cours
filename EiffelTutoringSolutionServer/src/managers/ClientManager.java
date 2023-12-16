package managers;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import gui.Main;



public class ClientManager extends UnicastRemoteObject implements IClientManager {

	// =======================
    // ===== CONSTRUCTOR =====
    // =======================
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientManager() throws RemoteException {
	}
	
    // ==================================================================
    // ===== SEND A MESSAGE TO THE SERVER WHEN A USER HAS CONNECTED =====
    // ==================================================================
	
	public void connexionMessage() throws RemoteException {
		Main.infoMessage("A user has established a connection to the service", Color.BLACK, false);
	}
	
    // =============================
    // ===== CREATE AN ACCOUNT =====
    // =============================
	
	public void createAccount(String email, String password, String userType) throws IOException {
		String dataToLoad;	// data file to load
		String lineToAdd;	// Line that will be added in the list of student or tutor
	    String name;		// Name of the user who wants to register
	    String surname;		// Surname of the user who wants to register
	    int id;				// Id that will be given to the user if their registration succeeds
	    
	    // If the email is valid, which means: ends by @edu.univ-eiffel.fr or @univ-eiffel.fr and has the following format : name.surname@...
	    if((email.contains("@edu.univ-eiffel.fr") || email.contains("@univ-eiffel.fr")) && email.matches("[a-zA-Z]+\\.[a-zA-Z]+@.*") && !password.equals("") && !isEmailUsed(email)) {
	    	
	    	// Retrieving name and surname from the email's split
    		String[] nameSurname = email.split("@")[0].split("\\.");
            name = nameSurname[0];
            surname = nameSurname[1];
            
            // If the user is registered as a student
    		if(userType.equals("student")) {
    			
    			// Creating the row of the account in the proper data file
    			dataToLoad = "src\\data\\students.csv";
    			// Getting id of the new account
    			id = CSVManager.getLastId(dataToLoad, 59, 6); // 59 is number of bytes in data file's headers, and 6 is the column of it's id
    			lineToAdd = name+","+surname+","+email+","+password+","+"yes"+","+"not_concerned"+","+id;
    			
			// If the user is registered as a tutor
    		} else {
    			
    			// Creating the row of the account in the proper data file
    			dataToLoad = "src\\data\\tutors.csv";
    			// Getting id of the new account
    			id = CSVManager.getLastId(dataToLoad, 38, 4); // 38 is number of bytes in data file's headers, and 4 is the column of it's id
    			lineToAdd = name+","+surname+","+email+","+password+","+id;
    		}
    		// The user is registered in the proper file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataToLoad, true))) {
                writer.write(lineToAdd);
                writer.newLine();
            }
            // A message is out on the server to tell that the user has been registered
            Main.infoMessage(userType+": "+name+" "+surname+" (ID: "+id+") has been registered",Color.BLACK, false);
	    }
	}
	
    // ============================================
    // ===== CREATE AN ACCOUNT ON WEB SERVICE =====
    // ============================================
	
	public void createAccountWebService(String email, String name, String surname, String password) throws IOException {
		String dataToLoad;	// data file to load
		String lineToAdd;	// Line that will be added in the list of student or tutor
	    int id;				// Id that will be given to the user if their registration succeeds
	    
	    // If there is no empty field and if email is free
	    if((!email.equals("") && !name.equals("") && !surname.equals("") &&!password.equals("") && !isEmailUsed(email))) {
            
	    	// Creating the row of the account in the proper data file
			dataToLoad = "src\\data\\students.csv";
			// Getting id of the new account
			id = CSVManager.getLastId(dataToLoad, 59, 6); // 59 is number of bytes in data file's headers, and 6 is the column of it's id
			lineToAdd = name+","+surname+","+email+","+password+","+"no"+","+"0"+","+id;
    		
    		// The user is registered in the proper file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataToLoad, true))) {
                writer.write(lineToAdd);
                writer.newLine();
            }
            // A message is out on the server to tell that the user has been registered
            Main.infoMessage("Student" + ": "+name+" "+surname+" (ID: "+id+") has been registered",Color.BLACK, false);
	    }
	}
	
    // =====================================
    // ===== CHECK IS AN EMAIL IS USED =====
    // =====================================
	
	public boolean isEmailUsed(String email) throws FileNotFoundException, IOException {
		boolean existingAccount = false;	// boolean to check if the account exists
		
		// Checking if the email is used among the students
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\data\\students.csv"))) {
        	String currentLine;
            String rowData[];
            while ((currentLine = reader.readLine()) != null) {
                rowData = currentLine.split(",");
                if(rowData[2].equals(email)) {
                	existingAccount = true;
                	break;
                }
            }    
        }
        // Checking is the email is using among the tutors
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\data\\tutors.csv"))) {
        	String currentLine;
            String rowData[];
            while ((currentLine = reader.readLine()) != null) {
                rowData = currentLine.split(",");
                if(rowData[2].equals(email)) {
                	existingAccount = true;
                	break;
                }
            }    
        }
		return existingAccount;
	}
	
    // ===========================================================
    // ===== CHECK IS LOGINS ARE CORRESPONDING TO AN ACCOUNT =====
    // ===========================================================

	public String[] isLoginTrue(String email, String password, String userType) throws IOException {
		String dataToLoad;									// data file to load
		String[] resultAndToken;							// contains the variables below
		String existingAccount = "false";					// boolean-like, to check if the account exists
		String accountInformations = null;					// informations of the account
		String token = null;								// session token of the connected user
		int idCol;											// contains the id column, relatively to user type
		String userId;
		
		if(userType.equals("student")) {
			dataToLoad = "src\\data\\students.csv";
			idCol = 6;
			userType = "Student";
		} else {
			dataToLoad = "src\\data\\tutors.csv";
			idCol = 4;
			userType = "Tutor";
		}
		
		// Reading data file to find an account corresponding to logins
        try (BufferedReader reader = new BufferedReader(new FileReader(dataToLoad))) {
        	String currentLine;
            String rowData[];
            while ((currentLine = reader.readLine()) != null) {
                rowData = currentLine.split(",");
                // If mail and password on a row are corresponding and if this account is the one of a tutor or a student that belongs to Gustave Eiffel University
                if(rowData[2].equals(email) && rowData[3].equals(password) && !rowData[4].equals("no")) {
                	// Retrieving informations of the account
                	accountInformations = currentLine;
                	if(userType.equals("Student")) {
                		userId = rowData[6];
                	} else {
                		userId = rowData[4];
                	}
                	// The account exists
                	existingAccount = "true";
                	// Creating a token for the actual session
                	token = SessionManager.createToken(userId);
                	// Sending a message to the server
                	Main.infoMessage(userType+": "+rowData[0]+" "+rowData[1]+" (ID: "+rowData[idCol]+") has connected (Token: "+token+")",Color.BLACK, false);
                	break;
                }
            }    
        }
        
        // Returning function's result
        resultAndToken = new String[] {existingAccount, token, accountInformations};
		return resultAndToken;
	}
	
    // ==========================================================================
    // ===== CHECK IS LOGINS ARE CORRESPONDING TO AN ACCOUNT ON WEB SERVICE =====
    // ==========================================================================

	public String[] isLoginTrueWebService(String email, String password) throws IOException {
		String dataToLoad = "src\\data\\students.csv";		// data file to load
		String[] resultAndToken;							// contains the variables below
		String existingAccount = "false";					// boolean-like, to check if the account exists
		String accountInformations = null;					// informations of the account
		String token = null;								// session token of the connected user
		String userId;
		
		// Reading data file to find an account corresponding to logins
        try (BufferedReader reader = new BufferedReader(new FileReader(dataToLoad))) {
        	String currentLine;
            String rowData[];
            while ((currentLine = reader.readLine()) != null) {
                rowData = currentLine.split(",");
                // If mail and password on a row are corresponding and if this account is the one of a student that doesn't belong to Gustave Eiffel University
                if(rowData[2].equals(email) && rowData[3].equals(password) && rowData[4].equals("no")) {
                	// Retrieving informations of the account
                	accountInformations = currentLine;
                	userId = rowData[6];
                	// The account exists
                	existingAccount = "true";
                	// Creating a token for the actual session
                	token = SessionManager.createToken(userId);
                	// Sending a message to the server
                	Main.infoMessage("Student" + ": "+rowData[0]+" "+rowData[1]+" (ID: "+rowData[6]+") has connected (Token: "+token+")",Color.BLACK, false);
                	break;
                }
            }    
        }
        
        // Returning function's result
        resultAndToken = new String[] {existingAccount, token, accountInformations};
		return resultAndToken;
	}

    // ============================
    // ===== DELETE A MESSAGE =====
    // ============================

    public void deleteMessage(String messageId, String token, String accType, String[] accInfos) throws RemoteException {
    	String userId = CSVManager.getAccountId(accType, accInfos);
    	if(SessionManager.isTokenValid(userId, token)) {
    		CSVManager.removeDataFromClient(Main.getMessageData(), messageId, 6);
    		Main.infoMessage(SessionManager.firstUpperCase(accType) + " " + accInfos[0] + " " + accInfos[1] + " (ID:" + userId + ") has deleted a message (ID: " + messageId + ")", Color.BLUE, false);
    	}
    }
    
    // ===========================
    // ===== DELETE A LESSON =====
    // ===========================
    
    public void deleteLesson(String lessonId, String[] accInfo, String accType, String token) throws RemoteException, IOException {
    	String userId = CSVManager.getAccountId(accType, accInfo); // Retrieving userId
		String[] lessonInfos = getLessonInfos(lessonId); // Getting lesson's informations
		String messageFilePath = "src\\data\\messages.csv"; // Getting path to the messages' file
    	
    	if(SessionManager.isTokenValid(userId, token)) {
    		// If the user is a tutor, the lesson will be completely deleted from datafile while if it's a student it will freed
    		if(accType.equals("tutor")) {
    			
    				String costStr = lessonInfos[5];
    				String studentId = lessonInfos[6];
    				String[] studentInfos = getInfos(studentId, "student");
    				double balance = Double.parseDouble(studentInfos[5]);
    				double cost = Double.parseDouble(costStr);
    				double newBalance = balance + cost;
    				CSVManager.changeValue("student", studentId, "5", String.valueOf(newBalance));
    			
    	    		CSVManager.removeDataFromClient(Main.getLessonData(), lessonId, 8);  
    	    		CSVManager.loadDataFromCSV(Main.getLessonData(), Main.getLessonTable());
    	    		Main.infoMessage(SessionManager.firstUpperCase(accType)+" "+ accInfo[0] + " " + accInfo[1] +" (ID:"+ userId +") has deleted a lesson (ID: "+lessonId+")", Color.BLUE, false);
    		} else {
    			
				String costStr = lessonInfos[5];
				String studentId = lessonInfos[6];
				String[] studentInfos = getInfos(studentId, "student");
				double balance = Double.parseDouble(studentInfos[5]);
				double cost = Double.parseDouble(costStr);
				double newBalance = balance + cost;
				CSVManager.changeValue("student", studentId, "5", String.valueOf(newBalance));
    			
    			cancelBookingRequest(lessonId);
    			Main.infoMessage(SessionManager.firstUpperCase(accType)+" "+ accInfo[0] + " " + accInfo[1] +" (ID:"+ userId +") has freed a lesson (ID: "+lessonId+")", Color.BLUE, false);
    		}
    		
    		boolean sendMessage = true;
    		if(accType.equals("tutor") && lessonInfos[6].equals("free")) {
    			sendMessage = false;
    		}
    		if(sendMessage) {
            	// Creating cancellation message, that the user who hasn't cancelled lesson will receive
                String waitingForAnswer = "no";
                String answer = "no";
                String recipientId;
                String studentId;
                String tutorId;
                
                if(accType.equals("student")) {
                	studentId = userId;
                	tutorId = lessonInfos[7];
                	recipientId = tutorId;
                } else {
                	studentId = lessonInfos[6];
                	tutorId = userId;
                	recipientId = studentId;
                }
                int messageId = CSVManager.getLastId(messageFilePath, 81, 6);
                String message = waitingForAnswer+","+answer+","+recipientId+","+studentId+","+tutorId+","+lessonId+","+messageId;
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(messageFilePath, true))) {
                    writer.write(message);
                    writer.newLine(); //
                }
    		}
    	} else {
    		Main.infoMessage("An unidentified user tried to remove a Lesson as a "+accType, Color.RED, false);
    	}
    }
    
    
    // ==============================
    // ===== GET LIST OF TUTORS =====
    // ==============================
    
    public String[][] getTutorsList() throws RemoteException {
        String filePath = "src\\data\\tutors.csv";

        List<String[]> tutors = new ArrayList<>(); // Use a list to store matching lesson_id values

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Added flag to skip the first line
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] values = line.split(",");
            		tutors.add(values.clone());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert List<String[]> to String[][]
        String[][] result = tutors.toArray(new String[tutors.size()][]);

        return result;
    }
    
    
    // =========================================
    // ===== GET LIST OF AVAILABLE LESSONS =====
    // =========================================
    
    public String[][] findAvailableLessons() throws RemoteException {
        String filePath = "src\\data\\lessons.csv";

        List<String[]> lessons = new ArrayList<>(); // Use a list to store matching lesson_id values

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Added flag to skip the first line
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] values = line.split(",");
                	if(values[6].equals("free")) {
                		lessons.add(values.clone());
                	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Convert List<String[]> to String[][]
        String[][] result = lessons.toArray(new String[lessons.size()][]);

        return result;
    }
    
    
    // =========================================
    // ===== GET INFORMATIONS OF A STUDENT =====
    // =========================================
    
    public String[] getInfos(String id, String dataToFind) throws RemoteException {
    	String dataFilePath = null;
        String line;
        int idCol;
        String[] rowData = null;
        
        if(dataToFind.contains("student")) {
        	idCol = 6;
        	dataFilePath = "src\\data\\students.csv";
        } else if(dataToFind.contains("tutor")) {
        	idCol = 4;
        	dataFilePath = "src\\data\\tutors.csv";
        } else { // lesson
        	idCol = 8;
        	dataFilePath = "src\\data\\lessons.csv";
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(dataFilePath))) {
            
            while ((line = br.readLine()) != null) {
            	rowData = line.split(",");
            	if(rowData[idCol].equals(id)) {
            		break;
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowData;
    }
    
    
    // ============================
    // ===== CREATE AN LESSON =====
    // ============================
    public void createLesson(String userId, String[] lessonData, String token) throws IOException {
    	// Retrieving current date
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = currentDateTime.format(formatter);
        // Retrieving date of lesson
        String lessonDateString = lessonData[0];
        LocalDate lessonDate = LocalDate.parse(lessonDateString);
        // Retrieving start time
        String startTime = lessonData[1];
        // Retrieving end time
        String endTime = lessonData[2];
        // Retrieving lesson type
        String lessonType = lessonData[3];
        String[] lessonTypes = {"Mathematics", "History", "Python", "Geography", "French", "Java", "English"};
        // Retrieving cost
        String cost = lessonData[4];
        // Retrieving is lesson if online or not
        String isOnline = lessonData[5];
        // Retrieving tutor id
        String tutorId = userId;
        
        // Checking if the lesson if valid
        boolean isValid = true;
        if (lessonDate.isBefore(LocalDate.parse(date))) {
            isValid = false; // The lesson date is in the past.
        } else if (!startTime.matches("\\d{2}:\\d{2}") || !endTime.matches("\\d{2}:\\d{2}")) {
            isValid = false; // The time format is incorrect.
        } else if (!Arrays.asList(lessonTypes).contains(lessonType)) {
        	 isValid = false; // The lesson type is not valid
        } else if(!CSVManager.isNumeric(cost)) {
        	isValid = false; // The cost isn't numeric
        } else if(!isOnline.equals("yes") && !isOnline.equals("no")) {
        	isValid = false;
        }
        
        // If the lesson is valid, writing the lesson in lesson data file
        if(isValid) {
        	// Getting the id of the new lesson
        	String lessonFile = "src\\data\\lessons.csv";
        	int lessonId = CSVManager.getLastId(lessonFile, 76, 8);
        	// Creating the lesson
            String lesson = lessonDate + "," + startTime + "," + endTime + "," + isOnline + "," + lessonType + "," + cost + "," + "free" + "," + tutorId + "," + lessonId;
            // Writing the lesson in lesson data file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(lessonFile, true))) {
                writer.write(lesson);
                writer.newLine();
            }
            Main.infoMessage("Tutor" + userId + " has added a lesson: " + lesson ,Color.BLACK, false);
        }
    }

    
    // ========================================
    // ===== GET INFORMATIONS OF A LESSON =====
    // ========================================
    
    public String[] getLessonInfos(String lessonId) throws RemoteException {
        String line;
        String[] rowData = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader("src\\data\\lessons.csv"))) {
            
            while ((line = br.readLine()) != null) {
            	rowData = line.split(",");
            	if(rowData[8].equals(lessonId)) {
            		break;
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowData;
    }
    
    // =========================
    // ===== BOOK A LESSON =====
    // =========================
    
    public void bookLesson(String studentId, String lessonId, String token) throws RemoteException, IOException {
        String lessonFilePath = "src\\data\\lessons.csv";
        String messageFilePath = "src\\data\\messages.csv";
        String line;
        String[] dataToCheck;
        String message;
        String tutorId = null;
        boolean alreadyBooked = false;
        boolean exists = false;
        
    	// Verifying that the lesson is not booked, in case where a user would try
    	// to book it when it would have been booked already by another one
        try (BufferedReader br = new BufferedReader(new FileReader(lessonFilePath))) {
            
            while ((line = br.readLine()) != null) {
            	dataToCheck = line.split(",");
            	if(dataToCheck[8].equals(lessonId)) {
            		exists = true;
            		if(!dataToCheck[6].equals("free")) {
            			alreadyBooked = true;
            		}
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // If the lesson isn't already booked and still exists
        if(!alreadyBooked && exists) {
        	// If the user asking for booking has a valid session token
            if (SessionManager.isTokenValid(studentId, token)) {
            	// Changing student_id to "pending"
                try {
                    BufferedReader br = new BufferedReader(new FileReader(new File(lessonFilePath)));
                    Vector<Vector<Object>> data = new Vector<>();

                    while ((line = br.readLine()) != null) {
                        String[] rowData = line.split(",");

                        // Check if it's the header row
                        if (rowData[8].equals(lessonId)) {
                            // Update the user ID in the specified column (index 6)
                            rowData[6] = "pending";
                            tutorId = rowData[7];
                        }

                        data.add(new Vector<>(Arrays.asList(rowData)));
                    }
                    br.close();

                    // Write the updated data back to the CSV file
                    PrintWriter writer = new PrintWriter(new FileWriter(lessonFilePath));
                    for (Vector<Object> rowData : data) {
                        String lineData = rowData.stream().map(Object::toString).collect(Collectors.joining(","));
                        writer.println(lineData);
                    }
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // Creating a message for the tutor
                String waitingForAnswer = "yes";
                String answer = "pending";
                String recipientId = tutorId;
                int messageId = CSVManager.getLastId(messageFilePath, 81, 6);
                message = waitingForAnswer+","+answer+","+recipientId+","+studentId+","+tutorId+","+lessonId+","+messageId;
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(messageFilePath, true))) {
                    writer.write(message);
                    writer.newLine();
                }
            }
        }
    }
    
    
    // ==================================
    // ===== CANCEL BOOKING REQUEST =====
    // ==================================
    
    public void cancelBookingRequest(String lessonId) throws RemoteException {
    	String lessonFilePath = "src\\data\\lessons.csv";
    	String line;
    	
        try (BufferedReader br = new BufferedReader(new FileReader(new File(lessonFilePath)))) {
            Vector<Vector<Object>> data = new Vector<>();

            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(",");

                if (rowData[8].equals(lessonId)) {
                    rowData[6] = "free";
                }

                data.add(new Vector<>(Arrays.asList(rowData)));
            }

            // Write the updated data back to the CSV file
            try (PrintWriter writer = new PrintWriter(new FileWriter(lessonFilePath))) {
                for (Vector<Object> rowData : data) {
                    String lineData = rowData.stream().map(Object::toString).collect(Collectors.joining(","));
                    writer.println(lineData);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    // ===================================================
    // ===== ADD OR SUBSTRACT TO A STUDENT'S BALANCE =====
    // ===================================================
    
    public void modifyBalance(String userId, Double value, String operationType) throws RemoteException {
    	String lessonFilePath = "src\\data\\students.csv";
    	String line;
    	String stringBalance;
    	double doubleBalance;
    	
        try (BufferedReader br = new BufferedReader(new FileReader(new File(lessonFilePath)))) {
            Vector<Vector<Object>> data = new Vector<>();

            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(",");

                if (rowData[6].equals(userId)) {
                	
                	stringBalance = rowData[5];
                	doubleBalance = Double.parseDouble(stringBalance);

                	// Convert the result back to a string
                	if(operationType.equals("add")) {
                		doubleBalance = doubleBalance + value;
                		rowData[5] = String.valueOf(doubleBalance);
                	} else {
                		doubleBalance = doubleBalance - value;
                		rowData[5] = String.valueOf(doubleBalance);
                	}
                    
                }
                data.add(new Vector<>(Arrays.asList(rowData)));
            }

            // Write the updated data back to the CSV file
            try (PrintWriter writer = new PrintWriter(new FileWriter(lessonFilePath))) {
                for (Vector<Object> rowData : data) {
                    String lineData = rowData.stream().map(Object::toString).collect(Collectors.joining(","));
                    writer.println(lineData);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    // =====================================
    // ===== VALIDATE A LESSON BOOKING =====
    // =====================================
    
    public void answerBooking(String tutorId, String studentId, String lessonId, String messageId, String token, int yesOrNo) throws RemoteException, IOException {
        String lessonFilePath = "src\\data\\lessons.csv";
        String messageFilePath = "src\\data\\messages.csv";
        String line;
        String[] studentInfos = getInfos(studentId, "student");
        double lessonCost;
        double studentBalance;
        double newBalance;
        
        if (SessionManager.isTokenValid(tutorId, token)) {
            // Update answer in messages.csv
            try (BufferedReader br = new BufferedReader(new FileReader(new File(messageFilePath)))) {
                Vector<Vector<Object>> data = new Vector<>();

                while ((line = br.readLine()) != null) {
                    String[] rowData = line.split(",");

                    if (rowData[6].equals(messageId)) {
                        rowData[1] = (yesOrNo == JOptionPane.YES_OPTION) ? "yes" : "no";
                    }

                    data.add(new Vector<>(Arrays.asList(rowData)));
                }
                
                // Write the updated data back to the CSV file
                try (PrintWriter writer = new PrintWriter(new FileWriter(messageFilePath))) {
                    for (Vector<Object> rowData : data) {
                        String lineData = rowData.stream().map(Object::toString).collect(Collectors.joining(","));
                        writer.println(lineData);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Update student_id in lessons.csv
            try (BufferedReader br = new BufferedReader(new FileReader(new File(lessonFilePath)))) {
                Vector<Vector<Object>> data = new Vector<>();

                while ((line = br.readLine()) != null) {
                    String[] rowData = line.split(",");

                    if (rowData[8].equals(lessonId)) {
                        rowData[6] = (yesOrNo == JOptionPane.YES_OPTION) ? studentId : "free";
                        
                        if(yesOrNo == JOptionPane.YES_OPTION && !studentInfos[5].equals("not_concerned")) {
                        	lessonCost = Double.parseDouble(rowData[5]);
                        	studentBalance = Double.parseDouble(studentInfos[5]);
                        	newBalance = studentBalance - lessonCost;
                        	subtractFunds(studentId, String.valueOf(newBalance));
                        }
                    }

                    data.add(new Vector<>(Arrays.asList(rowData)));
                }

                // Write the updated data back to the CSV file
                try (PrintWriter writer = new PrintWriter(new FileWriter(lessonFilePath))) {
                    for (Vector<Object> rowData : data) {
                        String lineData = rowData.stream().map(Object::toString).collect(Collectors.joining(","));
                        writer.println(lineData);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    
    // ===========================================================
    // ===== FIND THE ROWS RELATED TO AN USER IN A DATA FILE =====
    // ===========================================================
    
    public String[][] findRowsUser(String userId, String file, String accType) throws RemoteException {
        int userIdCol;
        int studentIdCol;
        int tutorIdCol;
        String filePath;

        if (file.contains("lessons")) {
            studentIdCol = 6;
            tutorIdCol = 7;
            userIdCol = accType.equals("student") ? studentIdCol : tutorIdCol;
            filePath = "src\\data\\lessons.csv";
        } else {
            studentIdCol = 3;
            tutorIdCol = 4;
            userIdCol = accType.equals("student") ? studentIdCol : tutorIdCol;
            filePath = "src\\data\\messages.csv";
            
           
        }
        List<String[]> matchingRows = new ArrayList<>(); // Use a list to store matching lesson_id values

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Added flag to skip the first line
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }

                String[] values = line.split(",");
                if (values[userIdCol].equals(userId)) {
                    matchingRows.add(values.clone()); // Use clone to avoid modifying the original array
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Convert List<String[]> to String[][]
        String[][] result = matchingRows.toArray(new String[matchingRows.size()][]);

        return result;
    }
    
    // ==========================================
    // ===== ADD FUNDS TO STUDENT'S BALANCE =====
    // ==========================================
    public void addFunds(String studentId, String addedValue) throws RemoteException {
    	CSVManager.changeValue("student", studentId, "5", addedValue);
    }
    
    // ==========================================
    // ===== SUBTRACT FUNDS TO STUDENT'S BALANCE =====
    // ==========================================
    public void subtractFunds(String studentId, String subtractedValue) throws RemoteException {
    	CSVManager.changeValue("student", studentId, "5", subtractedValue);
    }
}
