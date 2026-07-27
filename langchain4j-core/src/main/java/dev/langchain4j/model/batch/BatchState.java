/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.batch;

import dev.langchain4j.Experimental;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Experimental
public enum BatchState {
    PENDING,
    RUNNING,
    SUCCEEDED,
    FAILED,
    CANCELLED,
    EXPIRED,
    UNSPECIFIED;

    private static final List<BatchState> TERMINAL_BATCH_STATES;

    public boolean isTerminal() {
        return TERMINAL_BATCH_STATES.contains((Object)this);
    }

    static {
        TERMINAL_BATCH_STATES = Collections.unmodifiableList(Arrays.asList(SUCCEEDED, FAILED, CANCELLED, EXPIRED));
    }
}

