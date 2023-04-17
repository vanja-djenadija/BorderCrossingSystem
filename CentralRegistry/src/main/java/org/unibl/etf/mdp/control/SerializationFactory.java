package org.unibl.etf.mdp.control;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerializationFactory {

	public static final String TERMINALS_PATH = "." + File.separator + "CentralRegistry" + File.separator + "resources"
			+ File.separator + "terminals" + File.separator;
	public static final String SERIAL_PATH = "." + File.separator + "CentralRegistry" + File.separator + "resources"
			+ File.separator + "serial.txt";
	private static HashMap<Integer, Class<?>> supportedFormats = new HashMap<>();
	private static final int NUMBER_OF_SERIALIZATION_TYPES = 4;
	public static final String LOG_PATH = "." + File.separator + "resources" + File.separator + "logs" + File.separator
			+ "central_registry_serialization.log";

	private static FileHandler handler;

	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(SerializationFactory.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(SerializationFactory.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}


	static {
		supportedFormats.put(0, HessianSerialization.class);
		supportedFormats.put(1, JavaSerialization.class);
		supportedFormats.put(2, KryoSerialization.class);
		supportedFormats.put(3, FSTSerialization.class);
	}

	public static IBorderTerminalSerialization getSerialization(Integer terminalId) throws Exception {
		return (IBorderTerminalSerialization) supportedFormats.get(terminalId % NUMBER_OF_SERIALIZATION_TYPES)
				.getConstructor().newInstance();
	}
}
