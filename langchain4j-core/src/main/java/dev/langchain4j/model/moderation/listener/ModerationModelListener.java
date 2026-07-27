/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.moderation.listener;

import dev.langchain4j.model.moderation.listener.ModerationModelErrorContext;
import dev.langchain4j.model.moderation.listener.ModerationModelRequestContext;
import dev.langchain4j.model.moderation.listener.ModerationModelResponseContext;

public interface ModerationModelListener {
    default public void onRequest(ModerationModelRequestContext requestContext) {
    }

    default public void onResponse(ModerationModelResponseContext responseContext) {
    }

    default public void onError(ModerationModelErrorContext errorContext) {
    }
}

