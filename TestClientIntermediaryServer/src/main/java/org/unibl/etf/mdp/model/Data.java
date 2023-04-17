package org.unibl.etf.mdp.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Data {
	private static HashMap<Client, PriorityQueue<String>> waitingPassengersPoliceControl = new HashMap<>();
	private static HashMap<Client, PriorityQueue<Document>> waitingPassengersCustomsControl = new HashMap<>();
	private static HashSet<String> passedPassengersPolice = new HashSet<>();
	private static HashSet<String> passedPassengersCustoms = new HashSet<>();
	private static HashSet<Client> activeClients = new HashSet<>();
	public static HashSet<Client> tempDeactivatedClients = new HashSet<>();

	public static HashMap<Client, PriorityQueue<Document>> getWaitingPassengersCustomsControl() {
		return waitingPassengersCustomsControl;
	}

	public static HashMap<Client, PriorityQueue<String>> getWaitingPassengersPoliceControl() {
		return waitingPassengersPoliceControl;
	}

	public static HashSet<String> getPassedPassengersPolice() {
		return passedPassengersPolice;
	}

	public static HashSet<String> getPassedPassengersCustoms() {
		return passedPassengersCustoms;
	}

	public static HashSet<Client> getActiveClients() {
		return activeClients;
	}
}
