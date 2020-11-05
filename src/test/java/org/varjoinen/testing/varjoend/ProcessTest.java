package org.varjoinen.testing.varjoend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.varjoinen.testing.varjoend.exception.ExecutionException;
import org.varjoinen.testing.varjoend.exception.ProcessIntegrityException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcessTest {

    @Test
    public void addNullStep() {
        Assertions.assertThrows(
                ProcessIntegrityException.class,
                () -> Process
                        .builder()
                        .step(null)
                        .build()
                        .execute());

    }

    @Test
    public void addDuplicateStep() {
        Assertions.assertThrows(
                ProcessIntegrityException.class,
                () -> Process
                        .builder()
                        .step(new Step("duplicate", (ctx) -> {}))
                        .step(new Step("duplicate", (ctx) -> {}))
                        .build()
                        .execute());

    }

    @Test
    public void executeSteps() throws Exception {
        ContextFunction mock = mock(ContextFunction.class);

        Process
                .builder()
                .step(new Step("Step 1", mock))
                .step(new Step("Step 2", mock))
                .build()
                .execute();

        verify(mock, times(2)).execute(any());
    }

    @Test
    public void runtimeExceptionInStep() {
        Assertions.assertThrows(
                ExecutionException.class,
                () -> Process
                        .builder()
                        .step(new Step("failing", (ctx) -> {
                            throw new RuntimeException("error");
                        }))
                        .build()
                        .execute());
    }

    @Test
    public void checkedExceptionInStep() {
        Assertions.assertThrows(
                ExecutionException.class,
                () -> Process
                        .builder()
                        .step(new Step("failing", (ctx) -> {
                            throw new ExecutionException();
                        }))
                        .build()
                        .execute());
    }

    @Test
    public void processContextHasErrors() throws Exception {
        Process process = Process
                .builder()
                .configuration(ProcessConfiguration.builder().propagateExceptions(false).build())
                .step(new Step("will fail", (ctx) -> {
                    throw new RuntimeException("error");
                }))
                .build();

        assertFalse(process.getContext().getProcessHasErrors());

        process.execute();

        assertTrue(process.getContext().getProcessHasErrors());
    }

    @Test
    public void listSteps() {
        Process process = Process
                .builder()
                .step(new Step("test", (ctx) -> {
                }))
                .build();

        assertEquals(1, process.getSteps().size());
        assertEquals(0, process.getSuccessfulSteps().size());
        assertEquals(0, process.getFailedSteps().size());

    }

    @Test
    public void listSuccessfulSteps() throws Exception {
        Process process = Process
                .builder()
                .step(new Step("test", (ctx) -> {
                }))
                .build();

        process.execute();

        assertEquals(1, process.getSteps().size());
        assertEquals(1, process.getSuccessfulSteps().size());
        assertEquals(0, process.getFailedSteps().size());
    }

    @Test
    public void listFailedSteps() {
        Process process = Process
                .builder()
                .step(new Step("test", (ctx) -> {
                    throw new RuntimeException("error");
                }))
                .build();

        Assertions.assertThrows(ExecutionException.class, process::execute);

        assertEquals(1, process.getSteps().size());
        assertEquals(0, process.getSuccessfulSteps().size());
        assertEquals(1, process.getFailedSteps().size());
    }

    @Test
    public void isNotExecuted() throws Exception {
        Process process = Process
                .builder()
                .step(new Step("test", (ctx) -> {
                }))
                .build();

        assertTrue(process.isNotExecuted());

        process.execute();

        assertFalse(process.isNotExecuted());
    }

    @Test
    public void isSuccessful() throws Exception {
        Process process = Process
                .builder()
                .step(new Step("test", (ctx) -> {
                }))
                .build();

        assertFalse(process.isSuccessful());

        process.execute();

        assertTrue(process.isSuccessful());
        assertTrue(process.isCompleted());
        assertFalse(process.isFailed());
    }

    @Test
    public void isCompleted() throws Exception {
        Process process = Process
                .builder()
                .configuration(ProcessConfiguration
                        .builder()
                        .propagateExceptions(false)
                        .build())
                .step(new Step("one", (ctx) -> {
                }))
                .step(new Step("two", (ctx) -> {
                    throw new RuntimeException("error");
                }))
                .step(new Step("three", (ctx) -> {
                }))
                .build();

        assertFalse(process.isCompleted());

        process.execute();

        assertTrue(process.isCompleted());
        assertFalse(process.isSuccessful());
        assertFalse(process.isFailed());
    }

    @Test
    public void isFailedWithoutPropagation() throws Exception {
        Process process = Process
                .builder()
                .configuration(ProcessConfiguration.builder().propagateExceptions(false).build())
                .step(new Step("one", (ctx) -> {
                }))
                .step(new Step("two", (ctx) -> {
                    throw new RuntimeException("error");
                }))
                .step(new Step("three", (ctx) -> {
                }))
                .build();

        assertFalse(process.isFailed());

        process.execute();

        assertFalse(process.isFailed());
        assertFalse(process.isSuccessful());
        assertTrue(process.isCompleted());
    }

    @Test
    public void isFailedWithPropagation() {

        Process process = Process
                .builder()
                .step(new Step("one", (ctx) -> {
                }))
                .step(new Step("two", (ctx) -> {
                    throw new RuntimeException("error");
                }))
                .step(new Step("three", (ctx) -> {
                }))
                .build();

        assertFalse(process.isFailed());

        Assertions.assertThrows(ExecutionException.class, process::execute);

        assertTrue(process.isFailed());
        assertFalse(process.isSuccessful());
        assertFalse(process.isCompleted());
    }

    @Test
    public void visualise() {
        Process process = new Process();
        System.out.println(process.visualise());
    }
}
