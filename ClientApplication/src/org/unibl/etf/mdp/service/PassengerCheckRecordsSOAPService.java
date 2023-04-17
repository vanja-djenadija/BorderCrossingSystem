/**
 * PassengerCheckRecordsSOAPService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.mdp.service;

public interface PassengerCheckRecordsSOAPService extends java.rmi.Remote {
    public void addOnWarantLog(java.lang.String personID) throws java.rmi.RemoteException;
    public void addPassenger(java.lang.String id) throws java.rmi.RemoteException;
    public byte[] getPassengerRecords() throws java.rmi.RemoteException;
}
