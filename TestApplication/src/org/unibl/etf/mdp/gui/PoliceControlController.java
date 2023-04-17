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
import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.service.TestAppService;
import org.unibl.etf.mdp.service.TestAppServiceServiceLocator;
import org.unibl.etf.mdp.util.Util;

import javafx.event.ActionEvent;

public class PoliceControlController {

	@FXML
	private Button continueButton;
	@FXML
	private TextField passengerIDTF;
	@FXML
	private ImageView image;

	private Client client;

	public PoliceControlController(Client client) {
		this.client = client;
	}

	public void initialize() {
		Image icon = new Image(new File(TestApplication.POLICE_ICON).toURI().toString());
		image.setImage(icon);
	}

	@FXML
	public void continueToBorderControl(ActionEvent event) {
		Stage stage = (Stage) continueButton.getScene().getWindow();
		String passengerId = passengerIDTF.getText();
		if (passengerId.isEmpty()) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Niste unijeli sve parametre.");
			return;
		}
		try {
			TestAppServiceServiceLocator locator = new TestAppServiceServiceLocator();
			TestAppService service = locator.getTestAppService();
			service.addPassengerToPoliceControl(client, passengerId);
			int wait = 0;
			while (!service.passengerPassedPoliceControl(passengerId)) {
				Thread.sleep(1000);
				wait++;
				if (wait == 5) {
					Util.showAlert(stage, AlertType.WARNING, "Upozorenje", "Nije moguæe iæi na carinsku kontrolu.");
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Border.fxml"));
					Util.showStage(stage, loader, 400, 400, "Test Application");
					stage.hide();
					return;
				}
			}

			FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomsControl.fxml"));
			client.setPolice(false);
			loader.setControllerFactory(c -> new CustomsControlController(client, passengerId));
			Util.showStage(stage, loader, 400, 300, "Customs Control");
			stage.hide();
		} catch (Exception e) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
			e.printStackTrace();
		}
	}
}
