package org.unibl.etf.mdp.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class ChatServerWriteService extends Thread {

	private int writePort;

	public ChatServerWriteService(int writePort) {
		this.writePort = writePort;
		start();
	}

	@Override
	public void run() {
		SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try (SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(writePort)) {
			while (true) {
				SSLSocket sock = (SSLSocket) ss.accept();
				new ChatServerWriteThread(sock);
			}
		} catch (IOException e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
