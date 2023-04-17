package org.unibl.etf.mdp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.rpc.ServiceException;

import org.unibl.etf.mdp.control.ClientReadMessagesThread;
import org.unibl.etf.mdp.control.ClientWriteMessagesThread;
import org.unibl.etf.mdp.control.CustomsControlThread;
import org.unibl.etf.mdp.control.GetActiveClientsThread;
import org.unibl.etf.mdp.control.PoliceControlThread;
import org.unibl.etf.mdp.model.ChatClient;
import org.unibl.etf.mdp.model.Client;
import org.unibl.etf.mdp.model.Message;
import org.unibl.etf.mdp.protocol.ProtocolMessages;
import org.unibl.etf.mdp.service.ClientAppService;
import org.unibl.etf.mdp.service.ClientAppServiceServiceLocator;
import org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPService;
import org.unibl.etf.mdp.service.PassengerCheckRecordsSOAPServiceServiceLocator;
import org.unibl.etf.mdp.util.Color;
import org.unibl.etf.mdp.util.Util;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;

public class ChatController {
	@FXML
	private ImageView unicastImage;
	@FXML
	private ImageView multicastImage;
	@FXML
	private ImageView broadcastImage;
	@FXML
	private ImageView unicastSendImage;
	@FXML
	private ImageView multicastSendImage;
	@FXML
	private ImageView broadcastSendImage;
	@FXML
	private ListView<String> usersUnicastLV;
	@FXML
	private VBox unicastMessagesVBox;
	@FXML
	private TextField unicastSendMessageTF;
	@FXML
	private VBox multicastMessagesVBox;
	@FXML
	private VBox broadcastMessagesVBox;
	@FXML
	private TextField multicastSendMessageTF;
	@FXML
	private TextField broadcastSendMessageTF;
	@FXML
	private HBox topHbox;
	@FXML
	private Button continueButton;

	private Stage stage;

	public static final String RESOURCES_PATH = "." + File.separator + "resources" + File.separator;
	private static final String UNICAST_ICON = RESOURCES_PATH + "unicast.png";
	private static final String MULTICAST_ICON = RESOURCES_PATH + "multicast.png";
	private static final String BROADCAST_ICON = RESOURCES_PATH + "broadcast.png";
	private static final String SEND_ICON = RESOURCES_PATH + "send.png";

	private ChatClient client; // myself
	private ChatClient clientTo;
	private HashMap<ChatClient, ArrayList<Message>> messages = new HashMap<>(); // inbox
	private ArrayList<Message> multicastMessages = new ArrayList<>();
	private ArrayList<Message> broadcastMessages = new ArrayList<>();
	private Consumer<HashSet<ChatClient>> showActiveClients;
	private Consumer<Message> showMessage;
	private Runnable sendNotification; // person on warrants crossing border
	private Runnable showContinueButton;
	private ClientWriteMessagesThread writeMessagesThread;
	private HashSet<ChatClient> activeClients;

	@SuppressWarnings("unused")
	public ChatController(ChatClient client) {
		this.client = client;
		showActiveClients = clients -> {
			activeClients = clients;
			Platform.runLater(() -> {
				usersUnicastLV.getItems().clear();
				clients.stream().filter(c -> !c.equals(client))
						.forEach(c -> usersUnicastLV.getItems().add(c.toString()));
			});
		};
		showMessage = message -> {
			System.out.println("SHOW MESSAGE");
			Platform.runLater(() -> {
				if (message.getType().equals(ProtocolMessages.UNICAST)) {
					if (!messages.containsKey(message.getFrom())) {
						messages.put(message.getFrom(), new ArrayList<Message>());
					}
					messages.get(message.getFrom()).add(message);
					System.out.println(messages.get(message.getFrom()).size());
					updateMessages();
				}
				if (message.getType().equals(ProtocolMessages.MULTICAST)) {
					synchronized (multicastMessages) {
						multicastMessages.add(message);
						multicastMessagesVBox.getChildren().add(Util.messageComponent(message));
					}
				}
				if (message.getType().equals(ProtocolMessages.BROADCAST)) {
					synchronized (broadcastMessages) {
						broadcastMessages.add(message);
						broadcastMessagesVBox.getChildren().add(Util.messageComponent(message));
					}
				}
			});
		};
		GetActiveClientsThread getActiveClientsThread = new GetActiveClientsThread(showActiveClients);
		ClientReadMessagesThread readMessagesThread = new ClientReadMessagesThread(client, showMessage);
		writeMessagesThread = new ClientWriteMessagesThread(client, showMessage, this);
		Client c = new Client(client.getEntryId(), client.isPolice(), client.getTerminalId());
		CustomsControlThread customsControlThread = new CustomsControlThread(c, (s) -> {
		});
		sendNotification = () -> {
			ArrayList<ChatClient> filteredClients = activeClients.stream()
					.filter(cli -> cli.getTerminalId().equals(client.getTerminalId()))
					.collect(Collectors.toCollection(ArrayList::new));
			String message = "TERMINAL_CLOSED_TEMPORARILY: Lice na potjernici";
			writeMessagesThread.send(new Message(ProtocolMessages.MULTICAST, client, filteredClients, message));
		};
		showContinueButton = () -> {
			System.out.println("SHOW BUTTTTON");
			Platform.runLater(() -> {
				continueButton.setVisible(true);
			});
		};
		PoliceControlThread policeControlThread = new PoliceControlThread(c, sendNotification, showContinueButton);
	}

