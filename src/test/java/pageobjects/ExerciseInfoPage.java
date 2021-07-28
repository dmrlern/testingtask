package pageobjects;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testprojectcore.core.Driver;
import testprojectcore.core.DriverUtils;
import testprojectcore.driverutil.PageObjectFactory;
import testprojectcore.util.Helper;

import java.util.HashMap;
import java.util.Map;

public class ExerciseInfoPage extends Driver {


    public ExerciseInfoPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[@data-cy='exercise-info-ex-title']")
    WebElement exerciseTitle;

    @FindBy(xpath = "//app-arrow-button[@class='start']")
    WebElement startTrainingButton;

    @FindBy(xpath = "//app-button[@data-cy='watch-video-btn']")
    WebElement startVideoButton;

    @FindBy(css = "vg-scrub-bar-current-time > .background")
    WebElement videoProgressBar;

    @FindBy(css = "#masterLoop")
    WebElement videoElement;


    public String getExerciseId() throws InterruptedException {
        try {
            DriverUtils.waitUntil(ExpectedConditions.visibilityOf(exerciseTitle), 1, driver);
        } catch (TimeoutException te) {
            ExercisesPage exercisesPage = PageObjectFactory.createClass(ExercisesPage.class);
            exercisesPage.clickARandomExercise();
        }
        return StringUtils.substringBetween(exerciseTitle.getText(), "#", " ");
    }

    public void clickStartVideo() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(startVideoButton), 10, driver);
        startVideoButton.click();
    }

    public Map<String, String> getInitialVideoProgress() {
        Map<String, String> initialProgress = new HashMap<>();
        initialProgress.put("progress bar", videoProgressBar.getAttribute("style"));   //progress bar
        initialProgress.put("current time", videoElement.getAttribute("currentTime"));   //video progress
        return initialProgress;
    }

    public void checkIfVideoIsProgressing() throws InterruptedException {
        Map videoProgressInitial = getInitialVideoProgress();
        long endWaitTime = System.currentTimeMillis() + 15 * 1000;  //15 seconds
        boolean isConditionMet = false;
        while (System.currentTimeMillis() < endWaitTime && !isConditionMet) {
            isConditionMet = !((videoProgressInitial.get("progress bar").equals(videoProgressBar.getAttribute("style"))) ||
                    (videoProgressInitial.get("current time").equals(videoProgressBar.getAttribute("currentTime"))));
            if (isConditionMet) {
                break;
            } else {
                Thread.sleep(100);
            }
        }
    }

    public void clickStartTraining() throws InterruptedException {
        startTrainingButton.click();
        Helper.waitForJavascriptToLoad(driver);
        Helper.waitForJavascriptToLoad(5000, 50);
    }
}
