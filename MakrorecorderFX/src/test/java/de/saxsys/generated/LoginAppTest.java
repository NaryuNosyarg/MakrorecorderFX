
package de.saxsys.generated;

import de.saxsys.login.LoginApp;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

public class LoginAppTest
    extends FxRobot
{


    @BeforeClass
    public static void setupSpec()
        throws Exception
    {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupStage(stage -> stage.show());
    }

    @Before
    public void setup()
        throws Exception
    {
        FxToolkit.setupApplication(LoginApp.class);
    }

    @Test
    public void loginTest() {
        clickOn("#usernameField");
        write("s");
        clickOn("#passwordField");
        write("s");
        clickOn("#loginButton");
    }

}
