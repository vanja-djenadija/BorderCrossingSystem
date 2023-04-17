package org.unibl.etf.mdp.model;

import java.io.Serializable;

public class BorderTerminal implements Serializable {

	private static final long serialVersionUID = 1L;
	private int count;
	private String id;
	private String name;

	private BorderCheckpoint[] checkPoints;

	public BorderTerminal() {
		super();
	}

	public BorderTerminal(String id, String name, BorderCheckpoint[] checkPoints) {
		super();
		this.id = id;
		this.name = name;
		this.checkPoints = checkPoints;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BorderCheckpoint[] getCheckPoints() {
		return checkPoints;
	}

	public void setCheckPoints(BorderCheckpoint[] checkPoints) {
		this.checkPoints = checkPoints;
	}

	@Override
	public String toString() {
		return "BorderTerminal [id=" + id + ", name=" + name + ", checkPoints=" + checkPoints + "]";
	}

}
