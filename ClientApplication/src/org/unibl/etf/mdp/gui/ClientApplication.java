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

public class ClientApplication extends Application {

	public static final String BORDER_ICON_PATH = "." + File.separator + "resources" + File.separator + "border-icon.png";
	public static final String CONFIG_PATH = "." + File.separator + "resources" + File.separator + "config.properties";
	public static final String LOG_PATH = "." + File.separator + "resources" + File.separator + "logs" + File.separator
			+ "client_application.log";
	private static int readPort, writePort, multicastPort, warrantsRmiPort, filesRmiPort;
	private static String keystorePath, keystorePassword, host, multicastHost, warrantsRmiName, filesRmiName, baseRedis;
	private static FileHandler handler;

	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(ClientApplication.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(ClientApplication.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Border.fxml"));
		Scene scene = new Scene(loader.load(), 400, 400);
		Image applicationIcon = new Image(new File(BORDER_ICON_PATH).toURI().toString());
		stage.getIcons().add(applicationIcon);
		stage.setScene(scene);
		stage.setTitle("Client Application");
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		try {
			loadProperties();
			System.setProperty("javax.net.ssl.trustStore", keystorePath);
			System.setProperty("javax.net.ssl.trustStorePassword", keystorePassword);
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		launch(args);
	}

	private static void loadProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(CONFIG_PATH));
		keystorePath = properties.getProperty("KEYSTORE_PATH");
		keystorePassword = properties.getProperty("KEYSTORE_PASSWORD");
		writePort = Integer.parseInt(properties.getProperty("WRITE_PORT"));
		readPort = Integer.parseInt(properties.getProperty("READ_PORT"));
		host = properties.getProperty("HOST");
		multicastHost = properties.getProperty("MULTICAST_HOST");
		multicastPort = Integer.parseInt(properties.getProperty("MULTICAST_PORT"));
		warrantsRmiName = properties.getProperty("WARRANTS_RMI_NAME");
		warrantsRmiPort = Integer.parseInt(properties.getProperty("WARRANTS_RMI_PORT"));
		filesRmiName = properties.getProperty("FILES_RMI_NAME");
		filesRmiPort = Integer.parseInt(properties.getProperty("FILES_RMI_PORT"));
		baseRedis = properties.getProperty("BASE_REDIS");
	}

	public static int getReadPort() {
		return readPort;
	}

	public static void setReadPort(int readPort) {
		ClientApplication.readPort = readPort;
	}

	public static int getWritePort() {
		return writePort;
	}

	public static void setWritePort(int writePort) {
		ClientApplication.writePort = writePort;
	}

	public static String getKeystorePath() {
		return keystorePath;
	}

	public static void setKeystorePath(String keystorePath) {
		ClientApplication.keystorePath = keystorePath;
	}

	public static String getKeystorePassword() {
		return keystorePassword;
	}

	public static void setKeystorePassword(String keystorePassword) {
		ClientApplication.keystorePassword = keystorePassword;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		ClientApplication.host = host;
	}

	public static int getMulticastPort() {
		return multicastPort;
	}

	public static String getMulticastHost() {
		return multicastHost;
	}

	public static int getWarrantsRmiPort() {
		return warrantsRmiPort;
	}

	public static void setWarrantsRmiPort(int warrantsRmiPort) {
		ClientApplication.warrantsRmiPort = warrantsRmiPort;
	}

	public static String getWarrantsRmiName() {
		return warrantsRmiName;
	}

	public static void setWarrantsRmiName(String warrantsRmiName) {
		ClientApplication.warrantsRmiName = warrantsRmiName;
	}

	public static int getFilesRmiPort() {
		return filesRmiPort;
	}

	public static void setFilesRmiPort(int filesRmiPort) {
		ClientApplication.filesRmiPort = filesRmiPort;
	}

	public static String getFilesRmiName() {
		return filesRmiName;
	}

	public static void setFilesRmiName(String filesRmiName) {
		ClientApplication.filesRmiName = filesRmiName;
	}

	public static String getBaseRedis() {
		return baseRedis;
	}
}
