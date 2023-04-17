package org.unibl.etf.mdp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

import org.unibl.etf.mdp.model.Message;
import org.unibl.etf.mdp.model.ChatClient;
import org.unibl.etf.mdp.protocol.ProtocolMessages;

import com.google.gson.Gson;

public class ChatServerReadThread extends Thread {

	private SSLSocket sock;
	private BufferedReader in;
	private Gson gson = new Gson();

	public ChatServerReadThread(SSLSocket sock) {
		try {
			this.sock = sock;
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			start();
		} catch (IOException e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	@Override
	public void run() {
		try {
			String request = gson.fromJson(in.readLine(), String.class);
			System.out.println("CS READ " + request);
			ChatClient from = null;
			if (ProtocolMessages.HELLO.getMessage().equals(request)) {
				from = gson.fromJson(in.readLine(), ChatClient.class);
				System.out.println("CS READ FROM " + from);
				ChatService.addClient(from);
			}
			Message message;
			do {
				message = gson.fromJson(in.readLine(), Message.class);
				System.out.println("CS READ MESSAGE " + message);
				ChatService.addMessage(message);
			} while (message.getContent() != null);
			sock.close();
		} catch (IOException e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
