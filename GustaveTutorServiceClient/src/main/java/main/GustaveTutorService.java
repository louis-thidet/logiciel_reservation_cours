/**
 * GustaveTutorService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package main;

public interface GustaveTutorService extends java.rmi.Remote {
    public double conversion(double sum, java.lang.String currency1, java.lang.String currency2) throws java.rmi.RemoteException;
    public void createAccount(java.lang.String email, java.lang.String name, java.lang.String surname, java.lang.String password) throws java.rmi.RemoteException;
    public boolean isEmailUsed(java.lang.String email) throws java.rmi.RemoteException;
    public void addFunds(java.lang.String studentId, java.lang.String addedValue) throws java.rmi.RemoteException;
    public void deleteMessage(java.lang.String messageId, java.lang.String token, java.lang.String accType, java.lang.String[] accInfos) throws java.rmi.RemoteException;
    public void bookLesson(java.lang.String studentId, java.lang.String lessonId, java.lang.String token) throws java.rmi.RemoteException;
    public java.lang.String[][] getTutorsList() throws java.rmi.RemoteException;
    public java.lang.String[] isLoginTrue(java.lang.String email, java.lang.String password, java.lang.String userType) throws java.rmi.RemoteException;
    public void answerBooking(java.lang.String tutorId, java.lang.String studentId, java.lang.String lessonId, java.lang.String messageId, java.lang.String token, int yesOrNo) throws java.rmi.RemoteException;
    public java.lang.String[] getInfos(java.lang.String id, java.lang.String dataToFind) throws java.rmi.RemoteException;
    public void createLesson(java.lang.String userId, java.lang.String[] lessonData, java.lang.String token) throws java.rmi.RemoteException;
    public void deleteLesson(java.lang.String lessonID, java.lang.String[] accountInformations, java.lang.String accountType, java.lang.String token) throws java.rmi.RemoteException;
    public java.lang.String[][] findRowsUser(java.lang.String userId, java.lang.String filePath, java.lang.String accType) throws java.rmi.RemoteException;
    public void subtractFunds(java.lang.String studentId, java.lang.String subtractedValue) throws java.rmi.RemoteException;
    public boolean startService() throws java.rmi.RemoteException;
    public void cancelBookingRequest(java.lang.String lessonId) throws java.rmi.RemoteException;
    public java.lang.String[][] findAvailableLessons() throws java.rmi.RemoteException;
}
