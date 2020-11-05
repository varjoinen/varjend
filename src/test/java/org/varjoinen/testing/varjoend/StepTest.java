package org.varjoinen.testing.varjoend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.varjoinen.testing.varjoend.exception.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StepTest {

    @Test
    public void construct() {

        String name = "test";
        Step step = new Step(name, (ctx) -> {
        });

        assertEquals(name, step.getName());
    }

    @Test
    public void emptyInputs() {

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Step(null, (ctx) -> {
                }));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Step("", (ctx) -> {
                }));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Step("valid", null));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Step(null, null));
    }

    @Test
    public void executeCallsFunction() throws Exception {

        ContextFunction function = mock(ContextFunction.class);

        Step step = new Step("test", function);
        step.execute(null);

        verify(function, times(1)).execute(any());
    }

    @Test
    public void failWithExecutionException() {

        Step step = new Step("test", (ctx) -> { throw new ExecutionException(); });

        try {
            step.execute(null);
        } catch (Exception e) {
            assertTrue(e instanceof ExecutionException);

            ExecutionException output = (ExecutionException) e;
            assertNotNull(output.getTimestamp());
            assertNotNull(output.getFailedStep());
            assertNull(output.getCause());
            assertEquals(step, output.getFailedStep());
        }
    }

    @Test
    public void failWithOtherException() {

        Step step = new Step("test", (ctx) -> { throw new RuntimeException("error"); });

        try {
            step.execute(null);
        } catch (Exception e) {
            assertTrue(e instanceof ExecutionException);

            ExecutionException output = (ExecutionException) e;
            assertNotNull(output.getTimestamp());
            assertNotNull(output.getFailedStep());
            assertNotNull(output.getCause());
            assertEquals(step, output.getFailedStep());
        }
    }

    @Test
    public void equals() {
        Step a = new Step("A", (ctx) -> System.out.println("test"));
        Step b = new Step("B", (ctx) -> {});
        Step c = new Step("A", (ctx) -> {});
        Step d = new Step("a", (ctx) -> {});

        assertNotEquals(a, b);
        assertEquals(a, c);
        assertEquals(a, d);
    }
}
