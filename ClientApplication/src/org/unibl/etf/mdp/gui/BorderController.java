package org.unibl.etf.mdp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.model.BorderCheckpoint;
import org.unibl.etf.mdp.model.BorderTerminal;
import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.service.BorderTerminalSOAPService;
import org.unibl.etf.mdp.service.BorderTerminalSOAPServiceServiceLocator;
import org.unibl.etf.mdp.service.ClientAppService;
import org.unibl.etf.mdp.service.ClientAppServiceServiceLocator;
import org.unibl.etf.mdp.util.Util;

import javafx.event.ActionEvent;

import javafx.scene.control.RadioButton;

import javafx.scene.Group;

public class BorderController {
	@FXML
	private ImageView image;
	@FXML
	private TextField terminalIDTF;
	@FXML
	private TextField entryIDTF;
	@FXML
	private Button continueButton;
	@FXML
	private Group group;
	@FXML
	private RadioButton policeRB;
	@FXML
	private RadioButton customsRB;
	@FXML
	private ToggleGroup toggleGroup;
		
    public void initialize() {
		Image applicationIcon = new Image(new File(ClientApplication.BORDER_ICON_PATH).toURI().toString());
		image.setImage(applicationIcon);
    }

	@FXML
	public void continueToLogin(ActionEvent event) {
		Stage stage = (Stage) terminalIDTF.getScene().getWindow();
		String terminalId = terminalIDTF.getText();
		String entryID = entryIDTF.getText();
		if (terminalId.isEmpty() || entryID.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
			return;
		}
		BorderTerminalSOAPServiceServiceLocator locator = new BorderTerminalSOAPServiceServiceLocator();
		try {
			BorderTerminalSOAPService service = locator.getBorderTerminalSOAPService();
			BorderTerminal terminal = service.getBorderTerminal(terminalId);
			if (terminal == null) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Ne postoji dati terminal");
				return;
			}
			BorderCheckpoint[] checkopoints = terminal.getCheckPoints();
			boolean result = false;
			for (BorderCheckpoint checkpoint : checkopoints) {
				if (checkpoint.getId().equals(entryID)) {
					result = true;
					break;
				}
			}
			if (result == false) {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Ne postoji terminal sa datim ulazom.");
				return;
			}
			ClientAppServiceServiceLocator loc = new ClientAppServiceServiceLocator();
			ClientAppService serv = loc.getClientAppService();
			if (!serv.isActive(new Client(entryID, policeRB.isSelected(), terminalId))) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
				loader.setControllerFactory(c -> new LoginController(terminalId, entryID, policeRB.isSelected()));
				Util.showStage(stage, loader, 400, 400, "Login");
			} else {
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Veæ je prijavljen korisnik.");
			}
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
