/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.micrometer.observation.Observation$Context
 *  io.micrometer.observation.ObservationConvention
 */
package dev.langchain4j.observation.convention;

import dev.langchain4j.observation.context.ChatModelObservationContext;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

public interface ChatModelConvention
extends ObservationConvention<ChatModelObservationContext> {
    default public boolean supportsContext(Observation.Context context) {
        return context instanceof ChatModelObservationContext;
    }
}

