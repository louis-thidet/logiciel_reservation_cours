package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.formdev.flatlaf.FlatLightLaf;

import managers.CSVManager;
import managers.ClientManager;
import managers.LogManager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;

// To avoid the message "The serializable class Main does not declare a static final serialVersionUID field of type long".
// This class doesn't have to be serializable, it won't be transmitted to another JVM.
@SuppressWarnings("serial")

public class Main extends JFrame {
	
    // ============================
    // ===== GLOBAL VARIABLES =====
    // ============================
	
	// Area where are displayed new log entries
	protected static JTextPane logsArea;
	
	// Paths to data files
	private String loadedData = "src\\data\\students.csv";
	private static String messageData = "src\\data\\messages.csv";
	private static String lessonData = "src\\data\\lessons.csv";
	
	// Tables loading data files
	@SuppressWarnings("exports")
	public static JTable personTable;
	@SuppressWarnings("exports")
	public static JTextField searchBarLesson;
	@SuppressWarnings("exports")
	public static JTable lessonTable;
	@SuppressWarnings("exports")
	public static JTable messageTable;
	
	// Search bars
	@SuppressWarnings("exports")
	public static JTextField searchBarMessage;
	@SuppressWarnings("exports")
	public static JTextField searchBarPerson;
	
    // ===============================
    // ===== GETTERS AND SETTERS =====
    // ===============================
	
	@SuppressWarnings("exports")
	public static JTable getLessonTable() {
		return lessonTable;
	}
	@SuppressWarnings("exports")
	public static void setLessonTable(JTable lessonTable) {
		Main.lessonTable = lessonTable;
	}
	public static String getLessonData() {
		return lessonData;
	}
	@SuppressWarnings("static-access")
	public void setLessonData(String lessonData) {
		this.lessonData = lessonData;
	}

    // ==================================
    // ===== CREATION OF THE OBJECT =====
    // ==================================
	
