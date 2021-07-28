package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testprojectcore.core.Driver;
import testprojectcore.core.DriverUtils;

public class MyWeekPage extends Driver {

    @FindBy(xpath = "//a[@data-cy='every-step-counts'][contains(.,'Go to exercises')]")
    WebElement gotoExercisesButton;

    @FindBy(xpath = "//a[@data-cy='every-step-counts'][contains(.,'View content')]")
    WebElement viewContentButton;

    @FindBy(xpath = "//a[@data-cy='every-step-counts'][contains(.,'Go to steps')]")
    WebElement gotoStepsButton;

    @FindBy(css = ".content-box_type_exercises > .count")
    WebElement remainingExerciseCount;

    @FindBy(css = ".content-box_type_knowledge > .count")
    WebElement remainingKnowledgeCount;



    public MyWeekPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void clickGoToExercises() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(gotoExercisesButton), 3, driver);
        gotoExercisesButton.click();
    }

    public void clickGoToSteps() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(gotoStepsButton), 3, driver);
        gotoStepsButton.click();
    }

    public void clickKnowledgeWellbeing() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(viewContentButton), 3, driver);
        viewContentButton.click();
    }

    public String getRemainingExerciseCount() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(gotoExercisesButton), 3, driver);
        return remainingExerciseCount.getText();
    }

    public String getRemainingKnowledgeCount() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(gotoExercisesButton), 3, driver);
        return remainingKnowledgeCount.getText();
    }

}
