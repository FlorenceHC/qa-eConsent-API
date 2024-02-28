package helpers.slack;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static helpers.TCLogger.*;
import static helpers.Utils.writeInPropertiesFile;
import static helpers.jiraXrayReporting.JiraXrayAPI.*;
import static helpers.propertyFile.DataProvider.*;
import static helpers.propertyFile.PropertyReader.getProperties;

public class SlackAPI {

    private static boolean slackMessageSent_QaValidatio = false;
    private static boolean slackMessageSent_PipelineResults = false;

    public static void sendSlackMsg_QaValidation(String suiteName, String payloadMsg) {

        if (!slackMessageSent_QaValidatio && payloadMsg.equals("start") && RC.contains("true") && TEST_RUN.equals("regression")) {

            String ts_value = sendSlackMessage(slackMessage(payloadMsg), QA_VALIDATION, "");
            writeInPropertiesFile("tsQaValidation", ts_value, "src/main/resources/tsQaValidation.properties");

        } else if (payloadMsg.equals("end") && RC.contains("true") && TEST_RUN.equals("regression")){

            String tsQaValidationData = getProperties("src/main/resources/tsQaValidation.properties", "tsQaValidation");
            uploadFileToSlack("results.txt", slackMessage(payloadMsg), QA_VALIDATION, tsQaValidationData);

        } else {
            loggerInformation("Slack message is not sent to: " + QA_VALIDATION + "-" + payloadMsg + "-" + RC);
        }

        slackMessageSent_QaValidatio = true;
    }


    public static void sendSlackMsg_PipelineResults(String suiteName, String payloadMsg) {

        if(!slackMessageSent_PipelineResults && payloadMsg.contains("gha-start") && RC.contains("gha") && XRAY.equals("false")){

            String ts_value = sendSlackMessage(slackMessage(payloadMsg), PIPELINE_RESULTS, "");
            writeInPropertiesFile("tsPipelineResults", ts_value, "src/main/resources/tsPipelineResults.properties");

        } else if (payloadMsg.equals("gha-end") && RC.contains("gha") && XRAY.equals("false")){

            String tsPipelineResultsData = getProperties("src/main/resources/tsPipelineResults.properties", "tsPipelineResults");
            uploadFileToSlack( "results.txt", "", PIPELINE_RESULTS, tsPipelineResultsData);

        } else {
            loggerInformation("Slack message is not sent to: " + PIPELINE_RESULTS + "-" + payloadMsg + "-" + RC);
        }

        slackMessageSent_PipelineResults= true;
    }



    public static String sendSlackMessage(String message, String channel, String threadTs) {

        String ts = null;
        try {
            Response response = RestAssured.given()
                    .multiPart("text", message)
                    .multiPart("channel", channel)
                    .multiPart("thread_ts", threadTs)
                    .header("Authorization", "Bearer " + SLACK_BOT_TOKEN)
                    .contentType("multipart/form-data")
                    .when()
                    .post(SLACK_API_ENDPOINT_POST_MESSAGE);

            String responseString = response.getBody().asString();
            String statusCode = String.valueOf(response.getStatusCode());

            JSONObject jsonObject = new JSONObject(responseString);
            ts = jsonObject.getString("ts");

            if (statusCode.equals("200")) {
                loggerInformation("Send Message To Slack Chanel Sent Successfully: " + statusCode);
//                loggerInformation("Send Message To Slack Chanel Sent Successfully: " + responseString);
            } else {
                loggerStep_Failed("Unable to Send Message To Slack Chanel: ", statusCode, false);
                loggerStep_Failed("Unable to Send Message To Slack Chanel: ", responseString, false);
            }
        } catch (Exception ex) {
            loggerStep_Failed("Unable to Send Message To Slack Message: ", ex.getMessage(), false);
        }
        return ts;
    }

