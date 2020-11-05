package org.varjoinen.testing.varjoend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {

    @Test
    public void variables() {
        Context ctx = new Context();

        String name = "key";
        String variable = "test";

        assertNull(ctx.getVariable(name, String.class));

        ctx.setVariable(name, variable);

        assertEquals(variable, ctx.getVariable(name, String.class));
    }

    @Test
    public void invalidVariableType() {
        Context ctx = new Context();
        String name = "key";
        ctx.setVariable("key", "test");

        Assertions.assertThrows(
                ClassCastException.class,
                () -> ctx.getVariable(name, BigDecimal.class));
    }

    @Test
    public void currentStep() {
        Context ctx = new Context();

        assertNull(ctx.getCurrentStep());

        ctx.setCurrentStep(new Step("test", (c) -> {}));

        assertNotNull(ctx.getCurrentStep());
    }
}
