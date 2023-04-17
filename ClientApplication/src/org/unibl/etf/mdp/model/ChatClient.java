package org.unibl.etf.mdp.model;

import java.io.Serializable;
import java.util.Objects;

public class ChatClient implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String terminalId;
	private String entryId;
	private boolean police;

	public ChatClient() {
		super();
	}

	public ChatClient(String username, String terminalId, String entryId, boolean police) {
		super();
		this.username = username;
		this.terminalId = terminalId;
		this.entryId = entryId;
		this.police = police;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public boolean isPolice() {
		return police;
	}

	public void setPolice(boolean police) {
		this.police = police;
	}

	@Override
	public int hashCode() {
		return Objects.hash(entryId, police, terminalId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatClient other = (ChatClient) obj;
		return Objects.equals(entryId, other.entryId) && police == other.police
				&& Objects.equals(terminalId, other.terminalId);
	}

	@Override
	public String toString() {
		return username + "#" + terminalId + "#" + entryId + "#" + police;
	}

}
