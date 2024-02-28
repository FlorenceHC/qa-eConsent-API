package helpers.testCaseId;

import org.testng.ITestResult;

public class TestCaseId {

    public static String getTestCaseId(ITestResult result) {

        String testCaseID = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TcID.class).tcId();
//        LoggerInformation("Xray Test Case ID: " + testCaseID);
        return testCaseID;
    }

}


