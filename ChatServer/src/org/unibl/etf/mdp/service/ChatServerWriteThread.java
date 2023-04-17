package org.unibl.etf.mdp.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

import org.unibl.etf.mdp.model.ChatClient;
import org.unibl.etf.mdp.model.Message;
import org.unibl.etf.mdp.protocol.ProtocolMessages;

import com.google.gson.Gson;

public class ChatServerWriteThread extends Thread {

	private SSLSocket sock;
	private BufferedReader in;
	private PrintWriter out;
	private Gson gson = new Gson();

	public ChatServerWriteThread(SSLSocket sock) {
		this.sock = sock;
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
		} catch (IOException e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		start();
	}

	@Override
	public void run() {
		try {
			String request = gson.fromJson(in.readLine(), String.class);
			System.out.println("CS WRITE " + request);
			ChatClient to = null;
			if (ProtocolMessages.HELLO.getMessage().equals(request)) {
				to = gson.fromJson(in.readLine(), ChatClient.class);
				System.out.println("CS WRITE TO " + to);
				ChatService.addClient(to);
			}
			Message message;
			do {
				message = ChatService.getMessage(to);
				System.out.println("CS WRITE MESSAGE " + to);
				out.println(gson.toJson(message));
			} while (message.getContent() != null);
			sock.close();
			ChatService.removeClient(to);
		} catch (Exception e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
