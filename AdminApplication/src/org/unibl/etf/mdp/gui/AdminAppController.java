package org.unibl.etf.mdp.gui;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import javafx.scene.control.TabPane;

import javafx.scene.control.Label;

import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.rpc.ServiceException;

import org.unibl.etf.mdp.model.BorderCheckpoint;
import org.unibl.etf.mdp.model.BorderTerminal;
import org.unibl.etf.mdp.model.User;
import org.unibl.etf.mdp.service.BorderTerminalSOAPService;
import org.unibl.etf.mdp.service.BorderTerminalSOAPServiceServiceLocator;
import org.unibl.etf.mdp.util.Util;

public class AdminAppController {
	@FXML
	private TabPane tabPane;
	@FXML
	private TextField searchTerminalIdTF;
	@FXML
	private Label updateTerminalIdLabel;
	@FXML
	private TextField updateTerminalNameLabel;
	@FXML
	private Label entriesLabel;
	@FXML
	private TextField addTerminalNameTF;
	@FXML
	private TextField addTerminalNumberOfEntriesTF;
	@FXML
	private TextField addTerminalNumberOfExitsTF;
	@FXML
	private TextField addUsernameTF;
	@FXML
	private TextField addPasswordTF;
	@FXML
	private Button addUserButton;
	@FXML
	private TextField updateUsernameTF;
	@FXML
	private TextField updateOldPasswordTF;
	@FXML
	private TextField updateNewPasswordTF;
	@FXML
	private TextField deleteUsernameTF;
	@FXML
	private TextField deleteTerminalIDTF;
	@FXML
	private Button deleteUser;
	@FXML
	private TextField personIdTF;
	@FXML
	private Button downloadPersonDocumentsButton;
	@FXML
	private Button downloadWantedPeopleDocumentButton;
	@FXML
	private ImageView documentsImage;
	@FXML
	private ImageView downloadImage;

	private static final String RESOURCES = "." + File.separator + "resources" + File.separator;
	private static final String DOWNLOAD_ICON = RESOURCES + "download.png";
	private static final String DOCUMENTS_ICON = RESOURCES + "documents.png";

	private Stage stage;
	private Client client;

	public AdminAppController() {
		client = ClientBuilder.newClient();
	}

	public void initialize() {
		Image downloadIcon = new Image(new File(DOWNLOAD_ICON).toURI().toString());
		downloadImage.setImage(downloadIcon);
		Image documentsIcon = new Image(new File(DOCUMENTS_ICON).toURI().toString());
		documentsImage.setImage(documentsIcon);
	}

