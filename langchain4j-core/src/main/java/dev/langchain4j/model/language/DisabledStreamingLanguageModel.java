/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.language;

import dev.langchain4j.model.ModelDisabledException;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.language.StreamingLanguageModel;

public class DisabledStreamingLanguageModel
implements StreamingLanguageModel {
    @Override
    public void generate(String prompt, StreamingResponseHandler<String> handler) {
        throw new ModelDisabledException("StreamingLanguageModel is disabled");
    }

    @Override
    public void generate(Prompt prompt, StreamingResponseHandler<String> handler) {
        throw new ModelDisabledException("StreamingLanguageModel is disabled");
    }
}

