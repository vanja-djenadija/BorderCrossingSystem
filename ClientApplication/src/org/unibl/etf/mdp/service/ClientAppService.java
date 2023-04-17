/**
 * ClientAppService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.mdp.service;

public interface ClientAppService extends java.rmi.Remote {
    public java.lang.String getNextPerson(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException;
    public void activateTerminal(java.lang.String terminalId) throws java.rmi.RemoteException;
    public org.unibl.etf.mdp.model.Document getNextDocument(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException;
    public boolean isActive(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException;
    public void login(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException;
    public void logPassengerCustomsControl(java.lang.String personId) throws java.rmi.RemoteException;
    public void temporarilyDeactive(java.lang.String terminalId) throws java.rmi.RemoteException;
    public void logPassengerPoliceControl(java.lang.String personId) throws java.rmi.RemoteException;
    public void logout(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException;
}
