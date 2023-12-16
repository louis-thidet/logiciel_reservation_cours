package gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//To avoid the message "The serializable class Connection does not declare a static final serialVersionUID field of type long"
//This class doesn't have to be serializable, it won't be transmitted to another JVM
@SuppressWarnings("serial")

public class ConnectionPanel extends JPanel {

    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
    public ConnectionPanel() {
    	
    	// Setting white background
        setBackground(new java.awt.Color(255, 255, 255));
        // Setting the panel's layout
        setLayout(new GridBagLayout());

        // layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 85, 0); // Adds vertical spacing
        // layout

        // Creating a label with an image
        JLabel gustaveImage = new JLabel(new ImageIcon(Main.class.getResource("/assets/img/gustave_tutoring_solution.png")));
        add(gustaveImage, gbc);

        // layout
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 1;
        gbcButtons.insets = new Insets(0, 0, 5, 0); // Adds vertical spacing
        // layout

        // Creating the Log In and Sign Up buttons
        JButton logInButton = new JButton(" Log In  ");
        logInButton.setFocusable(false);
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFocusable(false);

        // Setting the preferred size for the buttons
        logInButton.setPreferredSize(new Dimension(100, 40));
        signUpButton.setPreferredSize(new Dimension(100, 40));

        // layout
        add(logInButton, gbcButtons); // Adding Log In button to the panel
        gbcButtons.gridy++;
        gbcButtons.insets = new Insets(20, 0, 60, 0); // Adds vertical spacing
        add(signUpButton, gbcButtons); // Adding Sign Up button to the panel

        // Creating the event listener for the Log In button
        logInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Hiding the connection panel to show the login panel
                setVisible(false);
                Main.showPanel("login");
            }
        });
        
        // Creating the event listener for the Sign Up button
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Hiding the connection panel to show the login panel
                setVisible(false);
                Main.showPanel("signup");
            }
        });
    }
}
