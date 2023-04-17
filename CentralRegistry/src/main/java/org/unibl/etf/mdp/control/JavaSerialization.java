package org.unibl.etf.mdp.control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.model.BorderTerminal;

public class JavaSerialization implements IBorderTerminalSerialization {

	@Override
	public boolean serialize(BorderTerminal borderTerminal) {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(SerializationFactory.TERMINALS_PATH + borderTerminal.getId() + ".bin"))) {
			oos.writeObject(borderTerminal);
			return true;
		} catch (IOException e) {
			Logger.getLogger(SerializationFactory.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
	}

	@Override
	public BorderTerminal deserialize(String terminalId) {
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(SerializationFactory.TERMINALS_PATH + terminalId + ".bin"))) {
			return (BorderTerminal) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Logger.getLogger(SerializationFactory.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return null;
	}
}
