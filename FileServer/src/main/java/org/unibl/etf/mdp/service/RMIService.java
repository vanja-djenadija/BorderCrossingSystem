package org.unibl.etf.mdp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIService {

	private static final String CONFIG_PATH = "." + File.separator + "resources" + File.separator + "config.properties";
	public static final String LOG_PATH = "." + File.separator + "resources" + File.separator + "logs" + File.separator
			+ "file_server.log";

	private static FileHandler handler;

	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(RMIService.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(RMIService.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(CONFIG_PATH));

			CustomsFileTransferService service = new CustomsFileTransferService();

			ICustomsFileTransferService stub = (ICustomsFileTransferService) UnicastRemoteObject.exportObject(service,
					0);
			Registry registry = LocateRegistry.createRegistry(Integer.parseInt(properties.getProperty("PORT")));
			registry.rebind(properties.getProperty("SERVICE_NAME"), stub);
		} catch (Exception e) {
			Logger.getLogger(RMIService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
