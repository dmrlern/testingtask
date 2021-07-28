package pageobjects;


import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testprojectcore.core.Driver;
import testprojectcore.core.DriverUtils;
import testprojectcore.util.Helper;


public class ExerciseExecutionPage extends Driver {

    @FindBy(id = "masterVideo")
    WebElement videoElement;

    By countdownBeforeTheExercise = By.xpath("//div[@data-cy='exercise-countdown']");

    @FindBy(css = ".content > .title")
    WebElement wellDoneTitle;

    @FindBy(css = ".countdown")
    WebElement countdownCounter;

    @FindBy(css = ".actions > .ng-star-inserted:nth-child(2)")
    WebElement completeButton;

    @FindBy(css = "app-close")
    WebElement closeExercise;

    @FindBy(xpath = "//button[contains(.,'YES, QUIT')]")
    WebElement confirmQuitButton;


    public ExerciseExecutionPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void checkIfCompleted() throws InterruptedException {
        DriverUtils.waitUntil(ExpectedConditions.visibilityOf(wellDoneTitle), 450, driver);
        Assertions.assertTrue(wellDoneTitle.getText().equals("Well done!"));
    }

    public void clickComplete(){
        completeButton.click();
    }

    public String getInitialCountdown() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(countdownBeforeTheExercise));
        long endWaitTime = System.currentTimeMillis() + 10 * 1000;  //10 seconds
        boolean isConditionMet = false;
        while (System.currentTimeMillis() < endWaitTime && !isConditionMet) {
            Helper.waitUntilElementIgnoringStale(driver,countdownCounter,1);
            int countdownBeforeTheExerciseIsGone = driver.findElements(countdownBeforeTheExercise).size();
            if (countdownBeforeTheExerciseIsGone == 0) {
                isConditionMet = true;
            }
            if (isConditionMet) {
                break;
            } else {
                Thread.sleep(100);
            }
        }
        return countdownCounter.getAttribute("d");
    }

    public void checkIfCountingDown() throws InterruptedException {
        String countdownInitial = getInitialCountdown();
        long endWaitTime = System.currentTimeMillis() + 10 * 1000;  //10 seconds
        boolean isConditionMet = false;
        while (System.currentTimeMillis() < endWaitTime && !isConditionMet) {
            Helper.waitUntilElementIgnoringStale(driver,countdownCounter,1);
            isConditionMet = !countdownInitial.equals(countdownCounter.getAttribute("d"));
            if (isConditionMet) {
                break;
            } else {
                Thread.sleep(100);
            }
        }
    }


    public void quitExercise() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(countdownBeforeTheExercise));
        long endWaitTime = System.currentTimeMillis() + 10 * 1000;  //10 seconds
        boolean isConditionMet = false;
        while (System.currentTimeMillis() < endWaitTime && !isConditionMet) {
            int countdownBeforeTheExerciseIsGone = driver.findElements(countdownBeforeTheExercise).size();
            if (countdownBeforeTheExerciseIsGone == 0) {
                isConditionMet = true;
            }
            if (isConditionMet) {
                break;
            } else {
                Thread.sleep(100);
            }
        }
        closeExercise.click();
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(confirmQuitButton), 2, driver);
        confirmQuitButton.click();
    }
}

