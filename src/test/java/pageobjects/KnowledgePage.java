package pageobjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testprojectcore.core.Driver;
import testprojectcore.core.DriverUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KnowledgePage extends Driver {

    private static final Logger logger = LogManager.getLogger();


    @FindBy(css = ".ng-star-inserted > .mat-icon")
    List<WebElement> checkIcons;

    @FindBy(css = ".ng-star-inserted .video-title")
    List<WebElement> videos;

    @FindBy(xpath = "//app-button[contains(.,'Watch')]")
    WebElement startVideoButton;

    @FindBy(css = ".ml-auto > .ng-star-inserted")
    WebElement videoTime;

    public KnowledgePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void startTheVideo() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(startVideoButton), 3, driver);
        JavascriptExecutor javascriptExecutor = DriverUtils.getJSExecutor(driver);
        javascriptExecutor.executeScript("document.getElementById(\"masterLoop\").play()");
        //This video contains some problems, it may not click
        try {
            startVideoButton.click();
        } catch (Exception e) {
            logger.warn("Couldn't play the Knowledge&Wellbeing video");
        }

    }

    public int getCompletedCount() {
        return checkIcons.size();
    }

    public int getIncompleteCount() {
        return (videos.size()) - (checkIcons.size());
    }

    public void checkIfVideoIsFinished() throws InterruptedException {
        long endWaitTime = System.currentTimeMillis() + 150 * 1000;
        boolean isConditionMet = false;
        while (System.currentTimeMillis() < endWaitTime && !isConditionMet) {
            String currentVideoTime = videoTime.getText();
            if (currentVideoTime.equals("00:00")) {
                isConditionMet = true;
            }
            if (isConditionMet) {
                break;
            } else {
                Thread.sleep(5000);
            }
        }
    }

    public void clickOnARandomVideo() {
        int randomIndex = ThreadLocalRandom.current().nextInt(videos.size()) % videos.size();
        try {
            videos.get(randomIndex).click();
        } catch (NoSuchElementException nse) {
            int randomIndexOld = randomIndex;
            randomIndex = ThreadLocalRandom.current().nextInt(videos.size()) % videos.size();
            while (randomIndexOld != randomIndex) {
                randomIndex = ThreadLocalRandom.current().nextInt(videos.size()) % videos.size();
            }
            videos.get(randomIndex).click();
        }
    }

    public void checkIfVideoIsPlaying() throws InterruptedException {
        String initialTime = videoTime.getText();
        Thread.sleep(600);
        String finalTime = videoTime.getText();

        Assertions.assertNotEquals(initialTime, finalTime);
    }

}
