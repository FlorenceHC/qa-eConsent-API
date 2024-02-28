package com.publicAPI.oauth2.tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import static helpers.AddTcResultsToFile.generateTestResults;
import static helpers.TCLogger.*;
import static helpers.TokenManager.*;
import static helpers.Utils.jasyptDecodingString;
import static helpers.Utils.jasyptEncodingString;
import static helpers.ZipFolder.zipFolder;
import static helpers.jiraXrayReporting.JiraXrayAPI.*;
import static helpers.propertyFile.DataProvider.*;
import static helpers.propertyFile.PropertyReader.getProperties;
import static helpers.slack.SlackAPI.*;


public class BaseTestSet {

    public String suiteName;
    public static String token;

    @BeforeSuite(alwaysRun=true,
                 description = "startSession")
    public void startSession(ITestContext context) {

//        Get Suite Name from TestNG xml file
        suiteName = context.getCurrentXmlTest().getSuite().getName();
        loggerInformation(": " + suiteName + ", one \"" + ENVIRONMENT + "\" environment");

//        Set Xray Test Run
        setTestRun_Xray_multipart(suiteName);

//        Sent Slack Message - Start of Test Run
        sendSlackMsg_QaValidation(suiteName, "start");
        sendSlackMsg_PipelineResults(suiteName, "gha-start");

//        Get m2m User Bearer Token
        token = getToken();
    }

    @Parameters({"zipEvidence"})
    @AfterSuite(alwaysRun=true,
            description = "Zip Evidence")
    public void zipEvidence(String zipEvidence) {

        if(zipEvidence.equals("true")){
//        Zip evidence folder
            zipFolder("evidence", "evidence.zip");
//        Create Test Case Result
            generateTestResults();
//         Sent Slack Message - End of Test Run
            sendSlackMsg_QaValidation(suiteName, "end");
            sendSlackMsg_PipelineResults(suiteName, "gha-end");
        }
    }
}