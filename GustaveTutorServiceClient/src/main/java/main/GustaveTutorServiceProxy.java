package main;

public class GustaveTutorServiceProxy implements main.GustaveTutorService {
  private String _endpoint = null;
  private main.GustaveTutorService gustaveTutorService = null;
  
  public GustaveTutorServiceProxy() {
    _initGustaveTutorServiceProxy();
  }
  
  public GustaveTutorServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initGustaveTutorServiceProxy();
  }
  
  private void _initGustaveTutorServiceProxy() {
    try {
      gustaveTutorService = (new main.GustaveTutorServiceServiceLocator()).getGustaveTutorService();
      if (gustaveTutorService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)gustaveTutorService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)gustaveTutorService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (gustaveTutorService != null)
      ((javax.xml.rpc.Stub)gustaveTutorService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public main.GustaveTutorService getGustaveTutorService() {
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService;
  }
  
  public double conversion(double sum, java.lang.String currency1, java.lang.String currency2) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService.conversion(sum, currency1, currency2);
  }
  
  public void createAccount(java.lang.String email, java.lang.String name, java.lang.String surname, java.lang.String password) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.createAccount(email, name, surname, password);
  }
  
  public boolean isEmailUsed(java.lang.String email) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService.isEmailUsed(email);
  }
  
  public void addFunds(java.lang.String studentId, java.lang.String addedValue) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.addFunds(studentId, addedValue);
  }
  
  public void deleteMessage(java.lang.String messageId, java.lang.String token, java.lang.String accType, java.lang.String[] accInfos) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.deleteMessage(messageId, token, accType, accInfos);
  }
  
  public void bookLesson(java.lang.String studentId, java.lang.String lessonId, java.lang.String token) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.bookLesson(studentId, lessonId, token);
  }
  
  public java.lang.String[][] getTutorsList() throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService.getTutorsList();
  }
  
  public java.lang.String[] isLoginTrue(java.lang.String email, java.lang.String password, java.lang.String userType) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService.isLoginTrue(email, password, userType);
  }
  
  public void answerBooking(java.lang.String tutorId, java.lang.String studentId, java.lang.String lessonId, java.lang.String messageId, java.lang.String token, int yesOrNo) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.answerBooking(tutorId, studentId, lessonId, messageId, token, yesOrNo);
  }
  
  public java.lang.String[] getInfos(java.lang.String id, java.lang.String dataToFind) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService.getInfos(id, dataToFind);
  }
  
  public void createLesson(java.lang.String userId, java.lang.String[] lessonData, java.lang.String token) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.createLesson(userId, lessonData, token);
  }
  
  public void deleteLesson(java.lang.String lessonID, java.lang.String[] accountInformations, java.lang.String accountType, java.lang.String token) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.deleteLesson(lessonID, accountInformations, accountType, token);
  }
  
  public java.lang.String[][] findRowsUser(java.lang.String userId, java.lang.String filePath, java.lang.String accType) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService.findRowsUser(userId, filePath, accType);
  }
  
  public void subtractFunds(java.lang.String studentId, java.lang.String subtractedValue) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.subtractFunds(studentId, subtractedValue);
  }
  
  public boolean startService() throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService.startService();
  }
  
  public void cancelBookingRequest(java.lang.String lessonId) throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    gustaveTutorService.cancelBookingRequest(lessonId);
  }
  
  public java.lang.String[][] findAvailableLessons() throws java.rmi.RemoteException{
    if (gustaveTutorService == null)
      _initGustaveTutorServiceProxy();
    return gustaveTutorService.findAvailableLessons();
  }
  
  
}