package org.varjoinen.testing.varjoend;

@FunctionalInterface
public interface ContextFunction {

    /**
     * Function to take context as input and allow throwing exception
     *
     * @param ctx Context
     * @throws Exception If execution fails
     */
    void execute(Context ctx) throws Exception;
}