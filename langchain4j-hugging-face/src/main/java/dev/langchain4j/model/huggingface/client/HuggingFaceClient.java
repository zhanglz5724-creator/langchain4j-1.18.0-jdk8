/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.huggingface.client;

import dev.langchain4j.model.huggingface.client.EmbeddingRequest;
import dev.langchain4j.model.huggingface.client.TextGenerationRequest;
import dev.langchain4j.model.huggingface.client.TextGenerationResponse;
import java.util.List;

public interface HuggingFaceClient {
    @Deprecated
    public TextGenerationResponse chat(TextGenerationRequest var1);

    @Deprecated
    public TextGenerationResponse generate(TextGenerationRequest var1);

    public List<float[]> embed(EmbeddingRequest var1);
}

