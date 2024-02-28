package helpers;

import io.restassured.builder.*;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.*;

import static helpers.propertyFile.DataProvider.*;

public class SpecBuilder {
  public static RequestSpecification getRequestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(baseURI())
        .addHeader("Content-Type", "application/json")
        .log(LogDetail.ALL)
        .build();
  }

  public static ResponseSpecification getResponseSpec() {
    return new ResponseSpecBuilder().log(LogDetail.ALL).build();
  }

  public static RequestSpecification getIntegrationUser() {
    return new RequestSpecBuilder()
        .setBaseUri(OAUTH_TOKEN_URL)
        .addHeader("Content-Type", "application/json")
        .build();
  }
}
