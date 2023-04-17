/**
 * TestAppService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.mdp.service;

public interface TestAppService extends java.rmi.Remote {
    public boolean isActive(org.unibl.etf.mdp.model.Client client) throws java.rmi.RemoteException;
    public void addPassengerToCustomsControl(org.unibl.etf.mdp.model.Client client, org.unibl.etf.mdp.model.Document document) throws java.rmi.RemoteException;
    public boolean passengerPassedPoliceControl(java.lang.String passengerId) throws java.rmi.RemoteException;
    public boolean passengerPassedCustomsControl(java.lang.String passengerId) throws java.rmi.RemoteException;
    public void addPassengerToPoliceControl(org.unibl.etf.mdp.model.Client client, java.lang.String passengerId) throws java.rmi.RemoteException;
}
