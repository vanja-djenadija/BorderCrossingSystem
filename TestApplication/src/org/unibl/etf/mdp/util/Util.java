package org.unibl.etf.mdp.util;

import java.io.File;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Util {

	private static final String ICON = "." + File.separator + "resources" + File.separator + "icon.png";
	
	public static void showAlert(Stage stage, AlertType type, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle("Obavještenje");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public static void showStage(Stage oldStage, FXMLLoader loader, int width, int height, String title)
			throws Exception {
		Parent root = loader.load();
		Scene scene = new Scene(root, width, height);
		Image applicationIcon = new Image(new File(ICON).toURI().toString());
		Stage newStage = new Stage();
		newStage.getIcons().add(applicationIcon);
		newStage.setResizable(false);
		newStage.setTitle(title);
		newStage.setScene(scene);
		oldStage.hide();
		newStage.show();
	}
}
