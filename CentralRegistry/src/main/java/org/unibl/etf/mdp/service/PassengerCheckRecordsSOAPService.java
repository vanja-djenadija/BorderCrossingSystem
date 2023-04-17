package org.unibl.etf.mdp.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class PassengerCheckRecordsSOAPService {

	public static final String PASSENGER_RECORDS_PATH = "." + File.separator + "CentralRegistry" + File.separator
			+ "resources" + File.separator + "passengers.txt";
	public static final String LOG_PATH = "." + File.separator + "resources" + File.separator + "logs" + File.separator
			+ "central_registry_passenger_check.log";

	private static FileHandler handler;

	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(PassengerCheckRecordsSOAPService.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(PassengerCheckRecordsSOAPService.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public void addPassenger(String id) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(PASSENGER_RECORDS_PATH, true)))) {
			out.println(id + " " + new Date().toString());
		} catch (IOException e) {
			Logger.getLogger(PassengerCheckRecordsSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public byte[] getPassengerRecords() {
		byte[] content = null;
		try {
			content = Files.readAllBytes(Path.of(PASSENGER_RECORDS_PATH));
		} catch (IOException e) {
			Logger.getLogger(PassengerCheckRecordsSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return content;
	}

	public void addOnWarantLog(String personID) {
		try {
			Document document = new SAXBuilder().build(new File(WarrantsRESTService.WARRANTS_PATH));
			Element passenger = new Element("passenger");
			passenger.setAttribute("id", personID);
			passenger.setAttribute("datetime", new Date().toString());
			document.getRootElement().addContent(passenger);
			XMLOutputter xml = new XMLOutputter();
			xml.setFormat(Format.getPrettyFormat());
			xml.output(document, new FileWriter(WarrantsRESTService.WARRANTS_PATH));
		} catch (Exception e) {
			Logger.getLogger(PassengerCheckRecordsSOAPService.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
