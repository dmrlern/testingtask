package pageobjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testprojectcore.core.Driver;
import testprojectcore.core.DriverManager;
import testprojectcore.core.DriverUtils;
import testprojectcore.testcontext.TestContext;
import testprojectcore.util.Helper;

public class DashboardPage extends Driver {


    @FindBy(css = ".today.ng-star-inserted")
    WebElement todayNavbar;

    @FindBy(css = ".my-week.ng-star-inserted")
    WebElement myWeekNavbar;

    @FindBy(xpath = "//app-arrow-button[@data-cy='start-training-button']")
    WebElement startTrainingButton;

    @FindBy(xpath = "//app-button[contains(.,'Learn morearrow_forward')]")
    WebElement knowledgeAndWellbeingButton;

    @FindBy(xpath = "//app-button[@data-cy='add-steps']")
    WebElement stepsButton;

    @FindBy(css = ".cancel")
    WebElement stepsWindowCancelButton;

    @FindBy(css = ".confirm")
    WebElement stepsWindowConfirmButton;

    @FindBy(css = ".mat-form-field:nth-child(3)")
    WebElement stepsFormInput;

    @FindBy(xpath = "//div[contains(@class,'completed')]")
    WebElement stepsLeftCounter;


    private static final Logger logger = LogManager.getLogger();

    private String steps;


    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickToday() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(todayNavbar), 10, driver);
        todayNavbar.click();
    }

    public void clickMyWeek() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(myWeekNavbar), 3, driver);
        myWeekNavbar.click();
    }

    public void clickStartTraining() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(startTrainingButton), 10, driver);
        startTrainingButton.click();
    }

    public void clickLearnMore() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(knowledgeAndWellbeingButton), 10, driver);
        knowledgeAndWellbeingButton.click();
    }

    public void clickSteps() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(stepsButton), 10, driver);
        steps = getStepsCount();
        stepsButton.click();
    }

    public String getStepsCount() {
        return stepsLeftCounter.getText();
    }

    public void addSteps(String stepCount) throws InterruptedException {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(stepsWindowCancelButton), 2, driver);
        DriverUtils.getActions(driver).moveToElement(stepsFormInput).click().build().perform();
        DriverUtils.getActions(driver).sendKeys(stepCount).build().perform();
        DriverUtils.getActions(driver).moveToElement(stepsWindowConfirmButton).click().build().perform();
        Helper.waitForJavascriptToLoad(DriverManager.getDriver());
        Helper.waitForJavascriptToLoad(2000, 50);
    }

    public void checkIfStepCounterChanged() throws InterruptedException {
        logger.info("Steps before: " + steps);
        String updatedSteps = null;
        long endWaitTime = System.currentTimeMillis() + 10 * 1000;  //10 seconds
        boolean isConditionMet = false;
        while (System.currentTimeMillis() < endWaitTime && !isConditionMet) {
            updatedSteps = getStepsCount();
            if (!(updatedSteps.equals(steps)) && (Integer.parseInt(updatedSteps) > Integer.parseInt(steps))) {
                isConditionMet = true;
            }
            if (isConditionMet) {
                logger.info("Steps after: " + updatedSteps);
                break;
            } else {
                Thread.sleep(100);
            }
        }

    }
}
