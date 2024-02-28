package helpers;


import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import static helpers.Utils.testReport;
import static helpers.Utils.writeLogToFile;
import static helpers.jiraTicket.JiraTicket.getJiraTicket;
import static helpers.jiraTicket.JiraTicket.getTicketNumberValue;
import static helpers.jiraXrayReporting.JiraXrayAPI.getXRayRunId;
import static helpers.jiraXrayReporting.JiraXrayAPI.setTcResult_Xray;
import static helpers.testCaseId.TestCaseId.getTestCaseId;


public class TestListener implements ITestListener {

    /**
     * Change the color of the log printout
     * Note - Test Rail cannot receive these codes when entering comments in Test Cases
     */
    private static final String reset = TCLogger.ANSI_RESET;
    private static final String red = TCLogger.ANSI_RED;
    private static final String green = TCLogger.ANSI_GREEN;
    private static final String yellow = TCLogger.ANSI_YELLOW;


    /**
     * Invoked each time a test succeeds.
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#STARTED
     * parameter "testStatus" - PASSED
     */
    @Override
    public void onTestStart(ITestResult result) {
        String nameTC = result.getName();
        String testCaseID = getTestCaseId(result);
        Reporter.log("", true);
        Reporter.log("", true);
        Reporter.log("--- Test Case - " + testCaseID + " - " + nameTC + " -- STARTED --\\\\n", true);
        Reporter.log("", true);
    }


    /**
     * Invoked each time a test succeeds.
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#SUCCESS
     * parameter "testStatus" - PASSED
     */
    @Override
    public void onTestSuccess(ITestResult result) {

        String nameTC = result.getName();
        String testCaseID = getTestCaseId(result);
        Reporter.log("--- Test Case - " + nameTC + " -- Finish --> {color:#36b37e}*PASSED*{color} ---\\\\\n", true);
        boolean jiraTicket = getJiraTicket(result, true);
        String jiraTicketNo = getTicketNumberValue(result);
        String testRunId = getXRayRunId();
        String getReport = testReport(result);
        writeLogToFile(testCaseID, nameTC, getReport, "PASSED");
        setTcResult_Xray(nameTC, testRunId, testCaseID, "PASSED", getReport, jiraTicket, jiraTicketNo);
        Reporter.log(green + "------------ >>>[ Test PASSED: " + nameTC + " ]<<< ------------" + reset, true);
    }

    /**
     * Invoked each time a test fails.
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#FAILURE
     * parameter "testStatus" - FAILED
     */
    @Override
    public void onTestFailure(ITestResult result) {

        String nameTC = result.getName();
        String testCaseID = getTestCaseId(result);
        Reporter.log("--- Test Case - " + nameTC + " -- Finish --> {color:#FF0000}*FAILED*{color}  ---\\\\\n", true);
        boolean jiraTicket = getJiraTicket(result, true);
        String jiraTicketNo = getTicketNumberValue(result);
        String testRunId = getXRayRunId();
        String getReport = testReport(result);
        writeLogToFile(testCaseID, nameTC, getReport, "FAILED");
        setTcResult_Xray(nameTC, testRunId, testCaseID, "FAILED", getReport, jiraTicket, jiraTicketNo);
        Reporter.log(red + " ------------ >>>[ Test FAILED: " + nameTC + " ]<<< ------------" + reset, true);
    }

    /**
     * Invoked each time a test is skipped.
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#SKIP
     * parameter "testStatus" - SKIPPED
     */
    @Override
    public void onTestSkipped(ITestResult result) {

        String nameTC = result.getName();
        String testCaseID = getTestCaseId(result);
        Reporter.log("--- Test Case - " + nameTC + " -- Finish --> {color:#FF8E00}*SKIPPED*{color} ---\\\\\n", true);
        boolean jiraTicket = getJiraTicket(result, true);
        String jiraTicketNo = getTicketNumberValue(result);
        String testRunId = getXRayRunId();
        String getReport = testReport(result);
        writeLogToFile(testCaseID, nameTC, getReport, "SKIPPED");
        setTcResult_Xray(nameTC, testRunId, testCaseID, "SKIPPED", getReport, jiraTicket, jiraTicketNo);
        Reporter.log(yellow + "------------ >>>[ Test SKIPPED: " + nameTC + " ]<<< ------------" + reset, true);
    }
}




