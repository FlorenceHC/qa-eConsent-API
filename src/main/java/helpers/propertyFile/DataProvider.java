package helpers.propertyFile;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static helpers.Utils.*;
import static helpers.propertyFile.PropertyReader.getProperties;


/**
 * These methods stored value as String from Resources Properties Files using the PropertyReader method
 */

public class DataProvider {


    /**
     * Data Provider for Environment variable (QA/FATE/UAT/PRODUCTION)
     */
    public static final String ENVIRONMENT = System.getProperty("env");


    public static final String ENV_QA = ".qa";
    public static final String ENV_FATE = ".fate";
    public static final String ENV_MIG = ".mig";
    public static final String ENV_UAT = ".uat";
    public static final String ENV_PROD = "";

    /**
     * Data Provider for Server variable (us/de/aus)
     */
    public static String SERVER = System.getProperty("server");

    /**
     * Data Provider for Test Run variable (regression/smoke)
     */
    public static String TEST_RUN = System.getProperty("testRun");

    /**
     * Data Provider for Xray Test Run ID variable (new/"MOB-1111")
     */
    public static String XRAY_RUN_ID = System.getProperty("runID");

    /**
     * Data Provider for secret key, used for decoding
     */
    public static String SECRET_KEY = System.getProperty("secret_key");

    /**
     * Data Provider for Release candidate
     */
    public static String RC = System.getProperty("rc");

    /**
     * Data Provider for Random String (5 characters)
     */
    public static String randomString = randomString_character(5);

    /**
     * Data Provider for Test Case data and verifications
     */


    /**
     * Data Provider for Environment Data
     */

        static String environmentPath = "src/main/resources/environment.properties";

        public static String DEV_TEAM = getProperties(environmentPath, "devTeam");

        public static String COMPANY_NAME = getProperties(environmentPath, "companyName");

        public static String APP_NAME = getProperties(environmentPath, "appName");

        public static String HEALTH_CHECK_API = getProperties(environmentPath, "healthCheckAPI");

        public static String ENV_VARIABLE_QA = getProperties(environmentPath, "envVariableQA");
        public static String ENV_VARIABLE_FATE = getProperties(environmentPath, "envVariableFATE");
        public static String ENV_VARIABLE_UAT = getProperties(environmentPath, "envVariableUAT");
        public static String ENV_VARIABLE_MIG = getProperties(environmentPath, "envVariableMIG");
        public static String SYS_MAIL = getProperties(environmentPath, "sysMail");


    /**
     * Data Provider for Xray Data
     */
        static String xRayReaderPath = "src/main/resources/xRay/xRayData.properties";

        public static String JIRA_PROJECT_ID = getProperties(xRayReaderPath, "jiraProjectId");
        public static String JIRA_ISSUE_TYPE = getProperties(xRayReaderPath, "issueType");
        public static String XRAY_USER = getProperties(xRayReaderPath, "xRay_user");
        public static String XRAY_USER_1 = getProperties(xRayReaderPath, "xRay_user_1");
        public static String XRAY_USER_2 = getProperties(xRayReaderPath, "xRay_user_2");
        public static String XRAY = System.getProperty("xray");
        public static String XRAY_CLIENT_NAME = getProperties(xRayReaderPath, "client_name");
        public static String XRAY_CLIENT_ID = jasyptDecodingString(SECRET_KEY, getProperties(xRayReaderPath, "client_id"));
        public static String XRAY_CLIENT_SECRET = jasyptDecodingString(SECRET_KEY, getProperties(xRayReaderPath, "client_secret"));
        public static String XRAY_CLIENT_NAME_2 = getProperties(xRayReaderPath, "client_name_2");
        public static String XRAY_CLIENT_ID_2 = jasyptDecodingString(SECRET_KEY, getProperties(xRayReaderPath, "client_id_2"));
        public static String XRAY_CLIENT_SECRET_2 = jasyptDecodingString(SECRET_KEY, getProperties(xRayReaderPath, "client_secret_2"));
        public static String ASSIGNEE_ID_1 = getProperties(xRayReaderPath, "assigneeId_1");
        public static String ASSIGNEE_ID_2 = getProperties(xRayReaderPath, "assigneeId_2");
        public static String XRAY_AUTHENTICATE_URL = getProperties(xRayReaderPath, "xRay_authenticate_url");
        public static String XRAY_EXECUTION_URL = getProperties(xRayReaderPath, "xRay_execution_url");
        public static String XRAY_GRAPHQL_URL = getProperties(xRayReaderPath, "xRay_graphQl_url");
        public static String REGRESSION_TEST_RUN_TEST_SUMMARY = getProperties(xRayReaderPath, "regression_testRun_summary");
        public static String REGRESSION_TEST_RUN_TEST_DESCRIPTION = getProperties(xRayReaderPath, "regression_testRun_description");
        public static String REGRESSION_TEST_PLAN_KEY = getProperties(xRayReaderPath, "regression_testPlan_key");
        public static String SMOKE_TEST_RUN_TEST_SUMMARY = getProperties(xRayReaderPath, "smoke_testRun_summary");
        public static String SMOKE_TEST_RUN_TEST_DESCRIPTION = getProperties(xRayReaderPath, "smoke_testRun_description");
        public static String SMOKE_TEST_PLAN_KEY = getProperties(xRayReaderPath, "smoke_testPlan_key");
        public static String TEST_KEY = getProperties(xRayReaderPath, "testKey");



