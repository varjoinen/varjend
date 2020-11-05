package org.varjoinen.testing.varjoend;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Context {

    @Getter
    @Setter
    private Step currentStep;

    @Getter
    @Setter
    private Boolean processHasErrors = false;

    private final Map<String, Object> variables = new HashMap<>();

    /**
     * Set variable
     *
     * @param name     Variable name
     * @param variable Variable
     */
    public void setVariable(String name, Object variable) {
        variables.put(name, variable);
    }

    /**
     * Get variable of given type
     *
     * @param name Variable name
     * @param type Variable type
     * @param <T>  Type
     * @return Variable or null if not found
     */
    public <T> T getVariable(String name, Class<T> type) {
        return type.cast(variables.get(name));
    }
}
