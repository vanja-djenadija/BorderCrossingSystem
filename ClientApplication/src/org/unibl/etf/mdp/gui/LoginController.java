package org.unibl.etf.mdp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.unibl.etf.mdp.model.ChatClient;
import org.unibl.etf.mdp.model.User;
import org.unibl.etf.mdp.service.ClientAppService;
import org.unibl.etf.mdp.service.ClientAppServiceServiceLocator;
import org.unibl.etf.mdp.util.Util;

import javafx.event.ActionEvent;

import javafx.scene.control.PasswordField;

public class LoginController {
	@FXML
	private ImageView image;
	@FXML
	private TextField usernameTF;
	@FXML
	private Button logInButton;
	@FXML
	private PasswordField passwordTF;

	private Stage stage;
	private Client client = ClientBuilder.newClient();
	private String terminalId;
	private String entryId;
	private boolean police;

	public LoginController(String terminalId, String entryId, boolean police) {
		this.terminalId = terminalId;
		this.entryId = entryId;
		this.police = police;
	}

	public void initialize() {
		Image applicationIcon = new Image(new File(ClientApplication.BORDER_ICON_PATH).toURI().toString());
		image.setImage(applicationIcon);
	}

	@FXML
	public void logIn(ActionEvent event) {
		stage = (Stage) usernameTF.getScene().getWindow();
		String username = usernameTF.getText();
		String password = passwordTF.getText();
		if (username.isEmpty() || password.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		WebTarget users = client.target(ClientApplication.getBaseRedis()).path("/users").path(username);
		try (Response response = users.request(MediaType.APPLICATION_JSON).get()) {
			if (!response.getStatusInfo().equals(Status.OK)) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Ne postoji korisnik sa datim kredencijalima.");
				return;
			}
			User result = response.readEntity(new GenericType<User>() {
			});
			if (!result.getPassword().equals(password)) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Neispravna lozinka.");
				return;
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
			ChatClient client = new ChatClient(username, terminalId, entryId, police);
			loader.setControllerFactory(c -> new ChatController(client));
			ClientAppServiceServiceLocator locator = new ClientAppServiceServiceLocator();
			ClientAppService service = locator.getClientAppService();
			service.login(new org.unibl.etf.mdp.model.Client(entryId, police, terminalId));
			Util.showStage(stage, loader, 600, 600, "Chat");
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
