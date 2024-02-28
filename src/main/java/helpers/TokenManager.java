package helpers;

import io.restassured.response.Response;
import org.testng.Assert;

import java.time.Instant;

import static helpers.TCLogger.*;

public class TokenManager {
  private static String access_token;
  private static Instant expiry_time;

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

    Response response = RestResource.getBearerToken_Auth0();

    if (response.statusCode() != 200) {
      loggerAssert_Failed("Unable to get m2m User Token");
      loggerInformation("Integration User Token, Status Code: "  + response.statusCode());
      loggerInformation("Integration User Token, Response Body: "  + response.body());
      Assert.fail();
    }else{
      loggerInformation("Integration User Token, Status Code: "  + response.statusCode());
//      loggerInformation("Integration User Token, Response Body: "  + response.body().prettyPrint());
    }
    return response;
  }

}
