/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.moderation.listener;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.moderation.ModerationRequest;
import java.util.Map;

public class ModerationModelErrorContext {
    private final Throwable error;
    private final ModerationRequest moderationRequest;
    private final ModelProvider modelProvider;
    private final Map<Object, Object> attributes;

    public ModerationModelErrorContext(Throwable error, ModerationRequest moderationRequest, ModelProvider modelProvider, Map<Object, Object> attributes) {
        this.error = ValidationUtils.ensureNotNull(error, "error");
        this.moderationRequest = ValidationUtils.ensureNotNull(moderationRequest, "moderationRequest");
        this.modelProvider = ValidationUtils.ensureNotNull(modelProvider, "modelProvider");
        this.attributes = ValidationUtils.ensureNotNull(attributes, "attributes");
    }

    public Throwable error() {
        return this.error;
    }

    public ModerationRequest moderationRequest() {
        return this.moderationRequest;
    }

    public ModelProvider modelProvider() {
        return this.modelProvider;
    }

    public Map<Object, Object> attributes() {
        return this.attributes;
    }

    public String toString() {
        return "ModerationModelErrorContext{error=" + this.error + ", moderationRequest=" + this.moderationRequest + ", modelProvider=" + (Object)((Object)this.modelProvider) + ", attributes=" + this.attributes + '}';
    }
}

