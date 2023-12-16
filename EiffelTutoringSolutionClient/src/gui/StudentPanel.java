package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import managers.CSVManager;

import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;

//To avoid the message "The serializable class StudentPanel does not declare a static final serialVersionUID field of type long"
// This class doesn't have to be serializable, it won't be transmitted to another JVM
@SuppressWarnings("serial")

public class StudentPanel extends JPanel {
	
    // ============================
    // ===== GLOBAL VARIABLES =====
    // ============================
	
	private static String[] lessonTypes = {"Mathematics", "History", "Python", "Geography", "French", "Java", "English"}; // Existing Lesson Types
    private static JComboBox<String> lessonTypeComboBox = new JComboBox<>(lessonTypes); // Used to select lesson type
    private static JComboBox<String> availableTutorsComboBox = new JComboBox<String>(); // Used to select the tutor
    private static DefaultListModel<String> lessonsListModel = new DefaultListModel<>(); // Model of the JList displaying available lessons
    private static JList<String> availableLessonsList = new JList<>(lessonsListModel); // It displays the available lessons
    
    private static DefaultListModel<String> receivedMessagesListModel = new DefaultListModel<>(); // Model of the JList displaying received messages
    private static JList<String> receivedMessagesList = new JList<>(receivedMessagesListModel); // It displays the received messages
    private static DefaultListModel<String> sentMessagesListModel = new DefaultListModel<>(); // Model of the JList displaying sent messages
    private static JList<String> sentMessagesList = new JList<>(sentMessagesListModel); // It displays the sent messages
    
    private static Map<String, String> messageMap = new HashMap<>();
    private static Map<String, String> lessonMap = new HashMap<>();
    
    public static CalendarView calendarView = new CalendarView(); // Generate a calendar showing booked lessons

	
    // ===============================
    // ===== GETTERS AND SETTERS =====
    // ===============================

	public JComboBox<String> getLessonTypeComboBox() {
		return lessonTypeComboBox;
	}
	public JComboBox<String> getAvailableTutorsComboBox() {
		return availableTutorsComboBox;
	}
	public JList<String> getAvailableLessonsList() {
		return availableLessonsList;
	}
	public static CalendarView getCalendarView() {
		return calendarView;
	}
	public static void setCalendarView(CalendarView calendarView) {
		StudentPanel.calendarView = calendarView;
	}
	
    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
	
