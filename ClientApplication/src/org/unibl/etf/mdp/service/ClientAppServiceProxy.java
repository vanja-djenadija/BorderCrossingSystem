package org.unibl.etf.mdp.service;

public class ClientAppServiceProxy implements org.unibl.etf.mdp.service.ClientAppService {
  private String _endpoint = null;
  private org.unibl.etf.mdp.service.ClientAppService clientAppService = null;
  
  public ClientAppServiceProxy() {
    _initClientAppServiceProxy();
  }
  
  public ClientAppServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initClientAppServiceProxy();
  }
  
  private void _initClientAppServiceProxy() {
    try {
      clientAppService = (new org.unibl.etf.mdp.service.ClientAppServiceServiceLocator()).getClientAppService();
      if (clientAppService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)clientAppService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)clientAppService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (clientAppService != null)
      ((javax.xml.rpc.Stub)clientAppService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.unibl.etf.mdp.service.ClientAppService getClientAppService() {
    if (clientAppService == null)
      _initClientAppServiceProxy();
    return clientAppService;
  }
  
  public java.lang.String getNextPerson(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    return clientAppService.getNextPerson(client);
  }
  
  public void activateTerminal(java.lang.String terminalId) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    clientAppService.activateTerminal(terminalId);
  }
  
  public org.unibl.etf.mdp.model.Document getNextDocument(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    return clientAppService.getNextDocument(client);
  }
  
  public boolean isActive(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    return clientAppService.isActive(client);
  }
  
  public void login(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    clientAppService.login(client);
  }
  
  public void logPassengerCustomsControl(java.lang.String personId) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    clientAppService.logPassengerCustomsControl(personId);
  }
  
  public void temporarilyDeactive(java.lang.String terminalId) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    clientAppService.temporarilyDeactive(terminalId);
  }
  
  public void logPassengerPoliceControl(java.lang.String personId) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    clientAppService.logPassengerPoliceControl(personId);
  }
  
  public void logout(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException{
    if (clientAppService == null)
      _initClientAppServiceProxy();
    clientAppService.logout(client);
  }
  
  
}