package de.saxsys.MakrorecorderFX.core.remoteapp;

import de.saxsys.login.view.LoginView;
import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RemoteAppServiceMock implements RemoteAppService{

	@Override
	public Node getRootElement() {
		Parent loginView = FluentViewLoader.fxmlView(LoginView.class).load().getView();
		
		Stage stage = new Stage();
		stage.setScene(new Scene(loginView));
		stage.show();
		
		return loginView;
	}
	

}
