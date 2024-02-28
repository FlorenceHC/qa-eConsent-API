package helpers.jiraXrayReporting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import static helpers.TCLogger.*;
import static helpers.Utils.*;
import static helpers.propertyFile.DataProvider.*;
import static helpers.propertyFile.DataProvider.xRayRunIdDataFate.*;
import static helpers.propertyFile.DataProvider.xRayRunIdDataMig.*;
import static helpers.propertyFile.DataProvider.xRayRunIdDataQa.*;
import static helpers.propertyFile.DataProvider.xRayRunIdDataUat.*;
import static helpers.propertyFile.DataProvider.xRay_token_Data.*;


public class JiraXrayAPI {
    static private final String authToken_1 = getXrayToken(1);
    static private final String authToken_2 = getXrayToken(2);
    static private int run_number = 1;
    static boolean setTestRun_Xray = false;
    static  public String testRun_issueID;
    static private String testCase_issueID;
    static public String xRayRunId = "Not Created";
    static private final String env = ENVIRONMENT;
    static private final String testRun = TEST_RUN;

    static private boolean tokenCreated = false;

    public static String authToken() {
        String authToken;
        String xRay_clientName;

        if(run_number == 1){
            authToken = authToken_1;
            run_number = 2;
            xRay_clientName = XRAY_CLIENT_NAME;
        }else{
            authToken = authToken_2;
            run_number = 1;
            xRay_clientName = XRAY_CLIENT_NAME_2;
        }
        loggerInformation("Using Xray Authentication Token from User: " + xRay_clientName);

        return authToken;
    }

    public static String getXray_token_fromPropertiesFile() {

        if (XRAY.equals("true")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long ts = timestamp.getTime();
            long l = Long.parseLong(XRAY_TOKEN_TIME);
            long deltaTime = ts - l;
//            23h = 82800000 milliseconds. It was not put 24h to be safe
            long oneDay = 82800000;
            long expiredTime = oneDay - deltaTime;
            String expiredTime_ = (new SimpleDateFormat("HH:mm:ss")).format(new Date(expiredTime));
            String xRayToken;

            if (deltaTime > oneDay) {

                if (!tokenCreated) {
                    loggerInformation("Xray Token has expired > 24h");
                    xRayToken = jasyptEncodingString(SECRET_KEY, getXrayToken(1));

                    String projectPath = System.getProperty("user.dir");
                    String path = projectPath + "/src/main/resources/xRayToken.properties";
                    try {
                        Properties props = new Properties();
                        props.put("xRay_token", xRayToken);
                        props.put("xRay_token_time", Long.toString(ts));
                        FileOutputStream outputStream = new FileOutputStream(path);
                        props.store(outputStream, "This is Xray token and Xray token time. If Token time is >24h, new token will be created.");
                        loggerInformation("Properties file created: " + path);
                        tokenCreated = true;
                    } catch (Exception ex) {
                        loggerStep_Failed("Properties file: " + path + " -Not created", ex.getMessage(), true);
                    }
                } else {
                    loggerInformation("Xray Token it has already been created");
                }
            } else {
                loggerInformation("Xray Token has not expired < 24h");
            }
        }
        return jasyptDecodingString(SECRET_KEY, XRAY_TOKEN);
    }

    public static String getXRayRunId() {
        if (testRun.equals("regression") || testRun.equals("testSet")) {
            if (env.equals(ENV_VARIABLE_QA)) {
                xRayRunId = XRAY_RUN_ID_QA_REGRESSION;
            } else if (env.equals(ENV_VARIABLE_FATE)) {
                xRayRunId = XRAY_RUN_ID_FATE_REGRESSION;
            } else if (env.equals(ENV_VARIABLE_UAT)) {
                xRayRunId = XRAY_RUN_ID_UAT_REGRESSION;
            }else if (env.equals(ENV_VARIABLE_MIG)) {
                xRayRunId = XRAY_RUN_ID_MIG_REGRESSION;
            }
        } else if (testRun.equals("smoke"))
            if (env.equals(ENV_VARIABLE_QA)) {
                xRayRunId = XRAY_RUN_ID_QA_SMOKE;
            } else if (env.equals(ENV_VARIABLE_FATE)) {
                xRayRunId = XRAY_RUN_ID_FATE_SMOKE;
            } else if (env.equals(ENV_VARIABLE_UAT)) {
                xRayRunId = XRAY_RUN_ID_UAT_SMOKE;
            } else if (env.equals(ENV_VARIABLE_MIG)) {
                xRayRunId = XRAY_RUN_ID_MIG_SMOKE;
            }

//        LoggerInformation("Xray Run ID: " + xRayRunId);
        return xRayRunId;
    }

