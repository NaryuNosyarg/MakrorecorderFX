package de.saxsys.MakrorecorderFX.viewmodel;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.*;
import javafx.scene.input.*;
import de.saxsys.MakrorecorderFX.RemoteAppService;
import de.saxsys.MakrorecorderFX.model.TestCase;
import de.saxsys.MakrorecorderFX.model.Testfallverwaltung;
import de.saxsys.MakrorecorderFX.model.uievents.EventWrapper;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationObserver;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainViewModel implements ViewModel {

	private ObjectProperty<TestCase> selectedTestCase = new SimpleObjectProperty<>();
	private ObservableList<TestCase> testCaseList = FXCollections
			.observableArrayList();
	private Testfallverwaltung verwaltung;
	private Desktop desktop = Desktop.getDesktop();
	private Function<Void, Stage> setParentStageFunction;
	
	private boolean recordingOn = false;
	private File selectedFile;
	private RemoteAppService remoteAppService;
	
	private List<EventWrapper<? extends Event>> eventList = new ArrayList<>();
	
	
	private Map<String, Node> elements = new HashMap<>();
	private Node root;

	public MainViewModel(Testfallverwaltung verwaltung, RemoteAppService remoteAppService) {
		this.verwaltung = verwaltung;
		this.remoteAppService = remoteAppService;

		MvvmFX.getNotificationCenter().subscribe("update",
				new NotificationObserver() {
					@Override
					public void receivedNotification(String key,
							Object... payload) {
						updateTestCaseList();
					}
				});
	}

	private void updateTestCaseList() {
		testCaseList.clear();
		testCaseList.addAll(verwaltung.getTestCaseList());
		System.out.println(testCaseList);
	}

	public void setParentStageFunction(
			Function<Void, Stage> setParentStageFunction) {
		this.setParentStageFunction = setParentStageFunction;
	}

		
//		public void openFileChooser() {
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.setTitle("Datei wählen");
//		fileChooser.setInitialDirectory(new File(System
//				.getProperty("user.home")));
//		fileChooser.getExtensionFilters().addAll(
//				new FileChooser.ExtensionFilter("Javadateien", "*.jar"),
//				new FileChooser.ExtensionFilter("Alle Dateien", "*.*"));
//		selectedFile = fileChooser.showOpenDialog(setParentStageFunction.apply(null));
//		if (selectedFile != null) {
//			openFile(selectedFile);
// 		}
// }

	public void openRemoteApp() {
		
		if(recordingOn) {
			stop();
		} else {
			root = remoteAppService.getRootElement();
			
			traverseSceneGraphFromRoot();
			recordingOn = true;
		}


	}
	
	
	private void recordEvent(EventWrapper<? extends Event> event) {
		if(isRecording()) {
			eventList.add(event);
			traverseSceneGraphFromRoot();
		}
	}
	
	private void registerListener(Node element) {
		
		if(element instanceof Button) {
			Button button = (Button) element;
			
			button.setOnAction(event -> {
				System.out.println("Button " + button.getId() + " wurde gedrückt");
				
				recordEvent(new EventWrapper<ActionEvent>(button.getId(), event));
			});
		}
			
		if(element instanceof TextField) {
			TextField textField = (TextField) element;
			
			textField.setOnKeyTyped(event -> {
				System.out.println("In Textfeld " + textField.getId() + " wurde getippt:" + event.getCharacter());
				recordEvent(new EventWrapper<KeyEvent>(textField.getId(), event));
			});
			
			textField.setOnKeyPressed(event -> {
				System.out.println("In Textfeld " + textField.getId() + " wurde gedrückt:" + event.getCode());
				recordEvent(new EventWrapper< KeyEvent>(textField.getId(), event));
			});
			
			textField.setOnMouseClicked(event -> {
				System.out.println("In Textfeld " + textField.getId() + " wurde gedrückt:" + event.getClickCount());
				recordEvent(new EventWrapper<MouseEvent>(textField.getId(), event));
			});
		
			textField.setOnMouseClicked(event -> {
				System.out.println("In Textfeld " + textField.getId() + " wurde gedrückt:" + event.getButton());
				recordEvent(new EventWrapper<MouseEvent>(textField.getId(), event));
			});
		}
		
		if(element instanceof PasswordField) {
			PasswordField passwordField = (PasswordField) element;
			
			passwordField.setOnKeyTyped(event -> {
				System.out.println("in Passwordfeld " + passwordField.getId() + " wurde getippt:" + event.getCharacter());
				recordEvent(new EventWrapper<KeyEvent>(passwordField.getId(), event));
			});
		}
	}
	
	
	private void traverseSceneGraphFromRoot(){
		traverseSceneGraph(root);
	}
	
	private void traverseSceneGraph(Node element) {
		
		if(element instanceof Parent) {
			
			Parent parent = (Parent) element;
			
			parent.getChildrenUnmodifiable().forEach(child -> {
				traverseSceneGraph(child);
			});
		}
		
		
		
		if(element.getId() != null && !element.getId().trim().isEmpty()) {
			
			if(! elements.containsKey(element.getId())) {
				elements.put(element.getId(), element);
				
				registerListener(element);
			}
		}
		
	
	}

	/*private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		recordingOn = true;
	}
	

	public void closeOpenFile() {
		closeFile(selectedFile);
		
	}*/
	
	public void closeFile(File file) {
		//TO DO
		recordingOn = false;
	}
	
	public boolean isRecording() {
		return recordingOn;
	}

	public ObservableList<TestCase> testCaseListProperty() {
		MvvmFX.getNotificationCenter().publish("update");
		return testCaseList;
	}

	public ObjectProperty<TestCase> selectedTestCaseProperty() {
		return selectedTestCase;
	}
	
	public void stop() {
		recordingOn = false;
		
		System.out.println("Recorded Events:");
		
		StringBuilder testFXCode = new StringBuilder();
		
		eventList.forEach(eventWrapper -> {
			System.out.println(eventWrapper.getNodeId() + " => " + eventWrapper.getEvent().toString());
			
			
			Event event = eventWrapper.getEvent();
			
			if(event instanceof MouseEvent) {
				MouseEvent mouseEvent = (MouseEvent) event;
				
				testFXCode.append("click(#" + eventWrapper.getNodeId() + ")\n");
			}
			
			if(event instanceof KeyEvent) {
				KeyEvent keyEvent = (KeyEvent) event;
				
				if(keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
					
					testFXCode.append("type(\"" + keyEvent.getText() + "\")\n");
				
				}
				
			}
			
		});

		System.out.println("Unser TestFX-Code:");
		System.out.println(testFXCode.toString());
		
		
	}

}
