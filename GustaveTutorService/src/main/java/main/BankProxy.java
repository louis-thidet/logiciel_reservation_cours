package main;

public class BankProxy implements main.Bank {
  private String _endpoint = null;
  private main.Bank bank = null;
  
  public BankProxy() {
    _initBankProxy();
  }
  
  public BankProxy(String endpoint) {
    _endpoint = endpoint;
    _initBankProxy();
  }
  
  private void _initBankProxy() {
    try {
      bank = (new main.BankServiceLocator()).getBank();
      if (bank != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)bank)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)bank)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (bank != null)
      ((javax.xml.rpc.Stub)bank)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public main.Bank getBank() {
    if (bank == null)
      _initBankProxy();
    return bank;
  }
  
  public double test() throws java.rmi.RemoteException{
    if (bank == null)
      _initBankProxy();
    return bank.test();
  }
  
  public double conversion(double sum, java.lang.String currency1, java.lang.String currency2) throws java.rmi.RemoteException{
    if (bank == null)
      _initBankProxy();
    return bank.conversion(sum, currency1, currency2);
  }
  
  
}