package de.saxsys.MakrorecorderFX.viewmodel;

import java.util.List;

import javafx.event.*;
import javafx.scene.input.*;
import de.saxsys.MakrorecorderFX.core.eventprocessing.EventProcessor;
import de.saxsys.MakrorecorderFX.core.eventrecording.Recorder;
import de.saxsys.MakrorecorderFX.core.remoteapp.RemoteAppService;
import de.saxsys.MakrorecorderFX.model.TestCase;
import de.saxsys.MakrorecorderFX.model.TestCaseManagement;
import de.saxsys.MakrorecorderFX.model.uievents.EventWrapper;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationObserver;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class MainViewModel implements ViewModel {

	private ObjectProperty<TestCase> selectedTestCase = new SimpleObjectProperty<>();
	private ObservableList<TestCase> testCaseList = FXCollections
			.observableArrayList();
	private TestCaseManagement management;
	private RemoteAppService remoteAppService;
	private final EventProcessor eventProcessor;
	private final Recorder recorder;
	private Node root;

	public MainViewModel(TestCaseManagement management,
			RemoteAppService remoteAppService, EventProcessor eventProcessor,
			Recorder recorder) {
		this.management = management;
		this.remoteAppService = remoteAppService;
		this.eventProcessor = eventProcessor;
		this.recorder = recorder;

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
		testCaseList.addAll(management.getTestCaseList());
		System.out.println(testCaseList);
	}

	public ObservableList<TestCase> testCaseListProperty() {
		MvvmFX.getNotificationCenter().publish("update");
		return testCaseList;
	}

	public ObjectProperty<TestCase> selectedTestCaseProperty() {
		return selectedTestCase;
	}

	public void openRemoteApp() throws Exception {

		if (recorder.isRecording()) {
			stop();
		} else {
			root = remoteAppService.getRootElement();

			recorder.start(root);
		}
	}

	public void closeRemoteApp() {
		// TODO
		stop();
	}

	public boolean isRecording() {
		return recorder.isRecording();
	}

	public void stop() {

		List<EventWrapper<? extends Event>> eventList = recorder.stop();
		eventProcessor.processEventList(eventList);

		// Textausgabe zum Testen
		System.out.println("Recorded Events:");

		StringBuilder testFXCode = new StringBuilder();

		eventList
				.forEach(eventWrapper -> {
					System.out.println(eventWrapper.getNodeId() + " => "
							+ eventWrapper.getEvent().toString());

					Event event = eventWrapper.getEvent();

					if (event instanceof MouseEvent) {
						MouseEvent mouseEvent = (MouseEvent) event;

						if (mouseEvent.getEventType().equals(
								MouseEvent.MOUSE_CLICKED)
								&& mouseEvent.getClickCount() == 2) {

							testFXCode.append("doubleClickOn(\"#"
									+ eventWrapper.getNodeId() + "\");\n");
						}

						if (mouseEvent.getEventType().equals(
								MouseEvent.MOUSE_CLICKED)
								&& mouseEvent.getClickCount() == 1
								&& mouseEvent.getButton().equals(
										MouseButton.PRIMARY)) {

							testFXCode.append("clickOn(\"#"
									+ eventWrapper.getNodeId() + "\");\n");
						}

						if (mouseEvent.getEventType().equals(
								MouseEvent.MOUSE_CLICKED)
								&& mouseEvent.getClickCount() == 1
								&& mouseEvent.getButton().equals(
										MouseButton.SECONDARY)) {

							testFXCode.append("rightClickOn(\"#"
									+ eventWrapper.getNodeId() + "\");\n");
						}
					}

					if (event instanceof KeyEvent) {
						KeyEvent keyEvent = (KeyEvent) event;

						if (keyEvent.getEventType()
								.equals(KeyEvent.KEY_PRESSED)) {

							testFXCode.append("write(\"" + keyEvent.getText()
									+ "\");\n");

						}

					}

				});

		System.out.println("Unser TestFX-Code: ");
		System.out.println(testFXCode.toString());

	}

}
