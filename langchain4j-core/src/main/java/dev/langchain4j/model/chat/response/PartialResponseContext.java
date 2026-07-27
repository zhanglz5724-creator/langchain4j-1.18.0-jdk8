/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.response;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.response.StreamingHandle;

@Experimental
@JacocoIgnoreCoverageGenerated
public class PartialResponseContext {
    private final StreamingHandle streamingHandle;

    public PartialResponseContext(StreamingHandle streamingHandle) {
        this.streamingHandle = ValidationUtils.ensureNotNull(streamingHandle, "streamingHandle");
    }

    public StreamingHandle streamingHandle() {
        return this.streamingHandle;
    }
}

