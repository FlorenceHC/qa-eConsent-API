package helpers;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * If the test case fails try "maxRetryCount" times
 * This is put into Test Annotation as retryAnalyzer = tcRetry.class
 */
public class TcRetry implements IRetryAnalyzer {

    private int retryCount = 5;
    private static final int maxRetryCount = 0;

    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}