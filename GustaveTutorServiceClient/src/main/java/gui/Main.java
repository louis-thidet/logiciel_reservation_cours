package gui;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import com.formdev.flatlaf.FlatLightLaf;

import main.GustaveTutorService;
import main.GustaveTutorServiceServiceLocator;
import managers.CSVManager;
import java.awt.Color;

// To avoid the message "The serializable class Main does not declare a static final serialVersionUID field of type long".
// This class doesn't have to be serializable, it won't be transmitted to another JVM.
@SuppressWarnings("serial")

public class Main extends JFrame {

    // ============================
    // ===== GLOBAL VARIABLES =====
    // ============================
	
	// To handle user's informations
    private static String connexionToken = "";  // Token used by the server to check if the connected user was correctly connected. Working with user's id
    private static String[] accountInformations; // Keeps the informations of the connected user
    private static String accountType = "student"; // Keeps the account type which is connected
    private static String accountId; // Keeps account id

	// To handle data
    private static List<String[]> accountLessons = new ArrayList<>(); // Contains lessons of the user
    private static String[][] accountMessages;
	private static String[][] availableLessons; // Contains all lessons that aren't booked in the service
	
    // To handle connection to server
	private static GustaveTutorService clientManager;
	
	// Panels of the program
    private static ConnectionPanel connection; // Panel that we see when we arrive on the server
    private static LogInPanel login; // Panel that we see when we click on Log In button on connection panel
	private static SignUpPanel signup; // Panel that we see when we click on Sign Up button on connection panel
    private static StudentPanel studentPanel; // Panel that we see when we're logged as a student
    
    // To handle panels
	private static JPanel contentPane = new JPanel(); // Main panel of the program, in which other panels are placed
    private static CardLayout cardLayout = new CardLayout(); // Layout of the main panel
	
    
    // ===============================
    // ===== GETTERS AND SETTERS =====
    // ===============================
    
	public static String getAccountId() {
		return accountId;
	}
	public static void setAccountId(String accountId) {
		Main.accountId = accountId;
	}
    public static String[][] getAvailableLessons() {
		return availableLessons;
	}
	public static void setAvailableLessons(String[][] availableLessons) {
		Main.availableLessons = availableLessons;
	}
	public static List<String[]> getAccountLessons() {
		return accountLessons;
	}
	public static void setAccountLessons(List<String[]> accountLessons) {
		Main.accountLessons = accountLessons;
	}
	public static String[][] getAccountMessages() {
		return accountMessages;
	}
	public static void setAccountMessages(String[][] accountMessages) {
		Main.accountMessages = accountMessages;
	}
    public static String getToken(){
        return connexionToken;
    }
	public static void setToken(String token) {
		connexionToken = token;
	}
    public static String[] getAccountInformations() {
		return accountInformations;
	}
	public static void setAccountInformations(String[] accountInformations) {
		Main.accountInformations = accountInformations;
	}
	public static String getAccountType() {
		return accountType;
	}
	public static void setAccountType(String accountType) {
		Main.accountType = accountType;
	}
    public static GustaveTutorService getClientManager() {
        return clientManager;
    }
	public static StudentPanel getStudentPanel() {
		return studentPanel;
	}
	public static void setStudentPanel(StudentPanel studentPanel) {
		Main.studentPanel = studentPanel;
	}
    
    // ========================
    // ===== SHOW A PANEL =====
    // ========================
    
    public static void showPanel(String panelName) {
    	cardLayout.show(contentPane, panelName);
    }
    
    // ============================
    // ===== REFRESH THE DATA =====
    // ============================
    
    @SuppressWarnings("static-access")
	public static void refreshData() {
    	String userId = CSVManager.getAccountId();
        try {
            // Loading the lessons of the account
            CSVManager.loadLesson();
            // Loading the messages of the account
            setAccountMessages(clientManager.findRowsUser(userId, "messages", "student"));
        
            // Removing all the tutors shown in the proper JComboBox
            studentPanel.getAvailableTutorsComboBox().removeAllItems();

            // Loading the available lessons
            setAvailableLessons(clientManager.findAvailableLessons());
            // Put the lesson Type selection JComboBox on the first type
            studentPanel.getLessonTypeComboBox().setSelectedIndex(0);
            // Refresh the messages
            studentPanel.loadMessages();
            // Reloading the calendarView
            studentPanel.getCalendarView().reloadPage();
            
            // Refreshing
            String[] studentInfos = Main.getClientManager().getInfos(Main.getAccountId(), "student");
            studentPanel.setBalanceValueLabel(studentInfos[5]);
            
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
    
	public Main() {
		
		// Setting program's look and feel
		FlatLightLaf.setup();

		// Yet, the program isn't connected to the server
		boolean connected = false;

		// Trying to connect to the server
		try {
		    // Retrieving client manager from the server
		    clientManager = new GustaveTutorServiceServiceLocator().getGustaveTutorService();
		    // If the retrieval was successful, this means the program is connected
		    if(clientManager.startService()) {
		    	connected = true;
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
			
		// JOptionPane to inform user that connection has failed
        if (!connected) {
        	JOptionPane.showMessageDialog(
        		    null,
        		    "Connection couldn't be established with the server. Try again later or contact the administrator.",
        		    "Failed to connect to the server",
        		    JOptionPane.INFORMATION_MESSAGE
        		);
        	// Exiting program
            System.exit(0);
        }

        
        setBackground(new Color(255, 255, 255)); // Setting white background
        setTitle("Eiffel Tutoring Solution");  // Setting program's title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Setting program's closing behavior
        setMinimumSize(new Dimension(700, 500)); // Setting dimensions of the program's JFrame
        setBounds(100, 100, 760, 720);
        setContentPane(contentPane); // Setting program's main panel
        contentPane.setLayout(cardLayout); // Setting main panel's layout

        // Creating panels
        connection = new ConnectionPanel();
        signup = new SignUpPanel();
        login = new LogInPanel();
        studentPanel = new StudentPanel();
        
        // Adding panels to contentPane
        contentPane.add(connection, "connection");
        contentPane.add(signup, "signup");
        contentPane.add(login, "login");
        contentPane.add(studentPanel, "student");

        // Showing connection panel by default
        cardLayout.show(contentPane, "connection");

        // Setting icon of the JFrame
        Image icon = new ImageIcon(this.getClass().getResource("/icons/gustave.png")).getImage();
        this.setIconImage(icon);
		}
	
    // ===============================
    // ===== MAIN OF THE PROGRAM =====
    // ===============================

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		// Initializing Swing GUI
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
	}
}