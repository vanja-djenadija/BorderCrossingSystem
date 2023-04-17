package org.unibl.etf.mdp.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.unibl.etf.mdp.gui.ClientApplication;
import org.unibl.etf.mdp.model.ChatClient;
import org.unibl.etf.mdp.model.Message;
import org.unibl.etf.mdp.protocol.ProtocolMessages;

import com.google.gson.Gson;

public class ClientReadMessagesThread extends Thread {

	private ChatClient client;
	private SSLSocket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Consumer<Message> showMessage;
	private Gson gson = new Gson();

	public ClientReadMessagesThread(ChatClient client, Consumer<Message> showMessage) {
		System.setProperty("javax.net.ssl.trustStore", ClientApplication.getKeystorePath());
		System.setProperty("javax.net.ssl.trustStorePassword", ClientApplication.getKeystorePassword());
		
		this.showMessage = showMessage;
		this.client = client;

		try {
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = (SSLSocket) socketFactory.createSocket(ClientApplication.getHost(), ClientApplication.getWritePort());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		start();
	}

	@Override
	public void run() {
		try {
			out.println(gson.toJson(ProtocolMessages.HELLO.getMessage()));
			out.println(gson.toJson(client));
			Message message = gson.fromJson(in.readLine(), Message.class);
			System.out.println("CLIENT READ " + message.getContent());
			while (message.getContent() != null) {
				showMessage.accept(message);
				message = gson.fromJson(in.readLine(), Message.class);
				System.out.println("CLIENT READ " + message.getContent());
			}
			socket.close();
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
