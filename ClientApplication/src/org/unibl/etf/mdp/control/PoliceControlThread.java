package org.unibl.etf.mdp.control;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.gui.ClientApplication;
import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.service.ClientAppService;
import org.unibl.etf.mdp.service.ClientAppServiceServiceLocator;
import org.unibl.etf.mdp.service.IWarrantsService;
import org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPService;
import org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPServiceServiceLocator;

public class PoliceControlThread extends Thread {

	private Client client;
	private Runnable sendNotification;
	private Runnable showContinueButton;

	public PoliceControlThread(Client client, Runnable sendNotification, Runnable showContinueButton) {
		this.client = client;
		this.sendNotification = sendNotification;
		this.showContinueButton = showContinueButton;
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				ClientAppServiceServiceLocator locator = new ClientAppServiceServiceLocator();
				ClientAppService service = locator.getClientAppService();
				String personID = service.getNextPerson(client);
				if (personID == null) {
					Thread.sleep(1000);
					continue;
				}
				Registry registry = LocateRegistry.getRegistry(ClientApplication.getWarrantsRmiPort());
				IWarrantsService rmi = (IWarrantsService) registry.lookup(ClientApplication.getWarrantsRmiName());
				if (rmi.isPersonWanted(personID)) {
					logWarrant(personID);
					sendNotification.run();
					service.temporarilyDeactive(client.getTerminalId());
					showContinueButton.run();
				} else
					service.logPassengerPoliceControl(personID);
			} catch (Exception e) {
				Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
	}

	private void logWarrant(String personID) {
		try {
			PassengerCheckRecordsSOAPServiceServiceLocator locator = new PassengerCheckRecordsSOAPServiceServiceLocator();
			PassengerCheckRecordsSOAPService service = locator.getPassengerCheckRecordsSOAPService();
			service.addOnWarantLog(personID);
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
