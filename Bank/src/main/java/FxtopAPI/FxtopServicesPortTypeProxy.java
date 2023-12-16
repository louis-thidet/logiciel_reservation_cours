package FxtopAPI;

public class FxtopServicesPortTypeProxy implements FxtopAPI.FxtopServicesPortType {
  private String _endpoint = null;
  private FxtopAPI.FxtopServicesPortType fxtopServicesPortType = null;
  
  public FxtopServicesPortTypeProxy() {
    _initFxtopServicesPortTypeProxy();
  }
  
  public FxtopServicesPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initFxtopServicesPortTypeProxy();
  }
  
  private void _initFxtopServicesPortTypeProxy() {
    try {
      fxtopServicesPortType = (new FxtopAPI.FxtopServicesLocator()).getFxtopServicesPort();
      if (fxtopServicesPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)fxtopServicesPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)fxtopServicesPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (fxtopServicesPortType != null)
      ((javax.xml.rpc.Stub)fxtopServicesPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public FxtopAPI.FxtopServicesPortType getFxtopServicesPortType() {
    if (fxtopServicesPortType == null)
      _initFxtopServicesPortTypeProxy();
    return fxtopServicesPortType;
  }
  
  public FxtopAPI.ConvertResult convert(java.lang.String originalAmount, java.lang.String c1, java.lang.String c2, java.lang.String date, java.lang.String user, java.lang.String password) throws java.rmi.RemoteException{
    if (fxtopServicesPortType == null)
      _initFxtopServicesPortTypeProxy();
    return fxtopServicesPortType.convert(originalAmount, c1, c2, date, user, password);
  }
  
  public FxtopAPI.CurrencyDescription descCurrency(java.lang.String lang, java.lang.String isocode) throws java.rmi.RemoteException{
    if (fxtopServicesPortType == null)
      _initFxtopServicesPortTypeProxy();
    return fxtopServicesPortType.descCurrency(lang, isocode);
  }
  
  public java.lang.String listCurrencies(java.lang.String user, java.lang.String password) throws java.rmi.RemoteException{
    if (fxtopServicesPortType == null)
      _initFxtopServicesPortTypeProxy();
    return fxtopServicesPortType.listCurrencies(user, password);
  }
  
  
}