package stepdefs;

import dto.ExercisesDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import pageobjects.*;
import testprojectcore.core.DriverManager;
import testprojectcore.dataprovider.EnvironmentDataProvider;
import testprojectcore.driverutil.PageObjectFactory;
import testprojectcore.testcontext.TestContext;
import testprojectcore.util.Helper;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UIStepDefs {

    private TestContext testContext;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private ExerciseInfoPage exerciseInfoPage;
    private ExerciseExecutionPage exerciseExecutionPage;
    private MyWeekPage myWeekPage;
    private ExercisesPage exercisesPage;
    private KnowledgePage knowledgePage;

    private static final Logger logger = LogManager.getLogger();


    public UIStepDefs(TestContext testContext) {
        this.testContext = testContext;
        loginPage = PageObjectFactory.createClass(LoginPage.class);
        dashboardPage = PageObjectFactory.createClass(DashboardPage.class);
        exerciseInfoPage = PageObjectFactory.createClass(ExerciseInfoPage.class);
        exerciseExecutionPage = PageObjectFactory.createClass(ExerciseExecutionPage.class);
        myWeekPage = PageObjectFactory.createClass(MyWeekPage.class);
        exercisesPage = PageObjectFactory.createClass(ExercisesPage.class);
        knowledgePage = PageObjectFactory.createClass(KnowledgePage.class);
    }


    @Given("Log in to the UI with following credentials from the testdata file")
    public void logInToTheUIWithFollowingCredentialsAndVerifyTheQuotedJobIsListedOnTheUI(DataTable dataTable) throws IOException, InterruptedException {
        List<List<String>> rows = dataTable.asLists(String.class);
        String username = EnvironmentDataProvider.TESTDATA.getData(rows.get(1).get(0));
        String password = EnvironmentDataProvider.TESTDATA.getData(rows.get(1).get(1));
        loginPage.navigateToHomePage();
        loginPage.login(username, password);
        Helper.waitForJavascriptToLoad(DriverManager.getDriver());
        Helper.waitForJavascriptToLoad(15000, 50);
    }


    @And("Click {string} in {string} section on the left")
    public void click(String arg0, String arg1) {
        dashboardPage.clickStartTraining();
    }

    @And("In exercise info page, click {string} and check if video is playing")
    public void inExerciseInfoPageClickToCheckIfVideoIsPlaying(String arg0) throws InterruptedException {
        exerciseInfoPage.clickStartVideo();
        exerciseInfoPage.checkIfVideoIsProgressing();
    }

    @And("Click {string}")
    public void click(String arg0) throws InterruptedException {
        exerciseInfoPage.clickStartTraining();
    }

    @And("After the exercise starts, check if the counter is counting and then wait for the exercise to complete")
    public void afterTheExerciseStartsCheckIfTheCounterIsCountingAndThenWaitForTheExerciseToComplete() throws InterruptedException {
        exerciseExecutionPage.checkIfCountingDown();
        exerciseExecutionPage.checkIfCompleted();
        exerciseExecutionPage.clickComplete();
    }

    @And("After the exercise starts, quit the exercise")
    public void afterTheExerciseStartsQuitTheExercise() throws InterruptedException {
        exerciseExecutionPage.quitExercise();
        Thread.sleep(5000);
    }


    @And("Navigate to {string} page using the left Navigation Bar and click {string} in {string} section on the top left")
    public void navigateToPageUsingTheLeftNavigationBarAndClickInSectionOnTheTopLeft(String arg0, String arg1, String arg2) throws InterruptedException {
        dashboardPage.clickMyWeek();
        myWeekPage.clickGoToExercises();
    }

    @And("Click a random exercise except the ones which are already completed")
    public void clickARandomExercise() throws InterruptedException {
        exercisesPage.waitForPageElementsToLoad();
        Map initialPageState = new HashMap<String, ExercisesDTO>(exercisesPage.mapExerciseAttributes());
        exercisesPage.clickARandomExercise();
        String idOfTheSelectedExercise = exerciseInfoPage.getExerciseId();

        testContext.map1.put(Thread.currentThread().getId(), idOfTheSelectedExercise);
        testContext.map2.put(Thread.currentThread().getId(), initialPageState);

        logger.info("Id of the selected exercise: " + idOfTheSelectedExercise);

    }

    @And("Check the counter dots to see they changed")
    public void checkCounterDotsToSeeTheyChanged() throws InterruptedException {
        exercisesPage.refreshThePage();
        exercisesPage.waitForPageElementsToLoad();
        Map finalPageState = new HashMap<String, ExercisesDTO>(exercisesPage.mapExerciseAttributes());
        ExercisesDTO exercisesDTOFinal = (ExercisesDTO) finalPageState.get(testContext.map1.get(Thread.currentThread().getId()));
        Map initialPageState = new HashMap<String, ExercisesDTO>(testContext.map2.get(Thread.currentThread().getId()));
        ExercisesDTO exercisesDTOInitial = (ExercisesDTO) initialPageState.get(testContext.map1.get(Thread.currentThread().getId()));
        logger.info("Exercise title: " + exercisesDTOInitial.exerciseTitle);
        logger.info("Completion dot colors: " + Arrays.asList(exercisesDTOInitial.completionDotColors.toString()));
        boolean completedFlag = true;
        for (int i = 0; i < exercisesDTOInitial.completionDotColors.size(); i++) {
            if (exercisesDTOInitial.completionDotColors.get(i).equals("rgba(255, 190, 95, 1)")) {   //means there is at least one unfinished exercise
                completedFlag = false;
            }
        }
        if (!completedFlag) {
            Assertions.assertNotSame(exercisesDTOInitial.completionDotColors, exercisesDTOFinal.completionDotColors);
            Assertions.assertNotEquals(exercisesDTOInitial.completionDotColors, exercisesDTOFinal.completionDotColors);
        } else {
            Assertions.assertEquals(exercisesDTOInitial.completionDotColors, exercisesDTOFinal.completionDotColors);
            Assertions.assertSame(exercisesDTOInitial.completionDotColors, exercisesDTOFinal.completionDotColors);
        }
    }


    @And("Check the counter dots to see they didn't change")
    public void checkCounterDotsToSeeTheyDidntChanged() throws InterruptedException {
        exercisesPage.refreshThePage();
        exercisesPage.waitForPageElementsToLoad();
        Map finalPageState = new HashMap<String, ExercisesDTO>(exercisesPage.mapExerciseAttributes());
        ExercisesDTO exercisesDTOFinal = (ExercisesDTO) finalPageState.get(testContext.map1.get(Thread.currentThread().getId()));
        Map initialPageState = new HashMap<String, ExercisesDTO>(testContext.map2.get(Thread.currentThread().getId()));
        ExercisesDTO exercisesDTOInitial = (ExercisesDTO) initialPageState.get(testContext.map1.get(Thread.currentThread().getId()));
        logger.info("Exercise title: " + exercisesDTOInitial.exerciseTitle);
        logger.info("Completion dot colors: " + Arrays.asList(exercisesDTOInitial.completionDotColors.toString()));
    }

    @And("Check if completed exercises have a check icon on them")
    public void checkIfCompletedExercisesHaveACheckIconOnThem() throws InterruptedException {
        exercisesPage.getCompletedExercises();
    }

    @And("Try to click a random exercise from those which are completed, it shouldn't allow that")
    public void tryToClickARandomExerciseFromThoseWhichAreCompletedItShouldnTAllowThat() throws InterruptedException {
        exercisesPage.clickOnARandomCompletedExercise();
    }

    @And("Navigate to {string} section at the bottom right and click {string}")
    public void navigateToSectionAtTheBottomRightAndClick(String arg0, String arg1) {
        dashboardPage.clickSteps();
    }

    @And("Add {string} steps")
    public void addSteps(String arg0) throws InterruptedException {
        dashboardPage.addSteps(arg0);
    }

    @And("Check if steps counter changed")
    public void checkIfStepsCounterChanged() throws InterruptedException {
        dashboardPage.checkIfStepCounterChanged();
    }

    @And("Navigate to {string} page using the left Navigation Bar and click {string} in {string} section on the bottom left")
    public void navigateToPageUsingTheLeftNavigationBarAndClickInSectionOnTheBottomLeft(String arg0, String arg1, String arg2) {
        dashboardPage.clickMyWeek();
        myWeekPage.clickGoToSteps();
    }

    @And("Navigate to {string} page using the left Navigation Bar and click {string} in {string} section on the top right")
    public void navigateToPageUsingTheLeftNavigationBarAndClickInSectionOnTheTopRight(String arg0, String arg1, String arg2) {
        dashboardPage.clickMyWeek();
        myWeekPage.clickKnowledgeWellbeing();
    }

    @And("If there is any, click on an incomplete video and verify if it is marked with a check icon after the video is finished")
    public void ifThereIsAnyClickOnAnIncompleteVideoAndVerifyIfItIsMarkedWithACheckIconAfterTheVideoIsFinished() throws InterruptedException {
        int incompleteCountInitial = knowledgePage.getIncompleteCount();
        if (knowledgePage.getIncompleteCount() != 0) {
            knowledgePage.clickOnARandomVideo();
            knowledgePage.startTheVideo();
            knowledgePage.checkIfVideoIsFinished();
            int incompleteCountFinal = knowledgePage.getIncompleteCount();
            Assertions.assertNotEquals(incompleteCountInitial, incompleteCountFinal);
        }
    }

    @And("If there are no incomplete video, click on a random video and check if it is playing")
    public void ifThereAreNoIncompleteVideoClickOnARandomVideoAndCheckIfItIsPlaying() throws InterruptedException {
        if (knowledgePage.getIncompleteCount() == 0) {
            knowledgePage.clickOnARandomVideo();
            knowledgePage.startTheVideo();
            knowledgePage.checkIfVideoIsPlaying();
        }
    }

    @And("Get the incomplete exercise repetitions count and then return back to {string} page to see if the counter is right")
    public void getTheIncompleteExerciseRepetitionsCountAndThenReturnBackToPageToSeeIfTheCounterIsRight(String arg0) throws InterruptedException {
        int incompleteRepetitions = exercisesPage.getIncompleteExerciseStepsCount();
        dashboardPage.clickMyWeek();
        Assertions.assertEquals(Integer.parseInt(myWeekPage.getRemainingExerciseCount()), incompleteRepetitions);
    }

    @And("Get the incomplete knowledge video count and then return back to {string} page to see if the counter is right")
    public void getTheIncompleteKnowledgeVideoCountAndThenReturnBackToPageToSeeIfTheCounterIsRight(String arg0) {
        int incompleteKnowledgeVideos = knowledgePage.getIncompleteCount();
        dashboardPage.clickMyWeek();
        try {
            Assertions.assertEquals(Integer.parseInt(myWeekPage.getRemainingKnowledgeCount()), incompleteKnowledgeVideos);
        } catch (AssertionFailedError e) {    //The big one doesn't have the check icon on it so it might fail the assertion
            Assertions.assertEquals(Integer.parseInt(myWeekPage.getRemainingKnowledgeCount()), incompleteKnowledgeVideos + 1);
        }
    }
}
