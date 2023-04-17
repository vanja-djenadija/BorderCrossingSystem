/**
 * BorderTerminalSOAPService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.mdp.service;

public interface BorderTerminalSOAPService extends java.rmi.Remote {
    public org.unibl.etf.mdp.model.BorderTerminal updateBorderTerminal(org.unibl.etf.mdp.model.BorderTerminal borderTerminal) throws java.rmi.RemoteException;
    public boolean deleteBorderTerminal(java.lang.String id) throws java.rmi.RemoteException;
    public org.unibl.etf.mdp.model.BorderTerminal getBorderTerminal(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String addBorderTerminal(org.unibl.etf.mdp.model.BorderTerminal borderTerminal) throws java.rmi.RemoteException;
}
