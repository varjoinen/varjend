package org.varjoinen.testing.varjoend.exception;

import lombok.Data;
import org.varjoinen.testing.varjoend.Step;

import java.time.Instant;

@Data
public class ExecutionException extends Exception {

    private Step failedStep;

    private Instant timestamp = Instant.now();

    public ExecutionException() {
        super();
    }

    public ExecutionException(Exception e, Step step) {
        super(e);
        this.failedStep = step;
    }
}
