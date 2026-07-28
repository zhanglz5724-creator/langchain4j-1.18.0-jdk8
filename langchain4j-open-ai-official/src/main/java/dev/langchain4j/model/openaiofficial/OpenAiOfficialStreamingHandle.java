/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.openai.core.http.AsyncStreamResponse
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.response.StreamingHandle
 */
package dev.langchain4j.model.openaiofficial;

import com.openai.core.http.AsyncStreamResponse;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.response.StreamingHandle;

class OpenAiOfficialStreamingHandle
implements StreamingHandle {
    private final AsyncStreamResponse<?> asyncStreamResponse;
    private volatile boolean isCancelled;

    OpenAiOfficialStreamingHandle(AsyncStreamResponse<?> asyncStreamResponse) {
        this.asyncStreamResponse = (AsyncStreamResponse)ValidationUtils.ensureNotNull(asyncStreamResponse, (String)"asyncStreamResponse");
    }

    public void cancel() {
        this.isCancelled = true;
        try {
            this.asyncStreamResponse.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }
}

