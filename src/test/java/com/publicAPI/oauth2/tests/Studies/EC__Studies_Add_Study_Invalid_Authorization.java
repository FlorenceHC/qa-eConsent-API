package com.publicAPI.oauth2.tests.Studies;

import com.publicAPI.oauth2.tests.DataTestSet;
import helpers.TcRetry;
import helpers.TestListener;
import helpers.jiraTicket.Bug;
import helpers.testCaseId.TcID;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.publicAPI.oauth2.api.applicationApi._CommonAPI.common_API_POST;
import static helpers.propertyFile.DataProvider.INVALID_TOKEN;
import static helpers.propertyFile.DataProvider.unauthorized_msg;


@Listeners(TestListener.class)
public class EC__Studies_Add_Study_Invalid_Authorization extends DataTestSet {

    @Bug(jiraTicket = "null")
    @TcID(tcId = "null")
    @Test(groups = {},
            testName = "Studies Add Study Invalid Authorization",
            description = "Test case check if Studies Add Stud Invalid Authorization works as expected",
            retryAnalyzer = TcRetry.class)

    public void Studies_Add_Study_Invalid_Authorization() {

        String URL = "sites/651bd172d4716d01b4a323d1/studies";

        common_API_POST(URL, INVALID_TOKEN, "", "application/json",  401, new String[]{unauthorized_msg}, "common/MessageResponse");
    }
}