    public static class xRay_token_Data {


        static String xRayReaderTokenPath = "src/main/resources/xRay/xRayToken.properties";
        public static String XRAY_TOKEN_TIME = getProperties(xRayReaderTokenPath, "xRay_token_time");
        public static String XRAY_TOKEN = getProperties(xRayReaderTokenPath, "xRay_token");
    }

    public static String xRay_clientId(int user) {
        return (user == 2) ? XRAY_CLIENT_ID_2 : XRAY_CLIENT_ID;
    }

    public static String xRay_clientSecret(int user) {
        return (user == 2) ? XRAY_CLIENT_SECRET_2 : XRAY_CLIENT_SECRET;
    }

    /**
     * Data Provider for Xray RunId data Android QA Env
     */
    public static class xRayRunIdDataQa {

        //        Regression
        static String xRay_ID_QA_RegressionPath = "src/main/resources/regression/QA_xRayRunId.properties";
        public static String XRAY_RUN_ID_QA_REGRESSION = getProperties(xRay_ID_QA_RegressionPath, "xRayRunIdData");
        //        Smoke
        static String xRay_ID_QA_SmokePath = "src/main/resources/smoke/QA_xRayRunId.properties";
        public static String XRAY_RUN_ID_QA_SMOKE = getProperties(xRay_ID_QA_SmokePath, "xRayRunIdData");
    }

    /**
     * Data Provider for Xray RunId data Android FATE Env
     */
    public static class xRayRunIdDataFate {

        //        Regression
        static String xRay_ID_FATE_RegressionPath = "src/main/resources/regression/FATE_xRayRunId.properties";
        public static String XRAY_RUN_ID_FATE_REGRESSION = getProperties(xRay_ID_FATE_RegressionPath,"xRayRunIdData");
        //        Smoke
        static String xRay_ID_FATE_SmokePath = "src/main/resources/smoke/FATE_xRayRunId.properties";
        public static String XRAY_RUN_ID_FATE_SMOKE = getProperties(xRay_ID_FATE_SmokePath,"xRayRunIdData");
    }

    /**
     * Data Provider for Xray RunId data Android UAT Env
     */
    public static class xRayRunIdDataUat {

        //        Regression
        static String xRay_ID_UAT_RegressionPath = "src/main/resources/regression/UAT_xRayRunId.properties";
        public static String XRAY_RUN_ID_UAT_REGRESSION = getProperties(xRay_ID_UAT_RegressionPath,"xRayRunIdData");
        //        Smoke
        static String xRay_ID_UAT_SmokePath = "src/main/resources/smoke/UAT_xRayRunId.properties";
        public static String XRAY_RUN_ID_UAT_SMOKE = getProperties(xRay_ID_UAT_SmokePath,"xRayRunIdData");
    }

    /**
     * Data Provider for Xray RunId data Android MIG Env
     */
    public static class xRayRunIdDataMig {

