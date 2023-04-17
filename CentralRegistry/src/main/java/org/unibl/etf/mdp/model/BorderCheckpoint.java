package org.unibl.etf.mdp.model;

import java.io.Serializable;

public class BorderCheckpoint implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private boolean entry = true;
	private boolean police = true;

	public BorderCheckpoint() {
		super();
	}

	public BorderCheckpoint(String id, boolean entry) {
		super();
		this.id = id;
		this.entry = entry;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEntry() {
		return entry;
	}

	public void setEntry(boolean entry) {
		this.entry = entry;
	}

	public boolean isPolice() {
		return police;
	}

	public void setPolice(boolean police) {
		this.police = police;
	}

	@Override
	public String toString() {
		return "BorderCheckpoint [id=" + id + ", entry=" + entry + "]";
	}
}
