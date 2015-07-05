package de.saxsys.MakrorecorderFX.view;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Singleton;

import de.saxsys.MakrorecorderFX.model.TestCase;
import de.saxsys.MakrorecorderFX.viewmodel.MainViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@Singleton
public class MainView implements FxmlView<MainViewModel>, Initializable {

	@InjectViewModel
	private MainViewModel viewModel;

	@FXML
	private Button recordButton;
	@FXML
	private ListView<TestCase> testCaseListView;
	@FXML
	private TableView<Object> testStepTableView;
	@FXML
	private TableColumn<Object, Object> actionColumn;
	@FXML
	private TableColumn<Object, Object> targetColumn;
	@FXML
	private TableColumn<Object, Object> valueColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		testCaseListView.setItems(viewModel.testCaseListProperty());

		viewModel.selectedTestCaseProperty().bind(testCaseListView.getSelectionModel().selectedItemProperty());

	}

	@FXML
	public void record() {

	}

}
