package com.publicAPI.oauth2.tests.Studies;

import com.publicAPI.oauth2.tests.DataTestSet;
import helpers.TcRetry;
import helpers.TestListener;
import helpers.jiraTicket.Bug;
import helpers.testCaseId.TcID;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.publicAPI.oauth2.api.applicationApi._CommonAPI.common_API_DELETE;

@Listeners(TestListener.class)
public class EC_8710_Studies_Add_Study_Happy_Path extends DataTestSet{

    String fileId;


    @Bug(jiraTicket ="null")
    @TcID(tcId = "PT-844")
    @Test(groups= {},
            testName = "File service - Delete File And Associated Data",
            description = "Test case check if can delete file and associated data",
            retryAnalyzer = TcRetry.class)

    public void Studies_Add_Study_Happy_Path() {

        String[] responseBody = {};

        common_API_DELETE(fileId, token, 204, responseBody, false, null);
    }
}