	@FXML
	public void addUser(ActionEvent event) {
		stage = (Stage) tabPane.getScene().getWindow();
		String username = addUsernameTF.getText();
		String password = addPasswordTF.getText();
		if (username.isEmpty() || password.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		User user = new User(username, password);
		WebTarget users = client.target(AdminApplication.getBaseRedis()).path("/users");
		try (Response response = users.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON));) {

			if (!response.getStatusInfo().equals(Status.OK)) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
				return;
			}
		}
		Util.showAlert(stage, AlertType.CONFIRMATION, "Informacija", "Korisnik uspješno dodan.");
	}

	@FXML
	public void updateUser(ActionEvent event) {
		stage = (Stage) tabPane.getScene().getWindow();
		String username = updateUsernameTF.getText();
		String oldPassword = updateOldPasswordTF.getText();
		String newPassword = updateNewPasswordTF.getText();
		if (username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		User user = new User(username, newPassword);
		WebTarget users = client.target(AdminApplication.getBaseRedis()).path("/users");
		try (Response response = users.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(user, MediaType.APPLICATION_JSON));) {
			if (!response.getStatusInfo().equals(Status.NO_CONTENT)) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
				return;
			}
		}
		Util.showAlert(stage, AlertType.CONFIRMATION, "Informacija", "Korisnik uspješno ažuriran.");
	}

	@FXML
	public void deleteUser(ActionEvent event) {
		stage = (Stage) tabPane.getScene().getWindow();
		String username = deleteUsernameTF.getText();
		if (username.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		WebTarget users = client.target(AdminApplication.getBaseRedis()).path("/users").path(username);
		try (Response response = users.request().delete();) {
			if (!response.getStatusInfo().equals(Status.NO_CONTENT)) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
				return;
			}
		}
		Util.showAlert(stage, AlertType.CONFIRMATION, "Informacija", "Korisnik uspješno obrisan.");
	}

	@FXML
	public void downloadPersonDocuments(ActionEvent event) {
		if (personIdTF.getText().isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli parametar.");
			return;
		}
		String personId = personIdTF.getText();
		WebTarget warrants = client.target(AdminApplication.getBaseFileServer()).path("documents").path(personId);
		try (Response response = warrants.request().get()) {
			if (!response.getStatusInfo().equals(Status.OK)) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
				return;
			}
			final ArrayList<byte[]> result = response.readEntity(new GenericType<ArrayList<byte[]>>() {
			});
			File file = new File(AdminApplication.RESOURCES_PATH + "passengers" + File.separator + personId);
			if (!file.exists())
				file.mkdir();

			int i = 1;
			for (byte[] content : result) {
				try {
					Files.write(Paths.get(file + File.separator + i + ".zip"), content);
					Util.showAlert(stage, AlertType.INFORMATION, "Informacija", "Fajl je uspješno saèuvan.");
				} catch (IOException e) {
					Logger.getLogger(AdminApplication.class.getName()).log(Level.SEVERE,
							e.fillInStackTrace().toString());
					Util.showAlert(stage, AlertType.ERROR, "Greška", "Fajl nije uspješno saèuvan.");
				}
				i++;
			}
		}
	}

	@FXML
	public void downloadWantedPeopleDocument(ActionEvent event) {
		WebTarget warrants = client.target(AdminApplication.getBaseCentralRegistry()).path("/warrants");
		try (Response response = warrants.request().get();) {
			if (!response.getStatusInfo().equals(Status.OK)) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
				return;
			}
			final List<String> lines = response.readEntity(new GenericType<List<String>>() {
			});
			File file = new File(AdminApplication.RESOURCES_PATH + "warrants.xml");
			try (PrintWriter pw = new PrintWriter(file)) {
				lines.forEach(l -> pw.println(l));
				Util.showAlert(stage, AlertType.INFORMATION, "Informacija", "Fajl je uspješno saèuvan.");
			} catch (IOException e) {
				Logger.getLogger(AdminApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Fajl nije uspješno saèuvan.");
			}
		}
	}

	@FXML
	public void searchTerminal(ActionEvent event) {
		Stage stage = (Stage) tabPane.getScene().getWindow();
		String terminalId = searchTerminalIdTF.getText();
		if (terminalId.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		BorderTerminalSOAPServiceServiceLocator locator = new BorderTerminalSOAPServiceServiceLocator();
		BorderTerminalSOAPService service;
		try {
			service = locator.getBorderTerminalSOAPService();
			BorderTerminal terminal = service.getBorderTerminal(terminalId);
			if (terminal == null) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Traženi terminal ne postoji.");
				return;
			}
			updateTerminalIdLabel.setText(terminal.getId());
			updateTerminalNameLabel.setText(terminal.getName());
			String text = Arrays.asList(terminal.getCheckPoints()).stream().map(c -> c.getId()).distinct().reduce(" ",
					(s1, s2) -> s1.concat(s2 + " "));
			entriesLabel.setText(text);
		} catch (ServiceException | RemoteException e) {
			Logger.getLogger(AdminApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
		}
	}

	@FXML
	public void updateTerminal(ActionEvent event) {
		Stage stage = (Stage) tabPane.getScene().getWindow();
		String terminalName = updateTerminalNameLabel.getText();
		String terminalId = updateTerminalIdLabel.getText();
		if (terminalName.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		BorderTerminalSOAPServiceServiceLocator locator = new BorderTerminalSOAPServiceServiceLocator();
		BorderTerminalSOAPService service;
		try {
			service = locator.getBorderTerminalSOAPService();
			BorderTerminal terminal = service.getBorderTerminal(terminalId);
			terminal.setName(terminalName);
			BorderTerminal updatedTerminal = service.updateBorderTerminal(terminal);
			if (updatedTerminal != null)
				Util.showAlert(stage, AlertType.INFORMATION, "Informacija", "Uspješno ste ažurirali terminal.");
			else
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Neuspješno ažuriranje.");
		} catch (ServiceException | RemoteException e) {
			Logger.getLogger(AdminApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
		}
	}

	@FXML
	public void deleteTerminal(ActionEvent event) {
		Stage stage = (Stage) tabPane.getScene().getWindow();
		String terminalId = deleteTerminalIDTF.getText();
		if (terminalId.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		BorderTerminalSOAPServiceServiceLocator locator = new BorderTerminalSOAPServiceServiceLocator();
		BorderTerminalSOAPService service;
		try {
			service = locator.getBorderTerminalSOAPService();
			boolean result = service.deleteBorderTerminal(terminalId);
			if (result) {
				Util.showAlert(stage, AlertType.INFORMATION, "Informacija", "Uspješno obrisan terminal.");
			} else
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Neuspješno brisanje.");
		} catch (ServiceException | RemoteException e) {
			Logger.getLogger(AdminApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
		}
	}

	@FXML
	public void addTerminal(ActionEvent event) {
		Stage stage = (Stage) tabPane.getScene().getWindow();
		String terminalName = addTerminalNameTF.getText();
		String numberOfEntries = addTerminalNumberOfEntriesTF.getText();
		String numberOfExits = addTerminalNumberOfExitsTF.getText();
		if (terminalName.isEmpty() || numberOfEntries.isEmpty() || numberOfExits.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		int total = Integer.parseInt(numberOfExits) + Integer.parseInt(numberOfEntries);
		BorderCheckpoint[] checkPoints = new BorderCheckpoint[2 * total];
		for (int i = 0; i < 2 * total; i += 2) {
			checkPoints[i] = new BorderCheckpoint(i < Integer.parseInt(numberOfEntries), "0", true);
			checkPoints[i + 1] = new BorderCheckpoint(i < Integer.parseInt(numberOfEntries), "0", false);
		}
		BorderTerminal borderTerminal = new BorderTerminal(checkPoints, 0, "0", terminalName);
		BorderTerminalSOAPServiceServiceLocator locator = new BorderTerminalSOAPServiceServiceLocator();
		BorderTerminalSOAPService service;
		try {
			service = locator.getBorderTerminalSOAPService();
			String terminalId = service.addBorderTerminal(borderTerminal);
			Util.showAlert(stage, AlertType.INFORMATION, "Informacija", "Uspješno dodan terminal: " + terminalId);
		} catch (ServiceException | RemoteException e) {
			Logger.getLogger(AdminApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
		}
	}
}
