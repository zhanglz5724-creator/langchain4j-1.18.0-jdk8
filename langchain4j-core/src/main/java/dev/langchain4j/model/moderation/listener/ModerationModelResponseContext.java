/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.moderation.listener;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.moderation.ModerationRequest;
import dev.langchain4j.model.moderation.ModerationResponse;
import java.util.Map;

public class ModerationModelResponseContext {
    private final ModerationResponse moderationResponse;
    private final ModerationRequest moderationRequest;
    private final ModelProvider modelProvider;
    private final Map<Object, Object> attributes;

    public ModerationModelResponseContext(ModerationResponse moderationResponse, ModerationRequest moderationRequest, ModelProvider modelProvider, Map<Object, Object> attributes) {
        this.moderationResponse = ValidationUtils.ensureNotNull(moderationResponse, "moderationResponse");
        this.moderationRequest = ValidationUtils.ensureNotNull(moderationRequest, "moderationRequest");
        this.modelProvider = ValidationUtils.ensureNotNull(modelProvider, "modelProvider");
        this.attributes = ValidationUtils.ensureNotNull(attributes, "attributes");
    }

    public ModerationResponse moderationResponse() {
        return this.moderationResponse;
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
        return "ModerationModelResponseContext{moderationResponse=" + this.moderationResponse + ", moderationRequest=" + this.moderationRequest + ", modelProvider=" + (Object)((Object)this.modelProvider) + ", attributes=" + this.attributes + '}';
    }
}

