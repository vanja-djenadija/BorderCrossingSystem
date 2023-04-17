package org.unibl.etf.mdp.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TestApplication extends Application {
	public static final String RESOURCES = "." + File.separator + "resources" + File.separator;
	public static final String CONFIG = RESOURCES + "config.properties";
	public static final String ICON = RESOURCES + "icon.png";
	public static final String POLICE_ICON = RESOURCES + "police.png";
	public static final String CUSTOMS_ICON = RESOURCES + "customs.png";
	public static final String LOG_PATH = RESOURCES + "logs" + File.separator + "test.log";
	private static String warrantsRegistryPort;
	private static String warrantsRegistryServiceName;

	private static FileHandler handler;

	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(TestApplication.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(TestApplication.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Border.fxml"));
		Scene scene = new Scene(loader.load(), 400, 400);
		Image applicationIcon = new Image(new File(ICON).toURI().toString());
		stage.getIcons().add(applicationIcon);
		stage.setScene(scene);
		stage.setTitle("Test Application");
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		try {
			loadProperties();
		} catch (Exception e) {
			Logger.getLogger(TestApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		launch(args);
	}

	private static void loadProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(CONFIG));
		warrantsRegistryPort = properties.getProperty("PORT");
		warrantsRegistryServiceName = properties.getProperty("SERVICE_NAME");
	}

	public static String getWarrantsRegistryPort() {
		return warrantsRegistryPort;
	}

	public static String getWarrantsRegistryServiceName() {
		return warrantsRegistryServiceName;
	}

}
