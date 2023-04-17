package org.unibl.etf.mdp.gui;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.unibl.etf.mdp.model.User;
import org.unibl.etf.mdp.util.Util;

import javafx.event.ActionEvent;

import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ChangePasswordController {
	@FXML
	private Button saveButton;
	@FXML
	private PasswordField passwordTF;
	@FXML
	private ImageView image;
	
	private static final String PASSWORD_ICON = ChatController.RESOURCES_PATH + "password.png";
	private Client client = ClientBuilder.newClient();
	private String username;
	private Stage oldStage;

	public ChangePasswordController(String username, Stage oldStage) {
		this.username = username;
		this.oldStage = oldStage;
	}

	public void initialize() {
		Image icon = new Image(new File(PASSWORD_ICON).toURI().toString());
		image.setImage(icon);
	}

	@FXML
	public void changePassword(ActionEvent event) {
		Stage stage = (Stage) saveButton.getScene().getWindow();
		String newPassword = passwordTF.getText();
		if (newPassword.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli parametar.");
			return;
		}
		User user = new User(username, newPassword);
		WebTarget users = client.target(ClientApplication.getBaseRedis()).path("/users");
		try (Response response = users.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(user, MediaType.APPLICATION_JSON))) {
			if (!response.getStatusInfo().equals(Status.NO_CONTENT)) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
				return;
			}
		}
		Util.showAlert(stage, AlertType.INFORMATION, "Informacija", "Uspješno promijenjena lozinka.");
		stage.hide();
		oldStage.show();
	}
}
