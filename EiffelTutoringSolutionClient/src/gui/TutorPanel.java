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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import managers.CSVManager;

import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.JRadioButton;
import com.github.lgooddatepicker.components.TimePicker;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;

// To avoid the message "The serializable class TutorPanel does not declare a static final serialVersionUID field of type long".
// This class doesn't have to be serializable, it won't be transmitted to another JVM.
@SuppressWarnings("serial")

public class TutorPanel extends JPanel {
	
    // ============================
    // ===== GLOBAL VARIABLES =====
    // ============================
	
	private static String[] lessonTypes = {"Mathematics", "History", "Python", "Geography", "French", "Java", "English"}; // Existing Lesson Types
    private static JComboBox<String> lessonTypeComboBox = new JComboBox<>(lessonTypes); // Used to select lesson type
    
    private static DefaultListModel<String> receivedMessagesListModel = new DefaultListModel<>(); // Model of the JList displaying received messages
    private static JList<String> receivedMessagesList = new JList<>(receivedMessagesListModel); // It displays the received messages
    
    private static Map<String, String> messageMap = new HashMap<>();
    
    public static CalendarView calendarView = new CalendarView(); // Generate a calendar showing booked lessons
	private JTextField dateField;
	private JTextField costField;

    // ===============================
    // ===== GETTERS AND SETTERS =====
    // ===============================

	public JComboBox<String> getLessonTypeComboBox() {
		return lessonTypeComboBox;
	}
	public static CalendarView getCalendarView() {
		return calendarView;
	}
	public static void setCalendarView(CalendarView calendarView) {
		TutorPanel.calendarView = calendarView;
	}
	
    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
	