        //        Regression
        static String xRay_ID_MIG_RegressionPath = "src/main/resources/regression/MIG_xRayRunId.properties";
        public static String XRAY_RUN_ID_MIG_REGRESSION = getProperties(xRay_ID_MIG_RegressionPath,"xRayRunIdData");
        //        Smoke
        static String xRay_ID_MIG_SmokePath = "src/main/resources/smoke/MIG_xRayRunId.properties";
        public static String XRAY_RUN_ID_MIG_SMOKE = getProperties(xRay_ID_MIG_SmokePath,"xRayRunIdData");
    }

    /**
     * Data Provider for routes
     */
    static String tcDataReaderPath = "src/main/resources/tcData.properties";

    public static String unauthorized_msg = getProperties(tcDataReaderPath,"unauthorized_msg");
    public static String invalidRequestBody_msg = getProperties(tcDataReaderPath,"invalidRequestBody_msg");
    public static String internalServerErrorOccurred_msg = getProperties(tcDataReaderPath,"internalServerErrorOccurred_msg");
    public static String emptyExtension = getProperties(tcDataReaderPath,"emptyExtension");
    public static String invalidRequestInput_msg = getProperties(tcDataReaderPath,"invalidRequestInput_msg");
    public static String missingTeamID_msg = getProperties(tcDataReaderPath,"missingTeamID_msg");
    public static String missingResource_msg = getProperties(tcDataReaderPath,"missingResource_msg");
    public static String incorrectDocument_msg = getProperties(tcDataReaderPath,"incorrectDocument_msg");
    public static String incorrectTeamID_msg = getProperties(tcDataReaderPath,"incorrectTeamID_msg");
    public static String malformedTeamID_msg = getProperties(tcDataReaderPath,"malformedTeamID_msg");
    public static String incorrectSortingDir_msg = getProperties(tcDataReaderPath,"incorrectSortDir_msg");
    public static String smallerNumberPerPage_msg=getProperties(tcDataReaderPath,"smallerNumberPerPage_msg");
    public  static String incorrectPage_msg = getProperties(tcDataReaderPath,"incorrectPage_msg");
    public static String incorrectFilter_msg = getProperties(tcDataReaderPath,"incorrect_FilterParameter_msg");
    public static String more_than_250_character = getProperties(tcDataReaderPath,"more_250_character_msg");
    public static String missingName = getProperties(tcDataReaderPath,"missingName_msg");
    public static String incorrectPerPage_msg = getProperties(tcDataReaderPath,"incorrectPerPage_msg");
    public static String largeNumberPerPage_msg = getProperties(tcDataReaderPath,"largeNumberThanExpectedPerPage_msg");
    public static String smallerNumberPage_msg = getProperties(tcDataReaderPath,"smallerNumberPage_msg");
    public static String documentNames = getProperties(tcDataReaderPath,"documentName");
    public static String invalidFileId = getProperties(tcDataReaderPath,"invalidFileId");
    public static String nonExistentFileId = getProperties(tcDataReaderPath,"nonExistentFileId");
    public static String explicitDeny = getProperties(tcDataReaderPath,"explicitDeny");
    public static String fileNotFound = getProperties(tcDataReaderPath,"fileNotFound");
    public static String fileMetadataNotFound = getProperties(tcDataReaderPath,"fileMetadataNotFound");
    public static String negativeValueError = getProperties(tcDataReaderPath,"negativeValueError");
    public static String wrongVersionType = getProperties(tcDataReaderPath,"wrongVersionType");
    public static String latestVersionError = getProperties(tcDataReaderPath, "latestVersionError");
    public static String couldNotFindMetadata = getProperties(tcDataReaderPath,"couldNotFindMetadata");
    public static String invalidExtension = getProperties(tcDataReaderPath,"invalidExtension");
    public static String convertedPageNotFound = getProperties(tcDataReaderPath, "convertedPageNotFound");


    /**
     * Data Provider for routes
     */
        static String routesPath = "src/main/resources/routes.properties";
        public static String  PRESIGNED_URL = getProperties(routesPath,"PRESIGNED_URL");
        public static String  DUPLICATE = getProperties(routesPath,"DUPLICATE");
        public static String  CONVERTED_PAGES = getProperties(routesPath,"CONVERTED_PAGES");
        public static String  FORM_FIELDS = getProperties(routesPath,"FORM_FIELDS");
        public static String  INVALID_TOKEN = getProperties(routesPath,"INVALID_TOKEN");

