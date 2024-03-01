package com.publicAPI.oauth2.tests.Studies;

import com.publicAPI.oauth2.tests.DataTestSet;
import helpers.TcRetry;
import helpers.TestListener;
import helpers.jiraTicket.Bug;
import helpers.testCaseId.TcID;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.publicAPI.oauth2.api.applicationApi._CommonAPI.*;
import static com.publicAPI.oauth2.api.applicationApi.studiesAPI.StudiesApi.studiesJsonPayload;
import static helpers.Utils.*;


@Listeners(TestListener.class)
public class EC_8710_Studies_Add_Study_Happy_Path extends DataTestSet {

    @Bug(jiraTicket = "EC-8756")
    @TcID(tcId = "EC-8710")
    @Test(groups = {},
            testName = "File service - Delete File And Associated Data",
            description = "Test case check if can delete file and associated data",
            retryAnalyzer = TcRetry.class)

    public void Studies_Add_Study_Happy_Path() {

        String URL = "sites/651bd172d4716d01b4a323d1/studies";

        String requestBody = studiesJsonPayload(
                "New",
                new String[]{"en"},
                randomString_character(10),
                randomString_character(10),
                "florence",
                "alex",
                "123456",
                "string",
                "string",
                "string",
                0,
                0,
                "Industry Sponsored",
                false);

        String[] responseBody = {"\"status\": \"New\""};

        common_API_POST(URL, token, requestBody, "application/json",  201, responseBody, "studies/studies_Add_Study");
    }
}

