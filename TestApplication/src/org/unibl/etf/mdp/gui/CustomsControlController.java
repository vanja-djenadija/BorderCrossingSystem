package org.unibl.etf.mdp.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.model.Document;
import org.unibl.etf.mdp.service.TestAppService;
import org.unibl.etf.mdp.service.TestAppServiceServiceLocator;
import org.unibl.etf.mdp.util.Util;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class CustomsControlController {

	@FXML
	private Button finishButton;
	@FXML
	private Button uploadButton;
	@FXML
	private ImageView image;

	private Client client;
	private String passengerId;
	private List<File> files;

	public CustomsControlController(Client client, String passengerId) {
		this.client = client;
		this.passengerId = passengerId;
	}

	public void initialize() {
		Image icon = new Image(new File(TestApplication.CUSTOMS_ICON).toURI().toString());
		image.setImage(icon);
	}
	
	@FXML
	public void finish(ActionEvent event) {
		Stage stage = (Stage) finishButton.getScene().getWindow();
		try {
			TestAppServiceServiceLocator locator = new TestAppServiceServiceLocator();
			TestAppService service = locator.getTestAppService();
			byte[] files = zipFiles();
			service.addPassengerToCustomsControl(client, new Document(files, passengerId));
			while (!service.passengerPassedCustomsControl(passengerId)) {
				Thread.sleep(1000);
			}
			Platform.exit();
		} catch (Exception e) {
			Util.showAlert(stage, AlertType.ERROR, "Greška", "Greška.");
			e.printStackTrace();
		}
	}

	private byte[] zipFiles() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(baos);
		for (File file : files) {
			ZipEntry entry = new ZipEntry(file.getName());
			out.putNextEntry(entry);
			byte[] data = Files.readAllBytes(file.toPath());
			out.write(data, 0, data.length);
			out.closeEntry();
		}
		out.close();
		return baos.toByteArray();
	}

	@FXML
	public void uploadDocuments(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		Stage stage = (Stage) finishButton.getScene().getWindow();
		files = chooser.showOpenMultipleDialog(stage);
	}
}
