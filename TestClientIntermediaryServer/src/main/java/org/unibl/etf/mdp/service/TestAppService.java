package org.unibl.etf.mdp.service;

import java.util.HashMap;
import java.util.PriorityQueue;
import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.model.Data;
import org.unibl.etf.mdp.model.Document;

public class TestAppService {

	public boolean isActive(Client client) {
		return Data.getActiveClients().contains(client);
	}

	public void addPassengerToPoliceControl(Client client, String passengerId) {
		HashMap<Client, PriorityQueue<String>> map = Data.getWaitingPassengersPoliceControl();
		synchronized (map) {
			if (!map.containsKey(client)) {
				map.put(client, new PriorityQueue<String>());
			}
			map.get(client).add(passengerId);
		}
	}

	public void addPassengerToCustomsControl(Client client, Document document) {
		HashMap<Client, PriorityQueue<Document>> map = Data.getWaitingPassengersCustomsControl();
		synchronized (map) {
			if (!map.containsKey(client)) {
				map.put(client, new PriorityQueue<Document>());
			}
			map.get(client).add(document);
		}
	}

	public boolean passengerPassedPoliceControl(String passengerId) {
		return Data.getPassedPassengersPolice().contains(passengerId);
	}

	public boolean passengerPassedCustomsControl(String passengerId) {
		return Data.getPassedPassengersCustoms().contains(passengerId);
	}
}