    public static void uploadFileToSlack(String filePath, String  message, String channels, String threadTs) {

        File file = new File(filePath);
        try {
            Response response = RestAssured.given()
                    .multiPart("file", file)
                    .multiPart("initial_comment", message)
                    .multiPart("channels", channels)
                    .multiPart("thread_ts", threadTs)
                    .header("Authorization", "Bearer " + SLACK_BOT_TOKEN)
                    .contentType("multipart/form-data")
                    .when()
                    .post(SLACK_API_ENDPOINT_UPLOAD);

            String responseString = response.getBody().asString();
            String statusCode = String.valueOf(response.getStatusCode());

            if(statusCode.equals("200")){
                loggerInformation("Upload File To Slack Message Sent Successfully: " + statusCode);
//                loggerInformation("Upload File To Slack Message Sent Successfully: " + responseString);
            }else{
                loggerStep_Failed("Unable Upload File To Slack Message: ", statusCode, false);
                loggerStep_Failed("Unable Upload File To Slack Message: ", responseString, false);
            }
        } catch (Exception ex) {
            loggerStep_Failed("Unable Upload File To Slack Message: ", ex.getMessage(), false);
        }
    }

    public static String slackMessage(String payloadMsg) {

        String msg = null;
        String xRayRunId_ = "--> Test Execution: Not Created" + "\n";
        String releaseCandidate = "";
        String suiteNameFix = null;


        switch (TEST_RUN) {
            case "regression":
                suiteNameFix = "Regression";
                break;
            case "smoke":
                suiteNameFix = "Smoke";
                break;
            case "testSet":
                suiteNameFix = "Partial Regression";
                break;
        }


        if (!XRAY_RUN_ID.equals("false") && !XRAY.equals("false")) {
            if (XRAY_RUN_ID.equals("new")) {
                xRayRunId_ =  "--> Xray Execution: <https://florencehc.atlassian.net/browse/" + xRayRunId + "|" + xRayRunId + ">" + "\n";
            } else if (XRAY_RUN_ID.contains("-")){
                xRayRunId_ =  "--> Xray Execution: <https://florencehc.atlassian.net/browse/" + XRAY_RUN_ID + "|" + XRAY_RUN_ID + ">" + "\n";
            }
        }

        if(RC.contains("RC")){releaseCandidate =  "--> Release candidate: " + RC + "\n";}

        if(payloadMsg.equals("start")){
            msg = ":warning: @here "  + "\n" +
                    "*" + suiteNameFix + " testing STARTED!*" + "\n" +
                    "--> App: " + DEV_TEAM + " " + APP_NAME + "\n" +
                    "--> App version: " + "no information" + "\n" +
                    "--> App revision: " + "no information" + "\n" +
                    "--> Environment: " + ENVIRONMENT  + "\n" +
                    "--> Server: " + SERVER + "\n" +
                    "--> GHA Execution: " + "<" + GHA_RUN_URL + GHA_RUN_ID + "|" + "GHA-" + GHA_RUN_ID + ">" + "\n" +
                    releaseCandidate +
                    xRayRunId_ +
                    "*Please don't deploy any changes until the regression testing is completed.*" + "\n" +
                    "Message will be sent automatically when testing is finished. For more information contact #automation-squad";
        }else if (payloadMsg.equals("end")){
            msg = ":checkered_flag: @here " + "\n" +
                    "*" + suiteNameFix + " testing COMPLETED!*" + "\n" +
                    "--> GHA Execution: " + "<" + GHA_RUN_URL + GHA_RUN_ID + "|" + "GHA-" + GHA_RUN_ID + ">" + "\n" +
                    xRayRunId_ +
                    "Thank you for your patience.";
        }

        if (payloadMsg.equals("gha-start")){
            DateTimeFormatter instantTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String time = LocalDateTime.now().format(instantTime);
            msg = "<" + GHA_RUN_URL + GHA_RUN_ID + "|" + "GHA-" + GHA_RUN_ID + " " + suiteNameFix + " -- "  + "> "  + DEV_TEAM + " " + APP_NAME + " - " + ENVIRONMENT + "_" + SERVER + " - " + time;
        }
        return msg;
    }
}
