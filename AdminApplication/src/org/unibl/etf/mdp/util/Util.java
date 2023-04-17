package org.unibl.etf.mdp.util;

import java.io.File;
import org.unibl.etf.mdp.model.Message;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Util {

	private static final String BORDER_ICON = "." + File.separator + "resources" + File.separator + "border-icon.png";
	
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
		Image applicationIcon = new Image(new File(BORDER_ICON).toURI().toString());
		Stage newStage = new Stage();
		newStage.getIcons().add(applicationIcon);
		newStage.setResizable(false);
		newStage.setTitle(title);
		newStage.setScene(scene);
		oldStage.hide();
		newStage.show();
	}

	public static Node messageComponent(Message message) {
		Label label = new Label(message.getContent());
		label.setMaxWidth(500);
		label.setFont(new Font("System", 13));
		label.setPadding(new Insets(5));
		label.setWrapText(true);
		label.setStyle("-fx-background-color:#007ACC; -fx-background-radius: 20 20 20 20; -fx-text-fill:white");
		return label;
	}

	public static Node displayMessage(Message message, String color) {
		String m = String.format("%s:  %s\n%s", message.getFrom().getUsername(), message.getContent(),
				message.getDate());
		Label label = new Label(m);
		label.setMaxWidth(250);
		label.setFont(new Font("System", 13));
		label.setPadding(new Insets(5));
		label.setWrapText(true);
		label.setStyle("-fx-background-color:" + color + "; -fx-background-radius: 20 20 20 20; -fx-text-fill:white");
		HBox box = new HBox();
		Region region = new Region();
		region.setMinWidth(100);
		if (color.equals(Color.MY_MESSAGES_COLOR.getColor()))
			box.getChildren().add(label);
		else {
			HBox.setHgrow(region, Priority.ALWAYS);
			box.getChildren().addAll(region, label);
		}
		return box;
	}
}
