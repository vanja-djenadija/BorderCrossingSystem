package org.unibl.etf.mdp.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICustomsFileTransferService extends Remote {

	void sendDocuments(String personId, byte[] files) throws RemoteException;
}
