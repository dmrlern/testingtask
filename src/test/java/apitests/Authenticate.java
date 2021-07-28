package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import testprojectcore.dataprovider.EnvironmentDataProvider;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

public class Authenticate {

    public String getBearerToken() throws IOException {
        RestAssured.baseURI = "https://login.caspar-health.com";

        Response response =
                given().
                        contentType(ContentType.JSON).
                        header("Content-Type", "application/x-www-form-urlencoded").
                        formParam("grant_type", "password").
                        formParam("client_id", "frontend").
                        formParam("scope", "openid email offline").
                        formParam("audience", "urn:casparhealth:service:backend").
                        formParam("nonce", "rADcm0w8FzTKRiiCqiTlF").
                        formParam("username", EnvironmentDataProvider.TESTDATA.getData("webapp.username")).
                        formParam("password", EnvironmentDataProvider.TESTDATA.getData("webapp.password")).
                        when().log().all().
                        post("/token?lang=en").
                        then().log().all().
                        assertThat().statusCode(200).body("isEmpty()", Matchers.is(false)).and().body("$", hasKey("access_token")).
                        using().extract().response();

        return response.jsonPath().getString("access_token");
    }
}
