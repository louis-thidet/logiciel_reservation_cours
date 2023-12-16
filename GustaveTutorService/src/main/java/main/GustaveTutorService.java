package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import managers.IClientManager;

public class GustaveTutorService implements Serializable {

	private static final long serialVersionUID = -2146512484892862246L;
	private static IClientManager clientManager;
	private static Bank bank;


	public double conversion(double sum, String currency1, String currency2) throws RemoteException, ServiceException {
		bank = new BankServiceLocator().getBank();
		return bank.conversion(sum, currency1, currency2);
	}
	
	
	public boolean startService() {
		boolean connected = false;
		try {
			// Retrieving the client manager from the RMI service
			clientManager = (IClientManager) Naming.lookup("clientManager");
            clientManager.connexionMessage();
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return connected;
	}
	
    public void createAccount(String email, String name, String surname, String password) throws IOException {
    	clientManager.createAccountWebService(email, name, surname, password);
    }
    
    public String[] isLoginTrue(String email, String password, String userType) throws IOException, RemoteException {
    	return clientManager.isLoginTrueWebService(email, password);
    }
    
    public boolean isEmailUsed(String email) throws FileNotFoundException, IOException {
    	return clientManager.isEmailUsed(email);
    }
    
    public void deleteMessage(String messageId, String token, String accType, String[] accInfos) throws RemoteException {
    	clientManager.deleteMessage(messageId, token, accType, accInfos);
    }
    
    public void deleteLesson(String lessonID, String[] accountInformations, String accountType, String token) throws RemoteException, IOException {
    	clientManager.deleteLesson(lessonID, accountInformations, accountType, token);
    }
    
    public String[] getInfos(String id, String dataToFind) throws RemoteException {
    	return clientManager.getInfos(id, dataToFind);
    }
    
    public void createLesson(String userId, String[] lessonData, String token) throws IOException {
    	clientManager.createLesson(userId, lessonData, token);
    }
    
    public void bookLesson(String studentId, String lessonId, String token) throws RemoteException, IOException {
    	clientManager.bookLesson(studentId, lessonId, token);
    }
    
    public void cancelBookingRequest(String lessonId) throws RemoteException {
    	clientManager.cancelBookingRequest(lessonId);
    }
    
    public void answerBooking(String tutorId ,String studentId, String lessonId, String messageId, String token, int yesOrNo) throws RemoteException, IOException {
    	clientManager.answerBooking(tutorId, studentId, lessonId, messageId, token, yesOrNo);
    }
    
    public String[][] getTutorsList() throws RemoteException {
    	return clientManager.getTutorsList();
    }
    
    public String[][] findAvailableLessons() throws RemoteException {
    	return clientManager.findAvailableLessons();
    }
    
    public String[][] findRowsUser(String userId, String filePath, String accType) throws RemoteException {
    	System.out.println("zdzadazdaz");
    	return clientManager.findRowsUser(userId, filePath, accType);
    }
    
    public void addFunds(String studentId, String addedValue) throws RemoteException {
    	clientManager.addFunds(studentId, addedValue);
    }
    
    public void subtractFunds(String studentId, String subtractedValue) throws RemoteException {
    	clientManager.addFunds(studentId, subtractedValue);
    }
    
}
