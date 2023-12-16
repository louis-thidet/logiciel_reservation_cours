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

// To avoid the message "The serializable class ConnectionPanel does not declare a static final serialVersionUID field of type long".
// This class doesn't have to be serializable, it won't be transmitted to another JVM.
@SuppressWarnings("serial")

public class ConnectionPanel extends JPanel {

    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
    public ConnectionPanel() {
    	
        setBackground(new java.awt.Color(255, 255, 255)); // Setting white background
        setLayout(new GridBagLayout()); // Setting panel's layout

        // Creating GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 85, 0); // Adds vertical spacing

        // Creating a label with an image
        JLabel gustaveImage = new JLabel(new ImageIcon(ConnectionPanel.class.getResource("/img/gustave_tutor_service.png")));
        add(gustaveImage, gbc);

        // Creating another GridBagConstraints
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 1;
        gbcButtons.insets = new Insets(0, 0, 5, 0); // Adds vertical spacing

        // Creating Log In and Sign Up buttons
        JButton logInButton = new JButton(" Log In  ");
        logInButton.setFocusable(false);
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFocusable(false);

        // Setting preferred size for the buttons
        logInButton.setPreferredSize(new Dimension(100, 40));
        signUpButton.setPreferredSize(new Dimension(100, 40));
        
        add(logInButton, gbcButtons);
        gbcButtons.gridy++;
        gbcButtons.insets = new Insets(20, 0, 60, 0); // Adds vertical spacing
        add(signUpButton, gbcButtons);

        // Creating event listener for the Log In button
        logInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Hiding Connection Panel to show Login Panel
                setVisible(false);
                Main.showPanel("login");
            }
        });
        
        // Creating event listener for the Sign Up button
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Hiding Connection Panel to show Sign Up Panel
                setVisible(false);
                Main.showPanel("signup");
            }
        });
    }
}
