package de.saxsys.MakrorecorderFX.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestCase {

	private final StringProperty id;
	private final StringProperty name;
	private final StringProperty description;

	public TestCase(String id, String name, String description) {

		this.id = new SimpleStringProperty(id);
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
	}
	
	public String getId() {
		return id.get();
	}
	
	public String getName() {
		return name.get();
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public void setId(String newId) {
		this.id.set(newId);
	}

	public void setName(String newName) {
		this.name.set(newName);
	}
	
	public void setDescription(String newDescription) {
		this.description.set(newDescription);
	}

	public StringProperty idProperty() {
		return id;
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public StringProperty descriptionProperty() {
		return description;
	}
	
	public String toString() {
		return getName();
	}

}
