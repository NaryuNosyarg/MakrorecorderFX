package de.saxsys.MakrorecorderFX.viewmodel;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.saxsys.MakrorecorderFX.model.TestCase;
import de.saxsys.MakrorecorderFX.model.Testfallverwaltung;
import de.saxsys.MakrorecorderFX.view.MainView;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationObserver;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	public MainViewModel(Testfallverwaltung verwaltung) {
		this.verwaltung = verwaltung;

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

	public void openFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Datei wählen");
		fileChooser.setInitialDirectory(new File(System
				.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Javadateien", "*.jar"),
				new FileChooser.ExtensionFilter("Alle Dateien", "*.*"));
		selectedFile = fileChooser.showOpenDialog(setParentStageFunction.apply(null));
		if (selectedFile != null) {
			openFile(selectedFile);
		}
	}

	private void openFile(File file) {
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
		
	}
	
	public void closeFile(File file) {
		//TO DO
		recordingOn = false;
	}
	
	public boolean isRecording() {

		if (recordingOn) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<TestCase> testCaseListProperty() {
		MvvmFX.getNotificationCenter().publish("update");
		return testCaseList;
	}

	public ObjectProperty<TestCase> selectedTestCaseProperty() {
		return selectedTestCase;
	}
	

}