	public StudentPanel() {
		
        // ************************************
        // ****** HANDLING LESSON BOOKING *****
        // ************************************
		
		DefaultListModel<String> lessonsModel = (DefaultListModel<String>) availableLessonsList.getModel();
        lessonsModel.addElement("No Tutor selected");
        
        availableLessonsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle mouse click event
                if (e.getClickCount() == 2) {
                    String selectedItem = availableLessonsList.getSelectedValue();

                    // Check if selectedItem is not null before using it
                    if (!selectedItem.equals("No Tutor selected")) {
                    	
                    	String lessonId = lessonMap.get(selectedItem); // Retrieving lesson's id

                    	// The user is asked if he wants to book the lesson
                        int result = JOptionPane.showConfirmDialog(null, "Do you want to book this lesson?", "Book a lesson", JOptionPane.YES_NO_OPTION);
                        System.out.println("zaeaezaz");
                        // If the user chooses to book the lesson...
                        if (result == JOptionPane.YES_OPTION) {
                        	System.out.println("deee");
							try {
								Main.getClientManager().bookLesson(Main.getAccountInformations()[6],lessonId, Main.getToken());
							} catch (RemoteException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Main.refreshData();
                        }
                    }
                }
            }
        });

        
        // **********************************************************
        // ****** HANDLING LESSON BOOKING VALIDATION FROM TUTOR *****
        // **********************************************************

        receivedMessagesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle mouse click event
                if (e.getClickCount() == 2) {
                	String accType = Main.getAccountType();
                	String[] accInfos = Main.getAccountInformations();
                	
                    // Retrieve the selected message
                    String selectedItem = receivedMessagesList.getSelectedValue();

                    // Retrieve the messageId associated with the selected message from the map
                    String messageId = messageMap.get(selectedItem);
                    // If selectedItem isn't pending, answer is received, it's no or yes


                    	// The user is asked if he wants to deleted the message
                        int result = JOptionPane.showConfirmDialog(null, "Do you want to delete this message?", "Message deletion", JOptionPane.YES_OPTION);
                        
                        // If deletion is validated
                        if (result == JOptionPane.YES_OPTION) {
							try {
								Main.getClientManager().deleteMessage(messageId, Main.getToken(), accType, accInfos);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Main.refreshData();
                        }

                }
            }
        });
        
        sentMessagesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle mouse click event
                if (e.getClickCount() == 2) {
                	String accType = Main.getAccountType();
                	String[] accInfos = Main.getAccountInformations();
                	String[][] messages = Main.getAccountMessages();
                	String lessonId = null;
                	
                    // Retrieve the selected message
                    String selectedItem = sentMessagesList.getSelectedValue();

                    // Retrieve the messageId associated with the selected message from the map
                    String messageId = messageMap.get(selectedItem);
                    
                    for(int i = 0; i < messages.length; i++) {
                    	if(messages[i][6].equals(messageId)) {
                    		lessonId = messages[i][5];
                    	}
                    }
                    
                    	// The user is asked if he wants to deleted the message
                        int result = JOptionPane.showConfirmDialog(null, "Do you want to cancel your booking?", "Cancel Booking", JOptionPane.YES_OPTION);
                        
                        // If deletion is validated
                        if (result == JOptionPane.YES_OPTION) {
							try {
								Main.getClientManager().deleteMessage(messageId, Main.getToken(), accType, accInfos);
								Main.getClientManager().cancelBookingRequest(lessonId);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Main.refreshData();
                        }

                }
            }
        });


        // ***********************************
        // ****** JTABBED PANEL CREATION *****
        // ***********************************
		
		setLayout(new BorderLayout());
		
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        JPanel takeLessonTab = new JPanel(); // Create a JPanel for each tab
        takeLessonTab.setBackground(new Color(255, 255, 255));
        takeLessonTab.setFont(new Font("Tahoma", Font.PLAIN, 10));
        //tab3.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        // ***********************************
        // ****** ACCOUNT MANAGEMENT TAB *****
        // ***********************************
        tabbedPane.addTab("Take Lessons", null, takeLessonTab, "takeLessons");
        takeLessonTab.setLayout(new GridLayout(0, 1, 0, 0));
        
        // ADD A LABEL "TAKE A LESSON"
        

        
        
        JPanel messageTab = new JPanel();
        messageTab.setFont(new Font("Tahoma", Font.PLAIN, 10));
        tabbedPane.addTab("Messages", null, messageTab, "messages");
        messageTab.setLayout(new GridLayout(0, 1, 0, 0));
        
        JSplitPane messagesTopAndBottomPanel = new JSplitPane();
        messagesTopAndBottomPanel.setDividerSize(0);
        messagesTopAndBottomPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
        messagesTopAndBottomPanel.setEnabled(false);
        messageTab.add(messagesTopAndBottomPanel);

        JButton refreshMessagesButton = new JButton("Refresh Messages");
        refreshMessagesButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        refreshMessagesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Main.refreshData();
            }
        });
        
        messagesTopAndBottomPanel.setLeftComponent(refreshMessagesButton);
        
        JSplitPane messagesPane = new JSplitPane();
        messagesPane.setDividerSize(0);
        messagesPane.setEnabled(false);
        messagesPane.setResizeWeight(0.5);
        messagesPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        messagesTopAndBottomPanel.setRightComponent(messagesPane);
        
        JSplitPane receivedMessagesPane = new JSplitPane();
        receivedMessagesPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        receivedMessagesPane.setDividerSize(0);
        messagesPane.setLeftComponent(receivedMessagesPane);
        
        JLabel receivedMessagesLabel = new JLabel("  Received Messages");
        receivedMessagesLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        
        receivedMessagesLabel.setBorder(new EmptyBorder(3, 0, 3, 0));
        receivedMessagesPane.setLeftComponent(receivedMessagesLabel);
        
        JScrollPane receivedMessagesScrollPane = new JScrollPane();
        receivedMessagesScrollPane.setBorder(new LineBorder(new Color(192, 192, 192)));
        receivedMessagesPane.setRightComponent(receivedMessagesScrollPane);
        receivedMessagesScrollPane.setViewportView(receivedMessagesList);
        
        JSplitPane sendMessagesPane = new JSplitPane();
        sendMessagesPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        sendMessagesPane.setDividerSize(0);
        messagesPane.setRightComponent(sendMessagesPane);
        
        JLabel sentMessagesLabel = new JLabel("  Sent messages (Waiting for answer)");
        sentMessagesLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        sentMessagesLabel.setBorder(new EmptyBorder(3, 0, 3, 0));
        sendMessagesPane.setLeftComponent(sentMessagesLabel);
        
        JScrollPane sentMessagesScrollPane = new JScrollPane();
        sentMessagesScrollPane.setBorder(new LineBorder(new Color(192, 192, 192)));
        sendMessagesPane.setRightComponent(sentMessagesScrollPane);
        sentMessagesScrollPane.setViewportView(sentMessagesList);
        

        // **************************
        // ****** CALENDAR TAB ******
        // **************************
        
        JPanel calendarTab = new JPanel(new BorderLayout());
        tabbedPane.addTab("Calendar", calendarTab);
        
        // Add the CalendarView component to the center of the JPanel
        calendarTab.add(calendarView, BorderLayout.CENTER);
        takeLessonTab.setLayout(new GridLayout(0, 1, 0, 0));
        
        JSplitPane pane0 = new JSplitPane();
        pane0.setDividerSize(0);
        pane0.setBackground(new Color(255, 255, 255));
        pane0.setEnabled(false);
        pane0.setOrientation(JSplitPane.VERTICAL_SPLIT);
        takeLessonTab.add(pane0);
        
        JSplitPane pane0_1 = new JSplitPane();
        pane0_1.setDividerSize(0);
        pane0_1.setEnabled(false);
        pane0_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pane0.setRightComponent(pane0_1);
        
        JSplitPane pane0_1_1 = new JSplitPane();
        pane0_1_1.setDividerSize(0);
        pane0_1_1.setEnabled(false);
        pane0_1.setLeftComponent(pane0_1_1);
        
        JLabel lessonTypeLab = new JLabel("  Lesson Type                         ");
        lessonTypeLab.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lessonTypeLab.setAlignmentX(0.5f);
        pane0_1_1.setLeftComponent(lessonTypeLab);
        
        lessonTypeComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
        
        
        
        lessonTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	boolean isEmpty = true;
            	String tutorToCheck;
            	boolean isContained = false;

            	int i, j, k;
            	String tutorId;
            	String[][] tutorsList = null;
				try {
					tutorsList = Main.getClientManager().getTutorsList();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				
                // Refresh the available lessons
                try {
					Main.setAvailableLessons(Main.getClientManager().findAvailableLessons());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
                
            	String[][] availableLessons = Main.getAvailableLessons();
                String lessonType = (String) lessonTypeComboBox.getSelectedItem();
                
                // Remove of the tutors
                availableTutorsComboBox.removeAllItems();
                
                for(i = 0; i < availableLessons.length ; i++) {
                	tutorId = availableLessons[i][7];
                	
                    for (j = 0; j < tutorsList.length; j++) {
                    	isContained = false;
                        if (tutorId.equals(tutorsList[j][4]) && availableLessons[i][4].equals(lessonType)) { 
                        	if(isEmpty) {
                        		availableTutorsComboBox.addItem("Choose a Tutor");
                            	isEmpty = false;
                        	}
                        	for (k = 0; k < availableTutorsComboBox.getItemCount(); k++) {
                        	    tutorToCheck = (String) availableTutorsComboBox.getItemAt(k);
                        	    if (tutorToCheck.equals(tutorsList[j][0] + " " + tutorsList[j][1])) {
                        	        isContained = true;
                        	        break;
                        	    }
                        	}
                        	if(!isContained) {
                                // Add tutor name to the combo box
                            	availableTutorsComboBox.addItem(tutorsList[j][0] + " " + tutorsList[j][1]);
                                break; // Break to avoid adding the same tutor multiple times
                        	}
                        }
                        
                    }
                }
                if(isEmpty) {
                	availableTutorsComboBox.addItem("No Tutor available");
                }

            }
        });
        
        
        pane0_1_1.setBorder(new EmptyBorder(2, 2, 2, 20));
        
        pane0_1_1.setRightComponent(lessonTypeComboBox);
        
        JSplitPane pane0_1_2 = new JSplitPane();
        pane0_1_2.setDividerSize(0);
        pane0_1_2.setEnabled(false);
        pane0_1_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pane0_1.setRightComponent(pane0_1_2);
        

        
        
        JSplitPane pane0_1_2_1_1 = new JSplitPane();
        pane0_1_2_1_1.setDividerSize(0);
        pane0_1_2_1_1.setEnabled(false);
        pane0_1_2_1_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        
        
        JSplitPane pane0_1_2_1_2 = new JSplitPane();
        pane0_1_2_1_2.setDividerSize(0);
        pane0_1_2_1_2.setEnabled(false);
        pane0_1_2_1_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pane0_1_2_1_1.setRightComponent(pane0_1_2_1_2);
        
        
        JPanel pane0_1_2_1_1_2 = new JPanel();
        pane0_1_2.setRightComponent(pane0_1_2_1_1_2);
        //pane0_1_2_1_2.setRightComponent(pane0_1_2_1_1_2);
        pane0_1_2_1_1_2.setLayout(new BorderLayout(0, 0));
        
        JScrollPane lessonScrollPane = new JScrollPane();
        lessonScrollPane.setBorder(new LineBorder(new Color(192, 192, 192)));
        lessonScrollPane.setViewportView(availableLessonsList);
        pane0_1_2_1_1_2.add(lessonScrollPane, BorderLayout.CENTER);
        
        
        JSplitPane pane0_1_2_2 = new JSplitPane();
        pane0_1_2_2.setDividerSize(0);
        pane0_1_2_2.setEnabled(false);
        pane0_1_2.setLeftComponent(pane0_1_2_2);
        
        JLabel availableTutorsLabel = new JLabel("  Available Tutors                    ");
        availableTutorsLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        availableTutorsLabel.setAlignmentX(0.5f);
        pane0_1_2_2.setBorder(new EmptyBorder(2, 2, 5, 20));
        pane0_1_2_2.setLeftComponent(availableTutorsLabel);
        
        availableTutorsComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
        pane0_1_2_2.setRightComponent(availableTutorsComboBox);
        
        availableTutorsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	availableLessonsList.clearSelection();
            	String[][] tutorsList = null;
            	String tutorName;
            	String[] tutor = null;
            	String[][] availableLessons = Main.getAvailableLessons();
            	boolean isGustave = true;
            	String cost;
            	int i;
            	String lessonId;
            	String lessonToAdd;
            	String lessonType = (String) lessonTypeComboBox.getSelectedItem();
            	
				try {
					tutorsList = Main.getClientManager().getTutorsList();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				
				if(Main.getAccountInformations()[4].equals("no")) {
					isGustave = false;
				}
				
				
            	String lineSelected = (String) availableTutorsComboBox.getSelectedItem();
            	
            	if(lineSelected != null) {


                	
                	if(!lineSelected.equals("No Tutor available") && !lineSelected.equals("Choose a Tutor"))
                	{
                		// Retrieve the tutors
                    	for(i = 0; i < tutorsList.length; i++) {
                    		tutorName = tutorsList[i][0]+" "+tutorsList[i][1];
                    		if(tutorName.equals(lineSelected)) {
                    			tutor = tutorsList[i];
                    			break;
                    		}
                    	}
                    	lessonsModel.removeAllElements();
                    	
                    	lessonMap = new HashMap<>();
                    	
                    	for(i = 0; i < availableLessons.length; i++) {
                    		if(availableLessons[i][7].equals(tutor[4]) && availableLessons[i][4].equals(lessonType)) {
                		        //defaultListModel.addElement("test");
                    			if(!isGustave) {
                    				cost = "                           COST:   " + availableLessons[i][5];
                    			} else {
                    				cost = "";
                    			}
                    			lessonToAdd = "DATE:   "+ availableLessons[i][0] +"                           TIME:   "+availableLessons[i][1] + " - " + availableLessons[i][2] +
                   					 "                           ONLINE:   " + availableLessons[i][3].toUpperCase() + cost + "                           ID:   "+availableLessons[i][8];
                    			lessonsModel.addElement(lessonToAdd);
                    			lessonId = availableLessons[i][8];
                                lessonMap.put(lessonToAdd, lessonId); // Associating lessonToAdd with lessonId
                    		}
                    	}
                	} else {
                		lessonsModel.removeAllElements();
                		lessonsModel.addElement("No Tutor selected");
                	}
            		
            		
            		
            	} else {

            	}
            	

            	 

            	
            	
            	
            	
         

            }
        });
        

        
        
        
        JSplitPane pane0_2 = new JSplitPane();
        pane0_2.setDividerSize(0);
        pane0_2.setEnabled(false);
        pane0_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pane0.setLeftComponent(pane0_2);
        
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(new Color(255, 255, 255));

        JLabel imageLabel = new JLabel("");
        imageLabel.setIcon(new ImageIcon(StudentPanel.class.getResource("/assets/img/gustave_tutoring_solution.png")));
        imageLabel.setForeground(new Color(192, 192, 192));
        imageLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

        imageLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        imagePanel.add(imageLabel);

        pane0_2.setLeftComponent(imagePanel);
        
        JSplitPane splitPane_12 = new JSplitPane();
        splitPane_12.setEnabled(false);
        splitPane_12.setDividerSize(0);
        splitPane_12.setResizeWeight(0.5);
        pane0_2.setRightComponent(splitPane_12);
        
        JLabel takeALessonLab = new JLabel(" Take a Lesson");
        takeALessonLab.setFont(new Font("Tahoma", Font.PLAIN, 20));
        splitPane_12.setLeftComponent(takeALessonLab);
        
        JButton refreshButton = new JButton("Refresh Available Lessons");
        refreshButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
        
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Main.refreshData();
            }
        });

        splitPane_12.setBorder(new EmptyBorder(5, 10, 10, 20));
        splitPane_12.setRightComponent(refreshButton);
        
        
        
        // ****************************************
        // ******* TO MANAGE THE RESEARCHES *******
        // ****************************************

        
        // ****************************************
        // ******* LISTENERS OF THE PROGRAM *******
        // ****************************************
        



        
        
        
        
        
        
       this.add(tabbedPane, BorderLayout.CENTER);

       

        

		
		
		
	}
	

	// =============================================
    // ====== LOAD THE LESSONS OF THE ACCOUNT ======
	// =============================================
	public static void loadMessages() throws RemoteException {
	    messageMap = new HashMap<>();
	    String userId = CSVManager.getAccountId(Main.getAccountType(), Main.getAccountInformations());
	    String[][] messages = Main.getAccountMessages();
	    DefaultListModel<String> receivedModel = (DefaultListModel<String>) receivedMessagesList.getModel();
	    DefaultListModel<String> sentModel = (DefaultListModel<String>) sentMessagesList.getModel();
	    String[] tutorInfos;
	    String tutorName;
	    String[] lessonInfos;
	    String messageId;
	    int i;

	    // Removing all the messages from the lists
	    receivedModel.removeAllElements();
	    sentModel.removeAllElements();

	    // Adding the messages to the lists
	    for (i = 0; i < messages.length; i++) {
	        // Retrieving names of tutor
	        tutorInfos = Main.getClientManager().getInfos(messages[i][4], "tutor");
	        tutorName = tutorInfos[0] + " " + tutorInfos[1];
	        // Retrieving informations of lesson
	        lessonInfos = Main.getClientManager().getInfos(messages[i][5], "lesson");

	        messageId = messages[i][6];

	        // If the message is waiting for an answer
	        if (messages[i][0].equals("yes")) {
	            // If the message's answer is pending
	            if (messages[i][1].equals("pending")) {
	                String messageToAdd = "Request to " + tutorName + " for a " + lessonInfos[4] + " lesson on " + lessonInfos[0] + " from "
	                        + lessonInfos[1] + " to " + lessonInfos[2];
	                sentModel.addElement(messageToAdd);
	                messageMap.put(messageToAdd, messageId); // Associating messageToAdd with messageId
	            }
	            // If the message answer is received
	            else {
	                String messageToAdd;
	                if (messages[i][1].equals("yes")) {
	                    messageToAdd = tutorName + " has confirmed the booking of your lesson (" + lessonInfos[0] + " from " + lessonInfos[1] + " to " + lessonInfos[2] + ")";
	                } else {
	                    messageToAdd = tutorName + " has refused the booking of your lesson (" + lessonInfos[0] + "  " + lessonInfos[1] + " to " + lessonInfos[2] + ")";
	                }
	                receivedModel.addElement(messageToAdd);
	                messageMap.put(messageToAdd, messageId); // Associating messageToAdd with messageId
	            }
	        }
	        // If the message is not waiting for an answer
	        else if (messages[i][2].equals(userId)) {
	            String messageToAdd = tutorName + " has canceled your lesson (" + lessonInfos[0] + "  " + lessonInfos[1] + "-" + lessonInfos[2] + ")";
	            receivedModel.addElement(messageToAdd);
	            messageMap.put(messageToAdd, messageId); // Associating messageToAdd with messageId
	        }
	    }
	}

	
	
	
	
	
	
	
	
	
	


}
