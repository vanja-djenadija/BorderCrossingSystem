package org.unibl.etf.mdp.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.unibl.etf.mdp.model.BorderTerminal;

public class FSTSerialization implements IBorderTerminalSerialization {
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

	@Override
	public boolean serialize(BorderTerminal borderTerminal) {
		try (FileOutputStream fos = new FileOutputStream(
				new File(SerializationFactory.TERMINALS_PATH + borderTerminal.getId() + ".bin"))) {
			FSTObjectOutput out = conf.getObjectOutput(fos);
			out.writeObject(borderTerminal, BorderTerminal.class);
			out.flush();
			return true;
		} catch (IOException e) {
			Logger.getLogger(SerializationFactory.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return false;
	}

	@Override
	public BorderTerminal deserialize(String borderId) {
		try (FileInputStream fis = new FileInputStream(
				new File(SerializationFactory.TERMINALS_PATH + borderId + ".bin"))) {
			FSTObjectInput in = conf.getObjectInput(fis);
			return (BorderTerminal) in.readObject(BorderTerminal.class);
		} catch (Exception e) {
			Logger.getLogger(SerializationFactory.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return null;
	}
}
