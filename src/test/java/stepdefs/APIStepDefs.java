package stepdefs;

import apitests.Exercises;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;


public class APIStepDefs {

    Response response;


    @Given("Call GET exercise configs with {string}")
    public void callGETExerciseConfigsWith(String arg0) throws IOException {
        Exercises exercises = new Exercises();
        response = exercises.getExerciseConfigs(arg0);
    }

    @And("GET exercise configs: It should return exercise info")
    public void getExerciseConfigsItShouldReturnExerciseInfo() {
        Assertions.assertEquals(response.getBody().jsonPath().getString("title"), "#173 Walking forwards along a line, throwing a ball");
    }

    @And("POST exercise results {string}, {string}, {string}, {string}")
    public void postExerciseResults(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Exercises exercises = new Exercises();
        response = exercises.completeExercise(arg0, arg1, arg2, arg3);
    }

    @And("POST exercise results: Http response status code should be {int} if not completed, 422 if already completed")
    public void postExerciseResultsHttpResponseStatusCodeShouldBe(int arg0) throws IOException {
        if (!response.getBody().jsonPath().getString("errors.completed_at[0]").equals("Exercise was already accomplished")) {
            Assertions.assertEquals(201, response.statusCode());
        } else {
            Assertions.assertEquals(422, response.statusCode());
        }
    }
}
