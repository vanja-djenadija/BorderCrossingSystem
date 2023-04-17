package org.unibl.etf.mdp.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.gui.ClientApplication;
import org.unibl.etf.mdp.model.ChatClient;

public class GetActiveClientsThread extends Thread {

	private Consumer<HashSet<ChatClient>> showActiveClients;

	public GetActiveClientsThread(Consumer<HashSet<ChatClient>> showActiveClients) {
		this.showActiveClients = showActiveClients;
		setDaemon(true);
		start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		MulticastSocket socket = null;
		byte[] buffer = new byte[1024];
		try {
			socket = new MulticastSocket(ClientApplication.getMulticastPort());
			InetAddress address = InetAddress.getByName(ClientApplication.getMulticastHost());
			socket.joinGroup(address);
			while (true) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
				ObjectInputStream ois = new ObjectInputStream(bais);
				HashSet<ChatClient> activeClients = (HashSet<ChatClient>) ois.readObject();
				showActiveClients.accept(activeClients);
			}
		} catch (IOException | ClassNotFoundException e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
