package de.saxsys.MakrorecorderFX;

import javax.inject.Provider;

import de.saxsys.MakrorecorderFX.model.Testfallverwaltung;
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

		Testfallverwaltung verwaltung = new Testfallverwaltung();
		easyDI.bindInstance(Testfallverwaltung.class, verwaltung);

		/*
		 * easyDI.bindProvider(Testfallverwaltung.class, new
		 * Provider<Testfallverwaltung>() {
		 * 
		 * @Override public Testfallverwaltung get() { Testfallverwaltung
		 * verwaltung = new Testfallverwaltung(); return verwaltung; } });
		 */

		easyDI.bindProvider(Stage.class, new Provider<Stage>() {
			@Override
			public Stage get() {
				return stage;
			}
		});

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
