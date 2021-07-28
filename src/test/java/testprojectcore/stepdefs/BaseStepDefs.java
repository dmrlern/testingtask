package testprojectcore.stepdefs;

import org.junit.jupiter.api.Order;
import testprojectcore.core.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import testprojectcore.dataprovider.TestConfigProvider;



/**
 * @author Eren Demirel
 */
public class BaseStepDefs {
    @Before("@browser")
    @Order(Integer.MIN_VALUE)
    public void initDriverAndBrowsers() throws Exception {       //Here, initialize the driver before each step and close the browser after each step, these operations are done via cucumber tags therefore no need for inheritance and composition
        DriverManager driverManager = new DriverManager();
        driverManager.initDriver();
    }

    @After("@browser")
    public void tearDown(Scenario scenario) throws Exception {
        if (scenario.isFailed()) {
            if (scenario.getSourceTagNames().contains("@browser")) {
               System.out.println("Scenario failed..");
            }
        }
        if ((TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("android") ||
                TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("ios"))) {
            DriverManager.quitDriver();
        } else
            DriverManager.closeDriver();
    }
}