        static String configPath = "src/main/resources/config.properties";
        private static final String DBASE_MAIN_LINK_PART_ONE = getProperties(configPath, "dbaseMainLink_partOne");
        private static final String DBASE_MAIN_LINK_PART_TWO = getProperties(configPath,"dbaseMainLink_partTwo");

        private static final String USER_MAIN_LINK_PART_ONE = getProperties(configPath,"userMainLink_partOne");
        private static final String USER_MAIN_LINK_PART_TWO = getProperties(configPath,"userMainLink_partTwo");

        private static final String AUDIENCE_PART_ONE = getProperties(configPath,"audience_partOne");
        private static final String AUDIENCE_PART_TWO = getProperties(configPath,"audience_partTwo");



    public static String baseURI() {
        return DBASE_MAIN_LINK_PART_ONE + ENVIRONMENT.toLowerCase()  + DBASE_MAIN_LINK_PART_TWO;
    }

    public static String userURI() {
        return USER_MAIN_LINK_PART_ONE + ENVIRONMENT.toLowerCase() + "." + SERVER.toLowerCase() + USER_MAIN_LINK_PART_TWO;
    }

    public static String client_id_1 = jasyptDecodingString(SECRET_KEY, getProperties(configPath,ENVIRONMENT + "_client_id_1"));
    public static String client_secret_1 = jasyptDecodingString(SECRET_KEY, getProperties(configPath,ENVIRONMENT + "_client_secret_1"));
    public static String API_M2M_EMAIL_1 = getProperties(configPath,ENVIRONMENT + "_api_m2m_email_1");
    public static String API_M2M_EMAIL_2 = getProperties(configPath,ENVIRONMENT + "_api_m2m_email_2");
    public static String API_M2M_ID_1 = getProperties(configPath,ENVIRONMENT + "_api_m2m_ID_1");
    public static String API_M2M_ID_2 = getProperties(configPath,ENVIRONMENT + "_api_m2m_ID_2");




    public static String audience() {
        return  AUDIENCE_PART_ONE + SERVER.toLowerCase() + "." + ENVIRONMENT.toLowerCase() + AUDIENCE_PART_TWO;
    }


    /**
     * Data Provider for Slack
     */
    static String slackReaderPath = "src/main/resources/slack.properties";
    public static String SLACK_BOT_TOKEN = jasyptDecodingString(SECRET_KEY, getProperties(slackReaderPath, "slackBotToken"));
    public static String SLACK_WEBHOOK_URL = getProperties(slackReaderPath, "slackWebHookUrl");
    public static String SLACK_API_ENDPOINT_UPLOAD = getProperties(slackReaderPath, "slackApiEndpointUpload");
    public static String SLACK_API_ENDPOINT_POST_MESSAGE = getProperties(slackReaderPath, "slackApiEndpointPostMessage");
    public static String GHA_RUN_URL = getProperties(slackReaderPath, "ghaRunUrl");
    public static String SLACK_CHANNEL_TEAM = getProperties(slackReaderPath, "channelTeam");
    public static String SLACK_CHANNEL_RESULT = getProperties(slackReaderPath, "channelResult");
    public static String AUTOMATION_TEST = getProperties(slackReaderPath, "automationTest");
    public static String PIPELINE_RESULTS = getProperties(slackReaderPath, "pipelineResults");
    public static String QA_VALIDATION = getProperties(slackReaderPath, "qaValidation");
    public static String SLACK_USERNAME = getProperties(slackReaderPath, "username");
    public static String SLACK_ICON = getProperties(slackReaderPath, "icon_emoji");


    /**
     * Data Provider for GitHub Actions
     */

    static String ghaReaderPath = "src/main/resources/ghaData.properties";
    public static String GHA_RUN_ID = getProperties(ghaReaderPath, "ghaRunId");



    static String environmentReaderPath = "src/main/resources/environment.properties";

