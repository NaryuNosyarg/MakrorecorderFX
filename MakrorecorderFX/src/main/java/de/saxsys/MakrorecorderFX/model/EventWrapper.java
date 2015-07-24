package de.saxsys.MakrorecorderFX.model;

import java.time.LocalDateTime;

import javafx.event.Event;
import javafx.scene.Node;

public class EventWrapper <E extends Event> {
	
	private final LocalDateTime timestamp;
	
	private final String nodeId;

	private final E event;
	
	public EventWrapper(String nodeId, E event) {
		timestamp = LocalDateTime.now();
		this.nodeId = nodeId;
		this.event = event;
	}

	public String getNodeId() {
		return nodeId;
	}

	public E getEvent() {
		return event;
	}
	
}