    public static String getXRayTestRun() {

        if (testRun.equals("regression") || testRun.equals("testSet")) {
            if (env.equals(ENV_VARIABLE_QA)) {
                xRayRunId = XRAY_RUN_ID_QA_REGRESSION;
            } else if (env.equals(ENV_VARIABLE_FATE)) {
                xRayRunId = XRAY_RUN_ID_FATE_REGRESSION;
            } else if (env.equals(ENV_VARIABLE_UAT)) {
                xRayRunId = XRAY_RUN_ID_UAT_REGRESSION;
            }
        } else if (testRun.equals("smoke"))
            if (env.equals(ENV_VARIABLE_QA)) {
                xRayRunId = XRAY_RUN_ID_QA_SMOKE;
            } else if (env.equals(ENV_VARIABLE_FATE)) {
                xRayRunId = XRAY_RUN_ID_FATE_SMOKE;
            } else if (env.equals(ENV_VARIABLE_UAT)) {
                xRayRunId = XRAY_RUN_ID_UAT_SMOKE;
            }

//        LoggerInformation("Xray Run ID: " + xRayRunId);
        return xRayRunId;
    }

    public static String getXrayToken(int user) {

        String xRay_clientName = (user == 1) ? XRAY_CLIENT_NAME : XRAY_CLIENT_NAME_2;


        if (XRAY.equals("true")) {
                loggerInformation("Creating Xray Authentication Token for user: " + xRay_clientName);

                try {
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpPost httpPost;
                    httpPost = new HttpPost(XRAY_AUTHENTICATE_URL);
                    String resultPayload = "{ \"client_id\": \"" + xRay_clientId(user) + "\", \"client_secret\": \"" +  xRay_clientSecret(user) + "\" }";
//                LoggerInformation("resultPayload: " + resultPayload);
                    StringEntity requestEntity = new StringEntity(resultPayload);
                    httpPost.setEntity(requestEntity);
                    httpPost.setHeader("Content-type", "application/json");
                    HttpResponse response = httpClient.execute(httpPost);
                    int status = response.getStatusLine().getStatusCode();
                    String statusString = Integer.toString(status);

                    if (statusString.equals("200")) {
                        HttpEntity entity = response.getEntity();
                        String responseString = EntityUtils.toString(entity, "UTF-8");
                        loggerInformation("Xray Authentication Token API status: " + statusString);
                        String authToken =  responseString.replace("\"", "");
                        return authToken;
                    } else {
                        loggerInformation("Xray Authentication Token API Failed, status: " + statusString);
                        loggerInformation("Xray Authentication Token API Failed, response: " + response);
                        Assert.fail();
                    }
                } catch (Exception ex) {
                    loggerStep_Failed("Xray Authentication Token Field: ", ex.getMessage(), true);
                }
        } else {
            loggerInformation("Xray Option is: " + XRAY);
        }
        return null;
    }

