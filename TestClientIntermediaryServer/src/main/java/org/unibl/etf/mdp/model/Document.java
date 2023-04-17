package org.unibl.etf.mdp.model;

import java.util.Arrays;
import java.util.Objects;

public class Document implements Comparable<Document> {
	private String passengerId;
	private byte[] document;

	public Document() {
		super();
	}

	public Document(String passengerId, byte[] document) {
		super();
		this.passengerId = passengerId;
		this.document = document;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(document);
		result = prime * result + Objects.hash(passengerId);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		return Arrays.equals(document, other.document) && Objects.equals(passengerId, other.passengerId);
	}

	@Override
	public int compareTo(Document o) {
		return hashCode() - o.hashCode();
	}
}
