package org.unibl.etf.mdp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.model.Message;
import org.unibl.etf.mdp.model.ChatClient;

public class ChatService {

	public static final String CONFIG_PATH = "." + File.separator + "resources" + File.separator + "config.properties";
	public static final String LOG_PATH = "." + File.separator + "resources" + File.separator + "logs" + File.separator
			+ "chat_server.log";
	private static HashSet<ChatClient> activeClients = new HashSet<>();
	private static ConcurrentHashMap<ChatClient, ArrayList<Message>> readMessages = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<ChatClient, PriorityBlockingQueue<Message>> unreadMessages = new ConcurrentHashMap<>();
	private static int readPort, writePort, multicastPort;
	private static String keystorePath, keystorePassword, multicastHost;

	private static FileHandler handler;

	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(ChatService.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(ChatService.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public static void main(String[] args) {
		try {
			loadProperties();
			System.setProperty("javax.net.ssl.keyStore", keystorePath);
			System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);

			new ChatServerWriteService(writePort);
			new ChatServerReadService(readPort);
			new ChatServerActiveClientsService();
		} catch (Exception e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	private static void loadProperties() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(CONFIG_PATH));
		keystorePath = prop.getProperty("KEYSTORE_PATH");
		keystorePassword = prop.getProperty("KEYSTORE_PASSWORD");
		writePort = Integer.parseInt(prop.getProperty("WRITE_PORT"));
		readPort = Integer.parseInt(prop.getProperty("READ_PORT"));
		multicastPort = Integer.parseInt(prop.getProperty("MULTICAST_PORT"));
		multicastHost = prop.getProperty("MULTICAST_HOST");
	}

	public static void addClient(ChatClient client) {
		synchronized (activeClients) {
			activeClients.add(client);
		}
	}

	public static void removeClient(ChatClient client) {
		synchronized (activeClients) {
			activeClients.remove(client);
		}
	}

	public static void addMessage(Message message) {
		for (ChatClient client : message.getTo()) {
			if (!unreadMessages.containsKey(client)) {
				unreadMessages.put(client, new PriorityBlockingQueue<Message>());
			}
			unreadMessages.get(client).add(message);
		}
	}

	public static Message getMessage(ChatClient client) {
		if (!unreadMessages.containsKey(client)) {
			unreadMessages.put(client, new PriorityBlockingQueue<Message>());
		}
		if (!readMessages.containsKey(client)) {
			readMessages.put(client, new ArrayList<Message>());
		}
		Message message = null;
		System.out.println(unreadMessages);
		System.out.println(unreadMessages.get(client));
		try {
			message = unreadMessages.get(client).take();
		} catch (InterruptedException e) {
			Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		synchronized (unreadMessages) {
			readMessages.get(client).add(message);
		}
		return message;
	}

	public static int getMulticastPort() {
		return multicastPort;
	}

	public static String getMulticastHost() {
		return multicastHost;
	}

	public static HashSet<ChatClient> getClients() {
		return activeClients;
	}
}