    public static void setTestRun_Xray(String suiteName) {
        if (XRAY_RUN_ID.equals("new")) {
            if (XRAY.equals("true") & !setTestRun_Xray) {

//            String authToken = getXray_token_fromPropertiesFile();

                String testRun_summary;
                String testRun_description;
                String testRun_user = xRayUserSelector(ASSIGNEE_ID_1, ASSIGNEE_ID_2);
                String testRun_testPlanKey;
                String testRun_testKey = TEST_KEY;
                String testRun_status = "EXECUTING";
                DateTimeFormatter instantTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                String time = LocalDateTime.now().format(instantTime);
                String testRun_key = null;
                String id;
                String self;
                String partial_1 = "";
                String partial_2 = "";

                if (TEST_RUN.equals("regression") || TEST_RUN.equals("testSet")) {
                    testRun_testPlanKey = REGRESSION_TEST_PLAN_KEY;
                    if (TEST_RUN.equals("testSet")) {
                        partial_1 = "Partial ";
                        partial_2 = " - " + suiteName;}
                    testRun_summary = partial_1 + REGRESSION_TEST_RUN_TEST_SUMMARY + partial_2;
                    testRun_description = REGRESSION_TEST_RUN_TEST_DESCRIPTION;
                } else if (TEST_RUN.equals("smoke")) {
                    testRun_testPlanKey = SMOKE_TEST_PLAN_KEY;
                    testRun_summary = SMOKE_TEST_RUN_TEST_SUMMARY;
                    testRun_description = SMOKE_TEST_RUN_TEST_DESCRIPTION;
                } else {
                    testRun_testPlanKey = null;
                    loggerInformation("Test Plan for Test Run: " + TEST_RUN + "Not Found");
                    testRun_summary = null;
                    loggerInformation("Test Run Summary for Test Run: " + TEST_RUN + "Not Found");
                    testRun_description = null;
                    loggerInformation("Test Run Description for Test Run: " + TEST_RUN + "Not Found");
                    Assert.fail();
                }

                String summary = testRun_summary + " --> eConsent API " + ENVIRONMENT + "_" + SERVER + " - " + time;

                try {
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpPost httpPost;
                    httpPost = new HttpPost(XRAY_EXECUTION_URL);
                    String resultPayload = String.valueOf(setTestRun_Payload(summary, testRun_description, testRun_user, testRun_status, testRun_testPlanKey, testRun_testKey));
//                LoggerInformation("resultPayload: " + resultPayload);
                    StringEntity requestEntity = new StringEntity(resultPayload);
                    httpPost.setEntity(requestEntity);
                    httpPost.setHeader("Content-type", "application/json");
                    httpPost.setHeader("Authorization", "Bearer " + authToken());
                    HttpResponse response = httpClient.execute(httpPost);
                    int status = response.getStatusLine().getStatusCode();
                    String statusString = Integer.toString(status);

                    HttpEntity responseEntity = response.getEntity();
                    String jsonObjectString = EntityUtils.toString(responseEntity, "UTF-8");
//                LoggerInformation("jsonObjectString: " + jsonObjectString);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(jsonObjectString);

                    if (statusString.equals("200")) {
                        loggerInformation("Xray Create Test Run API status: " + statusString);
                        testRun_issueID = jsonObject.get("id").toString();
                        testRun_key = jsonObject.get("key").toString();
                        self = jsonObject.get("self").toString();
                        loggerInformation("Xray Test Case Result ID: " + testRun_issueID);
                        loggerInformation("Xray Test Case Result key: " + testRun_key);
                        setTestRun_Xray = true;
//                    LoggerInformation("Test Case Result self: " + self);
//                        Delete dummy Test Case
                        deleteTestCaseFromExecution(testRun_issueID, getTestCase_issuedId(TEST_KEY));
                    } else {
                        loggerInformation("XRay Create Test Run  status: " + statusString);
                        loggerInformation("eConsent POST API Response Body: " + jsonObject);
                    }
                } catch (Exception ex) {
                    loggerStep_Failed("Create Test Run Field: ", ex.getMessage(), true);
                }
                if (testRun_key != null) {
                    String testRun_ = testRun;
                    if (TEST_RUN.equals("testSet")) {testRun_ = "regression";}
                    String projectPath = System.getProperty("user.dir");
                    String path = projectPath + "/src/main/resources/" + testRun_ + "/" + env + "_xRayRunId.properties";
                    try {
                        //Instantiating the properties file
                        Properties props = new Properties();
                        //Populating the properties file
                        props.put("xRayRunIdData", testRun_key);
                        //Instantiating the FileInputStream for output file
                        FileOutputStream outputStrem = new FileOutputStream(path);
                        //Storing the properties file
                        props.store(outputStrem, "This is " + testRun_ + "/" + env + "_xRayRunId.properties file");
                        loggerInformation("Properties file created: " + path);
                    } catch (Exception ex) {
                        loggerInformation("Properties file: " + path + " -Not created");
                    }
                } else {
                    loggerInformation("Test Key is Not Created");
                }
            } else {
                loggerInformation("This Test Run does Not imply the Creation of a Xray Test Report");
            }
        } else if (XRAY_RUN_ID.contains("-")) {
            String testRun_ = testRun;
            if (TEST_RUN.equals("testSet")) {testRun_ = "regression";}
            String projectPath = System.getProperty("user.dir");
            String path = projectPath + "/src/main/resources/" + testRun_ + "/" + env + "_xRayRunId.properties";
            try {
                loggerInformation("Xray Run ID is taken from input data: " + XRAY_RUN_ID);
                //Instantiating the properties file
                Properties props = new Properties();
                //Populating the properties file
                props.put("xRayRunIdData", XRAY_RUN_ID);
                //Instantiating the FileInputStream for output file
                FileOutputStream outputStrem = new FileOutputStream(path);
                //Storing the properties file
                props.store(outputStrem, "This is " + testRun_ + "/" + env + "_xRayRunId.properties file");
                loggerInformation("Properties file created: " + path);
            } catch (Exception ex) {
                loggerInformation("Properties file: " + path + " -Not created");
            }
        }else{
            loggerInformation("Wrong Xray Run ID: " + XRAY_RUN_ID);
            Assert.fail();
        }
    }

