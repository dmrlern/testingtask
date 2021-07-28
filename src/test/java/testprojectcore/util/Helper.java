package testprojectcore.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import testprojectcore.core.DriverManager;


/**
 * @author Eren Demirel
 */
public class Helper {

    private static final Logger logger = LogManager.getLogger(Helper.class);


    public static void waitForJavascriptToLoad(int maxWaitMillis, int pollDelimiterInMillis) throws InterruptedException {
        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + maxWaitMillis) {
            String prevState = DriverManager.getDriver().getPageSource();
            Thread.sleep(pollDelimiterInMillis);
            if (prevState.equals(DriverManager.getDriver().getPageSource())) {
                return;
            }
        }
    }


    public static boolean waitForJavascriptToLoad(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, 15);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver12 -> {
            try {
                return ((Long) js.executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                return true;
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver1 -> js.executeScript("return document.readyState")
                .toString().equals("complete");

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }


    public static void waitUntilElementIgnoringStale(WebDriver driver, WebElement element, int timeout) {
        new WebDriverWait(driver, timeout)
                .ignoring(StaleElementReferenceException.class)
                .until(driver1 -> element.isDisplayed());
    }
}

