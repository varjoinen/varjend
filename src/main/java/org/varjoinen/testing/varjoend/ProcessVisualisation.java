package org.varjoinen.testing.varjoend;

public class ProcessVisualisation {

    private static final String ok = "[ok] ";

    private static final String failed = "[failed] ";

    private static final String notExecuted = "[ ] ";

    private static final String continuation = " --> ";

    private static final String processNotExecuted = "[process not executed]";

    private static final String processSuccess = "[process completed]";

    private static final String processWithErrors = "[process completed with errors]";

    private static final String processFailed = "[process failed]";

    /**
     * Visualise process steps and state(s).
     * <p>
     * [ok] step 1 --> [failed] step 2  --> [ ] step 3 --> [process failed]
     *
     * @return String visualisation
     */
    public static String visualise(Process process) {

        boolean stopOnFailure = process.getConfiguration().isPropagateExceptions();

        StringBuilder sb = new StringBuilder();

        for (Step step : process.getSteps()) {
            if (process.getSuccessfulSteps().contains(step)) {
                sb.append(ok);
            } else if (process.getFailedSteps().contains(step)) {
                sb.append(failed);
            } else {
                sb.append(notExecuted);
            }

            sb.append(step != null ? step.getName() : "null");
            sb.append(continuation);
        }

        if (process.getSuccessfulSteps().isEmpty() && process.getFailedSteps().isEmpty()) {
            sb.append(processNotExecuted);
        } else if (process.getFailedSteps().isEmpty()) {
            sb.append(processSuccess);
        } else if (stopOnFailure) {
            sb.append(processFailed);
        } else {
            sb.append(processWithErrors);
        }

        return sb.toString();
    }
}