	public Main() throws RemoteException {
		FlatLightLaf.setup();
		setMinimumSize(new Dimension(500, 300));
		// Definition of the JFrame
		setTitle("DO NOT CLOSE : Hosting Eiffel Tutoring Solution...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 770);
		
        // Creation of a JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel personTab = new JPanel(); // Create a JPanel for each tab
        personTab.setFont(new Font("Tahoma", Font.PLAIN, 10));
        JPanel lessonTab = new JPanel();
        lessonTab.setFont(new Font("Tahoma", Font.PLAIN, 10));
        JPanel logsTab = new JPanel();
        JPanel turnOffTab = new JPanel();
        //tab3.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        // ***********************************
        // ****** ACCOUNT MANAGEMENT TAB *****
        // ***********************************
        tabbedPane.addTab("Account Management", null, personTab, "account");
        personTab.setLayout(new GridLayout(0, 1, 0, 0));
        
        JSplitPane splitPane_1 = new JSplitPane();
        splitPane_1.setEnabled(false);
        splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        personTab.add(splitPane_1);
        
        JSplitPane splitPane_2 = new JSplitPane();
        splitPane_2.setEnabled(false);
        splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1.setLeftComponent(splitPane_2);
        
        JSplitPane splitPane_4 = new JSplitPane();
        splitPane_4.setDividerSize(3);
        splitPane_4.setEnabled(false);
        splitPane_2.setLeftComponent(splitPane_4);
        
        JButton buttonShowStudents = new JButton("Show Students");        
        buttonShowStudents.setBackground(new Color(0, 255, 0));
        buttonShowStudents.setFocusable(false);
        buttonShowStudents.setFont(new Font("Tahoma", Font.PLAIN, 15));
        splitPane_4.setLeftComponent(buttonShowStudents);
        
        JButton buttonShowTutors = new JButton("Show Tutors");
        buttonShowTutors.setFocusable(false);
        buttonShowTutors.setFont(new Font("Tahoma", Font.PLAIN, 15));
        splitPane_4.setRightComponent(buttonShowTutors);
        
        splitPane_4.setResizeWeight(0.5);
        
        JSplitPane splitPane_5 = new JSplitPane();
        splitPane_5.setMinimumSize(new Dimension(185, 25));
        splitPane_5.setPreferredSize(new Dimension(185, 25));
        splitPane_5.setDividerSize(0);
        splitPane_5.setEnabled(false);
        splitPane_2.setRightComponent(splitPane_5);
        
        searchBarPerson = new JTextField();
        searchBarPerson.setColumns(10);
        splitPane_5.setRightComponent(searchBarPerson);
        
        JButton buttonSearch = new JButton("Search");
        buttonSearch.setFocusable(false);

        buttonSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
        
        splitPane_5.setLeftComponent(buttonSearch);
        
        JSplitPane splitPane_3 = new JSplitPane();
        splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1.setRightComponent(splitPane_3);
        
        JScrollPane tableScrollPane = new JScrollPane();
        splitPane_3.setRightComponent(tableScrollPane);
        
        personTable = new JTable();
        personTable.getTableHeader().setReorderingAllowed(false); // prevent the columns to be dragged and reorganized
        tableScrollPane.setViewportView(personTable);
        
        JSplitPane splitPane_6 = new JSplitPane();
        splitPane_6.setDividerSize(3);
        splitPane_6.setEnabled(false);
        splitPane_3.setLeftComponent(splitPane_6);
        splitPane_6.setResizeWeight(0.5);
        
        JButton buttonRemovePerson = new JButton("Remove Student");
        
        buttonRemovePerson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	CSVManager.removeData(personTable, loadedData);
            }
        });
        
        buttonRemovePerson.setFocusable(false);
        buttonRemovePerson.setFont(new Font("Tahoma", Font.PLAIN, 13));
        splitPane_6.setLeftComponent(buttonRemovePerson);
        
        JButton buttonAddData = new JButton("Add Student");
        
        buttonAddData.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CSVManager.addData(personTable, loadedData);
        	}
        });
        
        buttonAddData.setFocusable(false);
        buttonAddData.setFont(new Font("Tahoma", Font.PLAIN, 13));
        splitPane_6.setRightComponent(buttonAddData);
        
        // ***********************************
        // ****** ACCOUNT MANAGEMENT TAB *****
        // ***********************************
        
        tabbedPane.addTab("Lessons Management", null, lessonTab, "lessons");
        lessonTab.setLayout(new GridLayout(0, 1, 0, 0));
        
        JSplitPane splitPane_1_1 = new JSplitPane();
        splitPane_1_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1_1.setEnabled(false);
        lessonTab.add(splitPane_1_1);
        
        JSplitPane splitPane_2_1 = new JSplitPane();
        splitPane_2_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_2_1.setEnabled(false);
        splitPane_1_1.setLeftComponent(splitPane_2_1);
        
        JSplitPane splitPane_4_1 = new JSplitPane();
        splitPane_4_1.setResizeWeight(0.5);
        splitPane_4_1.setEnabled(false);
        splitPane_4_1.setDividerSize(3);
        splitPane_2_1.setLeftComponent(splitPane_4_1);
        
        JButton btnShowComingLessons = new JButton("Show Coming Lessons");
        btnShowComingLessons.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnShowComingLessons.setFocusable(false);
        btnShowComingLessons.setBackground(Color.GREEN);
        splitPane_4_1.setLeftComponent(btnShowComingLessons);
        
        JButton btnShowPastLessons = new JButton("Show Past Lessons");
        btnShowPastLessons.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnShowPastLessons.setFocusable(false);
        splitPane_4_1.setRightComponent(btnShowPastLessons);
        
        JSplitPane splitPane_5_1 = new JSplitPane();
        splitPane_5_1.setPreferredSize(new Dimension(185, 25));
        splitPane_5_1.setMinimumSize(new Dimension(185, 25));
        splitPane_5_1.setEnabled(false);
        splitPane_5_1.setDividerSize(0);
        splitPane_2_1.setRightComponent(splitPane_5_1);
        
        JButton buttonSearch_1 = new JButton("Search");
        buttonSearch_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        buttonSearch_1.setFocusable(false);
        splitPane_5_1.setLeftComponent(buttonSearch_1);
        
        searchBarLesson = new JTextField();
        searchBarLesson.setColumns(10);
        splitPane_5_1.setRightComponent(searchBarLesson);
        
        JSplitPane splitPane_3_1 = new JSplitPane();
        splitPane_3_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1_1.setRightComponent(splitPane_3_1);
        
        JSplitPane splitPane_6_1 = new JSplitPane();
        splitPane_6_1.setResizeWeight(0.5);
        splitPane_6_1.setEnabled(false);
        splitPane_6_1.setDividerSize(3);
        splitPane_3_1.setLeftComponent(splitPane_6_1);
        
        JButton btnRemoveLessons = new JButton("Remove Lesson");
        btnRemoveLessons.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CSVManager.removeData(lessonTable, lessonData);
        	}
        });
        btnRemoveLessons.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnRemoveLessons.setFocusable(false);
        splitPane_6_1.setLeftComponent(btnRemoveLessons);
        
        JButton btnAddLesson = new JButton("Add Lesson");
        btnAddLesson.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CSVManager.addData(lessonTable, lessonData);
        	}
        });
        btnAddLesson.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnAddLesson.setFocusable(false);
        splitPane_6_1.setRightComponent(btnAddLesson);
        
        JScrollPane lessonTableScrollPane = new JScrollPane();
        splitPane_3_1.setRightComponent(lessonTableScrollPane);
        
        lessonTable = new JTable();
        lessonTableScrollPane.setViewportView(lessonTable);
        lessonTable.getTableHeader().setReorderingAllowed(false);
        personTab.setLayout(new GridLayout(0, 1, 0, 0));
        
        JPanel messageTab = new JPanel();
        messageTab.setFont(new Font("Tahoma", Font.PLAIN, 10));
        tabbedPane.addTab("Message Management", null, messageTab, null);
        messageTab.setLayout(new GridLayout(0, 1, 0, 0));
        
        JSplitPane splitPane_1_1_1 = new JSplitPane();
        splitPane_1_1_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1_1_1.setEnabled(false);
        messageTab.add(splitPane_1_1_1);
        
        JSplitPane splitPane_3_1_1 = new JSplitPane();
        splitPane_3_1_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1_1_1.setRightComponent(splitPane_3_1_1);
        
        JSplitPane splitPane_6_1_1 = new JSplitPane();
        splitPane_6_1_1.setResizeWeight(0.5);
        splitPane_6_1_1.setEnabled(false);
        splitPane_6_1_1.setDividerSize(3);
        splitPane_3_1_1.setLeftComponent(splitPane_6_1_1);
        
        JButton btnRemoveMessage = new JButton("Remove Message");
        btnRemoveMessage.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnRemoveMessage.setFocusable(false);
        splitPane_6_1_1.setLeftComponent(btnRemoveMessage);
        btnRemoveMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	CSVManager.removeData(messageTable, messageData);
            }
        });
        
        JButton btnAddMessage = new JButton("Add Message");
        btnAddMessage.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CSVManager.addData(messageTable, messageData);
        	}
        });
        
        btnAddMessage.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnAddMessage.setFocusable(false);
        splitPane_6_1_1.setRightComponent(btnAddMessage);
        
        JScrollPane tableScrollPane_1_1 = new JScrollPane();
        splitPane_3_1_1.setRightComponent(tableScrollPane_1_1);
        
        messageTable = new JTable();
        tableScrollPane_1_1.setViewportView(messageTable);
        messageTable.getTableHeader().setReorderingAllowed(false);
        
        JSplitPane splitPane_5_1_1 = new JSplitPane();
        splitPane_5_1_1.setPreferredSize(new Dimension(185, 25));
        splitPane_5_1_1.setMinimumSize(new Dimension(185, 25));
        splitPane_5_1_1.setEnabled(false);
        splitPane_5_1_1.setDividerSize(0);
        splitPane_1_1_1.setLeftComponent(splitPane_5_1_1);
        
        JButton buttonSearchMessage = new JButton("Search");
        buttonSearchMessage.setFont(new Font("Tahoma", Font.PLAIN, 15));
        buttonSearchMessage.setFocusable(false);
        splitPane_5_1_1.setLeftComponent(buttonSearchMessage);
        
        searchBarMessage = new JTextField();
        searchBarMessage.setColumns(10);
        splitPane_5_1_1.setRightComponent(searchBarMessage);
        
        // ***********************************
        // ************* LOG TAB *************
        // ***********************************
        
        tabbedPane.addTab("Logs", null, logsTab, "logs");
        logsTab.setLayout(new BorderLayout(0, 0));
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setEnabled(false);
        logsTab.add(splitPane, BorderLayout.SOUTH);
        
        JButton btnNewButton = new JButton("Show Log File in Explorer");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String directoryPath = LogManager.getlogPath()[0]+File.separator+"logs.txt";
        		File directory = new File (directoryPath);
        		if(!directory.exists()) {
        			infoMessage("logs.txt doesn't exist, it will be created", Color.RED, false);
        			LogManager.createLogFile("created");
        		}
        		openDirectory(LogManager.getlogPath()[0]);
        		
        	}
        });
        
        splitPane.setRightComponent(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Empty Log File");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String directoryPath = LogManager.getlogPath()[0]+File.separator+"logs.txt";
        		File directory = new File (directoryPath);
        		if(!directory.exists()) {
        			infoMessage("logs.txt doesn't exist, it will be created", Color.RED, false);
        			LogManager.createLogFile("created");
        		} else {
        			LogManager.createLogFile("erased");
        		}
        	}
        });
        
        splitPane.setLeftComponent(btnNewButton_1);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        logsTab.add(scrollPane_1, BorderLayout.CENTER);
        
        logsArea = new JTextPane();
        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        logsArea.setEditable(false);
        scrollPane_1.setViewportView(logsArea);
        infoMessage("Service has been Turned On", Color.BLACK, false);
        
        // ****************************************
        // ************* TURN OFF TAB *************
        // ****************************************
        
        tabbedPane.addTab("Turn Off", null, turnOffTab, "turnoff");
        turnOffTab.setLayout(new BoxLayout(turnOffTab, BoxLayout.X_AXIS));
        
        JScrollPane scrollPane = new JScrollPane();
        turnOffTab.add(scrollPane);
        
        JLabel lblNewLabel = new JLabel("*!* WARNING : this button turns off the service *!*");
        lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNewLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        lblNewLabel.setOpaque(true);
        lblNewLabel.setBackground(new Color(255, 0, 0));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        scrollPane.setColumnHeaderView(lblNewLabel);
        
        // Turn off button
        JButton turnOffButton = new JButton("Turn Off");
        turnOffButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		int choice = JOptionPane.showConfirmDialog(
        		        null,
        		        "Confirm the service's shutdown:",
        		        "Confirmation",
        		        JOptionPane.YES_NO_OPTION,
        		        JOptionPane.WARNING_MESSAGE
        		);
        		if (choice == JOptionPane.YES_OPTION) {
            		infoMessage("Service has been Turned Off", Color.BLACK, false);
            		System.exit(0);
        		}
        	}
        });
        turnOffButton.setFocusable(false);
        turnOffButton.setFont(new Font("Tahoma", Font.BOLD, 65));
        scrollPane.setViewportView(turnOffButton);
        
        
        // ****************************************
        // ******* LISTENERS OF THE PROGRAM *******
        // ****************************************
        

        // colors green...
        ActionListener commonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == buttonShowStudents) {
                    buttonShowStudents.setBackground(Color.GREEN);
                    buttonShowTutors.setBackground(new Color(240, 240, 240));
                    loadedData = "src\\data\\students.csv";
                    searchBarPerson.setText("");
                    buttonAddData.setText("Add a student");
                    buttonRemovePerson.setText("Remove a student");
                    CSVManager.loadDataFromCSV(loadedData, personTable);
                } else if (e.getSource() == buttonShowTutors) {
                	buttonShowTutors.setBackground(Color.GREEN);
                	buttonShowStudents.setBackground(new Color(240, 240, 240));
                	loadedData = "src\\data\\tutors.csv";
                	searchBarPerson.setText("");
                    buttonAddData.setText("Add a tutor");
                    buttonRemovePerson.setText("Remove a tutor");
                    CSVManager.loadDataFromCSV(loadedData, personTable);
                // search
                } else{
                	CSVManager.loadDataFromCSV(loadedData, personTable);
                }
            }
        };

        buttonShowStudents.addActionListener(commonListener);
        buttonShowTutors.addActionListener(commonListener);
        buttonSearch.addActionListener(commonListener);
        
        searchBarPerson.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // When Enter key is pressed, perform the action
                    commonListener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null));
                }
            }
        });
        
        
        
        
        ActionListener lessonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnShowComingLessons) {
                	btnShowComingLessons.setBackground(Color.GREEN);
                	btnShowPastLessons.setBackground(new Color(240, 240, 240));
                	searchBarLesson.setText("");
                	CSVManager.loadDataFromCSV(lessonData, lessonTable);
                } else if (e.getSource() == btnShowPastLessons){
                	btnShowPastLessons.setBackground(Color.GREEN);
                	btnShowComingLessons.setBackground(new Color(240, 240, 240));
                	searchBarLesson.setText("");
                	CSVManager.loadDataFromCSV(lessonData, lessonTable);
                // search
                } else {
                	CSVManager.loadDataFromCSV(lessonData, lessonTable);
                }
            }
        };
        
        btnShowComingLessons.addActionListener(lessonListener);
        btnShowPastLessons.addActionListener(lessonListener);
        buttonSearch_1.addActionListener(lessonListener);
        
        searchBarLesson.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // When Enter key is pressed, perform the action
                    lessonListener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null));
                }
            }
        });
        
        
        
        ActionListener messageListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	CSVManager.loadDataFromCSV(messageData, messageTable);
                
            }
        };
        
        btnShowComingLessons.addActionListener(lessonListener);
        btnShowPastLessons.addActionListener(lessonListener);
        buttonSearch_1.addActionListener(lessonListener);
        
        searchBarMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // When Enter key is pressed, perform the action
                	messageListener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null));
                }
            }
        });
        
        // ****************************************
        // ****** PREPARATION OF THE PROGRAM ******
        // ****************************************
        
        // loading the data
        CSVManager.loadDataFromCSV(loadedData, personTable);
        CSVManager.loadDataFromCSV(lessonData, lessonTable);
        CSVManager.loadDataFromCSV(messageData, messageTable);
        
        // Adding tabbedPane to content pane
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Set the custom icon
        Image icon = new ImageIcon(this.getClass().getResource("/assets/icons/gustave.png")).getImage();
        this.setIconImage(icon);
        
        // Checking if log file exists
  		String logPath = LogManager.getlogPath()[0]+File.separator+"logs.txt";
		File directory = new File (logPath);
		// If log file doesn't exist, it's created
		if(!directory.exists()) {
			infoMessage("logs.txt doesn't exist, it will be created", Color.RED, false);
			LogManager.createLogFile("created");
		}
	}
	
    // ===============================
    // ===== MAIN OF THE PROGRAM =====
    // ===============================
	
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		
		// Initializing the JFrame
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		// Rebinding ClientManager
        LocateRegistry.createRegistry(1099);
        ClientManager clientManager = new ClientManager();
        Naming.rebind("clientManager", clientManager);
	}
	
    // =====================================================
    // ===== OPEN A DIRECTORY IN THE SYSTEM'S EXPLORER =====
    // =====================================================
	
	public static void openDirectory(String directoryPath) {
		// Retrieve the directory
		File directory = new File (directoryPath);
		// Check if the directory exists
		if (!directory.exists()) {
			infoMessage(directoryPath+" does not exist", Color.RED, false);
		    return;
		}
		// Open the directory in the system's file explorer
		try {
		    Desktop.getDesktop().open(directory);
		} catch (IOException e) {
		    infoMessage("Error opening "+directoryPath+": "+e.getMessage(), Color.BLUE, false);
		}
	}
	
    public static String getMessageData() {
		return messageData;
	}

	// ===================================================
    // ===== PRINT A MESSAGE IN TERMINAL AND IN LOGS =====
    // ===================================================
    
    public static void infoMessage(String message, @SuppressWarnings("exports") Color color, boolean bold) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        String output = formattedDateTime + ": " + message;

        // Output in log tab
        StyledDocument doc = logsArea.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, color);
        StyleConstants.setBold(style, bold);
        try {
            doc.insertString(doc.getLength(), output + "\n", style);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Output in log file
        LogManager.writeLogFile(output);

        // Output in terminal
        System.out.println(output);
    }

}