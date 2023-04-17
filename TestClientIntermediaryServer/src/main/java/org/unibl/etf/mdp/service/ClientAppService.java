package org.unibl.etf.mdp.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.model.Data;
import org.unibl.etf.mdp.model.Document;

public class ClientAppService {

	private Object lock = new Object();

	public ClientAppService() {

	}

	public boolean isActive(Client client) {
		return Data.getActiveClients().contains(client);
	}

	public void temporarilyDeactive(String terminalId) {
		synchronized (lock) {
			HashSet<Client> toBeRemoved = new HashSet<>();
			for (Client client : Data.getActiveClients()) {
				if (client.getTerminalId().equals(terminalId)) {
					toBeRemoved.add(client);
					Data.tempDeactivatedClients.add(client);
				}
			}
			for (Client client : toBeRemoved) {
				Data.getActiveClients().remove(client);
			}
		}
	}

	public void activateTerminal(String terminalId) {
		synchronized (lock) {
			HashSet<Client> toBeRemoved = new HashSet<>();
			for (Client client : Data.tempDeactivatedClients) {
				if (client.getTerminalId().equals(terminalId)) {
					toBeRemoved.add(client);
					Data.getActiveClients().add(client);
				}
			}
			for (Client client : toBeRemoved) {
				Data.tempDeactivatedClients.remove(client);
			}
		}
	}

	public void login(Client client) {
		HashSet<Client> set = Data.getActiveClients();
		synchronized (set) {
			set.add(client);
		}
		if (!Data.getWaitingPassengersCustomsControl().containsKey(client))
			Data.getWaitingPassengersCustomsControl().put(client, new PriorityQueue<Document>());

		if (!Data.getWaitingPassengersPoliceControl().containsKey(client))
			Data.getWaitingPassengersPoliceControl().put(client, new PriorityQueue<String>());

	}

	public void logout(Client client) {
		HashSet<Client> set = Data.getActiveClients();
		synchronized (set) {
			set.remove(client);
		}
	}

	public String getNextPerson(Client client) {
		HashMap<Client, PriorityQueue<String>> map = Data.getWaitingPassengersPoliceControl();
		synchronized (map) {
			return map.get(client).poll();
		}
	}

	public Document getNextDocument(Client client) {
		HashMap<Client, PriorityQueue<Document>> map = Data.getWaitingPassengersCustomsControl();
		synchronized (map) {
			return map.get(client).poll();
		}
	}

	public void logPassengerPoliceControl(String personId) {
		HashSet<String> passedPassengers = Data.getPassedPassengersPolice();
		synchronized (passedPassengers) {
			passedPassengers.add(personId);
		}
	}

	public void logPassengerCustomsControl(String personId) {
		HashSet<String> passedPassengers = Data.getPassedPassengersCustoms();
		synchronized (passedPassengers) {
			passedPassengers.add(personId);
		}
	}
}
