package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.RemoteException;

//To avoid the message "The serializable class LogIn does not declare a static final serialVersionUID field of type long"
// This class doesn't have to be serializable, it won't be transmitted to another JVM
@SuppressWarnings("serial")

public class LogInPanel extends JPanel {

    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
    public LogInPanel() {
    	
    	// Setting white background
        setBackground(new java.awt.Color(255, 255, 255));
        // Setting the panel's layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0); // Adds vertical spacing

        // Creating a label with an image
        JLabel gustaveImage = new JLabel(new ImageIcon(Main.class.getResource("/assets/img/gustave_tutoring_solution.png")));
        add(gustaveImage, gbc);

        // Creating a login panel
        gbc.gridy++;
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(255, 255, 255));
        
        loginPanel.setLayout(new GridLayout(4, 2, 10, 10));
        
        // components to enter email
        loginPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        loginPanel.add(emailField);
        
        // components to enter password
        loginPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        loginPanel.add(passwordField);
        
        // components to enter if the user is a student or a tutor
        JPanel personPanel = new JPanel();
        personPanel.setBackground(new Color(255, 255, 255));
        JTextField personField = new JTextField();
        personField.setText("student");
        loginPanel.add(new JLabel("Student or Tutor?"));
        JRadioButton studentButton = new JRadioButton("Student");
        studentButton.setBackground(new Color(255, 255, 255));
        JRadioButton tutorButton = new JRadioButton("Tutor");
        tutorButton.setBackground(new Color(255, 255, 255));
        studentButton.setSelected(true);
        ButtonGroup personType = new ButtonGroup();
        personType.add(studentButton);
        personType.add(tutorButton);
        personPanel.add(studentButton);
        personPanel.add(tutorButton);
        loginPanel.add(personPanel);
        
        studentButton.addActionListener((ActionEvent e) -> {
        	personField.setText("student");
        });
        tutorButton.addActionListener((ActionEvent e) -> {
        	personField.setText("tutor");
        });
        
        // components to go back to the connexion menu
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Main.showPanel("connection");
        	}
        });
        backButton.setFocusable(false);
        loginPanel.add(backButton);

        // components to validate login
        JButton validationButton = new JButton("Log In");
        validationButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		logInValidation(emailField, passwordField, personField);
        	}
        });
        loginPanel.add(validationButton);
        
        // listener to validate login by pressing enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // When Enter key is pressed, perform the action
                    logInValidation(emailField, passwordField, personField);
                }
            }
        };
        emailField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        validationButton.addKeyListener(enterKeyListener);
        
        gbc.gridy++;
        add(loginPanel, gbc);
    }
    
    // ==============================
    // ===== VALIDATE THE LOGIN =====
    // ==============================
    private void logInValidation(JTextField emailField, JPasswordField passwordField, JTextField personField) {
		String email = emailField.getText();					// email
		char[] passwordChars = passwordField.getPassword();		
		String password = new String(passwordChars);			// password
		String userType = personField.getText();				// account type
		String isTrueAccount[];									// array to handle the login
		try {
			// Checking if the email and passsword are correct and relative to the user type selected
			isTrueAccount = Main.getClientManager().isLoginTrue(email, password, userType);
			// If the informations provided were correct
			if(isTrueAccount[0].equals("true")) {
				// We set the token created in isLoginTrue()
				Main.setToken(isTrueAccount[1]);
				String accountInformationsLine = isTrueAccount[2];
				Main.setAccountInformations(accountInformationsLine.split(","));
				Main.setAccountType(personField.getText());
				// Showing student's panel or tutor's panel relatively to the account type
				if(Main.getAccountType().equals("student")) {
					Main.showPanel("student");
				} else {
					Main.showPanel("tutor");
				}
				// Loading data with refresh()
				Main.refreshData();
				
				// Loading the calendar html file in the calendar
				if(Main.getAccountType().equals("student")) {
					StudentPanel.getCalendarView().loadURL("src\\gui\\calendar\\calendar.html");
					Main.setAccountId(Main.getAccountInformations()[6]);
				} else {
					TutorPanel.getCalendarView().loadURL("src\\gui\\calendar\\calendar.html");
					Main.setAccountId(Main.getAccountInformations()[4]);
				}
				
		    // If the informations provided weren't correct
			} else {
				JOptionPane.showMessageDialog(null, "Connection failed. Wrongs logins entered", "Wrong logins", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
}