    //        eBinders Application URL
    final static String URL_FIRST_PART = getProperties(environmentReaderPath,"url_firstPart");
    final static String URL_SECOND_PART = getProperties(environmentReaderPath,"url_secondPart");
    public static String EBINDERS_APP_URL = URL_FIRST_PART + SERVER + "." + ENVIRONMENT.toLowerCase(Locale.ROOT) + URL_SECOND_PART;

    //        Auth URL
    final static String AUTH_URL_FIRST_PART = getProperties(environmentReaderPath,"authUrl_firstPart");
    final static String AUTH_URL_SECOND_PART = getProperties(environmentReaderPath,"authUrl_secondPart");
    public static String AUTH_URL = AUTH_URL_FIRST_PART + ENVIRONMENT.toLowerCase(Locale.ROOT) + AUTH_URL_SECOND_PART;

    //        Auth0 URL
    final static String AUTH0_URL_FIRST_PART = getProperties(environmentReaderPath,"auth0Url_firstPart");
    final static String AUTH0_URL_SECOND_PART = getProperties(environmentReaderPath,"auth0Url_secondPart");
    public static String AUTH0_URL = AUTH0_URL_FIRST_PART + ENVIRONMENT.toLowerCase(Locale.ROOT) + AUTH0_URL_SECOND_PART;

    //        Oauth token URL
    final static String OAUTH_TOKEN_URL_FIRST_PART = getProperties(environmentReaderPath,"oauth_token_url_firstPart");
    final static String OAUTH_TOKEN_URL_SECOND_PART = getProperties(environmentReaderPath,"oauth_token_url_secondPart");
    public static String OAUTH_TOKEN_URL = OAUTH_TOKEN_URL_FIRST_PART + ENVIRONMENT.toLowerCase(Locale.ROOT) + OAUTH_TOKEN_URL_SECOND_PART;

    //        Oauth token  Admin URL
    final static String OAUTH_TOKEN_URL_ADMIN_FIRST_PART = getProperties(environmentReaderPath,"oauth_token_url_admin_firstPart");
    final static String OAUTH_TOKEN_URL_ADMIN_SECOND_PART = getProperties(environmentReaderPath,"oauth_token_url_admin_secondPart");
    public static String OAUTH_TOKEN_URL_ADMIN = OAUTH_TOKEN_URL_ADMIN_FIRST_PART + ENVIRONMENT.toLowerCase(Locale.ROOT) + "." + SERVER + OAUTH_TOKEN_URL_ADMIN_SECOND_PART;

    //        CS App URl
    final static String CS_APP_URL_FIRST_PART = getProperties(environmentReaderPath,"csAppUrl_firstPart");
    final static String CS_APP_URL_SECOND_PART = getProperties(environmentReaderPath,"csAppUrl_secondPart");
    final static String CS_APP_URL_THIRD_PART = getProperties(environmentReaderPath,"csAppUrl_thirdPart");
    public static String CS_APP_URL = CS_APP_URL_FIRST_PART + SERVER + CS_APP_URL_SECOND_PART + ENVIRONMENT.toLowerCase(Locale.ROOT) + CS_APP_URL_THIRD_PART;


    // Team Level Permissions IDs
    public static String manageDocumentSharingRoleID = "documentSharingManage";
    public static String downloadDocumentsWithPHIID = "downloadDocumentWithPii";
    public static String manageLongTermArchiveID = "manageArchive";
    public static String manageMonitorReviewsID = "manageMonitorReviews";
    public static String manageQCReviewID = "manageQCreview";
    public static String manageTeamAndItsContentsID = "manageTeam";
    public static String manageTeamSecurityID = "manageTeamSecurity";
    public static String viewDocumentsWithPHIID = "viewPiiInDocument";


    // basic permissions defined in https://florencehc.atlassian.net/browse/EBS-27715
    public static List<String> getBasicPermissions() {
        List<String> basicPermissions = new ArrayList<>();
        basicPermissions.add(manageMonitorReviewsID);
        basicPermissions.add(manageLongTermArchiveID);
        basicPermissions.add(manageTeamAndItsContentsID);
        basicPermissions.add(downloadDocumentsWithPHIID);
        basicPermissions.add(viewDocumentsWithPHIID);

        return basicPermissions;
    }



}