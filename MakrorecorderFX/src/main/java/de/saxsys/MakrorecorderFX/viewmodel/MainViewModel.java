package de.saxsys.MakrorecorderFX.viewmodel;

import de.saxsys.MakrorecorderFX.model.TestCase;
import de.saxsys.MakrorecorderFX.model.Testfallverwaltung;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationObserver;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainViewModel implements ViewModel {

	private ObjectProperty<TestCase> selectedTestCase = new SimpleObjectProperty<>();
	private ObservableList<TestCase> testCaseList = FXCollections.observableArrayList();
	private Testfallverwaltung verwaltung;

	public MainViewModel(Testfallverwaltung verwaltung) {
		this.verwaltung = verwaltung;

		MvvmFX.getNotificationCenter().subscribe("update", new NotificationObserver() {
			@Override
			public void receivedNotification(String key, Object... payload) {
				updateTestCaseList();
			}
		});
	}

	private void updateTestCaseList() {
		testCaseList.clear();
		testCaseList.addAll(verwaltung.getTestCaseList());
		System.out.println(testCaseList);
	}

	public ObservableList<TestCase> testCaseListProperty() {
		MvvmFX.getNotificationCenter().publish("update");
		return testCaseList;
	}

	public ObjectProperty<TestCase> selectedTestCaseProperty() {
		return selectedTestCase;
	}

}
