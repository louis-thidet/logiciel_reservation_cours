package managers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientManager extends Remote {
    
    public void connexionMessage() throws RemoteException;
    public void createAccount(String email, String password, String userType) throws IOException;
    public void createAccountWebService(String email, String name, String surname, String password) throws IOException;
    public String[] isLoginTrue(String email, String password, String userType) throws IOException, RemoteException;
    public String[] isLoginTrueWebService(String email, String password) throws IOException;
    public boolean isEmailUsed(String email) throws FileNotFoundException, IOException;
    public void deleteMessage(String messageId, String token, String accType, String[] accInfos) throws RemoteException;
    public void deleteLesson(String lessonID, String[] accountInformations, String accountType, String token) throws RemoteException, IOException;
    public String[] getInfos(String id, String dataToFind) throws RemoteException;
    public void createLesson(String userId, String[] lessonData, String token) throws IOException;
    public void bookLesson(String studentId, String lessonId, String token) throws RemoteException, IOException;
    public void cancelBookingRequest(String studentId) throws RemoteException;
    public void modifyBalance(String userId, Double value, String operationType) throws RemoteException;
    public void answerBooking(String tutorId, String studentId, String lessonId, String messageId, String token, int yesOrNo) throws RemoteException, IOException;
    public String[][] getTutorsList() throws RemoteException;
    public String[][] findAvailableLessons() throws RemoteException;
    public String[][] findRowsUser(String userId, String filePath, String accType) throws RemoteException;
    public void addFunds(String studentId, String addedValue) throws RemoteException;
    public void subtractFunds(String studentId, String subtractedValue) throws RemoteException;
}
