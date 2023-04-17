package org.unibl.etf.mdp.control;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.gui.ClientApplication;
import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.model.Document;
import org.unibl.etf.mdp.service.ClientAppService;
import org.unibl.etf.mdp.service.ClientAppServiceServiceLocator;
import org.unibl.etf.mdp.service.ICustomsFileTransferService;
import org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPService;
import org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPServiceServiceLocator;

public class CustomsControlThread extends Thread {

	private Client client;

	private Consumer<String> filesSentCallback;

	public CustomsControlThread(Client client, Consumer<String> filesSentCallback) {
		this.client = client;
		this.filesSentCallback = filesSentCallback;
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				ClientAppServiceServiceLocator locator = new ClientAppServiceServiceLocator();
				ClientAppService service = locator.getClientAppService();
				Document files = service.getNextDocument(client);
				if (files == null) {
					Thread.sleep(1000);
					continue;
				}
				Registry registry = LocateRegistry.getRegistry(ClientApplication.getFilesRmiPort());
				ICustomsFileTransferService rmi = (ICustomsFileTransferService) registry
						.lookup(ClientApplication.getFilesRmiName());
				rmi.sendDocuments(files.getPassengerId(), files.getDocument());
				logPassenger(files.getPassengerId());
				filesSentCallback.accept("Fajlovi uspjesno poslati.");
				service.logPassengerCustomsControl(files.getPassengerId());
			} catch (Exception e) {
				Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
	}

	private void logPassenger(String personID) {
		try {
			PassengerCheckRecordsSOAPServiceServiceLocator locator = new PassengerCheckRecordsSOAPServiceServiceLocator();
			PassengerCheckRecordsSOAPService service = locator.getPassengerCheckRecordsSOAPService();
			service.addPassenger(personID);
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
