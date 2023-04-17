package org.unibl.etf.mdp.control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.unibl.etf.mdp.gui.ChatController;
import org.unibl.etf.mdp.gui.ClientApplication;
import org.unibl.etf.mdp.model.ChatClient;
import org.unibl.etf.mdp.model.Message;
import org.unibl.etf.mdp.protocol.ProtocolMessages;

import com.google.gson.Gson;

public class ClientWriteMessagesThread {

	private SSLSocket socket;
	private PrintWriter out;
	private Gson gson = new Gson();
	private ChatController chatController;

	public ClientWriteMessagesThread(ChatClient client, Consumer<Message> showMessage, ChatController chatController) {
		System.setProperty("javax.net.ssl.trustStore", ClientApplication.getKeystorePath());
		System.setProperty("javax.net.ssl.trustStorePassword", ClientApplication.getKeystorePassword());
		
		this.chatController = chatController;
		
		SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		try {
			socket = (SSLSocket) socketFactory.createSocket(ClientApplication.getHost(),
					ClientApplication.getReadPort());
			
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			out.println(gson.toJson(ProtocolMessages.HELLO.getMessage()));
			out.println(gson.toJson(client));
		} catch (IOException e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public void send(Message message) {
		try {
			System.out.println("SEND MESSAGE  " + message.getContent());
			out.println(gson.toJson(message));
			HashMap<ChatClient, ArrayList<Message>> messages = chatController.getMessages();
			if (message.getType().equals(ProtocolMessages.UNICAST)) {
				ChatClient client = message.getTo().get(0);
				if (!messages.containsKey(client)) {
					chatController.getMessages().put(client, new ArrayList<Message>());
				}
				chatController.getMessages().get(client).add(message);
				chatController.updateMessages();
			}
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