	public TutorPanel() {
        
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
                	String[][] messages = Main.getAccountMessages();
                    String selectedItem = receivedMessagesList.getSelectedValue();

                    // Retrieve the messageId associated with the selected message from the map
                    String messageId = messageMap.get(selectedItem);
                    String studentId = null;
                    String lessonId = null;
                    
                    for(int i = 0; i < messages.length; i++) {
                    	if(messages[i][6].equals(messageId)) {
                    		studentId = messages[i][3];
                    		lessonId = messages[i][5];
                    	}
                    }

                    // If selectedItem contains "wants", it means it's a message for a lesson request
                    if (selectedItem.contains("wants")) {
                        
                    	// The user is asked if he accepts the lesson booking by the student
                        int result = JOptionPane.showConfirmDialog(null, "Accept lesson booking for student", "Booking validation", JOptionPane.YES_NO_OPTION);
                        if(result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
                            int choiceConfirmation = JOptionPane.showConfirmDialog(null, "Are you sure of this choice?", "Choice confirmation", JOptionPane.YES_NO_OPTION);
                            if(choiceConfirmation == JOptionPane.YES_OPTION) {
        						try {
        							Main.getClientManager().answerBooking(Main.getAccountId(), studentId, lessonId, messageId, Main.getToken(), result);
        						} catch (IOException e1) {
        							e1.printStackTrace();
        						}
        						Main.refreshData();
                            }
                        }
                    } else {
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
        

        
        
        JSplitPane receivedMessagesPane = new JSplitPane();
        receivedMessagesPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        receivedMessagesPane.setDividerSize(0);
        messagesTopAndBottomPanel.setRightComponent(receivedMessagesPane);
        
        JLabel receivedMessagesLabel = new JLabel("  Received Messages");
        receivedMessagesLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        
        receivedMessagesLabel.setBorder(new EmptyBorder(3, 0, 3, 0));
        receivedMessagesPane.setLeftComponent(receivedMessagesLabel);
        
        JScrollPane receivedMessagesScrollPane = new JScrollPane();
        receivedMessagesScrollPane.setBorder(new LineBorder(new Color(192, 192, 192)));
        receivedMessagesPane.setRightComponent(receivedMessagesScrollPane);
        

        receivedMessagesScrollPane.setViewportView(receivedMessagesList);
        

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
        
        
        
        
        
        pane0_1_1.setBorder(new EmptyBorder(2, 2, 2, 20));
        
        JLabel startTimeLabel = new JLabel("  Start Time                           ");
        pane0_1_1.setLeftComponent(startTimeLabel);
        startTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        startTimeLabel.setAlignmentX(0.5f);
        
        TimePicker startTimePicker = new TimePicker();
        pane0_1_1.setRightComponent(startTimePicker);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
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
        
        
        JSplitPane pane0_1_2_2 = new JSplitPane();
        pane0_1_2_2.setDividerSize(0);
        pane0_1_2_2.setEnabled(false);
        pane0_1_2.setLeftComponent(pane0_1_2_2);
        pane0_1_2_2.setBorder(new EmptyBorder(2, 2, 5, 20));
        
        TimePicker endTimePicker = new TimePicker();
        pane0_1_2_2.setRightComponent(endTimePicker);
        
        JLabel endTimeLabel = new JLabel("  End Time                             ");
        pane0_1_2_2.setLeftComponent(endTimeLabel);
        endTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        endTimeLabel.setAlignmentX(0.5f);
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pane0_1_2.setRightComponent(splitPane);
        
        JSplitPane pane0_1_2_2_1 = new JSplitPane();
        pane0_1_2_2_1.setEnabled(false);
        pane0_1_2_2_1.setDividerSize(0);
        pane0_1_2_2_1.setBorder(new EmptyBorder(2, 2, 5, 20));
        splitPane.setLeftComponent(pane0_1_2_2_1);
        
        JLabel lessonTypeLabel = new JLabel("  Lesson Type                        ");
        pane0_1_2_2_1.setLeftComponent(lessonTypeLabel);
        lessonTypeLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lessonTypeLabel.setAlignmentX(0.5f);
        pane0_1_2_2_1.setRightComponent(lessonTypeComboBox);
        
        lessonTypeComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
        
        JSplitPane splitPane_1 = new JSplitPane();
        splitPane_1.setEnabled(false);
        splitPane_1.setDividerSize(0);
        splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setRightComponent(splitPane_1);
        
        JSplitPane splitPane_2 = new JSplitPane();
        splitPane_2.setEnabled(false);
        splitPane_2.setDividerSize(0);
        splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane_1.setRightComponent(splitPane_2);
        
        JSplitPane pane0_1_2_2_1_1_1 = new JSplitPane();
        pane0_1_2_2_1_1_1.setResizeWeight(0.5);
        pane0_1_2_2_1_1_1.setEnabled(false);
        pane0_1_2_2_1_1_1.setDividerSize(0);
        pane0_1_2_2_1_1_1.setBorder(new EmptyBorder(2, 2, 5, 20));
        splitPane_2.setLeftComponent(pane0_1_2_2_1_1_1);
        
        JLabel isOnlineLabel = new JLabel("  Online");
        pane0_1_2_2_1_1_1.setLeftComponent(isOnlineLabel);
        isOnlineLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        isOnlineLabel.setAlignmentX(0.5f);
        
        JSplitPane isOnlineButtonGroup = new JSplitPane();
        
        
        
        
        JRadioButton isOnlineNoRadio = new JRadioButton("No");
        isOnlineButtonGroup.setRightComponent(isOnlineNoRadio);

        JRadioButton isOnlineYesRadio = new JRadioButton("Yes");
        isOnlineButtonGroup.setLeftComponent(isOnlineYesRadio);

        // Create a ButtonGroup to ensure only one radio button is selected at a time
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(isOnlineNoRadio);
        buttonGroup.add(isOnlineYesRadio);

        // Set "No" as the default selection
        isOnlineNoRadio.setSelected(true);
        
        
        
        
        
        
        pane0_1_2_2_1_1_1.setRightComponent(isOnlineButtonGroup);
        isOnlineButtonGroup.setDividerSize(0);
        isOnlineButtonGroup.setResizeWeight(0.5);
        
        JButton addLessonButton = new JButton("Add Lesson");
        
        addLessonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Are the lesson's parameters valid?
                boolean isValid = true;
                String error = "";

                // Retrieving current date
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String currentDate = currentDateTime.format(formatter);

                // Retrieving date of lesson
                String lessonDateString = dateField.getText();

                // Retrieving start time and end time
                String startTime = startTimePicker.getText();
                String endTime = endTimePicker.getText();

                String lessonType = (String) lessonTypeComboBox.getSelectedItem();

                // Retrieving cost
                String cost = costField.getText();

                // Retrieving if the lesson is online or not
                String isOnline = isOnlineNoRadio.isSelected() ? "yes" : "no";

                // Checking date format
                if (lessonDateString.matches("^\\d{4}-\\d{2}-\\d{2}$") || lessonDateString.matches("^\\d{4}-\\d{1}-\\d{2}$")) {
                    LocalDate lessonDate = LocalDate.parse(lessonDateString);

                    // If the lesson date is before the current date
                    if (lessonDate.isBefore(LocalDate.parse(currentDate))) {
                        isValid = false;
                        error = "The date is in the past";
                    }
                } else {
                    isValid = false;
                    error = "Invalid date format";
                }

                // Checking start time and end time format
                if (!startTime.matches("\\d{2}:\\d{2}") || !endTime.matches("\\d{2}:\\d{2}")) {
                    isValid = false;
                    error = "Invalid Start Time or/and End Time format";
                } else {
                    // Checking if startTime is before or equal to endTime
                    LocalTime start = LocalTime.parse(startTime);
                    LocalTime end = LocalTime.parse(endTime);
                    if (start.isAfter(end)) {
                        isValid = false;
                        error = "Start Time cannot be after End Time";
                    }
                }

                // Checking cost format
                if (!CSVManager.isNumeric(cost)) {
                    isValid = false;
                    error = "Cost must be numerical";
                }

                // If all the parameters of the lesson are valid
                if (isValid) {
                    String[] lessonData = {lessonDateString, startTime, endTime, lessonType, cost, isOnline};

                    // Retrieving account id
                    String accountId = CSVManager.getAccountId(Main.getAccountType(), Main.getAccountInformations());

                    try {
                        // Creating the lesson
                        Main.getClientManager().createLesson(accountId, lessonData, Main.getToken());
                        dateField.setText("");
                        startTimePicker.setText("");
                        endTimePicker.setText("");
                        costField.setText("");
                        Main.refreshData();
                        // Lesson creation done
                        JOptionPane.showMessageDialog(null, "This Lesson has been validated", "Lesson Added", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    // Lesson creation has failed
                    JOptionPane.showMessageDialog(null, "This Lesson cannot be validated: " + error, "Lesson wasn't added", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        // public static void  throws IOException;
        
        splitPane_2.setRightComponent(addLessonButton);
        
        JSplitPane pane0_1_2_2_1_1 = new JSplitPane();
        pane0_1_2_2_1_1.setEnabled(false);
        pane0_1_2_2_1_1.setDividerSize(0);
        pane0_1_2_2_1_1.setBorder(new EmptyBorder(2, 2, 5, 20));
        splitPane_1.setLeftComponent(pane0_1_2_2_1_1);
        
        JLabel costLabel = new JLabel("  Cost                                   ");
        pane0_1_2_2_1_1.setLeftComponent(costLabel);
        costLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        costLabel.setAlignmentX(0.5f);
        
        costField = new JTextField();
        pane0_1_2_2_1_1.setRightComponent(costField);
        costField.setColumns(10);
        

        
        
        
        JSplitPane pane0_2 = new JSplitPane();
        pane0_2.setDividerSize(0);
        pane0_2.setEnabled(false);
        pane0_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pane0.setLeftComponent(pane0_2);
        
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(new Color(255, 255, 255));

        JLabel imageLabel = new JLabel("");
        imageLabel.setIcon(new ImageIcon(TutorPanel.class.getResource("/assets/img/gustave_tutoring_solution.png")));
        imageLabel.setForeground(new Color(192, 192, 192));
        imageLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

        imageLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        imagePanel.add(imageLabel);

        pane0_2.setLeftComponent(imagePanel);
        
        JSplitPane pane0_1_1_1 = new JSplitPane();
        pane0_1_1_1.setEnabled(false);
        pane0_1_1_1.setDividerSize(0);
        pane0_1_1_1.setBorder(new EmptyBorder(2, 2, 2, 20));
        pane0_2.setRightComponent(pane0_1_1_1);
        
        JLabel dateLabel = new JLabel("  Enter the Date                      ");
        dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        dateLabel.setAlignmentX(0.5f);
        pane0_1_1_1.setLeftComponent(dateLabel);
        
        dateField = new JTextField();
        pane0_1_1_1.setRightComponent(dateField);
        dateField.setColumns(10);
        
        DateChooser dateChooser = new DateChooser();
        dateChooser.toDay();
        
        dateChooser.addEventDateChooser(new EventDateChooser() {
            @Override
            public void dateSelected(SelectedAction action, SelectedDate date) {
                if (action.getAction() == SelectedAction.DAY_SELECTED) {
                	
                	int day = date.getDay();
                	String dayStr =  String.valueOf(day);
                	
                	if(date.getDay() < 10) {
                		dayStr = 0+dayStr;
                	}
                	
                	int month = date.getMonth();
                	String monthStr =  String.valueOf(month);
                	
                	if(date.getMonth() < 10) {
                		monthStr = 0+monthStr;
                	}
                	
                	dateField.setText(date.getYear() + "-" + monthStr + "-" + dayStr);
                    dateChooser.hidePopup();
                }
            }
        });
        
        dateField.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		dateChooser.showPopup(dateField, 65, 30);
        	}
        });
        
        
        
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
    	String userId = CSVManager.getAccountId(Main.getAccountType(), Main.getAccountInformations());			// Id of the user
    	String[][] messages = Main.getAccountMessages(); 														// Retrieve messages of the account
    	
    	String messageToAdd; 																					// Informations of the message
		DefaultListModel<String> receivedModel = (DefaultListModel<String>) receivedMessagesList.getModel();	// Model for received messages list
		String studentName;																						// Name of a tutor
		String[] lessonInfos;																					// Informations of a lesson
		String[] studentInfos;
		String messageId;
		int i;
		
		// Removing all the messages from the list
		receivedModel.removeAllElements();

		// Adding the messages to the lists
		for(i = 0; i < messages.length; i++) {
			
			// Retrieving names of student
			studentInfos = Main.getClientManager().getInfos(messages[i][3], "student");
			studentName = studentInfos[0] + " " + studentInfos[1];
			// Retrieving informations of lesson
			lessonInfos = Main.getClientManager().getInfos(messages[i][5], "lesson");
			
			messageId = messages[i][6];
			
			// If the message is waiting for answer
			if(messages[i][0].equals("yes")) {
				// If the message's answer is pending
				if(messages[i][1].equals("pending")) {
					
					// Adding the message to the proper list
					if(messages[i][2].equals(userId)) { // index 2 is the column of the recipient id
						messageToAdd = "Student " + studentName + " wants to book your lesson: " + lessonInfos[0] + " from " 
								+ lessonInfos[1] + " to "+ lessonInfos[2];
						receivedModel.addElement(messageToAdd);
						messageMap.put(messageToAdd, messageId); // Associating messageToAdd with messageId
					}
				}
			}
			// Message not waiting for answer
			else if(messages[i][2].equals(userId)) {
				messageToAdd = studentName + " has canceled your lesson ("+ lessonInfos[0] + "  " + lessonInfos[1] + "-" + lessonInfos[2] + ")";
				receivedModel.addElement(messageToAdd);
				messageMap.put(messageToAdd, messageId); // Associating messageToAdd with messageId
			}

		}
    }
    
}
