/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  org.reactivestreams.Subscription
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.response.StreamingHandle;
import org.reactivestreams.Subscription;

class BedrockStreamingHandle
implements StreamingHandle {
    private final Subscription subscription;
    private volatile boolean isCancelled;

    BedrockStreamingHandle(Subscription subscription) {
        this.subscription = (Subscription)ValidationUtils.ensureNotNull((Object)subscription, (String)"subscription");
    }

    public void cancel() {
        this.isCancelled = true;
        try {
            this.subscription.cancel();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }
}

