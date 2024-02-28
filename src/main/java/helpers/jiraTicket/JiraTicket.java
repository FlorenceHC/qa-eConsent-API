package helpers.jiraTicket;

import org.testng.ITestResult;

import static helpers.TCLogger.*;


public class JiraTicket {

    /**
     * Get Jira Ticket Value from Bug Annotation
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @param log    Whether to print a log or not
     * @see Bug
     * @see ITestResult
     */

    public static boolean getJiraTicket(ITestResult result, Boolean log) {

            try {
                String jiraTicket = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Bug.class).jiraTicket();
                if (!jiraTicket.equals("null")) {
                    if (log) {
                        loggerInformation("Jira Bug Ticket: Yes");
                    }
                    return true;
                }
            } catch (Exception ex) {
                if (log)
                    loggerInformation("Jira Bug Ticket: No");
            }
        return false;
    }




    /**
     * Get Jira Ticket Number Value from Bug Annotation
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @see Bug
     * @see ITestResult
     */

    public static String getTicketNumberValue(ITestResult result) {

            boolean jiraTicket = getJiraTicket(result, false);
            if (jiraTicket) {
                String ticketNo = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Bug.class).jiraTicket();
                loggerInformation("Jira Bug Ticket Number: " + ticketNo);
                return ticketNo;
            } else {
                loggerInformation("No Jira Bug Ticket");
            }
            return null;
    }
}


