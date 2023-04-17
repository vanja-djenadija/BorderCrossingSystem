package org.unibl.etf.mdp.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class ChatServerReadService extends Thread {

	private int readPort;

	public ChatServerReadService(int readPort) {
		this.readPort = readPort;
		start();
	}

	@Override
	public void run() {
		SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try (SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(readPort)) {
			while (true) {
				SSLSocket sock = (SSLSocket) ss.accept();
				new ChatServerReadThread(sock);
			}
		} catch (IOException e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
