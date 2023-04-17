package org.unibl.etf.mdp.service;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	public static final String LOG_PATH = "." + File.separator + "resources" + File.separator + "logs" + File.separator
			+ "warrants.log";
	
	private static FileHandler handler;
	
	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(Main.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(Main.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public static void main(String[] args) {
		try {
			WarrantsService service = new WarrantsService();
			IWarrantsService stub = (IWarrantsService) UnicastRemoteObject.exportObject(service, 0);
			Registry registry = LocateRegistry.createRegistry(WarrantsService.getPort());
			registry.rebind(WarrantsService.getServiceName(), stub);
		} catch (Exception e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
