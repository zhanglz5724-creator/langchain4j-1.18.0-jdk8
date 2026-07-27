/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.ModelDisabledException;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import java.util.List;

public class DisabledEmbeddingModel
implements EmbeddingModel {
    @Override
    public Response<Embedding> embed(String text) {
        throw new ModelDisabledException("EmbeddingModel is disabled");
    }

    @Override
    public Response<Embedding> embed(TextSegment textSegment) {
        throw new ModelDisabledException("EmbeddingModel is disabled");
    }

    @Override
    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        throw new ModelDisabledException("EmbeddingModel is disabled");
    }
}

