package org.varjoinen.testing.varjoend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EndToEndTest {

    @Test
    public void endToEndTest() throws Exception {

        Process process = Process
                .builder()
                .configuration(ProcessConfiguration.builder()
                        .propagateExceptions(false)
                        .build())
                .step(new Step("Count 1", this::incrementCount))
                .step(new Step("Count 2", this::incrementCount))
                .step(new Step("Exception", this::throwException))
                .step(new Step("Assertions", (ctx) -> {
                    Integer count = ctx.getVariable("count", Integer.class);
                    assertEquals(2, count);
                }))
                .build();

        process.getContext().setVariable("count", 0);

        process.execute();

        //Output: [ok] Count 1 --> [ok] Count 2 --> [failed] Exception --> [ok] Assertions --> [process completed with errors]
        System.out.println(process.visualise());
    }

    private void throwException(Context ctx) {
        throw new RuntimeException("error");
    }

    private void incrementCount(Context ctx) {
        Integer count = ctx.getVariable("count", Integer.class);
        ctx.setVariable("count", count + 1);
    }
}
