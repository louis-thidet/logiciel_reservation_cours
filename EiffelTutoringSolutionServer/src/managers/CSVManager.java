package managers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.TimePicker;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

import gui.Main;

public class CSVManager {

	private static Object formerValue; // keep the value of the clicked cell on a JTable
	
	// ========================================
    // ====== GET HEADERS OF DATA TABLES ======
	// ========================================
	
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
	
	// =================================
    // ====== GET VALUES OF A ROW ======
	// =================================
	
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
	

	// ===============================================
    // ====== TO CREATE NON EXISTING DATA FILES ======
	// ===============================================
    
	private static void dataFileCreation(String fileToCreate) {
		String dataType = getDataType(fileToCreate, true);
		String headers[] = getFileHeaders(fileToCreate);
    	Main.infoMessage(dataType+" File not found. It will be created", Color.RED, false);
    	// Trying to create the file requested
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileToCreate))) {
        	Main.infoMessage(dataType+" File created successfully", Color.BLUE, false);
            writer.println(String.join(",", headers));
        } catch (IOException e) {
        	Main.infoMessage(dataType+" File couldn't be created", Color.RED, false);
            e.printStackTrace();
        }
	}
	
	// =========================================
    // ====== REMOVE A ROW IN A DATA FILE ======
	// =========================================
	
	@SuppressWarnings("exports")
	public static void removeData(JTable table, String datafilePath) {
		String dataToRemove = getDataType(datafilePath, false);
		String lineRemoved = "";
    	int selectedRow = table.getSelectedRow() + 1; // +1 because the first row has index 1, and not 0
    	// selectedRow has a value of -1 when no row is selected
    	if(selectedRow != 0) { 
    		// Create a dialog to confirm the deletion
    		int choice = JOptionPane.showConfirmDialog(
    		        null,
    		        "Confirm " + dataToRemove + " deletion?",
    		        "Confirmation",
    		        JOptionPane.YES_NO_OPTION,
    		        JOptionPane.QUESTION_MESSAGE
    		);
    		// If the deletion is validated
    		if (choice == JOptionPane.YES_OPTION) {
    			// If selectedRow is greater than 1, to  avoid removing the header
                if (selectedRow > 0) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(datafilePath));
                        List<String> lines = new ArrayList<>();

                        // Read all lines from the CSV file	
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lines.add(line);
                        }

                        reader.close();

                        // Get the content of the selected row as a String array
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        String[] rowData = new String[model.getColumnCount()];
                        
                        for (int i = 0; i < model.getColumnCount(); i++) {
                        	rowData[i] = String.valueOf(model.getValueAt(selectedRow-1, i));
                        }
                        
                        for(int i = 0; i < rowData.length; i++) {
                        	if(i == 0) {
                        		lineRemoved += rowData[i];
                        	} else {
                        		lineRemoved += "," + rowData[i];
                        	}
                        }
                        // Remove the selected row
                        lines.remove(selectedRow);

                        // Write the updated lines back to the CSV file
                        BufferedWriter writer = new BufferedWriter(new FileWriter(datafilePath));
                        for (String updatedLine : lines) {
                            writer.write(updatedLine);
                            writer.newLine();
                        }

                        writer.close();

                        Main.infoMessage("The row: '"+lineRemoved+"' has been removed successfully", Color.BLUE, false);
                        loadDataFromCSV(datafilePath, table);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
    		}
    	}
	}
	
	// =======================================================
    // ====== CHANGE THE VALUE OF A CELL IN A DATA FILE ======
	// =======================================================
	
	public static void changeValue(String dataType, String id, String changingCol, String newValue) {
		// Setting path relatively to data type
		String filePath;
		int idCol;
		if(dataType.contains("student")) {
			filePath = "src\\data\\students.csv";
			idCol = 6;
		} else if(dataType.contains("tutor")) {
			filePath = "src\\data\\tutors.csv";
			idCol = 5;
		} else if(dataType.contains("lesson")) {
			filePath = "src\\data\\lessons.csv";
			idCol = 8;
		} else { // Message
			filePath = "src\\data\\messages.csv";
			idCol = 6;
		}
		String line;
		
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            Vector<Vector<Object>> data = new Vector<>();

            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(",");

                if (rowData[idCol].equals(id)) {
                	int changingColInt = Integer.parseInt(changingCol);
                    rowData[changingColInt] = newValue;
                }

                data.add(new Vector<>(Arrays.asList(rowData)));
            }
            br.close();

            // Write the updated data back to the CSV file
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            for (Vector<Object> rowData : data) {
                String lineData = rowData.stream().map(Object::toString).collect(Collectors.joining(","));
                writer.println(lineData);
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
	
	// ===============================================
    // ====== REMOVE DATA FROM CLIENT'S REQUEST ======
	// ===============================================
	
	public static void removeDataFromClient(String filePath, String id, int idColumn) {
		String [] rowData;
	    try {
	        BufferedReader reader = new BufferedReader(new FileReader(filePath));
	        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".tmp"));

	        String line;
	        while ((line = reader.readLine()) != null) {
	            rowData = line.split(",");
	            if (!rowData[idColumn].equals(id)) {
	                writer.write(line + System.lineSeparator());
	            }
	        }

	        reader.close();
	        writer.close();

	        File oldFile = new File(filePath);
	        File newFile = new File(filePath + ".tmp");
	        oldFile.delete();
	        newFile.renameTo(oldFile);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	// ===============================================
    // ===== LOAD DATA FROM CSV FILES IN JTABLES =====
	// ===============================================
	
	@SuppressWarnings("exports")
	public static void loadDataFromCSV(String dataToLoad, JTable table) {
		JTextField searchBar;
		if(dataToLoad.contains("lessons")) {
			searchBar = Main.searchBarLesson;
		} else if(dataToLoad.contains("messages")) {
			searchBar = Main.searchBarMessage;
		} else { // if(dataToLoad.contains("students") || dataToLoad.contains("tutors"))
			searchBar = Main.searchBarPerson;
		}
		String lowerCaseResearch = searchBar.getText().toLowerCase();
		String lowerCaseData; // data and search are lowered to be compared more easily
		String[] headers;
		int i;
		
		// Opening of a data file
        File file = new File(dataToLoad);
        // If the file doesn't exists
        if (!file.exists()) {
        	dataFileCreation(dataToLoad);
        }
        // If the file exists
        else {
        	// Creation of a model corresponding to the loaded .csv file
            DefaultTableModel model = new DefaultTableModel();
            try (BufferedReader br = new BufferedReader(new FileReader(dataToLoad))) {
                // Set the header of the requested file in the model
                String line = br.readLine();
                headers = line.split(",");
                model.setColumnIdentifiers(headers);
                // Add the data of the requested file to the model
                // If the searchbar is empty
                if(lowerCaseResearch.equals("")) {
                    while ((line = br.readLine()) != null) {
                        String[] rowData = line.split(",");
                        model.addRow(rowData);
                    }
                // If the searchbar isn't empty
                }
                else {
                	while ((line = br.readLine()) != null) {
                		// loading a row
                	    String[] rowData = line.split(",");
                	    Set<String> addedValues = new HashSet<>(); // hashSet to avoid showing a same row more than one row in a research (a same value can be in two columns on a row)
                	    // each column of the row is tested
                	    for (i = 0; i < rowData.length; i++) {
                	        lowerCaseData = rowData[i].toLowerCase();
                	        // If the value doesn't already exist in another column of the row, it's added to the hashSet, and in the model
                	        if (!addedValues.contains(lowerCaseData) && lowerCaseData.equals(lowerCaseResearch)) {
                	            model.addRow(rowData);
                	            addedValues.add(lowerCaseData); // Add the value to the set to avoid duplicates
                	        }
                	    }
                	}
                }
                // Set the model to the JTable
                table.setModel(model);
                
            	// ================================================================
                // ===== LISTENER THAT GIVES THE POSITION OF THE CLICKED CELL =====
            	// ================================================================
                
                // This listener permits to retrieve the value of the cell clicked
                // by the user, which is stored in the global variable formerValue.
                // The variable formerValue is used in this other listener, that is just
                // below, and is triggered when the user modifies a value in a data model
                table.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                    	
                    	// We talk about modelRow and no just row because this is the position
                    	// of a row in the data model, and not in the file from where this model
                    	// was loaded. The difference is very important because this program
                    	// implements a search feature for the data models. When a research is made
                    	// and is successful, not all data are shown in the data table which
                    	// shows the data model, and the rows that will be shown won't have the same
                    	// index there and in their file.
                    	
                        int modelRow = table.rowAtPoint(e.getPoint());
                        int column = table.columnAtPoint(e.getPoint());
                        formerValue = table.getValueAt(modelRow, column);
                        
                        // Some columns can receive only boolean values.
                        // To prevent the user from putting other values as these, the following
                        // code triggers the spawn of JOptionPane, that asks if the value of the
                        // cell is yes or no.
                        
                        int[] optionColumns;
                        String title;
                        String newValue;
                    	int IDColumn;
                    	int row;
                        
                        if(dataToLoad.contains("student") || dataToLoad.contains("Student")) {
                        	optionColumns = new int[] {4};
                        	title = "Does this student belong to Gustave Eiffel?";
                        	IDColumn = 6;
                        } else if(dataToLoad.contains("lesson") || dataToLoad.contains("Lesson")) {
                        	optionColumns = new int[] {3};
                        	title = "Is the lesson online?";
                        	IDColumn = 8;
                        } else {
                        	optionColumns = new int[] {0,1};
                        	if(column == 0) {
                        		title = "Is this message waiting for answer?";
                        	} else { // if(column == 1)
                        		title = "Answer's state?";
                        	}
                        	IDColumn = 6;
                        }
                        
                        for (int i = 0; i < optionColumns.length; i++) {
                            if (optionColumns[i] == column) {
                                row = Integer.parseInt(table.getModel().getValueAt(modelRow, IDColumn).toString());
                                
                                int choice = -1;
                                if (title.equals("Answer's state?")) {
                                    // If the title is "Answer's state?", provide a third option "Pending"
                                    Object[] options = {"Yes", "No", "Pending"};
                                    choice = JOptionPane.showOptionDialog(
                                            null,
                                            title,
                                            "Confirmation",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            options,
                                            options[2]  // Default to "Pending"
                                    );
                                } else {
                                    // For other titles, use yes/no options
                                    choice = JOptionPane.showConfirmDialog(
                                            null,
                                            title,
                                            "Confirmation",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE
                                    );
                                }

                                if (choice == JOptionPane.YES_OPTION) {
                                    newValue = "yes";
                                    updateCSVFile(dataToLoad, table, row, column, newValue);
                                } else if (choice == JOptionPane.NO_OPTION) {
                                    newValue = "no";
                                    updateCSVFile(dataToLoad, table, row, column, newValue);
                                } else if (choice == 2 && title.equals("Answer's state?")) {
                                    // If the choice is "Pending" and the title is "Answer's state?"
                                    newValue = "pending";
                                    updateCSVFile(dataToLoad, table, row, column, newValue);
                                }
                            }
                        }
                    }
                }); /// 
                
            	// =====================================================================
                // ===== LISTENER THAT SAVE A DATA MODIFICATION IN THE PROPER FILE =====
            	// =====================================================================
                
                table.getModel().addTableModelListener(new TableModelListener() {
                    public void tableChanged(TableModelEvent e) {
                    	 // retrieve the modified column and row of the data model
                        int column = e.getColumn();
                        int modelRow = e.getFirstRow();
                        
                        // retrieve the value entered in the cell
                        Object newValue = table.getModel().getValueAt(modelRow, column);
                        
                        // retrieve the name of column and file modified
                    	String columnNames[] = getFileHeaders(dataToLoad); // getting the headers of the file
                    	String tableName = getDataType(dataToLoad, true);
                    	String columnName = columnNames[column]; // getting the header of the modified column
                    	
                    	// retrieving the column holding the id of the rows in the datafile
                    	// also getting indexes of that cannot receive non-numeric characters (except some cases)
                    	int IDColumn;
                    	int[] otherIDcols = null;
                    	
                    	if(tableName.equals("Student")) {
                    		IDColumn = 6;
                    		otherIDcols = new int[]{5};
                    	} else if(tableName.equals("Tutor")) {
                    		IDColumn = 4;
                    	} else if(tableName.equals("Lesson")) {
                    		IDColumn = 8;
                    		otherIDcols = new int[]{5,6,7};
                    	} else { // if(tableName.equals("Message"))
                    		IDColumn = 6;
                    		otherIDcols = new int[]{2,3,4,5};
                    	}
                    	boolean takeOnlyNumeric = false;
                    	
                    	// Checking if the column changed only takes numeric characters
                    	if(tableName.equals("Student") || tableName.equals("Lesson") || tableName.equals("Message")) {
                     		for(int i = 0; i < otherIDcols.length; i++) {
                    			if(otherIDcols[i] == column) {
                    				takeOnlyNumeric = true;
                    			}
                    		}
                    	}

                    	// If the column only takes numeric characters...
                    	if(takeOnlyNumeric){
                    		// We check if the new value is an exception for the column
                    		boolean exception = false;
                    		if(tableName.equals("Student") && column == 5 && newValue.toString().equals("not_concerned") ||
                    			tableName.equals("Lesson") && column == 6 && (newValue.toString().equals("free") || newValue.toString().equals("pending"))) {
                    			exception = true;
                    		}
                    		// If the new value is numeric or an exception, the changed is saved
                    		if(isNumeric(newValue.toString()) || exception) {
                        		int row = Integer.parseInt(table.getModel().getValueAt(modelRow, IDColumn).toString());
                        		updateCSVFile(dataToLoad, table, row, column, newValue);
                        		Main.infoMessage(columnName+" has been changed at row "+row+" from "+formerValue+" to "+newValue+" in "+tableName+"s", Color.BLUE, false);
                    		}
                    		// else, there is no change
                    		else {
                    			Main.infoMessage("Service manager tried to modify "+columnName+" with an invalid value. Operation wasn't carried out.", Color.RED, false);
                    			loadDataFromCSV(dataToLoad, table);
                    		}
                    	}
                    	// If a column isn't IDColumn and if the new value isn't the same as the former one, the file loaded will be updated
                    	else if(column != IDColumn && !newValue.equals(formerValue) && !takeOnlyNumeric) {
                    		int row = Integer.parseInt(table.getModel().getValueAt(modelRow, IDColumn).toString());
                    		updateCSVFile(dataToLoad, table, row, column, newValue);
                    		Main.infoMessage(columnName+" has been changed at row "+row+" from "+formerValue+" to "+newValue+" in "+tableName+"s", Color.BLUE, false);
                    	}
                    	// If a column isn't IDColumn and if the new value isn't the same as the former one, modification will be prevented
                    	else if(column == IDColumn && !newValue.equals(formerValue)) {
                        	Main.infoMessage("Service manager tried to modify ID column in "+tableName+"s. Operation wasn't carried out.", Color.RED, false);
                        	loadDataFromCSV(dataToLoad, table); // The data are loaded again, so the value of the changed ID is back to default
                    	}
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	// =====================================
    // ===== UPDATE THE CSV FILES DATA =====
	// =====================================
	
	private static void updateCSVFile(String loadedData, JTable table, int row, int column, Object value) {
	    int IDColumn;
	    if (loadedData.contains("student")) {
	        IDColumn = 6;
	    } else if (loadedData.contains("tutor")) {
	        IDColumn = 4;
	    } else if (loadedData.contains("lesson")) {
	        IDColumn = 8;
	    } else { // if(tableName.equals("Message"))
	        IDColumn = 6;
	    }

	    try {
	        // Read existing data from the CSV file
	        File file = new File(loadedData);
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        Vector<Vector<Object>> data = new Vector<>();
	        String line;
	        int currentRow = 0;

	        while ((line = br.readLine()) != null) {
	            String[] rowData = line.split(",");
	            
	            // Check if it's the header row
	            if (currentRow == 0) {
	                data.add(new Vector<>(Arrays.asList(rowData)));
	            } else {
	                Vector<Object> rowVector = new Vector<>(Arrays.asList(rowData));
	                data.add(rowVector);

	                // Check if the ID in the specified column matches the specified rowID
	                if (row == Integer.parseInt(rowData[IDColumn])) {
	                    // Update the specific cell value
	                    rowVector.set(column, value);
	                }
	            }

	            currentRow++;
	        }
	        br.close();

	        // Write the updated data back to the CSV file
	        PrintWriter writer = new PrintWriter(new FileWriter(file));
	        for (Vector<Object> rowData : data) {
	            String lineData = rowData.stream().map(Object::toString).collect(Collectors.joining(","));
	            writer.println(lineData);
	        }
	        writer.close();

	        // Reload the data to reflect the changes in the table
	        loadDataFromCSV(loadedData, table);

	    } catch (IOException | NumberFormatException ex) {
	        ex.printStackTrace();
	    }
	}

	
	public static String getDataType(String dataPath, boolean firstUpperCase) {
		String dataName = null;
		if(dataPath.contains("students")) {
			dataName = "student";
		} else if(dataPath.contains("tutors")) {
			dataName = "tutor";
		} else if(dataPath.contains("lessons")) {
			dataName = "lesson";
		} else {// messages
			dataName = "message";
		}
		if(firstUpperCase) {
			dataName = dataName.substring(0, 1).toUpperCase() + dataName.substring(1);
		}
		return dataName;
	}



	
	@SuppressWarnings("exports")
	public static void addData(JTable table, String datafilePath) {
		if(!datafilePath.equals("")) {
    		String dataTypeAdded;
    		if(datafilePath.contains("students")) {
    			dataTypeAdded = "Student";
    		} else if(datafilePath.contains("tutors")){
    			dataTypeAdded = "Tutor";
    		} else {
    			dataTypeAdded = "Lesson";
    		}
    		// ...
            // Create a new secondary JFrame
            JFrame secondaryFrame = new JFrame("Add a " + dataTypeAdded);

            // Set layout manager for the secondary frame
            JPanel mainPanel = new JPanel(new BorderLayout());
            JTextField[] fields;
            JPanel panel;
            
        	// ===================
            // ===== STUDENT =====
        	// ===================
            if(datafilePath.contains("student")){
            	
                panel = new JPanel(new GridLayout(6, 2, 10, 10)); // 5 rows, 2 columns, with gaps
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

                // components to enter name
                panel.add(new JLabel("Name:"));
                JTextField nameField = new JTextField();
                panel.add(nameField);
                
                // components to enter surname
                panel.add(new JLabel("Surname:"));
                JTextField surnameField = new JTextField();
                panel.add(surnameField);

                // components to enter email
                panel.add(new JLabel("Email:"));
                JTextField emailField = new JTextField();
                panel.add(emailField);
                
                // component to enter password
                panel.add(new JLabel("Password:"));
                JTextField passwordField = new JTextField();
                panel.add(passwordField);
                
                // component to enter if it's a student of Gustave or not
                JPanel gustavePanel = new JPanel();
                JTextField gustaveField = new JTextField();
                gustaveField.setText("yes");
                panel.add(new JLabel("Gustave Eiffel student:"));
                
                JRadioButton yesRadioButton = new JRadioButton("Yes");
                JRadioButton noRadioButton = new JRadioButton("No");
                yesRadioButton.setSelected(true);
                ButtonGroup onlineGroup = new ButtonGroup();
                onlineGroup.add(yesRadioButton);
                onlineGroup.add(noRadioButton);
                gustavePanel.add(yesRadioButton);
                gustavePanel.add(noRadioButton);
                
                panel.add(gustavePanel);
                
                panel.add(new JLabel("Balance:"));
                JTextField balanceField = new JTextField();
                panel.add(balanceField);
                balanceField.setEnabled(false);
                balanceField.setText("not_concerned");
                
            	noRadioButton.addActionListener((ActionEvent e) -> {
            		gustaveField.setText("no");
            	    balanceField.setEnabled(true);
            	    balanceField.setText("0");
            	});

            	yesRadioButton.addActionListener((ActionEvent e) -> {
            		gustaveField.setText("yes");
            	    balanceField.setEnabled(false);
            	    balanceField.setText("not_concerned");
            	});
                
                fields = new JTextField[]{nameField, surnameField, emailField, passwordField, gustaveField, balanceField};
                
	    	// ===================
	        // ====== TUTOR ======
	    	// ===================
            } else if(datafilePath.contains("tutor")) {
                panel = new JPanel(new GridLayout(5, 2, 10, 10)); // 5 rows, 2 columns, with gaps
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

                // components to enter name
                panel.add(new JLabel("Name:"));
                JTextField nameField = new JTextField();
                panel.add(nameField);

                // components to enter surname
                panel.add(new JLabel("Surname:"));
                JTextField surnameField = new JTextField();
                panel.add(surnameField);

                // components to enter email
                panel.add(new JLabel("Email:"));
                JTextField emailField = new JTextField();
                panel.add(emailField);

                // components to enter password
                panel.add(new JLabel("Password:"));
                JTextField passwordField = new JTextField();
                panel.add(passwordField);
                
                fields = new JTextField[]{nameField, surnameField, emailField, passwordField};
            }
        	// ===================
            // ====== LESSON =====
        	// ===================
            else if(datafilePath.contains("lesson")) {
            	// Setting the panel to add the lesson
                panel = new JPanel(new GridLayout(8, 2, 10, 10)); // 5 rows, 2 columns, with gaps
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
                
                // components to enter the date
                panel.add(new JLabel("Date"));
                JTextField dateField = new JTextField();
                dateField.setFocusable(false);
                panel.add(dateField);
                
        		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        		String todayDateString = LocalDate.now().format(formatter);
        		dateField.setText(todayDateString);
        		
                DateChooser dateChooser = new DateChooser();
                dateChooser.toDay();
                
                dateChooser.addEventDateChooser(new EventDateChooser() {
                    @Override
                    public void dateSelected(SelectedAction action, SelectedDate date) {
                        if (action.getAction() == SelectedAction.DAY_SELECTED) {
                        	dateField.setText(date.getYear() + "-" + date.getMonth() + "-" + date.getDay());
                            dateChooser.hidePopup();
                        }
                    }
                });
                
                dateField.addMouseListener(new MouseAdapter() {
                	@Override
                	public void mouseClicked(MouseEvent e) {
                		dateChooser.showPopup(secondaryFrame, 65, 30);
                	}
                });

                // components to enter start time
                panel.add(new JLabel("Start time"));
                JTextField startTimeField = new JTextField();
        		TimePicker startTimePicker = new TimePicker();
        		panel.add(startTimePicker);
        		startTimePicker.addTimeChangeListener(event -> {
        		    startTimeField.setText(event.getNewTime().toString());
        		});

        		// components to enter end time
        		panel.add(new JLabel("End Time"));
        		JTextField endTimeField = new JTextField();
        		TimePicker endTimePicker = new TimePicker();
        		panel.add(endTimePicker);
        		endTimePicker.addTimeChangeListener(event -> {
        			endTimeField.setText(event.getNewTime().toString());
        		});

        		
                // components to enter student ID
                panel.add(new JLabel("Student ID"));
                JTextField studentField = new JTextField();
                studentField.setText("free");
                panel.add(studentField);

                // components to enter tutor id
                panel.add(new JLabel("Tutor ID"));
                JTextField tutorField = new JTextField();
                panel.add(tutorField);

                // components to enter the cost
                panel.add(new JLabel("Cost"));
                JTextField costField = new JTextField();
                panel.add(costField);
                
                // components to enter the type
                panel.add(new JLabel("Type"));
                JTextField typeField = new JTextField();
                typeField.setText("Mathematics");
                String[] lessonTypes = {"Mathematics", "History", "Python", "Geography", "French", "Java", "English"}; // Add your specific lesson types
                JComboBox<String> lessonTypeComboBox = new JComboBox<>(lessonTypes);
                panel.add(lessonTypeComboBox);
                lessonTypeComboBox.addActionListener((ActionEvent e) -> {
                	 typeField.setText((String) lessonTypeComboBox.getSelectedItem());
                });
                
                // components to enter if the lesson is online or is not
                JPanel onlinePanel = new JPanel();
                panel.add(new JLabel("Online:"));
                
                JTextField onlineField = new JTextField();
                onlineField.setText("yes");
                
                JRadioButton yesRadioButton = new JRadioButton("Yes");
                JRadioButton noRadioButton = new JRadioButton("No");
                yesRadioButton.setSelected(true);
                ButtonGroup onlineGroup = new ButtonGroup();
                onlineGroup.add(yesRadioButton);
                onlineGroup.add(noRadioButton);
                onlinePanel.add(yesRadioButton);
                onlinePanel.add(noRadioButton);
                
                panel.add(onlinePanel);
                
                yesRadioButton.addActionListener((ActionEvent e) -> {
                	onlineField.setText("yes");
                });
                noRadioButton.addActionListener((ActionEvent e) -> {
                	onlineField.setText("no");
                });
                
                fields = new JTextField[]{dateField, startTimeField, endTimeField, onlineField, typeField, costField, studentField, tutorField}; // oooo
        	// ===================
            // ===== MESSAGE =====
        	// ===================
            } else { // else if(datafilePath.contains("message")) {
                panel = new JPanel(new GridLayout(7, 2, 10, 10)); // 5 rows, 2 columns, with gaps
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

                // components to enter if the message is read or not
                JPanel readPanel = new JPanel();
                panel.add(new JLabel("Waiting for answer:"));
                
                JTextField readField = new JTextField();
                readField.setText("yes");
                
                JRadioButton yesRadioButton = new JRadioButton("Yes");
                JRadioButton noRadioButton = new JRadioButton("No");
                yesRadioButton.setSelected(true);
                ButtonGroup readGroup = new ButtonGroup();
                readGroup.add(yesRadioButton);
                readGroup.add(noRadioButton);
                readPanel.add(yesRadioButton);
                readPanel.add(noRadioButton);
                
                panel.add(readPanel);
                
                yesRadioButton.addActionListener((ActionEvent e) -> {
                	readField.setText("yes");
                });
                noRadioButton.addActionListener((ActionEvent e) -> {
                	readField.setText("no");
                });

                // component to know if an answer is required or not 
                JPanel answerPanel = new JPanel();
                panel.add(new JLabel("Answer:"));
                
                JTextField answerField = new JTextField();
                answerField.setText("no");
                
                JRadioButton yesAnswer = new JRadioButton("Yes");
                JRadioButton noAnswer = new JRadioButton("No");
                JRadioButton pendingAnswer = new JRadioButton("Pending");
                noAnswer.setSelected(true);
                ButtonGroup answerGroup = new ButtonGroup();
                answerGroup.add(yesAnswer);
                answerGroup.add(noAnswer);
                answerGroup.add(pendingAnswer);
                answerPanel.add(yesAnswer);
                answerPanel.add(noAnswer);
                answerPanel.add(pendingAnswer);
                
                panel.add(answerPanel);
                
                yesAnswer.addActionListener((ActionEvent e) -> {
                	answerField.setText("yes");
                });
                noAnswer.addActionListener((ActionEvent e) -> {
                	answerField.setText("no");
                });
                pendingAnswer.addActionListener((ActionEvent e) -> {
                	answerField.setText("pending");
                });
                
                // components to enter recipient_id
                panel.add(new JLabel("Recipient ID:"));
                JTextField recipientField = new JTextField();
                panel.add(recipientField);

                // components to enter student id
                panel.add(new JLabel("Student ID:"));
                JTextField studentField = new JTextField();
                panel.add(studentField);

                // components to enter tutor id
                panel.add(new JLabel("Tutor ID:"));
                JTextField tutorField = new JTextField();
                panel.add(tutorField);
                
                // components to enter lesson id
                panel.add(new JLabel("Lesson ID:"));
                JTextField lessonField = new JTextField();
                panel.add(lessonField);
                
                fields = new JTextField[]{readField, answerField, recipientField, studentField, tutorField, lessonField};
            }


            // Center the Validation button
            JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton validationButton = new JButton("Validation");
            
            
            validationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
            		try {
						dataValidation(table, datafilePath, fields, secondaryFrame);
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            });

            centeringPanel.add(validationButton);

            // Add components to the main panel
            mainPanel.add(panel, BorderLayout.CENTER);
            mainPanel.add(centeringPanel, BorderLayout.SOUTH);

            // Set frame properties
            secondaryFrame.setSize(400, 350);//
            secondaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame
            secondaryFrame.setResizable(false); // Lock frame dimensions
            secondaryFrame.setLocationRelativeTo(null);
            secondaryFrame.setContentPane(mainPanel); // Set the main panel as the content pane
            secondaryFrame.setVisible(true);
		}
	}
	
	
	public static boolean checkNumeric(String[] stringArray, int[] indicesToCheck) {
		for (int i : indicesToCheck) {
			// Checking if the value at the specified index is not numeric
			if (!isNumeric(stringArray[i])) {
			    return true; // Returning true if a non-numeric value is found
			}
		}
		
		return false; // Returning false if all values are numeric
	}
	
	@SuppressWarnings("exports")
	public static void dataValidation(JTable table, String dataToLoad, JTextField[] fields, JFrame secondaryFrame) throws NumberFormatException, IOException {
		String dataTypeAdded = getDataType(dataToLoad, true);
		String[] dataToAdd = new String[10];
		String lineToAdd;
		
		
		// Checking if a field is empty
		boolean empty = false;
		boolean notNumeric = false;
		for(int i = 0; i < fields.length; i++) {
			dataToAdd[i] = fields[i].getText();
			// Checking is any field is empty
			if(dataToAdd[i].isEmpty()) {
				empty = true;
			}
			// Checking if the id fields are correctly entered (must be numeric ; except for student_id in Lessons, that can hold "free"
			if(dataTypeAdded.equals("Student") && (i == 5 && !dataToAdd[5].equals("not_concerned"))) {
				if(!isNumeric(dataToAdd[i])) {
					notNumeric = true;
				}
			}
			if(dataTypeAdded.equals("Lesson") && (i == 5 || (i == 6 && (!dataToAdd[6].equals("free") && !dataToAdd[6].equals("pending"))) || i == 7)) {
				if(!isNumeric(dataToAdd[i])) {
					notNumeric = true;
				}
			}
			if(dataTypeAdded.equals("Message") && (i == 2 || i == 3 || i == 4 || i == 5)) {
				if(!isNumeric(dataToAdd[i])) {
					notNumeric = true;
				}
			}
		}

		// If a field is empty, the row won't be added
		if(empty) {
			JOptionPane.showMessageDialog(null, "Validation failed. Empty input(s) detected.");
		}
		// If an ID field isn't numeric, the row won't be added
		else if(notNumeric) {
			JOptionPane.showMessageDialog(null, "Validation failed. ID input(s) detected.");
		}
		// If no field is empty, the row will normally be added
		else {
            try {
            	// If the row added is a student
            	if(dataTypeAdded.equals("Student")) {
                    int lastID = getLastId(dataToLoad, 59, 6);
                	lineToAdd = dataToAdd[0]+","+dataToAdd[1]+","+dataToAdd[2]+","+dataToAdd[3]+","+dataToAdd[4]+","+dataToAdd[5]+","+lastID;
            	}
            	// If the row added is a tutor
            	else if(dataTypeAdded.equals("Tutor")) {
                    int lastID = getLastId(dataToLoad, 38, 4);
                	lineToAdd = dataToAdd[0]+","+dataToAdd[1]+","+dataToAdd[2]+","+dataToAdd[3]+","+lastID;
            	}
            	// If the row added is a lesson
            	else if(dataTypeAdded.equals("Lesson")) {
                    int lastID = getLastId(dataToLoad, 76, 8);
                	lineToAdd = dataToAdd[0]+","+dataToAdd[1]+","+dataToAdd[2]+","+dataToAdd[3]+","+dataToAdd[4]+","+dataToAdd[5]+","+dataToAdd[6]+","+dataToAdd[7]+","+lastID;
            	}
            	// If the row added is a message
            	else { // if(dataTypeAdded.equals("Message"))
                    int lastID = getLastId(dataToLoad, 81, 6);
                    lineToAdd = dataToAdd[0]+","+dataToAdd[1]+","+dataToAdd[2]+","+dataToAdd[3]+","+dataToAdd[4]+","+dataToAdd[5]+","+lastID;
            	}
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataToLoad, true))) {
                    writer.write(lineToAdd);
                    writer.newLine();
                }
                Main.infoMessage("The " + dataTypeAdded+" has been added successfully: "+lineToAdd, Color.BLUE, false);
                JOptionPane.showMessageDialog(null, "The "+ dataTypeAdded+" has been successfully added");
                loadDataFromCSV(dataToLoad, table);
                secondaryFrame.dispose();
            } catch (IOException e1) {
            	Main.infoMessage("Error when appending line to the file: " + e1.getMessage(), Color.RED, false);
            }
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
	
	// =====================================
    // ====== CHECK IS AN ID IS VALID ======
	// =====================================
	
	public static boolean isTrueID(String dataToLoad, int idColumn, int IDEntered) throws IOException {
        int currentID = 1; // ID of the new row, in the case where there is no previous row
        String[] rowData;
        
        
        boolean existing = false;

        // Reading the last line of the file to load to fetch the highest ID registered
        try (BufferedReader reader = new BufferedReader(new FileReader(dataToLoad))) {
            String currentLine;
            String lastLine = null;
                while ((currentLine = reader.readLine()) != null) {
                    lastLine = currentLine;
                }
                rowData = lastLine.split(",");
                currentID = Integer.parseInt(rowData[idColumn]);
                if(currentID == IDEntered) {
                	System.out.print("exist : "+currentID);
                	existing = true;
                }
        }
        return existing;
	}
	
	// ============================================
    // ====== GENERATE THE ID OF THE NEW ROW ======
	// ============================================
	
	public static int getLastId(String dataToLoad, int headerLength, int IDColumn) throws IOException {
		
		// This function is used to retrieve the highest ID registered. When a row id added is a file
		// in this program, we get it and increment it to attribute it to the new row. IDColumn is the
		// column in which the ID of a row is stored for a file. Since all the files don't have the
		// same number of columns, the value of IDColumn varies.
		
        int lastID = 1; // ID of the new row, in the case where there is no previous row in the file
        String[] rowData;

        // Reading the last line of the file to fetch the highest ID registered
        try (BufferedReader reader = new BufferedReader(new FileReader(dataToLoad))) {
            String currentLine;
            String lastLine = null;
    	    File file = new File(dataToLoad);

    	    // headerLength is the number of bytes contained by the headers of the file + EOL
    	    // If file.length is superior to headerLength, it means the file is not empty
    	    // and that the row that will be added won't have the value 1 in the id column.
    	    if(file.length() > headerLength) {
                while ((currentLine = reader.readLine()) != null) {
                    lastLine = currentLine;
                }
                rowData = lastLine.split(",");
    	    	// We get the id the last line recorded, and we increment it, to obtain
    	    	// the id that the new row will have.
                lastID = Integer.parseInt(rowData[IDColumn])+1;
    	    }
        }
        return lastID;
	}
}
