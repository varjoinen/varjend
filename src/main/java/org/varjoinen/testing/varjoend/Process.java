package org.varjoinen.testing.varjoend;

import lombok.*;
import org.varjoinen.testing.varjoend.exception.ProcessIntegrityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Builder()
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Process {

    @Singular
    private List<Step> steps = new ArrayList<>();

    @Builder.Default
    private ProcessConfiguration configuration = new ProcessConfiguration();

    private final List<Step> successfulSteps = new ArrayList<>();

    private final List<Step> failedSteps = new ArrayList<>();

    private final Context context = new Context();


    /**
     * Get process configuration
     *
     * @return Process configuration
     */
    public ProcessConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Execute process by running every step in order
     *
     * @throws Exception If process integrity has failed or
     *                   If step fails and configuration allows propagation
     */
    public void execute() throws Exception {

        validate();

        for (Step step : steps) {
            if (step == null) {
                return;
            }

            context.setCurrentStep(step);

            try {
                step.execute(context);
                successfulSteps.add(step);
            } catch (Exception e) {
                failedSteps.add(step);
                context.setProcessHasErrors(true);

                if (configuration.isPropagateExceptions()) {
                    throw e;
                }
            }
        }
    }

    /**
     * Validate steps
     *
     * @throws ProcessIntegrityException If integrity has failed
     */
    private void validate() throws ProcessIntegrityException {
        if (steps == null || steps.isEmpty()) {
            throw new ProcessIntegrityException("Process has no steps");
        }

        if (steps.stream().anyMatch(Objects::isNull)) {
            throw new ProcessIntegrityException("Steps contain null(s)");
        }

        Set<Step> duplicates = steps
                .stream()
                .filter(s1 -> steps.stream().filter(s1::equals).count() > 1)
                .collect(Collectors.toSet());

        if (duplicates.size() > 0) {
            throw new ProcessIntegrityException(String.format(
                    "There are multiple steps with the same name: %s",
                    duplicates));
        }
    }

    /**
     * Check whether process is executed
     *
     * @return True if process has been executed
     */
    public boolean isNotExecuted() {
        return successfulSteps.isEmpty() && failedSteps.isEmpty();
    }

    /**
     * Check whether process has been executed successfully (without errors)
     *
     * @return True if process has been executed without errors
     */
    public boolean isSuccessful() {
        int stepCount = steps.size();
        return stepCount > 0 && successfulSteps.size() == stepCount;
    }

    /**
     * Check whether process has been executed with or without errors
     *
     * @return True if process has been executed
     */
    public boolean isCompleted() {
        int stepCount = steps.size();
        return stepCount > 0 && successfulSteps.size() + failedSteps.size() == stepCount;
    }

    /**
     * Check whether process has failed
     *
     * @return True if process has been executed and failed
     */
    public boolean isFailed() {
        return configuration.isPropagateExceptions() && failedSteps.size() > 0;
    }

    /**
     * Visualise process steps and state(s).
     *
     * @return String visualisation
     */
    public String visualise() {
        return ProcessVisualisation.visualise(this);
    }
}
