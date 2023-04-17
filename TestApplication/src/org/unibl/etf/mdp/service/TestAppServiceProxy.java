package org.unibl.etf.mdp.service;

public class TestAppServiceProxy implements org.unibl.etf.mdp.service.TestAppService {
  private String _endpoint = null;
  private org.unibl.etf.mdp.service.TestAppService testAppService = null;
  
  public TestAppServiceProxy() {
    _initTestAppServiceProxy();
  }
  
  public TestAppServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initTestAppServiceProxy();
  }
  
  private void _initTestAppServiceProxy() {
    try {
      testAppService = (new org.unibl.etf.mdp.service.TestAppServiceServiceLocator()).getTestAppService();
      if (testAppService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)testAppService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)testAppService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (testAppService != null)
      ((javax.xml.rpc.Stub)testAppService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.unibl.etf.mdp.service.TestAppService getTestAppService() {
    if (testAppService == null)
      _initTestAppServiceProxy();
    return testAppService;
  }
  
  public boolean isActive(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException{
    if (testAppService == null)
      _initTestAppServiceProxy();
    return testAppService.isActive(client);
  }
  
  public void addPassengerToCustomsControl(org.unibl.etf.mdp.model.Client client, org.unibl.etf.mdp.model.Document document) throws java.rmi.RemoteException{
    if (testAppService == null)
      _initTestAppServiceProxy();
    testAppService.addPassengerToCustomsControl(client, document);
  }
  
  public boolean passengerPassedPoliceControl(java.lang.String passengerId) throws java.rmi.RemoteException{
    if (testAppService == null)
      _initTestAppServiceProxy();
    return testAppService.passengerPassedPoliceControl(passengerId);
  }
  
  public boolean passengerPassedCustomsControl(java.lang.String passengerId) throws java.rmi.RemoteException{
    if (testAppService == null)
      _initTestAppServiceProxy();
    return testAppService.passengerPassedCustomsControl(passengerId);
  }
  
  public void addPassengerToPoliceControl(org.unibl.etf.mdp.model.Client client, java.lang.String passengerId) throws java.rmi.RemoteException{
    if (testAppService == null)
      _initTestAppServiceProxy();
    testAppService.addPassengerToPoliceControl(client, passengerId);
  }
  
  
}