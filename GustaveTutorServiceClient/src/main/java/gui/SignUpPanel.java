package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//To avoid the message "The serializable class SignUpPanel does not declare a static final serialVersionUID field of type long"
//This class doesn't have to be serializable, it won't be transmitted to another JVM
@SuppressWarnings("serial")

public class SignUpPanel extends JPanel {

    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
	public SignUpPanel() {

    	// Setting white background
        setBackground(new java.awt.Color(255, 255, 255));
        // Setting the panel's layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0); // Adds vertical spacing

        // Creating a label with an image
        JLabel gustaveImage = new JLabel(new ImageIcon(ConnectionPanel.class.getResource("/img/gustave_tutor_service.png")));
        add(gustaveImage, gbc);

        // Creating a signup panel
        gbc.gridy++;
        JPanel signupPanel = new JPanel();
        signupPanel.setBackground(new Color(255, 255, 255));
        signupPanel.setLayout(new GridLayout(5, 2, 10, 10));
        
        // components to enter email
        signupPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        signupPanel.add(emailField);
        
        // components to enter name
        signupPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        signupPanel.add(nameField);
        
        // components to enter surname
        signupPanel.add(new JLabel("Surname:"));
        JTextField surnameField = new JTextField();
        signupPanel.add(surnameField);
        
        // components to enter password
        signupPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        signupPanel.add(passwordField);
        
        // components to go back to the connexion menu
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Main.showPanel("connection");
        	}
        });
        backButton.setFocusable(false);
        signupPanel.add(backButton);

        // components to validate Sign Up
        JButton validationButton = new JButton("Sign Up");
        validationButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		signUpValidation(emailField, nameField, surnameField, passwordField);
        	}
        });
        signupPanel.add(validationButton);
        
        // listener to validate Sign Up by pressing enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // When Enter key is pressed, perform the action
                	signUpValidation(emailField, nameField, surnameField, passwordField);
                }
            }
        };
        emailField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        validationButton.addKeyListener(enterKeyListener);
        
        gbc.gridy++;
        add(signupPanel, gbc);
		
	}
	
    // ===================================
    // ===== VALIDATE A REGISTRATION =====
    // ===================================
	private void signUpValidation(JTextField emailField, JTextField nameField, JTextField surnameField, JPasswordField passwordField) {
		String email = emailField.getText();					// email
		String name = nameField.getText();						// name
		String surname = surnameField.getText();				// surname
		char[] passwordChars = passwordField.getPassword();		// password
		String password = new String(passwordChars);			// password
		boolean isEmailUsed;									// is the email already used
		try {
			// Checking if the email is already used
			isEmailUsed = Main.getClientManager().isEmailUsed(email);
			// If the email is already used
			if(isEmailUsed) {
				JOptionPane.showMessageDialog(null, "An account has already been created for this Email", "Account already created", JOptionPane.INFORMATION_MESSAGE);
			// If the email entered is incorrect
			} else if(email.equals("") || name.equals("") || surname.equals("") || password.equals("")) {
				JOptionPane.showMessageDialog(null, "Error: Empty field(s) detected", "Empty Password", JOptionPane.WARNING_MESSAGE);
			}
			// If everything is fine
			else {
				JOptionPane.showMessageDialog(null, "You will receive a validation link in mail. Click on it activate your account", "Registration received", JOptionPane.INFORMATION_MESSAGE);
				// The account is created directly, because the program doesn't have anything to send activation link
				// Also, the name and surname of the user are gathered from they're mail, while in a true case it would be gathered
				// from Gustave Eiffel University's mail service.
				Main.getClientManager().createAccount(email, name, surname, password);
				// Resetting fields
				emailField.setText("");
				nameField.setText("");
				surnameField.setText("");
				passwordField.setText("");
				// Back to connexion panel
				Main.showPanel("connection");
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} 
	}
}
