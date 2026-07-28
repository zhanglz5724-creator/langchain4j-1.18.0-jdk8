/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.agentic.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.agentic.internal.DeferredResponse;

public class SuspendedResponse<T>
extends DeferredResponse<T> {
    @JsonCreator
    public SuspendedResponse(@JsonProperty(value="responseId") String responseId) {
        super(responseId);
    }
}

