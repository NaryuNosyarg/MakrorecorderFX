package de.saxsys.MakrorecorderFX.core.eventprocessing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

import javafx.event.Event;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import de.saxsys.MakrorecorderFX.model.uievents.EventWrapper;
import de.saxsys.login.LoginApp;

public class TestFxEventProcessor implements EventProcessor {

	@Override
	public void processEventList(List<EventWrapper<? extends Event>> eventList) {

		JCodeModel codeModel = new JCodeModel();

		try {

			createTestClass(codeModel, eventList);

			File file = new File("src/test/java/");
			file.mkdirs();

			System.out.println(">" + file.getAbsolutePath());

			codeModel.build(file);

		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createTestClass(JCodeModel codeModel,
			List<EventWrapper<? extends Event>> eventList)
			throws JClassAlreadyExistsException {

		final JDefinedClass testClass = setupTestClass(codeModel);

		JMethod testMethod = testClass.method(JMod.PUBLIC, Void.TYPE,
				"loginTest");
		testMethod.annotate(codeModel.ref(Test.class));
		JBlock testMethodBody = testMethod.body();

		eventList.forEach(eventWrapper -> {
			Event event = eventWrapper.getEvent();

			if (event instanceof KeyEvent) {
				KeyEvent keyEvent = (KeyEvent) event;
				processKeyEvent(testMethodBody, keyEvent);
			}

			if (event instanceof MouseEvent) {
				MouseEvent mouseEvent = (MouseEvent) event;
				String nodeId = eventWrapper.getNodeId();
				processMouseEvent(testMethodBody, mouseEvent, nodeId);
			}
		});
	}

	private JDefinedClass setupTestClass(JCodeModel codeModel)
			throws JClassAlreadyExistsException {
		final JPackage pack = codeModel._package("de.saxsys.generated");
		final JDefinedClass testClass = pack._class("LoginAppTest");
		testClass._extends(codeModel.ref(FxRobot.class));

		JMethod setupSpecMethod = testClass.method(JMod.PUBLIC | JMod.STATIC,
				Void.TYPE, "setupSpec");
		setupSpecMethod._throws(Exception.class);
		setupSpecMethod.annotate(codeModel.ref(BeforeClass.class));
		JBlock setupSpecMethodBlock = setupSpecMethod.body();

		JClass fxToolKit = codeModel.ref(FxToolkit.class);

		JInvocation staticInvoke = fxToolKit
				.staticInvoke("registerPrimaryStage");
		setupSpecMethodBlock.add(staticInvoke);

		// TODO alle Codeblöcke typsicher nachbauen
		setupSpecMethodBlock
				.directStatement("FxToolkit.setupStage(stage -> stage.show());");

		JMethod setupMethod = testClass.method(JMod.PUBLIC, Void.TYPE, "setup");
		setupMethod._throws(Exception.class);
		setupMethod.annotate(codeModel.ref(Before.class));
		JBlock setupMethodBlock = setupMethod.body();

		JInvocation setupStatement = fxToolKit.staticInvoke("setupApplication")
				.arg(JExpr.dotclass(codeModel.ref(LoginApp.class)));
		setupMethodBlock.add(setupStatement);

		return testClass;
	}

	// TODO alle Codeblöcke typsicher nachbauen, staticInvoke der TestFX-Methoden
	private void processMouseEvent(JBlock targetCodeBlock,
			MouseEvent mouseEvent, String nodeId) {
		if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)
				&& mouseEvent.getClickCount() == 2) {

			targetCodeBlock.directStatement("doubleClickOn(\"#"
					+ nodeId + "\");");
		}

		if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)
				&& mouseEvent.getClickCount() == 1
				&& mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

			targetCodeBlock.directStatement("clickOn(\"#" + nodeId
					+ "\");");
		}

		if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)
				&& mouseEvent.getClickCount() == 1
				&& mouseEvent.getButton().equals(MouseButton.SECONDARY)) {

			targetCodeBlock.directStatement("rightClickOn(\"#" + nodeId
					+ "\");");
		}
	}

	private void processKeyEvent(JBlock targetCodeBlock, KeyEvent keyEvent) {
		if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {

			targetCodeBlock.directStatement("write(\""
					+ keyEvent.getText() + "\");");
		}
	}

}
