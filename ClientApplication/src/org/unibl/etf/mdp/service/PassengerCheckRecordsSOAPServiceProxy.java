package org.unibl.etf.mdp.service;

public class PassengerCheckRecordsSOAPServiceProxy implements org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPService {
  private String _endpoint = null;
  private org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPService passengerCheckRecordsSOAPService = null;
  
  public PassengerCheckRecordsSOAPServiceProxy() {
    _initPassengerCheckRecordsSOAPServiceProxy();
  }
  
  public PassengerCheckRecordsSOAPServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initPassengerCheckRecordsSOAPServiceProxy();
  }
  
  private void _initPassengerCheckRecordsSOAPServiceProxy() {
    try {
      passengerCheckRecordsSOAPService = (new org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPServiceServiceLocator()).getPassengerCheckRecordsSOAPService();
      if (passengerCheckRecordsSOAPService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)passengerCheckRecordsSOAPService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)passengerCheckRecordsSOAPService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (passengerCheckRecordsSOAPService != null)
      ((javax.xml.rpc.Stub)passengerCheckRecordsSOAPService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPService getPassengerCheckRecordsSOAPService() {
    if (passengerCheckRecordsSOAPService == null)
      _initPassengerCheckRecordsSOAPServiceProxy();
    return passengerCheckRecordsSOAPService;
  }
  
  public void addOnWarantLog(java.lang.String personID) throws java.rmi.RemoteException{
    if (passengerCheckRecordsSOAPService == null)
      _initPassengerCheckRecordsSOAPServiceProxy();
    passengerCheckRecordsSOAPService.addOnWarantLog(personID);
  }
  
  public void addPassenger(java.lang.String id) throws java.rmi.RemoteException{
    if (passengerCheckRecordsSOAPService == null)
      _initPassengerCheckRecordsSOAPServiceProxy();
    passengerCheckRecordsSOAPService.addPassenger(id);
  }
  
  public byte[] getPassengerRecords() throws java.rmi.RemoteException{
    if (passengerCheckRecordsSOAPService == null)
      _initPassengerCheckRecordsSOAPServiceProxy();
    return passengerCheckRecordsSOAPService.getPassengerRecords();
  }
  
  
}