package org.unibl.etf.mdp.control;

import org.unibl.etf.mdp.model.BorderTerminal;

public interface IBorderTerminalSerialization {

	boolean serialize(BorderTerminal borderTerminal);

	BorderTerminal deserialize(String borderId);
}
