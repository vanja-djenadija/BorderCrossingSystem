package org.unibl.etf.mdp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.control.SerializationFactory;
import org.unibl.etf.mdp.model.BorderCheckpoint;
import org.unibl.etf.mdp.model.BorderTerminal;

public class BorderTerminalSOAPService {

	public static final String LOG_PATH = "." + File.separator + "resources" + File.separator + "logs" + File.separator
			+ "central_registry_border_terminal.log";

	private static FileHandler handler;

	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(BorderTerminalSOAPService.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(BorderTerminalSOAPService.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public String addBorderTerminal(BorderTerminal borderTerminal) {
		int serialId = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(SerializationFactory.SERIAL_PATH))) {
			serialId = Integer.parseInt(br.readLine());
			Files.writeString(Paths.get(SerializationFactory.SERIAL_PATH), (serialId + 1) + "");
		} catch (IOException e) {
			Logger.getLogger(BorderTerminalSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			return null;
		}

		try {
			borderTerminal.setId(String.valueOf(serialId));
			BorderCheckpoint[] checkPoints = borderTerminal.getCheckPoints();
			for (int i = 0; i < checkPoints.length; i += 2) {
				int count = borderTerminal.getCount();
				checkPoints[i].setId(count + "");
				checkPoints[i + 1].setId(count + "");
				borderTerminal.setCount(count + 1);
			}
			SerializationFactory.getSerialization(Integer.parseInt(borderTerminal.getId())).serialize(borderTerminal);
		} catch (Exception e) {
			Logger.getLogger(BorderTerminalSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return borderTerminal.getId();
	}

	public BorderTerminal updateBorderTerminal(BorderTerminal borderTerminal) {
		BorderTerminal terminal = null;
		try {
			terminal = SerializationFactory.getSerialization(Integer.parseInt(borderTerminal.getId()))
					.deserialize(borderTerminal.getId());
		} catch (Exception e) {
			Logger.getLogger(BorderTerminalSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}

		int count = terminal.getCount();
		if (count == 0)
			count++;

		for (BorderCheckpoint checkPoint : terminal.getCheckPoints())
			if ("0".equals(terminal.getId()))
				checkPoint.setId(count++ + "");

		terminal.setCount(count);
		terminal.setName(borderTerminal.getName());
		try {
			SerializationFactory.getSerialization(Integer.parseInt(terminal.getId())).serialize(terminal);
		} catch (Exception e) {
			Logger.getLogger(BorderTerminalSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return terminal;
	}

	public boolean deleteBorderTerminal(String id) {
		try {
			return new File(SerializationFactory.TERMINALS_PATH + id + ".bin").delete();
		} catch (Exception e) {
			Logger.getLogger(BorderTerminalSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
	}

	public BorderTerminal getBorderTerminal(String id) {
		try {
			return SerializationFactory.getSerialization(Integer.parseInt(id)).deserialize(id);
		} catch (Exception e) {
			Logger.getLogger(BorderTerminalSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			return null;
		}
	}
}
