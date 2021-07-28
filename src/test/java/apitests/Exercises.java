package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import requests.CompleteExerciseRequest;
import testprojectcore.dataprovider.JacksonObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

public class Exercises {

    String bearerToken = null;

    public Exercises() throws IOException {
        bearerToken = UseBearerToken.INSTANCE.getBearerToken();
    }


    public Response completeExercise(String exerciseConfigId, String sessionNumber, String startedAt, String finishedAt) throws IOException {

        RestAssured.baseURI = "https://backend.caspar-health.com";
        RestAssured.basePath = "/api/v1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startedAtTime = LocalDateTime.parse(startedAt, formatter);
        LocalDateTime finishedAtTime = LocalDateTime.parse(finishedAt, formatter);


        CompleteExerciseRequest completeExerciseRequest = JacksonObjectMapper.mapJsonFileToObject(CompleteExerciseRequest.class, "src/test/java/requests/completeexercise.json");
        completeExerciseRequest.exercise_result.exercise_config_id = Integer.parseInt(exerciseConfigId);
        completeExerciseRequest.exercise_result.session_number = Integer.parseInt(sessionNumber);
        completeExerciseRequest.exercise_result.started_at = startedAtTime;
        completeExerciseRequest.exercise_result.finished_at = finishedAtTime;
        completeExerciseRequest.exercise_result.sets_count = 2;
        completeExerciseRequest.exercise_result.intensity_level = 1;


        Response response = given().contentType(ContentType.JSON).
                when().log().all().
                header("Authorization", "Bearer " + bearerToken).
                body(completeExerciseRequest).
                post("/exercise_results.json?lang=en").
                then().log().all().
                assertThat().extract().response();

        return response;
    }


    public Response getExerciseConfigs(String exerciseId) throws IOException {

        Response response = given().contentType(ContentType.JSON).
                when().log().all().
                header("Authorization", "Bearer " + bearerToken).
                get("https://backend.caspar-health.com/api/v1/exercise_configs/" + exerciseId + ".json?lang=en").
                then().log().all().
                assertThat().extract().response();

        return response;
    }
}