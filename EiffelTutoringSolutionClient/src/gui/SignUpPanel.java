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
import java.io.IOException;
import java.rmi.RemoteException;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

// To avoid the message "The serializable class SignUp does not declare a static final serialVersionUID field of type long.
// This class doesn't have to be serializable, it won't be transmitted to another JVM.
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
        JLabel gustaveImage = new JLabel(new ImageIcon(Main.class.getResource("/assets/img/gustave_tutoring_solution.png")));
        add(gustaveImage, gbc);

        // Creating a signup panel
        gbc.gridy++;
        JPanel signupPanel = new JPanel();
        signupPanel.setBackground(new Color(255, 255, 255));
        signupPanel.setLayout(new GridLayout(4, 2, 10, 10));
        
        // components to enter email
        signupPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        signupPanel.add(emailField);
        
        // components to enter password
        signupPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        signupPanel.add(passwordField);
        
        // components to enter if the user is a student or a tutor
        JPanel personPanel = new JPanel();
        personPanel.setBackground(new Color(255, 255, 255));
        JTextField personField = new JTextField();
        personField.setText("student");
        signupPanel.add(new JLabel("Student or Tutor?"));
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
        signupPanel.add(personPanel);
        
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
        signupPanel.add(backButton);

        // components to validate Sign Up
        JButton validationButton = new JButton("Sign Up");
        validationButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		signUpValidation(emailField, passwordField, personField);
        	}
        });
        signupPanel.add(validationButton);
        
        // listener to validate Sign Up by pressing enter
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // When Enter key is pressed, perform the action
                    signUpValidation(emailField, passwordField, personField);
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
	private void signUpValidation(JTextField emailField, JPasswordField passwordField, JTextField personField) {
		String email = emailField.getText();					// email
		char[] passwordChars = passwordField.getPassword();		
		String password = new String(passwordChars);			// password
		String userType = personField.getText();				// account type
		boolean isEmailUsed;									// is the email already used
		try {
			// Checking if the email is already used
			isEmailUsed = Main.getClientManager().isEmailUsed(email);
			// If the email is already used
			if(isEmailUsed) {
				JOptionPane.showMessageDialog(null, "An account has already been created for this Email", "Account already created", JOptionPane.INFORMATION_MESSAGE);
			// If the email entered is incorrect
			} else if((!email.contains("@edu.univ-eiffel.fr") && !email.contains("@univ-eiffel.fr")) || !email.matches("[a-zA-Z]+\\.[a-zA-Z]+@.*")) {
				JOptionPane.showMessageDialog(null, "Invalid Email entered", "Invalid Email", JOptionPane.INFORMATION_MESSAGE);
			// If the password sent is empty
			} else if(password.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a Password", "Empty Password", JOptionPane.INFORMATION_MESSAGE);
			}
			// If everything is fine
			else {
				JOptionPane.showMessageDialog(null, "You will receive a validation link in mail. Click on it activate your account", "Registration received", JOptionPane.INFORMATION_MESSAGE);
				// The account is created directly, because the program doesn't have anything to send activation link
				// Also, the name and surname of the user are gathered from they're mail, while in a true case it would be gathered
				// from Gustave Eiffel University's mail service.
				Main.getClientManager().createAccount(email, password, userType);
				// Resetting fields
				emailField.setText("");
				passwordField.setText("");
				// Back to connexion panel
				Main.showPanel("connection");
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
