package org.varjoinen.testing.varjoend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.varjoinen.testing.varjoend.exception.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessVisualisationTest {

    @Test
    public void emptyProcessVisualisation() {
        Process process = new Process();
        assertEquals(
                "[process not executed]",
                ProcessVisualisation.visualise(process));
    }

    @Test
    public void notExecutedProcessVisualisation() {

        Process process = Process
                .builder()
                .configuration(ProcessConfiguration.builder().propagateExceptions(false).build())
                .step(new Step("A", (ctx) -> {
                }))
                .step(new Step("B", (ctx) -> {
                }))
                .step(new Step("C", (ctx) -> {
                }))
                .build();

        assertEquals(
                "[ ] A --> [ ] B --> [ ] C --> [process not executed]",
                ProcessVisualisation.visualise(process));
    }

    @Test
    public void successfulProcessVisualisation() {

        Process process = Process
                .builder()
                .configuration(ProcessConfiguration.builder().propagateExceptions(false).build())
                .step(new Step("A", (ctx) -> {
                }))
                .step(new Step("B", (ctx) -> {
                }))
                .step(new Step("C", (ctx) -> {
                }))
                .build();

        assertEquals(
                "[ ] A --> [ ] B --> [ ] C --> [process not executed]",
                ProcessVisualisation.visualise(process));
    }

    @Test
    public void failingProcessVisualisation() {

        Process process = Process
                .builder()
                .step(new Step("A", (ctx) -> {
                }))
                .step(new Step("B", (ctx) -> {
                    throw new RuntimeException("error");
                }))
                .step(new Step("C", (ctx) -> {
                }))
                .build();

        Assertions.assertThrows(ExecutionException.class, process::execute);

        assertEquals(
                "[ok] A --> [failed] B --> [ ] C --> [process failed]",
                ProcessVisualisation.visualise(process));
    }

    @Test
    public void processWithErrorsVisualisation() throws Exception {

        Process process = Process
                .builder()
                .configuration(ProcessConfiguration.builder().propagateExceptions(false).build())
                .step(new Step("A", (ctx) -> {
                }))
                .step(new Step("B", (ctx) -> {
                    throw new RuntimeException("error");
                }))
                .step(new Step("C", (ctx) -> {
                }))
                .build();

        process.execute();

        assertEquals(
                "[ok] A --> [failed] B --> [ok] C --> [process completed with errors]",
                ProcessVisualisation.visualise(process));
    }
}
