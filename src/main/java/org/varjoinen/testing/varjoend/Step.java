package org.varjoinen.testing.varjoend;

import lombok.Builder;
import org.varjoinen.testing.varjoend.exception.ExecutionException;

@Builder
public class Step {

    private final String name;

    private final ContextFunction function;

    /**
     * Create a new step
     *
     * @param name     Step name
     * @param function Function implementing this step
     */
    public Step(String name, ContextFunction function) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (function == null) {
            throw new IllegalArgumentException("function cannot be empty");
        }

        this.name = name;
        this.function = function;
    }

    /**
     * Get step name
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Execute step function
     *
     * @param ctx Execution context
     * @throws ExecutionException If step fails
     */
    public void execute(Context ctx) throws ExecutionException {
        try {
            function.execute(ctx);
        } catch (Exception e) {
            ExecutionException propagatedException;

            if (e instanceof ExecutionException) {
                propagatedException = (ExecutionException) e;
                propagatedException.setFailedStep(this);
            } else {
                propagatedException = new ExecutionException(e, this);
            }

            throw propagatedException;
        }
    }

    /**
     * Check equality by step name
     *
     * @param other Other step
     * @return True if this step's name equals (ignoring case) to other step's name
     */
    public boolean equals(Object other) {
        return other instanceof Step && name.equalsIgnoreCase(((Step) other).getName());
    }
}
