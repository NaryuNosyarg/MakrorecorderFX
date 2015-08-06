package de.saxsys.MakrorecorderFX.core.uievents;

import java.time.LocalDateTime;

import javafx.event.Event;

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
