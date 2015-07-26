package de.saxsys.MakrorecorderFX.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

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
import javafx.stage.Stage;

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
	
	private final Stage parentStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		testCaseListView.setItems(viewModel.testCaseListProperty());

		viewModel.selectedTestCaseProperty().bind(
				testCaseListView.getSelectionModel().selectedItemProperty());
		
		viewModel.setParentStageFunction(new Function<Void, Stage>() {
			@Override
			public Stage apply(Void t) {
				return parentStage;
			}
		});
		
	}

	public MainView(Stage stage) {
		this.parentStage = stage;
	}

	@FXML
	public void record() {
		/*if (viewModel.isRecording()) {
			viewModel.closeOpenFile();
		} else {
			viewModel.openFileChooser();
		}*/
		viewModel.openRemoteApp();
		if(viewModel.isRecording()){
			recordButton.setText("Stop");
		} else {
			recordButton.setText("Aufnehmen");
		}
			
	}

}
