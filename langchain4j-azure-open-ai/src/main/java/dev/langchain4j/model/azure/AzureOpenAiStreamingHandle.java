/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  reactor.core.Disposable
 */
package dev.langchain4j.model.azure;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.response.StreamingHandle;
import reactor.core.Disposable;

class AzureOpenAiStreamingHandle
implements StreamingHandle {
    private final Disposable disposable;
    private volatile boolean isCancelled;

    AzureOpenAiStreamingHandle(Disposable disposable) {
        this.disposable = (Disposable)ValidationUtils.ensureNotNull((Object)disposable, (String)"disposable");
    }

    public void cancel() {
        this.isCancelled = true;
        try {
            this.disposable.dispose();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }
}

