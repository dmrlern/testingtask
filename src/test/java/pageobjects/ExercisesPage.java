package pageobjects;

import dto.ExercisesDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testprojectcore.core.Driver;
import testprojectcore.core.DriverManager;
import testprojectcore.core.DriverUtils;
import testprojectcore.util.Helper;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ExercisesPage extends Driver {


    By videoTitles = By.cssSelector(".video-title");

    By checkedIcons = By.cssSelector("app-video-preview > .mat-icon");



    Map<String, ExercisesDTO> exercisesDTOList = new HashMap<>();

    private static final Logger logger = LogManager.getLogger();


    public ExercisesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public Map<String, ExercisesDTO> mapExerciseAttributes() throws InterruptedException {
        List<WebElement> exercises;
        exercises = driver.findElements(videoTitles);
        for (int i = 0; i < exercises.size(); i++) {
            int finalI = i + 1;
            ExercisesDTO exercisesDTO = new ExercisesDTO();
            exercisesDTO.orderNo = finalI;
            exercisesDTO.exerciseTitle = exercises.get(i).getText();
            exercisesDTO.exerciseId = StringUtils.substringBetween(exercises.get(i).getText(), "#", " ");
            exercisesDTO.exerciseDuration = driver.findElement(By.xpath("(//div[@class='duration'])[" + finalI + "]")).getText();
            List<WebElement> completionDots = driver.findElements(By.cssSelector(".ng-star-inserted:nth-child(" + finalI + ") > .video-thumb-col .completion-dot"));
            for (int j = 0; j < completionDots.size(); j++) {
                exercisesDTO.completionDotColors.add(completionDots.get(j).getCssValue("background-color"));
            }
            exercisesDTOList.put(exercisesDTO.exerciseId, exercisesDTO);
        }
        return exercisesDTOList;
    }


    public void clickARandomExercise() throws InterruptedException {
        Map<String, ExercisesDTO> exerciseAttributes = new HashMap<String, ExercisesDTO>(mapExerciseAttributes());
        Map<String, ExercisesDTO> completedExerciseAttributes = new HashMap<String, ExercisesDTO>(getCompletedExercises());
        Map<String, ExercisesDTO> incompleteExercises = new HashMap<>(exerciseAttributes);
        List<WebElement> checkedExercises = driver.findElements(checkedIcons);
        logger.info("Checked exercises: " + Arrays.asList(checkedExercises.toString()));
        Helper.waitForJavascriptToLoad(DriverManager.getDriver());
        Helper.waitForJavascriptToLoad(5000, 50);
        List<WebElement> exercises = driver.findElements(videoTitles);
        for (int i = 0; i < exercises.size(); i++) {
            DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(exercises.get(i)), 2, driver);
        }

        List<String> completedExerciseIds = new ArrayList<>(completedExerciseAttributes.keySet());
        for (String exerciseId : completedExerciseIds) {
            incompleteExercises.remove(exerciseId);
        }

        List<WebElement> incompleteExercisesList = new ArrayList<>();
        for (WebElement exercise : exercises) {
            for (var entry : incompleteExercises.entrySet()) {
                if (exercise.getText().contains(entry.getKey())) {
                    incompleteExercisesList.add(exercise);
                }
            }
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(incompleteExercisesList.size()) % incompleteExercisesList.size();
        incompleteExercisesList.get(randomIndex).click();
    }

    public void waitForPageElementsToLoad() throws InterruptedException {
        Helper.waitForJavascriptToLoad(DriverManager.getDriver());
        Helper.waitForJavascriptToLoad(5000, 50);
        List<WebElement> exercises = driver.findElements(videoTitles);
        for (int i = 0; i < exercises.size(); i++) {
            try {
                DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(exercises.get(i)), 2, driver);
            } catch (TimeoutException te) {
                //Do nothing
            }
        }
    }

    public void refreshThePage() throws InterruptedException {
        driver.navigate().refresh();
        Thread.sleep(2000);     //Unfortunately
    }


    public Map<String, ExercisesDTO> getCompletedExercises() throws InterruptedException {
        Map<String, ExercisesDTO> exerciseAttributes = new HashMap<String, ExercisesDTO>(mapExerciseAttributes());
        Map<String, ExercisesDTO> completedExerciseAttributes = new HashMap<String, ExercisesDTO>(exerciseAttributes);
        List<String> completedExerciseIds = new ArrayList<>();
        List<WebElement> checkIcons = driver.findElements(checkedIcons);

        for (Map.Entry<String, ExercisesDTO> entry : exerciseAttributes.entrySet()) {
            String key = entry.getKey();
            ExercisesDTO value = entry.getValue();
            for (int j = 0; j < value.completionDotColors.size(); j++) {
                for (int k = 1; k < value.completionDotColors.size(); k++) {
                    //First condition: if two consecutive dots have different color, second condition: check if the both dots have the orange like color
                    if (!(value.completionDotColors.get(j).equals(value.completionDotColors.get(k)) && !(value.completionDotColors.get(j).equals("rgba(255, 190, 95, 1)") && value.completionDotColors.get(k).equals("rgba(255, 190, 95, 1)")))) {
                        completedExerciseIds.add(value.exerciseId);
                    }
                }
            }
        }

        for (String exerciseId : completedExerciseIds) {
            completedExerciseAttributes.remove(exerciseId);
        }

        Assertions.assertEquals(completedExerciseAttributes.size(), checkIcons.size());

        return completedExerciseAttributes;
    }

    public void clickOnARandomCompletedExercise() throws InterruptedException {
        List<WebElement> exercises = driver.findElements(videoTitles);
        Map<String, ExercisesDTO> completedExerciseAttributes = new HashMap<String, ExercisesDTO>(getCompletedExercises());

        for (Map.Entry<String, ExercisesDTO> entry : completedExerciseAttributes.entrySet()) {
            String key = entry.getKey();
            ExercisesDTO value = entry.getValue();
            for (int i = 0; i < exercises.size(); i++) {
                if (StringUtils.substringBetween(exercises.get(i).getText(), "#", " ").equals(value.exerciseId)) {
                    String urlBefore = driver.getCurrentUrl();
                    exercises.get(i).click();
                    String urlAfter = driver.getCurrentUrl();
                    //Check the url, it shouldn't change after clicking
                    Assertions.assertEquals(urlBefore, urlAfter);
                }
            }
        }
    }

    public int getIncompleteExerciseStepsCount() throws InterruptedException {
        Map<String, ExercisesDTO> exerciseAttributes = new HashMap<String, ExercisesDTO>(mapExerciseAttributes());
        int count = 0;

        for (Map.Entry<String, ExercisesDTO> entry : exerciseAttributes.entrySet()) {
            String key = entry.getKey();
            ExercisesDTO value = entry.getValue();

            for (int i = 0; i < value.completionDotColors.size(); i++) {
                if (value.completionDotColors.get(i).equals("rgba(255, 190, 95, 1)")) {
                    count++;
                }
            }

        }
        return count;
    }

}