	public void initialize() {
		Image unicastIcon = new Image(new File(UNICAST_ICON).toURI().toString());
		unicastImage.setImage(unicastIcon);
		Image multicastIcon = new Image(new File(MULTICAST_ICON).toURI().toString());
		multicastImage.setImage(multicastIcon);
		Image broadcastIcon = new Image(new File(BROADCAST_ICON).toURI().toString());
		broadcastImage.setImage(broadcastIcon);
		Image sendIcon = new Image(new File(SEND_ICON).toURI().toString());
		unicastSendImage.setImage(sendIcon);
		multicastSendImage.setImage(sendIcon);
		broadcastSendImage.setImage(sendIcon);
	}

	@FXML
	public void onClientSelected(MouseEvent event) {
		String to = usersUnicastLV.getSelectionModel().getSelectedItem();
		System.out.println("SELECTED " + to);
		if (to == null)
			return;
		String[] elements = to.split("#");
		clientTo = new ChatClient(elements[0], elements[1], elements[2], Boolean.parseBoolean(elements[3]));
		System.out.println("SELECTED " + clientTo);
		updateMessages();
	}

	@FXML
	public void unicastSendMessage(ActionEvent event) {
		System.out.println("UNICAST SEND MESSAGE " + clientTo);
		String content = unicastSendMessageTF.getText();
		if (content.isEmpty())
			return;
		if (clientTo == null)
			return;
		ArrayList<ChatClient> to = new ArrayList<ChatClient>();
		to.add(clientTo);
		Message message = new Message(ProtocolMessages.UNICAST, client, to, content);
		writeMessagesThread.send(message);
	}

	@FXML
	public void multicastSendMessage(ActionEvent event) {
		String message = multicastSendMessageTF.getText();
		if (message.isEmpty())
			return;

		ArrayList<ChatClient> filteredClients = activeClients.stream()
				.filter(c -> c.getTerminalId().equals(client.getTerminalId()))
				.collect(Collectors.toCollection(ArrayList::new));
		writeMessagesThread.send(new Message(ProtocolMessages.MULTICAST, client, filteredClients, message));
	}

	@FXML
	public void broadcastSendMessage(ActionEvent event) {
		String message = broadcastSendMessageTF.getText();
		if (message.isEmpty())
			return;
		writeMessagesThread.send(new Message(ProtocolMessages.BROADCAST, client,
				activeClients.stream().collect(Collectors.toCollection(ArrayList::new)), message));
	}

	@FXML
	public void downloadLogs(ActionEvent event) {
		stage = (Stage) usersUnicastLV.getScene().getWindow();
		PassengerCheckRecordsSOAPServiceServiceLocator locator = new PassengerCheckRecordsSOAPServiceServiceLocator();
		try {
			PassengerCheckRecordsSOAPService service = (PassengerCheckRecordsSOAPService) locator
					.getPassengerCheckRecordsSOAPService();
			byte[] document = service.getPassengerRecords();
			try {
				Files.write(Paths.get(RESOURCES_PATH + "passengers.txt"), document);
				Util.showAlert(stage, AlertType.INFORMATION, "Informacija", "Fajl je uspješno saèuvan.");
			} catch (IOException e) {
				Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
				Util.showAlert(stage, AlertType.ERROR, "Greška", "Fajl nije uspješno saèuvan.");
			}
		} catch (ServiceException | RemoteException e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	@FXML
	public void changePassword(ActionEvent event) {
		stage = (Stage) usersUnicastLV.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePassword.fxml"));
		loader.setControllerFactory(c -> new ChangePasswordController(client.getUsername(), stage));
		try {
			Util.showStage(stage, loader, 400, 300, "Change Password");
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	@FXML
	public void logOut(ActionEvent event) {
		writeMessagesThread.send(new Message(client));
		ClientAppServiceServiceLocator locator = new ClientAppServiceServiceLocator();
		try {
			ClientAppService service = locator.getClientAppService();
			service.logout(
					new org.unibl.etf.mdp.model.Client(client.getEntryId(), client.isPolice(), client.getTerminalId()));
		} catch (Exception e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		Platform.exit();
	}

	@FXML
	public void continueTerminal() {
		System.out.println("CONTINUE BUTTON CLICKED");
		ClientAppServiceServiceLocator locator = new ClientAppServiceServiceLocator();
		try {
			ClientAppService service = locator.getClientAppService();
			service.activateTerminal(client.getTerminalId());
		} catch (ServiceException | RemoteException e) {
			Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		Platform.runLater(() -> {
			continueButton.setVisible(false);
		});
	}

	public HashMap<ChatClient, ArrayList<Message>> getMessages() {
		return messages;
	}

	public void updateMessages() {
		unicastMessagesVBox.getChildren().clear();
		if (messages.get(clientTo) == null) {

			return;
		}
		messages.get(clientTo).forEach(m -> {
			if (m.getFrom().equals(client))
				unicastMessagesVBox.getChildren().add(Util.displayMessage(m, Color.MY_MESSAGES_COLOR.getColor()));
			else
				unicastMessagesVBox.getChildren().add(Util.displayMessage(m, Color.OTHER_MESSAGES_COLOR.getColor()));
		});
	}
}
