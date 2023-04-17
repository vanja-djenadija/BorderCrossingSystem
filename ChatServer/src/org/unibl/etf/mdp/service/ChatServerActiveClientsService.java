package org.unibl.etf.mdp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServerActiveClientsService extends Thread {
	private int port;

	public ChatServerActiveClientsService() {
		port = ChatService.getMulticastPort();
		start();
	}

	@Override
	public void run() {
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket();
			InetAddress address = InetAddress.getByName(ChatService.getMulticastHost());
			socket.joinGroup(address);
			while (true) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(baos);
				out.writeObject(ChatService.getClients());
				byte[] bytes = baos.toByteArray();
				DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
				socket.send(packet);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
				}
			}
		} catch (IOException e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
