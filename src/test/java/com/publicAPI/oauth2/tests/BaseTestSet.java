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

//        System.out.println("eConsent.api_automation@florencehc.com - Client ID: " + jasyptEncodingString(SECRET_KEY, "2D1B6CA3A8C346B2AEE99C1E0AB72149"));
//        System.out.println("eConsent.api_automation@florencehc.com - client_secret : " + jasyptEncodingString(SECRET_KEY, "c2aa6e4a3426b53df74acaeb2fcbf7bd2e85b8711661b6124935abee8a934d51"));
//
//        System.out.println("eConsent.api_automation_2@florencehc.com - Client ID: " + jasyptEncodingString(SECRET_KEY, "F0CE5E5A9DD74753A62A681085132E29"));
//        System.out.println("eConsent.api_automation_2@florencehc.com - client_secret : " + jasyptEncodingString(SECRET_KEY, "eb427211498431deebc726f33567314f0429d27aee84538aee8cf9eca540396e"));
//
//        Assert.fail();

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