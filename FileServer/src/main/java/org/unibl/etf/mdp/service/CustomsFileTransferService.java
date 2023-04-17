package org.unibl.etf.mdp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomsFileTransferService implements ICustomsFileTransferService {

	public static final String DOCUMENTS_PATH = "." + File.separator + "resources" + File.separator + "documents"; 

	@Override
	public void sendDocuments(String personId, byte[] files) throws RemoteException {
		File file = new File(DOCUMENTS_PATH + File.separator + personId);
		if (!file.exists())
			file.mkdir();
		try (FileOutputStream fos = new FileOutputStream(
				file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".zip")) {
			fos.write(files);
		} catch (Exception e) {
			Logger.getLogger(RMIService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		} 
	}
}
