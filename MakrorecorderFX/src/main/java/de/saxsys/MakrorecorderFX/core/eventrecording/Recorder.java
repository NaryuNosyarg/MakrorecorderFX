package de.saxsys.MakrorecorderFX.core.eventrecording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.saxsys.MakrorecorderFX.model.uievents.EventWrapper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Recorder {

	private Node root;
	private List<EventWrapper<? extends Event>> eventList = new ArrayList<>();
	private Map<String, Node> elements = new HashMap<>();

	private boolean isRecording = false;

	public void start(Node root) {
		this.root = root;

		eventList.clear();
		elements.clear();
		isRecording = true;

		traverseSceneGraphFromRoot();

	}

	public boolean isRecording() {
		return isRecording;
	}

	public List<EventWrapper<? extends Event>> stop() {
		isRecording = false;
		return eventList;
	}

	private void traverseSceneGraphFromRoot() {
		traverseSceneGraph(root);
	}

	private void traverseSceneGraph(Node element) {

		if (element instanceof Parent) {

			Parent parent = (Parent) element;

			parent.getChildrenUnmodifiable().forEach(child -> {
				traverseSceneGraph(child);
			});
		}

		if (element.getId() != null && !element.getId().trim().isEmpty()) {

			if (!elements.containsKey(element.getId())) {
				elements.put(element.getId(), element);

				registerListener(element);
			}
		}
	}

	// TODO für weitere Elemente
	private void registerListener(Node element) {

		if (element instanceof Button) {
			registerListenersOnButton((Button) element);
		}

		if (element instanceof TextField) {
			registerListenersOnTextField((TextField) element);
		}

		if (element instanceof PasswordField) {
			registerListenersOnPasswordField((PasswordField) element);
		}
	}

	private void registerListenersOnPasswordField(PasswordField passwordField) {
		passwordField
				.setOnKeyTyped(event -> {
					System.out.println("in Passwordfeld "
							+ passwordField.getId() + " wurde getippt:"
							+ event.getCharacter());
					recordEvent(new EventWrapper<KeyEvent>(passwordField
							.getId(), event));
				});

		passwordField.setOnMouseClicked(event -> {
			System.out.println("In Textfeld " + passwordField.getId()
					+ " wurde " + event.getClickCount() + " mal geklickt: "
					+ event.getButton());
			recordEvent(new EventWrapper<MouseEvent>(passwordField.getId(),
					event));
		});
	}

	private void registerListenersOnTextField(TextField textField) {
		textField.setOnKeyTyped(event -> {
			System.out.println("In Textfeld " + textField.getId()
					+ " wurde getippt: " + event.getCharacter());
			recordEvent(new EventWrapper<KeyEvent>(textField.getId(), event));
		});

		textField.setOnKeyPressed(event -> {
			System.out.println("In Textfeld " + textField.getId()
					+ " wurde gedrückt: " + event.getCode());
			recordEvent(new EventWrapper<KeyEvent>(textField.getId(), event));
		});

		textField
				.setOnMouseClicked(event -> {
					System.out.println("In Textfeld " + textField.getId()
							+ " wurde " + event.getClickCount()
							+ " mal geklickt: " + event.getButton());
					recordEvent(new EventWrapper<MouseEvent>(textField.getId(),
							event));
				});
	}

	private void registerListenersOnButton(Button button) {
		button.setOnAction(event -> {
			System.out.println("Button " + button.getId() + " wurde gedrückt");

			recordEvent(new EventWrapper<ActionEvent>(button.getId(), event));
		});

		button.setOnMouseClicked(event -> {
			System.out.println("Button " + button.getId() + " wurde gedrückt");

			recordEvent(new EventWrapper<MouseEvent>(button.getId(), event));
		});
	}

	private void recordEvent(EventWrapper<? extends Event> event) {
		if (isRecording) {
			eventList.add(event);
			traverseSceneGraphFromRoot();
		}
	}

}
