package org.unibl.etf.mdp.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.mdp.util.Util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class AdminApplication extends Application {

	public static final String RESOURCES_PATH = "." + File.separator + "resources" + File.separator;
	public static final String LOG_PATH = RESOURCES_PATH + File.separator + "logs" + File.separator
			+ "admin_application.log";
	private static final String CONFIG_PATH = RESOURCES_PATH + "config.properties";
	
	private static String baseRedis, baseCentralRegistry, baseFileServer;
	private static FileHandler handler;

	static {
		try {
			handler = new FileHandler(LOG_PATH, true);
			Logger.getLogger(AdminApplication.class.getName()).setUseParentHandlers(false);
			Logger.getLogger(AdminApplication.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminApp.fxml"));
		try {
			Util.showStage(stage, loader, 600, 400, "Admin Application");
		} catch (Exception e) {
			Logger.getLogger(AdminApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public static void main(String[] args) {
		try {
			loadProperties();
		} catch (Exception e) {
			Logger.getLogger(AdminApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		} 
		launch(args);
	}

	private static void loadProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(CONFIG_PATH));
		baseRedis = properties.getProperty("BASE_REDIS");
		baseCentralRegistry = properties.getProperty("BASE_CENTRAL_REGISTRY");
		baseFileServer = properties.getProperty("BASE_FILE_SERVER");
	}

	public static String getBaseRedis() {
		return baseRedis;
	}

	public static String getBaseCentralRegistry() {
		return baseCentralRegistry;
	}

	public static String getBaseFileServer() {
		return baseFileServer;
	}
}
