package helpers;

import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import java.io.File;
import java.util.Arrays;

import static helpers.JsonValidator.jsonSchemaValidator;
import static helpers.TCLogger.*;
import static helpers.propertyFile.DataProvider.*;
import static io.restassured.RestAssured.*;

public class RestResource {

  /**
   * RestAssured API POST Method - This method checks the status code, the body as a string that is an array, and as a JSON Schema. Also, it is possible to turn off all these verifications, so the method returns the Response.
   * @param path - API URL path
   * @param token  - Authorization token
   * @param requestBody - A request body is data sent by the client to your API.
   * @param contentType - The Content-Type field in the HTTP headers indicates in which format the data is sent to, or returned by, the HTTP methods of the Rule Execution Server REST API
   * @param responseCode_expected - "expected"  The API response is the generic error message, given when an unexpected condition was encountered and no more specific message is suitable
   * @param responseBody_expected - "expected" - The response body can contain information about the output and additional output ports. The response body can also contain a success or failure code and message.
   * @param assertStep - Option whether we want to verify all values received from the api call
   * @param JSON_Path -The path to the JSON file used for Json Schema Validator
   */
  public static Response post_ApiMethod(String path, String token, Object requestBody, String contentType, int responseCode_expected, String [] responseBody_expected, boolean assertStep, String JSON_Path) {

    String schemaFilePath = "src/test/resources/" + JSON_Path + ".json";

    String baseUri = baseURI();
    loggerInformation("API Endpoint: " + baseUri + path);
    loggerInformation("Request Body: " + requestBody);
    loggerInformation("Path: " + path);
    Response response = null;

    String headerValue = "Bearer " + token;
    if(token.isEmpty()) headerValue = "";

    try {
      RestAssured.urlEncodingEnabled = false;
      response = given()
//              .log().everything()
              .baseUri(baseUri)
              .contentType(contentType)
              .header("Authorization", headerValue)
              .body(requestBody)
              .when()
              .post(path)
              .then()
              .and()
              .assertThat()
              .extract()
              .response();

      int responseCode = response.getStatusCode();
      String responseBody = response.getBody().prettyPrint();

      if (assertStep) {
        if (responseCode == responseCode_expected) {
          loggerAssert_Passed("Verification successful. Status Code for API POST: " + responseCode);
          loggerInformation("Response Body: " + responseBody);

          for (String str : responseBody_expected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
              Assert.fail();
            }
          }

          jsonSchemaValidator(responseBody, schemaFilePath);

        } else {
          loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
          loggerAssert_Passed("Response Body: " + responseBody);
          Assert.fail();
        }
      }
    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
    }
    return response;
  }


  /**
   * RestAssured API POST Method - This method checks the status code, the body as a string that is an array, and as a JSON Schema. Also, it is possible to turn off all these verifications, so the method returns the Response.
   * @param path - API URL path
   * @param token  - Authorization token
//   * @param requestBody - A request body is data sent by the client to your API.
   * @param contentType - The Content-Type field in the HTTP headers indicates in which format the data is sent to, or returned by, the HTTP methods of the Rule Execution Server REST API
   * @param responseCode_expected - "expected"  The API response is the generic error message, given when an unexpected condition was encountered and no more specific message is suitable
   * @param responseBody_expected - "expected" - The response body can contain information about the output and additional output ports. The response body can also contain a success or failure code and message.
   * @param assertStep - Option whether we want to verify all values received from the api call
   * @param JSON_Path -The path to the JSON file used for Json Schema Validator
   */
  public static Response postContent_ApiMethod(String path, String token, File file, String contentType, int responseCode_expected, String [] responseBody_expected, boolean assertStep, String JSON_Path) {

    String schemaFilePath = "src/test/resources/" + JSON_Path + ".json";

    String baseUri = baseURI();
    loggerInformation("API Endpoint: " + baseUri + path);
//    LoggerInformation("Request Body: " + requestBody);
    loggerInformation("Path: " + path);
    Response response = null;
    try {
      RestAssured.urlEncodingEnabled = false;
      response = given()
//              .log().all()
              .baseUri(baseUri)
              .contentType(contentType)
              .header("Authorization", "Bearer " + token)
              .multiPart("file", file)
              .when()
              .post(path)
              .then()
              .and()
              .assertThat()
              .extract()
              .response();

      int responseCode = response.getStatusCode();
      String responseBody = response.getBody().prettyPrint();

      if (assertStep) {
        if (responseCode == responseCode_expected) {
          loggerAssert_Passed("Verification successful. Status Code for API POST: " + responseCode);
          loggerInformation("Response Body: " + responseBody);

          for (String str : responseBody_expected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
              Assert.fail();
            }
          }

          jsonSchemaValidator(responseBody, schemaFilePath);

        } else {
          loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
          loggerAssert_Passed("Response Body: " + responseBody);
          Assert.fail();
        }
      }

    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
    }
    return response;
  }

  /**
   * RestAssured API PUT Method - This method checks the status code, the body as a string that is an array, and as a JSON Schema. Also, it is possible to turn off all these verifications, so the method returns the Response.
   * @param path - API URL path
   * @param token  - Authorization token
   * @param contentType - The Content-Type field in the HTTP headers indicates in which format the data is sent to, or returned by, the HTTP methods of the Rule Execution Server REST API
   * @param responseCode_expected - "expected"  The API response is the generic error message, given when an unexpected condition was encountered and no more specific message is suitable
   * @param responseBody_expected - "expected" - The response body can contain information about the output and additional output ports. The response body can also contain a success or failure code and message.
   * @param assertStep - Option whether we want to verify all values received from the api call
   * @param JSON_Path -The path to the JSON file used for Json Schema Validator
   */
  public static Response putContent_ApiMethod(String path, String token, File file, String contentType, int responseCode_expected, String [] responseBody_expected, boolean assertStep, String JSON_Path) {

    String schemaFilePath = "src/test/resources/" + JSON_Path + ".json";

    loggerInformation("API Endpoint: " + path);
    loggerInformation("Path: " + path);
    Response response = null;
    try {
      RestAssured.urlEncodingEnabled = false;
      response = given()
//              .log().all()
              .baseUri(path)
              .contentType(contentType)
              .header("Authorization", "Bearer " + token)
              .multiPart("file", file)
              .when()
              .put(path)
              .then()
              .and()
              .assertThat()
              .extract()
              .response();

      int responseCode = response.getStatusCode();
      String responseBody = response.getBody().prettyPrint();

      if (assertStep) {
        if (responseCode == responseCode_expected) {
          loggerAssert_Passed("Verification successful. Status Code for API POST: " + responseCode);
          loggerInformation("Response Body: " + responseBody);

          for (String str : responseBody_expected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
              Assert.fail();
            }
          }

          jsonSchemaValidator(responseBody, schemaFilePath);

        } else {
          loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
          loggerAssert_Passed("Response Body: " + responseBody);
          Assert.fail();
        }
      }
    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
    }
    return response;
  }

  public static Response putContent_ApiMethod(String path, File file, String contentType, int responseCode_expected, String [] responseBody_expected, boolean assertStep) {

    loggerInformation("API Endpoint: " + path);
    Response response = null;
    try {
      RestAssured.urlEncodingEnabled = false;
      if(file ==null){
        response = given()
//                .log().all()
                .baseUri(path)
                .contentType(contentType)
                .when()
                .put(path)
                .then()
                .and()
                .assertThat()
                .extract()
                .response();
      }
      else{
        response = given()
//                .log().all()
                .baseUri(path)
                .contentType(contentType)
                .multiPart("", file)
                .when()
                .put(path)
                .then()
                .and()
                .assertThat()
                .extract()
                .response();
      }
      
      int responseCode = response.getStatusCode();
      String responseBody = response.getBody().prettyPrint();

      if (assertStep) {
        if (responseCode == responseCode_expected) {
          loggerAssert_Passed("Verification successful. Status Code for API POST: " + responseCode);
          loggerInformation("Response Body: " + responseBody);

          for (String str : responseBody_expected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
              Assert.fail();
            }
          }
        } else {
          loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
          loggerAssert_Passed("Response Body: " + responseBody);
          Assert.fail();
        }
      }

    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
    }
    return response;
  }

  public static Response putContent_ApiMethod(String path, String contentType, int responseCode_expected, String [] responseBody_expected, boolean assertStep) {

    loggerInformation("API Endpoint: " + path);
    Response response = null;
    try {
      RestAssured.urlEncodingEnabled = false;

      response = given()
//              .log().all()
              .baseUri(path)
              .contentType(contentType)
              .when()
              .put(path)
              .then()
              .and()
              .assertThat()
              .extract()
              .response();

      int responseCode = response.getStatusCode();
      String responseBody = response.getBody().prettyPrint();

      if (assertStep) {
        if (responseCode == responseCode_expected) {
          loggerAssert_Passed("Verification successful. Status Code for API POST: " + responseCode);
          loggerInformation("Response Body: " + responseBody);

          for (String str : responseBody_expected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
              Assert.fail();
            }
          }
        } else {
          loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
          loggerAssert_Passed("Response Body: " + responseBody);
          Assert.fail();
        }
      }

    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
    }
    return response;
  }

  /**
   * RestAssured API PUT Method - This method checks the status code, the body as a string that is an array, and as a JSON Schema. Also, it is possible to turn off all these verifications, so the method returns the Response.
   * @param path - API URL path
   * @param token  - Authorization token
   * @param requestBody - A request body is data sent by the client to your API.
   * @param responseCode_expected - "expected"  The API response is the generic error message, given when an unexpected condition was encountered and no more specific message is suitable
   * @param responseBody_expected - "expected" - The response body can contain information about the output and additional output ports. The response body can also contain a success or failure code and message.
   * @param assertStep - Option whether we want to verify all values received from the api call
   */
  public static Response put_ApiMethod(String token, String path, Object requestBody, int responseCode_expected, String [] responseBody_expected, boolean assertStep, boolean validateJsonSchema, String JSON_Path) {

    String schemaFilePath = "src/test/resources/" + JSON_Path + ".json";

    String baseUri = baseURI();
    loggerInformation("API Endpoint: " + baseUri + path);
  //   LoggerInformation("Bearer Token: " + token);

    String headerValue = "Bearer " + token;
    if(token.isEmpty()) headerValue = "";

    loggerInformation("Request Body: " + requestBody);
    loggerInformation("Path: " + path);
    Response response = null;
    try {
      response = expect()
              .given()
//              .log().all()
              .baseUri(baseUri)
              .contentType("application/json")
              .header("Authorization", headerValue)
              .body(requestBody)
              .when()
              .put(path)
              .then()
              .and()
              .assertThat()
              .extract()
              .response();

      int responseCode = response.statusCode();
      String responseBody = response.getBody().prettyPrint();
      if(validateJsonSchema){
        jsonSchemaValidator(responseBody, schemaFilePath);
      }

      if (assertStep) {
        if (responseCode == responseCode_expected) {
          loggerAssert_Passed("Verification successful.Status Code for API POST: " + responseCode);
         

          for (String str : responseBody_expected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
              Assert.fail();
            }
          }
          } else{
            loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
            loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + Arrays.toString(responseBody_expected) + ", Actual- " + responseBody);
            Assert.fail();
          }
        }
    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
    }
    return response;
  }

  /**
   * RestAssured API GET Method - This method checks the status code, the body as a string that is an array, and as a JSON Schema. Also, it is possible to turn off all these verifications, so the method returns the Response.
   * @param path - API URL path
   * @param token  - Authorization token
   * @param responseCode_expected - "expected"  The API response is the generic error message, given when an unexpected condition was encountered and no more specific message is suitable
   * @param responseBody_expected - "expected" - The response body can contain information about the output and additional output ports. The response body can also contain a success or failure code and message.
   * @param assertStep - Option whether we want to verify all values received from the api call
   * @param JSON_Path -The path to the JSON file used for Json Schema Validator
   */
  public static Response get_ApiMethod(String path, String token, int responseCode_expected, String [] responseBody_expected, boolean assertStep, String JSON_Path) {

    String schemaFilePath = "src/test/resources/" + JSON_Path + ".json";

    String baseUri = baseURI();
    loggerInformation("API Endpoint: " + baseUri + path);
    loggerInformation("Bearer Token: " + token);
    String headerValue = "Bearer " + token;
    if(token.isEmpty()) headerValue = "";
    Response response = null;
    try {
      RestAssured.urlEncodingEnabled = false;
      response = expect()
              .given()
//                      .log().all()
                      .baseUri(baseUri)
                      .header("Authorization", headerValue)
              .when()
                    .get(path)
              .then()
              .and()
                   .assertThat()
                   .extract()
                   .response();
      int responseCode = response.statusCode();
      String responseBody = response.getBody().prettyPrint();

      if (assertStep) {
        if (responseCode == responseCode_expected) {
          loggerAssert_Passed("Verification successful.Status Code for API POST: " + responseCode);
          loggerInformation("Response Body: " + responseBody);

          for (String str : responseBody_expected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
              Assert.fail();
            }
        }

          jsonSchemaValidator(responseBody, schemaFilePath);

        } else {
          loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
          loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + Arrays.toString(responseBody_expected) + ", Actual- " + responseBody);
          Assert.fail();
        }
      }
    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API GET Method", ex.getMessage(), true);
    }
    return response;
  }

  /**
   * RestAssured API UPDATE Method - This method checks the status code, the body as a string that is an array, and as a JSON Schema. Also, it is possible to turn off all these verifications, so the method returns the Response.
   * @param path - API URL path
   * @param token  - Authorization token
   * @param requestBody - A request body is data sent by the client to your API.
   * @param responseCode_expected - "expected"  The API response is the generic error message, given when an unexpected condition was encountered and no more specific message is suitable
   * @param responseBody_expected - "expected" - The response body can contain information about the output and additional output ports. The response body can also contain a success or failure code and message.
   * @param assertStep - Option whether we want to verify all values received from the api call
   * @param JSON_Path -The path to the JSON file used for Json Schema Validator
   * @param contentType - The Content-Type field in the HTTP headers indicates in which format the data is sent to, or returned by, the HTTP methods of the Rule Execution Server REST API
   */
  public static Response patch_ApiMethod(String token, String path, Object requestBody,String contentType,int responseCode_expected, String [] responseBody_expected, boolean assertStep, String JSON_Path) {

    String schemaFilePath = "src/test/resources/" + JSON_Path + ".json";

      String baseUri = baseURI();
      loggerInformation("API Endpoint: " + baseUri + path);
      loggerInformation("Request Body: " + requestBody);
      Response response = null;

      try {
        response = expect()
                .given()
                .baseUri(baseUri)
                .contentType(contentType)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .patch(path)
                .then()
                .and()
                .assertThat()
                .extract()
                .response();

        int responseCode = response.statusCode();
        String responseBody = response.getBody().prettyPrint();

        if (assertStep) {
          if (responseCode == responseCode_expected) {
            loggerAssert_Passed("Verification successful.Status Code for API POST: " + responseCode);
           
                  loggerInformation("Response Body: " + responseBody);

            for (String str : responseBody_expected) {
              if (responseBody.contains(str)) {
                loggerAssert_Passed("Verification successful. Response Body contains: " + str);
              } else {
                loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
                Assert.fail();
              }
            }

            jsonSchemaValidator(responseBody, schemaFilePath);

          } else {
            loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
            loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + Arrays.toString(responseBody_expected) + ", Actual- " + responseBody);
            Assert.fail();
          }
        }
      } catch (Exception ex) {
        loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
      }
      return response;
    }

  /**
   * RestAssured API DELETE Method - This method checks the status code, the body as a string that is an array, and as a JSON Schema. Also, it is possible to turn off all these verifications, so the method returns the Response.
   * @param path - API URL path
   * @param token  - Authorization token
   * @param responseCode_expected - "expected"  The API response is the generic error message, given when an unexpected condition was encountered and no more specific message is suitable
   * @param responseBody_expected - "expected" - The response body can contain information about the output and additional output ports. The response body can also contain a success or failure code and message.
   * @param assertStep - Option whether we want to verify all values received from the api call
   * @param JSON_Path -The path to the JSON file used for Json Schema Validator
   */
  public static Response delete_ApiMethod(String token, String path, int responseCode_expected, String [] responseBody_expected, boolean assertStep, boolean validateJsonSchema, String JSON_Path) {

    String schemaFilePath = "src/test/resources/" + JSON_Path + ".json";

    String baseUri = baseURI();
    loggerInformation("API Endpoint: " + baseUri + path);

    String headerValue = "Bearer " + token;
    if(token.isEmpty()) headerValue = "";

    Response response = null;
    try {
      response = expect()
              .given()
              .baseUri(baseUri)
              .header("Authorization", headerValue)
              .when()
              .delete(path)
              .then()
              .and()
              .assertThat()
              .extract()
              .response();

      int responseCode = response.statusCode();
      String responseBody = response.getBody().prettyPrint();

      if (assertStep) {
        if(validateJsonSchema){
          jsonSchemaValidator(responseBody, schemaFilePath);
        }

        if (responseCode == responseCode_expected) {
          loggerAssert_Passed("Verification successful.Status Code for API POST: " + responseCode);
          loggerInformation("Response Body: " + responseBody);

          for (String str : responseBody_expected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody);
              Assert.fail();
            }
          }
        } else {
          loggerAssert_Failed("Verification unsuccessful! Status Code for API POST: Expected- " + responseCode_expected + ", Actual- " + responseCode);
          loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + Arrays.toString(responseBody_expected) + ", Actual- " + responseBody);
          Assert.fail();
        }
      }
    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
    }
    return response;
  }


  public static Response getBearerToken_Auth0() {

    JsonObject jsonPayload = new JsonObject();
    jsonPayload.addProperty("client_id", client_id_1);
    jsonPayload.addProperty("client_secret", client_secret_1);
    jsonPayload.addProperty("grant_type", "client_credentials");
    jsonPayload.addProperty("audience", AUTH0_URL);

    String body = String.valueOf(jsonPayload);

    return given(SpecBuilder.getIntegrationUser())
            .body(body)
            .when()
            .post()
            .then()
//            .spec(SpecBuilder.getResponseSpec())
            .extract()
            .response();
  }

}
