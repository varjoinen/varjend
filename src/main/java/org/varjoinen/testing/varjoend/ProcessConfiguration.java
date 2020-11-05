package org.varjoinen.testing.varjoend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessConfiguration {

    /**
     * Propagate exception from step execution
     * and fail the process.
     * <p>
     * Default is to propagate exceptions.
     */
    private boolean propagateExceptions = true;
}