    public static void setTcResult_Xray(String nameTC, String testRunId, String testKey, String testStatus, String getReport, boolean jiraTicket, String jiraTicketNo) {

        loggerInformation("Writing the Test Case results in Xray: " + XRAY);
        if (XRAY.equals("true")) {

//            String authToken = getXray_token_fromPropertiesFile();


            String testStatus_data;
            if (testStatus.equals("FAILED") && (jiraTicket)) {
                testStatus_data = "KNOWN_ISSUE";
            } else {
                testStatus_data = testStatus;
            }

            try {
                String token = authToken();
//                LoggerInformation("authToken: " + token);
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost;
                httpPost = new HttpPost(XRAY_EXECUTION_URL);
                String resultPayload = configureJson(nameTC, testStatus_data, testRunId, testKey, getReport, jiraTicket, jiraTicketNo);
//                LoggerInformation("resultPayload: " + resultPayload);
                StringEntity requestEntity = new StringEntity(resultPayload);
                httpPost.setEntity(requestEntity);
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Authorization", "Bearer " + token);
                HttpResponse response = httpClient.execute(httpPost);
                int status = response.getStatusLine().getStatusCode();
                String statusString = Integer.toString(status);

//                HttpEntity responseEntity = response.getEntity();
//                InputStream inputStream = responseEntity.getContent();
//                JSONObject jsonObject = (JSONObject) new JSONParser().parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                if (statusString.equals("200")) {
                    loggerInformation("Xray Set Test Result API status: " + statusString);

//                    String id = jsonObject.get("id").toString();
//                    String key = jsonObject.get("key").toString();
//                    String self = jsonObject.get("self").toString();
//                    LoggerInformation("Test Case Result ID: " + id);
//                    LoggerInformation("Test Case Result key: " + key);
//                    LoggerInformation("Test Case Result self: " + self);
                } else {
                    loggerInformation("XRay set Test Result status: " + statusString);
//                    LoggerInformation("eConsent POST API Response Body: " + jsonObject);
                }
            } catch (Exception ex) {
                loggerStep_Failed("Add New Test Result Field: ", ex.getMessage(), true);
            }
        }
    }

    private static String configureJson(String nameTC, String testStatus, String testRunId, String testKey, String comment, Boolean jiraTicket, String jiraTicketNo) {

        String json = null;
        final String testCaseLogStart = "*Test Case Execution Log*\n=====\n\n --- Test Case - " + nameTC + " -- *Start* ---\n";
        final String testCaseLogEnd = "\n\n=====\n\n";
        final String commentFull = testCaseLogStart + comment + testCaseLogEnd;

        switch (testStatus) {
            case "FAILED":
            case "KNOWN_ISSUE":
            case "SKIPPED":
            case "PASSED":
                if (jiraTicket) {
                    json = String.valueOf(setTcResult_Xray_Payload(true, false, testStatus, testRunId, testKey, commentFull, jiraTicketNo));
                } else {
                    json = String.valueOf(setTcResult_Xray_Payload(false, false, testStatus, testRunId, testKey, commentFull, jiraTicketNo));
                }
                break;
            case "EXECUTING":
                json = String.valueOf(setTcResult_Xray_Payload(false, false, testStatus, testRunId, testKey, "The test is currently running", jiraTicketNo));

                break;
        }
        return json;
    }

    public static JsonObject setTcResult_Xray_Payload(boolean defect_, boolean evidence_, String testStatus, String testRunId, String testKey, String commentFull, String ticketNo){

        String encodedFile = null;
        String fileName = null;

        JsonObject jsonPayload = new JsonObject();

        jsonPayload.addProperty("testExecutionKey", testRunId);

        JsonObject tests = new JsonObject();
        JsonArray testsArray = new JsonArray();
        tests.addProperty("testKey", testKey);
        tests.addProperty("status", testStatus);
        tests.addProperty("comment", commentFull);
        testsArray.add(tests);

        if (defect_) {
            JsonArray defectsArray = new JsonArray();
            defectsArray.add(ticketNo);
            tests.add("defects", defectsArray);
        }

        if (evidence_) {
            JsonArray evidenceArray = new JsonArray();
            JsonObject evidence = new JsonObject();
            evidence.addProperty("data", encodedFile);
            evidence.addProperty("filename", fileName);
            evidence.addProperty("contentType", "image/jpeg");
            evidenceArray.add(evidence);
            tests.add("evidence", evidenceArray);
        }

        jsonPayload.add("tests", testsArray);

        return jsonPayload;
    }

    public static JsonElement setTestRun_Payload(String testRun_summary, String testRun_description, String testRun_user, String testRun_status, String testRun_testPlanKey, String testKey){

        JsonObject jsonPayload = new JsonObject();

        JsonObject info = new JsonObject();
        info.addProperty("summary", testRun_summary);
        info.addProperty("description", testRun_description);
        info.addProperty("user", testRun_user);
        info.addProperty("testPlanKey", testRun_testPlanKey);
//        info.addProperty("version", app_version);
        jsonPayload.add("info", info);

        JsonArray testEnvironments = new JsonArray();
        testEnvironments.add(ENVIRONMENT);
//        info.add("testEnvironments", testEnvironments);

        JsonObject tests = new JsonObject();
        JsonArray testsArray = new JsonArray();
        tests.addProperty("testKey", testKey);
        tests.addProperty("status", testRun_status);
        tests.addProperty("comment", testRun_status);
        testsArray.add(tests);

        jsonPayload.add("tests", testsArray);

        return jsonPayload;
    }

    public static String getTestCase_issuedId(String tcId) {

        try {
//            String authToken = getXray_token_fromPropertiesFile();

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(XRAY_GRAPHQL_URL);

            String requestBody = "{ \"query\": \"{ getTests(jql: \\\"Issue = '" + tcId + "'\\\", limit: 1) { results { issueId } } }\" }";
//            LoggerInformation("requestBody: " + requestBody);

            httpPost.setEntity(new StringEntity(requestBody));
            httpPost.setHeader("Authorization", "Bearer " + authToken_1);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
//            LoggerInformation("responseString: " + responseString);
            String statusString = String.valueOf(response.getStatusLine().getStatusCode());

            if (statusString.contains("200")) {

                org.json.JSONObject items = new org.json.JSONObject(responseString);
                org.json.JSONObject jObj = items.getJSONObject("data").getJSONObject("getTests");
                String getTests = String.valueOf(jObj).replace("[", "").replace("]", "");

                org.json.JSONObject items1 = new org.json.JSONObject(getTests);
                org.json.JSONObject jObj1 = items1.getJSONObject("results");
                testCase_issueID = String.valueOf(jObj1).replace("{\"issueId\":\"", "").replace("\"}", "");

                loggerInformation("Xray Get TC issued ID for TC " + tcId + ": " + testCase_issueID);
                return testCase_issueID;

            } else {
                loggerInformation("Xray Get TC issued ID Status: " + statusString);
                loggerInformation("Xray Get TC issued ID Response: " + responseString);
                Assert.fail();
            }
        } catch (Exception ex) {
            loggerStep_Failed("Unable to get Xray TC issued ID: ", ex.getMessage(), true);
        }
        return null;
    }

    public static void deleteTestCaseFromExecution( String testRun_issueID_, String testCase_issueID_) {

        try {

//            String authToken = getXray_token_fromPropertiesFile();

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(XRAY_GRAPHQL_URL);

            String requestBody1 = "{\"query\":\"mutation {removeTestsFromTestExecution(issueId: \\\"" + testRun_issueID_ + "\\\", testIssueIds: [\\\"" + testCase_issueID_+ "\\\"])} \",\"variables\":{}}";
//            LoggerInformation("requestBody: " + requestBody1);

            httpPost.setEntity(new StringEntity(requestBody1));
            httpPost.setHeader("Authorization", "Bearer " + authToken_1);
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
//            LoggerInformation("responseString: " + responseString);
            String statusString = String.valueOf(response.getStatusLine().getStatusCode());

            if (statusString.contains("200")) {
                loggerInformation("Xray Delete Test Case From Execution Status: " + statusString);

                org.json.JSONObject items = new org.json.JSONObject(responseString);
                org.json.JSONObject jObj = items.getJSONObject("data");
                String removeTestsFromTestExecution = String.valueOf(jObj).replace("{\"removeTestsFromTestExecution\":\"", "").replace("\"}", "");

                loggerInformation("Xray Delete Test Case From Execution Response: " + removeTestsFromTestExecution);
            } else {
                loggerInformation("Xray Get TC issued ID Status: " + statusString);
                loggerInformation("Xray Get TC issued ID Response: " + responseString);
                Assert.fail();
            }
        } catch (Exception ex) {
            loggerStep_Failed("Unable to get Xray TC issued ID: ", ex.getMessage(), true);
        }
    }

    public static String multipart_postMethod(String URL, String firstFilePath, String secondFilePath, String authToken) {

//        LoggerInformation("Multipart Post Method");
//        LoggerInformation("XRAY_EXECUTION_URL: " + XRAY_EXECUTION_URL + "/multipart");
//        LoggerInformation("info: " + firstFileName);
//        LoggerInformation("results: " + secondFileName);
//        LoggerInformation("info: " + firstFilePath);
//        LoggerInformation("results: " + secondFilePath);

        // Build the multipart entity
        HttpResponse response;
        String responseBody = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            String firstFileName = org.apache.commons.lang3.StringUtils.substringBetween(firstFilePath, "/", "");
            String secondFileName = org.apache.commons.lang3.StringUtils.substringBetween(secondFilePath, "/", "");
            HttpPost httpPost = new HttpPost(URL);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authToken);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("info", new File(firstFilePath), ContentType.APPLICATION_OCTET_STREAM, firstFileName + ".json")
                    .addBinaryBody("results", new File(secondFilePath), ContentType.APPLICATION_OCTET_STREAM, secondFileName + ".json");

            HttpEntity entity = entityBuilder.build();
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            responseBody = EntityUtils.toString(response.getEntity());

            if (statusCode == 200) {
                loggerInformation("Multipart Post Method: " + statusCode);
                loggerInformation("Multipart Post Method Response body: " + responseBody);
            } else {
                loggerInformation("Multipart Post Method Status: " + statusCode);
                loggerInformation("Multipart Post Method  Response: " + responseBody);
                Assert.fail();
            }
        } catch (Exception ex) {
            loggerStep_Failed("Unable to Execute Xray Multipart Post Method: ", ex.getMessage(), false);
        }
        return responseBody;

    }

    public static String setTestRun_Payload_info(String projectId, String assigneeId, String issueType, String testRun_summary, String testRun_description, String testRun_testPlanKey){

        JSONObject fields = new JSONObject();
        JSONObject project = new JSONObject();
        JSONObject assignee = new JSONObject();
        JSONObject issuetype = new JSONObject();
        JSONArray fixVersions = new JSONArray();
        JSONArray customfield_11069 = new JSONArray();
        JSONObject xrayFields = new JSONObject();

        project.put("id", projectId);
        assignee.put("id", assigneeId);
        issuetype.put("name", issueType);

        fields.put("project", project);
        fields.put("summary", testRun_summary);
        fields.put("description", testRun_description);
        fields.put("assignee", assignee);
        fields.put("issuetype", issuetype);


//      TODO Waiting for helatcheck
//        JSONObject fixVersion = new JSONObject();
//        fixVersion.put("name", app_version);
//        fixVersions.put(fixVersion);
//        fields.put("fixVersions", fixVersions);

        JSONObject customfield_11069Value = new JSONObject();
        customfield_11069Value.put("value", ENVIRONMENT);
        customfield_11069.put(customfield_11069Value);
        fields.put("customfield_11069", customfield_11069);

        JSONArray environments = new JSONArray();
        environments.put(ENVIRONMENT);
        xrayFields.put("environments", environments);
        xrayFields.put("testPlanKey", testRun_testPlanKey);

        JSONObject json = new JSONObject();
        json.put("fields", fields);
        json.put("xrayFields", xrayFields);

        String infoJsonFilePath = "src/main/resources/xRay/info.json";

        writeInTxtFile(String.valueOf(json), infoJsonFilePath);

        return infoJsonFilePath;
    }

    public static String setTestRun_Payload_results(String testRun_status, String testKey){

        JSONObject test = new JSONObject();
        test.put("testKey", testKey);
        test.put("status", testRun_status);

        JSONArray tests = new JSONArray();
        tests.put(test);

        JSONObject json = new JSONObject();
        json.put("tests", tests);

        String resultsJsonFilePath = "src/main/resources/xRay/results.json";

        writeInTxtFile(String.valueOf(json), resultsJsonFilePath);

        return resultsJsonFilePath;
    }

    public static void setTestRun_Xray_multipart(String suiteName) {
        if (XRAY_RUN_ID.equals("new")) {
            if (XRAY.equals("true") && !setTestRun_Xray) {

//            String authToken = getXray_token_fromPropertiesFile();

                String testRun_summary;
                String testRun_description;
                String testRun_user = xRayUserSelector(ASSIGNEE_ID_1, ASSIGNEE_ID_2);
                String testRun_testPlanKey;
                String testRun_testKey = TEST_KEY;
                String testRun_status = "EXECUTING";
                DateTimeFormatter instantTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                String time = LocalDateTime.now().format(instantTime);
                String testRun_key = null;
                String id;
                String self;
                String partial_1 = "";
                String partial_2 = "";

                if (TEST_RUN.equals("regression") || TEST_RUN.equals("testSet")) {
                    testRun_testPlanKey = REGRESSION_TEST_PLAN_KEY;
                    if (TEST_RUN.equals("testSet")) {
                        partial_1 = "Partial ";
                        partial_2 = " - " + suiteName;}
                    testRun_summary = partial_1 + REGRESSION_TEST_RUN_TEST_SUMMARY;
                    testRun_description = REGRESSION_TEST_RUN_TEST_DESCRIPTION + " on " + " " + APP_NAME +  "\\\n" +  "GitHub Actions Link: [" + GHA_RUN_URL + GHA_RUN_ID + "|" + GHA_RUN_URL + GHA_RUN_ID + "]";
                } else if (TEST_RUN.equals("smoke")) {
                    testRun_testPlanKey = SMOKE_TEST_PLAN_KEY;
                    testRun_summary = SMOKE_TEST_RUN_TEST_SUMMARY;
                    testRun_description = SMOKE_TEST_RUN_TEST_DESCRIPTION + " on " + " " + DEV_TEAM + " " + APP_NAME +  "\\\n" +  "GitHub Actions Link: [" + GHA_RUN_URL + GHA_RUN_ID + "|" + GHA_RUN_URL + GHA_RUN_ID + "]";
                } else {
                    testRun_testPlanKey = null;
                    loggerInformation("Test Plan for Test Run: " + TEST_RUN + "Not Found");
                    testRun_summary = null;
                    loggerInformation("Test Run Summary for Test Run: " + TEST_RUN + "Not Found");
                    testRun_description = null;
                    loggerInformation("Test Run Description for Test Run: " + TEST_RUN + "Not Found");
                    Assert.fail();
                }

                String summary = testRun_summary + " --> " + APP_NAME + " " + ENVIRONMENT + "_" + SERVER + " - " + time;

                try {
                    String info = setTestRun_Payload_info(JIRA_PROJECT_ID, testRun_user, JIRA_ISSUE_TYPE, summary, testRun_description, testRun_testPlanKey);
                    String results = setTestRun_Payload_results(testRun_status, testRun_testKey);
                    String response = multipart_postMethod(XRAY_EXECUTION_URL + "/multipart", info, results, authToken_1);

                    testRun_issueID = org.apache.commons.lang3.StringUtils.substringBetween(response, "{\"id\":\"", "\"");
                    xRayRunId = org.apache.commons.lang3.StringUtils.substringBetween(response, "\"key\":\"", "\"");
                    self = org.apache.commons.lang3.StringUtils.substringBetween(response, "\"self\":\"", "\"");
                    loggerInformation("Xray Test Case Result key: " + xRayRunId);
                    loggerInformation("Xray Test Case Result ID: " + testRun_issueID);
                    setTestRun_Xray = true;
//                     LoggerInformation("Test Case Result self: " + self);
//                        Delete dummy Test Case
                    deleteTestCaseFromExecution(testRun_issueID, getTestCase_issuedId(TEST_KEY));

                } catch (Exception ex) {
                    loggerStep_Failed("Create Test Run Field: ", ex.getMessage(), true);
                }
                if (xRayRunId != null) {
                    String testRun_ = testRun;
                    if(TEST_RUN.equals("testSet")) {testRun_ = "regression";}
                    String projectPath = System.getProperty("user.dir");
                    String path = projectPath + "/src/main/resources/" + testRun_ + "/" + env + "_xRayRunId.properties";
                    try {
                        writeInPropertiesFile("xRayRunIdData", xRayRunId,  path);
//                        writeInPropertiesFile("xRayIssueIdData", testRun_issueID,  path);
                    } catch (Exception ex) {
                        loggerInformation("Properties file: " + path + " -Not created");
                    }
                }else{
                    loggerInformation("Test Key is Not Created");
                }
            } else {
                loggerInformation("This Test Run does Not imply the Creation of a Xray Test Report");
            }
        } else if (XRAY_RUN_ID.contains("-")) {
            String testRun_ = testRun;
            xRayRunId = XRAY_RUN_ID;
            if (TEST_RUN.equals("testSet")) {testRun_ = "regression";}
            String projectPath = System.getProperty("user.dir");
            String path = projectPath + "/src/main/resources/" + testRun_ + "/" + env + "_xRayRunId.properties";
            try {
                loggerInformation("Xray Execution ID is taken from input data: " + XRAY_RUN_ID);
                writeInPropertiesFile("xRayRunIdData", xRayRunId,  path);
                setTestRun_Xray = true;
            } catch (Exception ex) {
                loggerInformation("Properties file: " + path + " -Not created");
            }
        }else{
            loggerInformation("Wrong Xray Run ID: " + XRAY_RUN_ID);
            Assert.fail();
        }
    }

}