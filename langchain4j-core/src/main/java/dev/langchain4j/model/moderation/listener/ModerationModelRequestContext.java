/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.moderation.listener;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.moderation.ModerationRequest;
import java.util.Map;

public class ModerationModelRequestContext {
    private final ModerationRequest moderationRequest;
    private final ModelProvider modelProvider;
    private final Map<Object, Object> attributes;

    public ModerationModelRequestContext(ModerationRequest moderationRequest, ModelProvider modelProvider, Map<Object, Object> attributes) {
        this.moderationRequest = ValidationUtils.ensureNotNull(moderationRequest, "moderationRequest");
        this.modelProvider = ValidationUtils.ensureNotNull(modelProvider, "modelProvider");
        this.attributes = ValidationUtils.ensureNotNull(attributes, "attributes");
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
        return "ModerationModelRequestContext{moderationRequest=" + this.moderationRequest + ", modelProvider=" + (Object)((Object)this.modelProvider) + ", attributes=" + this.attributes + '}';
    }
}

