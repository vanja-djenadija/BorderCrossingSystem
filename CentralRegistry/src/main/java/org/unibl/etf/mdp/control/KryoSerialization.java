package org.unibl.etf.mdp.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.model.BorderTerminal;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerialization implements IBorderTerminalSerialization {

	@Override
	public boolean serialize(BorderTerminal borderTerminal) {
		Kryo kryo = new Kryo();
		kryo.register(BorderTerminal.class);
		try (Output out = new Output(new FileOutputStream(
				new File(SerializationFactory.TERMINALS_PATH + borderTerminal.getId() + ".bin")))) {
			kryo.writeClassAndObject(out, borderTerminal);
			return true;
		} catch (Exception e) {
			Logger.getLogger(SerializationFactory.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}

	}

	@Override
	public BorderTerminal deserialize(String terminalId) {
		Kryo kryo = new Kryo();
		kryo.register(BorderTerminal.class);
		try (Input in = new Input(
				new FileInputStream(new File(SerializationFactory.TERMINALS_PATH + terminalId + ".bin")))) {
			BorderTerminal terminal = (BorderTerminal) kryo.readClassAndObject(in);
			return terminal;
		} catch (Exception e) {
			Logger.getLogger(SerializationFactory.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			return null;
		}
	}
}
