package org.unibl.etf.mdp.model;

import java.util.ArrayList;
import java.util.Date;

import org.unibl.etf.mdp.protocol.ProtocolMessages;

public class Message implements Comparable<Message> {
	private ProtocolMessages type;
	private ChatClient from;
	private ArrayList<ChatClient> to;
	private String content;
	private Date date = new Date();

	public Message() {
		super();
	}

	// Message when END
	public Message(ChatClient from) {
		super();
		this.from = from;
		to = new ArrayList<ChatClient>();
		to.add(from);
	}

	public Message(ProtocolMessages type, ChatClient from, ArrayList<ChatClient> to, String content) {
		super();
		this.type = type;
		this.from = from;
		this.to = to;
		this.content = content;
	}

	public ChatClient getFrom() {
		return from;
	}

	public void setFrom(ChatClient from) {
		this.from = from;
	}

	public ArrayList<ChatClient> getTo() {
		return to;
	}

	public void setTo(ArrayList<ChatClient> to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ProtocolMessages getType() {
		return type;
	}

	public void setType(ProtocolMessages type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public int compareTo(Message o) {
		return hashCode() - o.hashCode();
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", from=" + from + ", to=" + to + ", content=" + content + ", date=" + date
				+ "]";
	}
	
}
