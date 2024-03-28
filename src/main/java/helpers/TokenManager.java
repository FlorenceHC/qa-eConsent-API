package helpers;

import com.google.gson.JsonObject;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.time.Instant;

import static helpers.TCLogger.*;
import static helpers.propertyFile.DataProvider.*;
import static helpers.propertyFile.DataProvider.AUTH0_URL;
import static io.restassured.RestAssured.given;

public class TokenManager {
  private static String access_token;
  private static Instant expiry_time;

  public static RequestSpecification getIntegrationUser() {
    return new RequestSpecBuilder()
            .setBaseUri(OAUTH_TOKEN_URL)
            .addHeader("Content-Type", "application/json")
            .build();
  }


  public static Response getBearerToken_Auth0() {

    JsonObject jsonPayload = new JsonObject();
    jsonPayload.addProperty("client_id", client_id_1);
    jsonPayload.addProperty("client_secret", client_secret_1);
    jsonPayload.addProperty("grant_type", "client_credentials");
    jsonPayload.addProperty("audience", AUTH0_URL);

    String body = String.valueOf(jsonPayload);

    return given(getIntegrationUser())
            .body(body)
            .when()
            .post()
            .then()
//            .spec(SpecBuilder.getResponseSpec())
            .extract()
            .response();
  }


  public static synchronized String getToken() {
    try {
      if (access_token == null || Instant.now().isAfter(expiry_time)) {
        Response response = renewToken();
        access_token = response.path("access_token");
        int expiryDurationInSeconds = response.path("expires_in");
        expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300);
      } else {
        loggerInformation("Integration User Token is valid");
      }
    } catch (Exception ex) {
      loggerStep_Failed("Unable to get Integration User Token", ex.getMessage(), true);
    }
    return access_token;
  }

  public static Response renewToken() {

    Response response = getBearerToken_Auth0();

    if (response.statusCode() != 200) {
      loggerAssert_Failed("Unable to get m2m User Token" + "-=-" + "Integration User Token, Status Code: "  + response.statusCode() + "--" + "Integration User Token, Response Body: "  + response.body(), true);
    }else{
      loggerInformation("Integration User Token, Status Code: "  + response.statusCode());
//      loggerInformation("Integration User Token, Response Body: "  + response.body().prettyPrint());
    }
    return response;
  }

}
