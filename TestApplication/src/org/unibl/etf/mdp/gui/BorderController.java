package org.unibl.etf.mdp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.service.TestAppService;
import org.unibl.etf.mdp.service.TestAppServiceServiceLocator;
import org.unibl.etf.mdp.util.Util;

import javafx.event.ActionEvent;

public class BorderController {

	@FXML
	private TextField terminalIDTF;
	@FXML
	private TextField entryIDTF;
	@FXML
	private Button continueButton;
	@FXML
	private ImageView image;

	public void initialize() {
		Image icon = new Image(new File(TestApplication.ICON).toURI().toString());
		image.setImage(icon);
	}

	@FXML
	public void continueToPoliceControl(ActionEvent event) {
		Stage stage = (Stage) continueButton.getScene().getWindow();
		String terminalId = terminalIDTF.getText();
		String entryId = entryIDTF.getText();
		if (terminalId.isEmpty() || entryId.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		try {
			TestAppServiceServiceLocator locator = new TestAppServiceServiceLocator();
			TestAppService service = locator.getTestAppService();
			Client policeClient = new Client(entryId, true, terminalId);
			Client customsClient = new Client(entryId, false, terminalId);
			if (service.isActive(customsClient) && service.isActive(policeClient)) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("PoliceControl.fxml"));
				loader.setControllerFactory(c -> new PoliceControlController(policeClient));
				Util.showStage(stage, loader, 400, 300, "Police Control");
				stage.hide();
			} else {
				Util.showAlert(stage, AlertType.INFORMATION, "Informacija", "Terminal nije aktivan.");
			}
		} catch (Exception e) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
			e.printStackTrace();
		}
	}
}
