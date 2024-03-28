package helpers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static helpers.JsonValidator.jsonSchemaValidator;
import static helpers.TCLogger.*;
import static helpers.propertyFile.DataProvider.*;
import static io.restassured.RestAssured.*;

public class RestResource {


  /**
   * RestAssured REST API Method - This method checks the status code, the body as a string that is an array, and as a JSON Schema. Also, it is possible to turn off all these verifications, so the method returns the Response.
   * @param method - The HTTP method to be used in the request
   *  @param path - API URL path
   * @param token  - Authorization token
   * @param requestBody - A request body is data sent by the client to your API.
   * @param contentType - The Content-Type field in the HTTP headers indicates in which format the data is sent to, or returned by, the HTTP methods of the Rule Execution Server REST API
   * @param responseCodeExpected - "expected"  The API response is the generic error message, given when an unexpected condition was encountered and no more specific message is suitable
   * @param responseBodyExpected - "expected" - The response body can contain information about the output and additional output ports. The response body can also contain a success or failure code and message.
   * @param assertStep - Option whether we want to verify all values received from the api call
   * @param validateJsonSchema - Option whether we want to verify the JSON Schema
   * @param JSONPath -The path to the JSON file used for Json Schema Validator
   */

  public static Response restApiMethod(String method, String path, String token, Object requestBody, String contentType, int responseCodeExpected, String [] responseBodyExpected, boolean assertStep, boolean validateJsonSchema, String JSONPath) {

    String schemaFilePath = "src/test/resources/" + JSONPath + ".json";

    String baseUri = baseURI();
    loggerInformation("API Endpoint: " + baseUri + path);
    loggerInformation("Path: " + path);
    Response response = null;

    try {
      RestAssured.urlEncodingEnabled = false;
      RequestSpecification request =given()
//              .log().all()
              .baseUri(baseUri)
              .header("Authorization", "Bearer " + token)
              .when();

      if (contentType != null) {
        request.contentType(contentType);
        loggerInformation("Content Type: " + contentType);
      }

      if (requestBody != null) {
        request.body(requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
          String bodyJson = objectMapper.writeValueAsString(requestBody);
          loggerInformation("Request Body: " + bodyJson);
        } catch (JsonProcessingException ex) {
          loggerStep_Failed("Error converting the request body to JSON: ", ex.getMessage(), false);
        }
      }

      switch (method.toUpperCase()) {
        case "GET":
          response = request.get(path);
          break;
        case "POST":
          response = request.post(path);
          break;
        case "PUT":
          response = request.put(path);
          break;
        case "PATCH":
          response = request.patch(path);
          break;
        case "DELETE":
          response = request.delete(path);
          break;
        default:
          loggerStep_Failed("Test failed, method is wrong, response is null", "", true);
          break;
      }

      response = response
              .then()
              .and()
              .assertThat()
              .extract()
              .response();

      int responseCode = response.getStatusCode();
      String responseBody = response.getBody().prettyPrint();

      if (assertStep) {
        if (responseCode == responseCodeExpected) {
          loggerAssert_Passed("Verification successful. Status Code for API " + method.toLowerCase() + ": " + responseCode);
          loggerAssert_Passed("Verification successful. JSON Schema is correct");
          loggerInformation("Response Body: " + responseBody);

          for (String str : responseBodyExpected) {
            if (responseBody.contains(str)) {
              loggerAssert_Passed("Verification successful. Response Body contains: " + str);
            } else {
              loggerAssert_Failed("Verification unsuccessful! Response Body: Expected- " + str + ", Actual- " + responseBody, true);
            }
          }
          if(validateJsonSchema){
            jsonSchemaValidator(responseBody, schemaFilePath);
          }
        } else {
          loggerAssert_Failed("Verification unsuccessful! Status Code for API " + method.toUpperCase() + ": Expected- " + responseCodeExpected + ", Actual- " + responseCode + "--" + "Response Body: " + responseBody, true);
        }
      }
    } catch (Exception ex) {
      loggerStep_Failed("Unable to execute API POST Method", ex.getMessage(), true);
    }
    return response;
  }

}