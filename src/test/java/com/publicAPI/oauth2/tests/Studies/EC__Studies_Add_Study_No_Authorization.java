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
import static helpers.propertyFile.DataProvider.unauthorized_msg;


@Listeners(TestListener.class)
public class EC__Studies_Add_Study_No_Authorization extends DataTestSet {

    @Bug(jiraTicket = "null")
    @TcID(tcId = "null")
    @Test(groups = {},
            testName = "Studies Add Study No Authorization",
            description = "Test case check if Studies Add Stud No Authorization works as expected",
            retryAnalyzer = TcRetry.class)

    public void Studies_Add_Study_No_Authorization() {

        String URL = "sites/651bd172d4716d01b4a323d1/studies";

        common_API_POST(URL, "", "", "application/json",  401, new String[]{unauthorized_msg}, "common/MessageResponse");
    }
}