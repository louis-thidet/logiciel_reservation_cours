package gui;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import com.formdev.flatlaf.FlatLightLaf;

import managers.CSVManager;
import managers.IClientManager;
import java.awt.Color;

//To avoid the message "The serializable class Main does not declare a static final serialVersionUID field of type long"
// This class doesn't have to be serializable, it won't be transmitted to another JVM
@SuppressWarnings("serial")

public class Main extends JFrame {

    // ============================
    // ===== GLOBAL VARIABLES =====
    // ============================
	
	// To handle user's informations
    private static String connexionToken = "";  // This tokens is used by the server to check if the connected user was correctly connected
    private static String[] accountInformations; // This keeps the informations of the connected user
    private static String accountType; // This keeps the account type which is connected
    private static String accountId; // Keeps account id
    
    // To handle data
    private static List<String[]> accountLessons = new ArrayList<>(); // Contains lessons of the user
    //private static List<String[]> accountMessages = new ArrayList<>(); // Contains messages of the user
    private static String[][] accountMessages;
	private static String[][] availableLessons; // Contains all lessons that aren't booked in the service
	
    // To handle connection to server
	private static IClientManager clientManager; // To call the class which permits to interact with the server
	private static boolean connected = false; // To check is the connection to the server was well established (not the login connection, but was the server reached?)
	
	// Panels of the program
    private static ConnectionPanel connection; // Panel that we see when we arrive on the server
    private static LogInPanel login; // Panel that we see when we click on Log In button on connection panel
	private static SignUpPanel signup; // Panel that we see when we click on Sign Up button on connection panel
    private static StudentPanel studentPanel; // Panel that we see when we're logged as a student
    private static TutorPanel tutorPanel; // Panel that we see when we're logged as a teacher
    
	//public static CalendarView calendarView = new CalendarView(); // Generates a calendar showing booked lessons
    
    // To handle panels
	private static JPanel contentPane = new JPanel(); // Main panel of the program, in which other panels are placed
    private static CardLayout cardLayout = new CardLayout(); // Layout of the main panel
	
    // ===============================
    // ===== GETTERS AND SETTERS =====
    // ===============================
    
    public static String[][] getAvailableLessons() {
		return availableLessons;
	}
	public static String getAccountId() {
		return accountId;
	}
	public static void setAccountId(String accountId) {
		Main.accountId = accountId;
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
    public static IClientManager getClientManager() {
        return clientManager;
    }
    public static boolean getConnexionState() {
        return connected;
    }
    // =================================
    // ===== SHOW THE WANTED PANEL =====
    // =================================
    
    public static void showPanel(String panelName) {
    	cardLayout.show(contentPane, panelName);
    }
    
    // ============================
    // ===== REFRESH THE DATA =====
    // ============================
    
    @SuppressWarnings("static-access")
	public static void refreshData() {
    	String userId = CSVManager.getAccountId(accountType, accountInformations);
        try {
            // Loading the lessons of the account
            CSVManager.loadLesson();
            // Loading the messages of the account
            setAccountMessages(clientManager.findRowsUser(userId, "messages", accountType));
        
            
            // If the user is a student
            if(accountType.equals("student")) {
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
            }
            // If the user is a tutor
            else {
            	tutorPanel.loadMessages();
                // Reloading the calendarView
                tutorPanel.getCalendarView().reloadPage();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    
    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
    
	public Main() {
		
		// Setting the look and feel of the program
		FlatLightLaf.setup();
		
		try {
			// Retrieving the client manager from the server
            clientManager = (IClientManager) Naming.lookup("clientManager");
            connected = true; // If the clientManager was retrieved, it means the connection was established
            clientManager.connexionMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		// A JOptionPane appears to inform the user the connection failed when the server couldn't be reached
        if (!connected) {
        	JOptionPane.showMessageDialog(
        		    null,
        		    "Connection couldn't be established with the server. Try again later or contact the administrator.",
        		    "Failed to connect to the server",
        		    JOptionPane.INFORMATION_MESSAGE
        		);
            System.exit(0);
        }

        // Setting white background
        setBackground(new Color(255, 255, 255));
        // Setting the program's title
        setTitle("Eiffel Tutoring Solution");
        // Setting the program's closing behaviour
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setting dimensions of the program's JFrame
        setMinimumSize(new Dimension(700, 500));
        setBounds(100, 100, 760, 720);
        // Setting the program's main panel
        setContentPane(contentPane);
        // Setting the panel's layout
        contentPane.setLayout(cardLayout);

        // Creating the panels
        connection = new ConnectionPanel();
        signup = new SignUpPanel();
        login = new LogInPanel();
        studentPanel = new StudentPanel();
        tutorPanel = new TutorPanel();
        
        // Adding panels to the contentPane
        contentPane.add(connection, "connection");
        contentPane.add(signup, "signup");
        contentPane.add(login, "login");
        contentPane.add(studentPanel, "student");
        contentPane.add(tutorPanel, "tutor");

        // Showing connection panel by default
        cardLayout.show(contentPane, "connection");

        // Setting the icon of the JFrame
        Image icon = new ImageIcon(this.getClass().getResource("/assets/icons/gustave.png")).getImage();
        this.setIconImage(icon);
		}
	
	
    // ===============================
    // ===== MAIN OF THE PROGRAM =====
    // ===============================
	
	public static void main(String[] args) throws RemoteException, MalformedURLException {
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