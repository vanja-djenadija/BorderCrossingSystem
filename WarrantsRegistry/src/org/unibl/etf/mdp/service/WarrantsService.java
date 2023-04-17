package org.unibl.etf.mdp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WarrantsService implements IWarrantsService {

	private static final String CONFIG_PATH = "." + File.separator + "resources" + File.separator + "config.properties";
	private static String WARRANTS_FILE;
	private static String SERVICE_NAME;
	private static int PORT;

	static {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(CONFIG_PATH));
			WARRANTS_FILE = properties.getProperty("WARRANTS_FILE");
			SERVICE_NAME = properties.getProperty("SERVICE_NAME");
			PORT = Integer.parseInt(properties.getProperty("PORT"));
		} catch (IOException e) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public boolean isPersonWanted(String personId) throws RemoteException {
		boolean isWanted = false;
		try {
			List<String> wantedPersons = Files.readAllLines(Paths.get(WARRANTS_FILE));
			for (String line : wantedPersons)
				if (line.equals(personId))
					isWanted = true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return isWanted;
	}

	public static String getServiceName() {
		return SERVICE_NAME;
	}

	public static int getPort() {
		return PORT;
	}
}
