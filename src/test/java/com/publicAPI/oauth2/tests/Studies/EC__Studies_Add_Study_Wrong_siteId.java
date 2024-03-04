package com.publicAPI.oauth2.tests.Studies;

import com.publicAPI.oauth2.tests.DataTestSet;
import helpers.TcRetry;
import helpers.TestListener;
import helpers.jiraTicket.Bug;
import helpers.testCaseId.TcID;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.publicAPI.oauth2.api.applicationApi._CommonAPI.common_API_POST;
import static com.publicAPI.oauth2.api.applicationApi.studiesAPI.StudiesApi.studiesJsonPayload;
import static helpers.Utils.randomString_character;


@Listeners(TestListener.class)
public class EC__Studies_Add_Study_Wrong_siteId extends DataTestSet {

    @Bug(jiraTicket = "null")
    @TcID(tcId = "null")
    @Test(groups = {},
            testName = "Studies Add Study Wrong site ID",
            description = "Test case check if Studies Add Stud Wrong Site Id works as expected",
            retryAnalyzer = TcRetry.class)

    public void Studies_Add_Study_Wrong_siteId() {

        String URL = "sites/" + randomString_character(10) + "/studies";

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

        String[] responseBody = {"\"message\": \"Missing required request parameters: [siteId]\""};

        common_API_POST(URL, token, requestBody, "application/json",  401, responseBody, "common/MessageResponse");
    }
}