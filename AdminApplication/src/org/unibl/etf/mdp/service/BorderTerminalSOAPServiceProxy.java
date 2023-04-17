package org.unibl.etf.mdp.service;

public class BorderTerminalSOAPServiceProxy implements org.unibl.etf.mdp.service.BorderTerminalSOAPService {
  private String _endpoint = null;
  private org.unibl.etf.mdp.service.BorderTerminalSOAPService borderTerminalSOAPService = null;
  
  public BorderTerminalSOAPServiceProxy() {
    _initBorderTerminalSOAPServiceProxy();
  }
  
  public BorderTerminalSOAPServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initBorderTerminalSOAPServiceProxy();
  }
  
  private void _initBorderTerminalSOAPServiceProxy() {
    try {
      borderTerminalSOAPService = (new org.unibl.etf.mdp.service.BorderTerminalSOAPServiceServiceLocator()).getBorderTerminalSOAPService();
      if (borderTerminalSOAPService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)borderTerminalSOAPService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)borderTerminalSOAPService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (borderTerminalSOAPService != null)
      ((javax.xml.rpc.Stub)borderTerminalSOAPService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.unibl.etf.mdp.service.BorderTerminalSOAPService getBorderTerminalSOAPService() {
    if (borderTerminalSOAPService == null)
      _initBorderTerminalSOAPServiceProxy();
    return borderTerminalSOAPService;
  }
  
  public org.unibl.etf.mdp.model.BorderTerminal updateBorderTerminal(org.unibl.etf.mdp.model.BorderTerminal borderTerminal) throws java.rmi.RemoteException{
    if (borderTerminalSOAPService == null)
      _initBorderTerminalSOAPServiceProxy();
    return borderTerminalSOAPService.updateBorderTerminal(borderTerminal);
  }
  
  public boolean deleteBorderTerminal(java.lang.String id) throws java.rmi.RemoteException{
    if (borderTerminalSOAPService == null)
      _initBorderTerminalSOAPServiceProxy();
    return borderTerminalSOAPService.deleteBorderTerminal(id);
  }
  
  public org.unibl.etf.mdp.model.BorderTerminal getBorderTerminal(java.lang.String id) throws java.rmi.RemoteException{
    if (borderTerminalSOAPService == null)
      _initBorderTerminalSOAPServiceProxy();
    return borderTerminalSOAPService.getBorderTerminal(id);
  }
  
  public java.lang.String addBorderTerminal(org.unibl.etf.mdp.model.BorderTerminal borderTerminal) throws java.rmi.RemoteException{
    if (borderTerminalSOAPService == null)
      _initBorderTerminalSOAPServiceProxy();
    return borderTerminalSOAPService.addBorderTerminal(borderTerminal);
  }
  
  
}