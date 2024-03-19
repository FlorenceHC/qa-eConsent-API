package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static helpers.TCLogger.*;

public class JsonValidator {

    public static boolean validateJsonResponse(String jsonResponse, String schemaFilePath) {
        try {
            JsonNode responseNode = JsonLoader.fromString(jsonResponse);
            JsonNode schemaNode = loadSchemaFromFile(schemaFilePath);
            return validateJsonAgainstSchema(responseNode, schemaNode);
        } catch (Exception ex) {
            loggerStep_Failed("Unable to Validate JSON Response", ex.getMessage(), false);
            return false;
        }
    }

    private static JsonNode loadSchemaFromFile(String schemaFilePath) throws IOException {
        Path path = Paths.get(schemaFilePath);
        byte[] schemaBytes = Files.readAllBytes(path);
        return JsonLoader.fromString(new String(schemaBytes));
    }

    private static boolean validateJsonAgainstSchema(JsonNode responseNode, JsonNode schemaNode) {
        try {
            JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(schemaNode);
            schema.validate(responseNode);
            return true;
        } catch (ProcessingException ex) {
            loggerStep_Failed("Unable to Validate JSON Against Schema", ex.getMessage(), false);
            return false;
        }
    }

    public static void jsonSchemaValidator(String jsonResponse, String schemaFilePath) {

        if (validateJsonResponse(jsonResponse, schemaFilePath)) {
            loggerAssert_Passed("JSON Schema: " + schemaFilePath + " is valid");
        } else {
            String messageContentFailed = "JSON Schema: " + schemaFilePath + " is NOT valid";
            loggerAssert_Failed(messageContentFailed);
            Assert.fail(messageContentFailed);
        }
    }
}
