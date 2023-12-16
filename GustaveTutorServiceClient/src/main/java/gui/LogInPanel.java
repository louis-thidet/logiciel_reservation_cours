package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
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
import java.rmi.RemoteException;

// To avoid the message "The serializable class LogInPanel does not declare a static final serialVersionUID field of type long".
// This class doesn't have to be serializable, it won't be transmitted to another JVM.
@SuppressWarnings("serial")

public class LogInPanel extends JPanel {

    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
	
    public LogInPanel() {
    	
        setBackground(new java.awt.Color(255, 255, 255)); // Setting white background
        setLayout(new GridBagLayout()); // Setting panel's layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 66, 0); // Adds vertical spacing

        // Creating a label with an image
        JLabel gustaveImage = new JLabel(new ImageIcon(ConnectionPanel.class.getResource("/img/gustave_tutor_service.png")));
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
        		logInValidation(emailField, passwordField);
        	}
        });
        loginPanel.add(validationButton);
        
        // listener to validate login by pressing enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // When Enter key is pressed, perform the action
                    logInValidation(emailField, passwordField);
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
    private void logInValidation(JTextField emailField, JPasswordField passwordField) {
		String email = emailField.getText();					// email
		char[] passwordChars = passwordField.getPassword();		
		String password = new String(passwordChars);			// password
		String isTrueAccount[];									// array to handle the login
		try {
			// Checking if the email and passsword are correct
			isTrueAccount = Main.getClientManager().isLoginTrue(email, password, "student");
            
			// If the informations provided were correct
			if(isTrueAccount[0].equals("true")) {
				// We set the token created in isLoginTrue()
				Main.setToken(isTrueAccount[1]);
				String accountInformationsLine = isTrueAccount[2];
				Main.setAccountInformations(accountInformationsLine.split(","));
				// Showing student's panel
				Main.showPanel("student");
				// Loading data
				Main.refreshData();
				// Loading the calendar html file in the calendar
				StudentPanel.getCalendarView().loadURL("src\\main\\java\\gui\\calendar\\calendar.html");
				Main.setAccountId(Main.getAccountInformations()[6]);
				Main.getStudentPanel().getBalanceValueLabel().setText(Main.getAccountInformations()[5] + " €");
		    // If the informations provided weren't correct
			} else {
				JOptionPane.showMessageDialog(null, "Connection failed. Wrongs logins entered", "Wrong logins", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
    }
}
