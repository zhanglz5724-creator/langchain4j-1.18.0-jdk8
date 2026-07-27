/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.language;

import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.input.Prompt;

public interface StreamingLanguageModel {
    public void generate(String var1, StreamingResponseHandler<String> var2);

    default public void generate(Prompt prompt, StreamingResponseHandler<String> handler) {
        this.generate(prompt.text(), handler);
    }
}

