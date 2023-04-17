package org.unibl.etf.mdp.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.model.BorderTerminal;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class HessianSerialization implements IBorderTerminalSerialization {

	@Override
	public boolean serialize(BorderTerminal borderTerminal) {
		try (FileOutputStream fos = new FileOutputStream(
				new File(SerializationFactory.TERMINALS_PATH + borderTerminal.getId() + ".bin"))) {
			Hessian2Output out = new Hessian2Output(fos);
			out.startMessage();
			out.writeObject(borderTerminal);
			out.completeMessage();
			out.close();
			return true;
		} catch (IOException e) {
			Logger.getLogger(SerializationFactory.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
	}

	@Override
	public BorderTerminal deserialize(String borderId) {
		try (FileInputStream fis = new FileInputStream(
				new File(SerializationFactory.TERMINALS_PATH + borderId + ".bin"))) {
			Hessian2Input in = new Hessian2Input(fis);
			in.startMessage();
			BorderTerminal terminal = (BorderTerminal) in.readObject();
			in.completeMessage();
			in.close();
			return terminal;
		} catch (IOException e) {
			Logger.getLogger(SerializationFactory.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return null;
	}
}
