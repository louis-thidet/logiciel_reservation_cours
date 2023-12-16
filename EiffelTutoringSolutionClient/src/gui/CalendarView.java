package gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import managers.CSVManager;
import netscape.javascript.JSObject;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.List;

//To avoid the message "The serializable class CalendarView does not declare a static final serialVersionUID field of type long"
// This class doesn't have to be serializable, it won't be transmitted to another JVM
@SuppressWarnings("serial")

public class CalendarView extends JFXPanel {
    private WebView webView;

    // We didn't find any satisfying and free library to make appear a simple calendar for our program, that's why 
    // we have decided to embed some JavaScript in it, to use the JS library FullCalendar. The JS code is all located 
    // in a file named calendar.html.
    // This class is used to give clients a clear and pleasant view  of their lessons. Each lesson they have taken is 
    // shown as an event on the calendar. Since this JS file works locally, it loads local data, that's why a csv file 
    // is produced on the client's computer.
    // The calendar is located in the "Lessons" tab of the client's GUI.
    
    // =======================
    // ===== CONSTRUCTOR =====
    // =======================
    
    public CalendarView() {
    	// This class uses JavaFX's class WebView, which creates an embedded web navigator for Java.
        Platform.runLater(() -> {
            webView = new WebView();
            setScene(new Scene(webView));
            webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                    JSObject jsObject = (JSObject) webView.getEngine().executeScript("window");
                    jsObject.setMember("calendarView", this);
                } else if (newValue == javafx.concurrent.Worker.State.FAILED) {
                    System.err.println("WebView failed to load: " + webView.getEngine().getLocation());
                }
            });
        });
    }
    
	// =============================
    // ====== LOAD A WEB PAGE ======
	// =============================
    public void loadURL(String filePath) {
        Platform.runLater(() -> {
            File file = new File(filePath);
            String fileURL;
            try {
                fileURL = file.toURI().toURL().toString();
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert file path to URL", e);
            }
            webView.getEngine().load(fileURL);
        });
    }

	// =================================================================================================
    // ====== OPEN A JOPTIONPANE WHEN CLICKING ON A LESSON ON THE CALENDAR, ALLOWING TO DELETE IT ======
	// =================================================================================================
    
    // This method is used from the JS code, each time the user clicks on one of their lesson in calendar.html.
    public void triggerDataDeletion(String id) throws IOException {
    	// When the user clicks on a lesson on it's calendar, they're asked if they want to cancel their lesson or not
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this lesson?", "Confirmation", JOptionPane.YES_NO_OPTION);
        // If the user chooses to cancel his lesson...
        if (result == JOptionPane.YES_OPTION) {
            // The type of the user's account, it's informations and it's token are gathered and entered in the deleteLesson() method
            Main.getClientManager().deleteLesson(id, Main.getAccountInformations() , Main.getAccountType(), Main.getToken());
            
            // The row is removed from the loaded lessons of the actual session
            List<String[]> accountLessons = Main.getAccountLessons();
            for (int i = 0; i < accountLessons.size(); i++) {
                String[] lesson = accountLessons.get(i);
                if (lesson.length > 8 && lesson[8].equals(id)) {
                    accountLessons.remove(i);
                    break;
                }
            }
            // A new file will be generated, without the deleted lesson, because it has been removed from the accountLessons list
            CSVManager.loadLesson();
            // The calendar is reloaded, to make the deleted lesson disappear from it
            reloadPage();
        }
    }
    
	// ============================================
    // ====== RELOAD THE CALENDARVIEW'S PAGE ======
	// ============================================
    public void reloadPage() {
        Platform.runLater(() -> webView.getEngine().reload());
    }

}
