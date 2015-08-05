package de.saxsys.MakrorecorderFX;

import javax.inject.Provider;

import de.saxsys.MakrorecorderFX.core.eventprocessing.EventProcessor;
import de.saxsys.MakrorecorderFX.core.eventprocessing.TestFxEventProcessor;
import de.saxsys.MakrorecorderFX.core.remoteapp.RemoteAppService;
import de.saxsys.MakrorecorderFX.core.remoteapp.RemoteAppServiceMock;
import de.saxsys.MakrorecorderFX.model.TestCaseManagement;
import de.saxsys.MakrorecorderFX.view.MainView;
import de.saxsys.MakrorecorderFX.viewmodel.MainViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewTuple;
import eu.lestard.easydi.EasyDI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

public class App extends Application {

	private Stage stage;
	private EasyDI easyDI;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;

		easyDI = new EasyDI();

		TestCaseManagement management = new TestCaseManagement();
		easyDI.bindInstance(TestCaseManagement.class, management);

		easyDI.bindProvider(Stage.class, new Provider<Stage>() {
			@Override
			public Stage get() {
				return stage;
			}
		});

		easyDI.bindInterface(RemoteAppService.class, RemoteAppServiceMock.class);

		easyDI.bindInterface(EventProcessor.class, TestFxEventProcessor.class);

		MvvmFX.setCustomDependencyInjector(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> arg0) {
				return easyDI.getInstance(arg0);
			}
		});

		showMainView();
	}

	public void showMainView() {
		ViewTuple<MainView, MainViewModel> viewTuple = FluentViewLoader
				.fxmlView(MainView.class).load();

		Parent root = viewTuple.getView();
		stage.setScene(new Scene(root));
		stage.show();
	}
}
