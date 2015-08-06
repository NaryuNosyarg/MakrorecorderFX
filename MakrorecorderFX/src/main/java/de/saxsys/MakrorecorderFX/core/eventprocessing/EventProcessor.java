package de.saxsys.MakrorecorderFX.core.eventprocessing;

import java.util.List;

import de.saxsys.MakrorecorderFX.core.uievents.EventWrapper;
import javafx.event.Event;

public interface EventProcessor {

	public void processEventList(List<EventWrapper<? extends Event>> eventList);

}
