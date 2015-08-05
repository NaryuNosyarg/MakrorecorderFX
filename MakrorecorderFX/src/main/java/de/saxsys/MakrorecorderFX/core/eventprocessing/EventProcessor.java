package de.saxsys.MakrorecorderFX.core.eventprocessing;

import java.util.List;

import javafx.event.Event;
import de.saxsys.MakrorecorderFX.model.uievents.EventWrapper;

public interface EventProcessor {

	public void processEventList(List<EventWrapper<? extends Event>> eventList);

}
