/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.service.tool.search.vector;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Experimental
class ToolCachingEmbeddingModel
implements EmbeddingModel {
    private static final TokenUsage ZERO_TOKEN_USAGE = new TokenUsage(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
    private final EmbeddingModel delegate;
    private final Map<String, Embedding> cache = new ConcurrentHashMap<String, Embedding>();

    public ToolCachingEmbeddingModel(EmbeddingModel delegateEmbeddingModel) {
        this.delegate = (EmbeddingModel)ValidationUtils.ensureNotNull((Object)delegateEmbeddingModel, (String)"delegateEmbeddingModel");
    }

    void clearCache() {
        this.cache.clear();
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        ValidationUtils.ensureNotNull(textSegments, (String)"textSegments");
        ArrayList<Embedding> result = new ArrayList<Embedding>(textSegments.size());
        ArrayList<TextSegment> toEmbed = new ArrayList<TextSegment>();
        ArrayList<Integer> toEmbedIndexes = new ArrayList<Integer>();
        for (int i = 0; i < textSegments.size(); ++i) {
            TextSegment segment = textSegments.get(i);
            Embedding cached = this.cache.get(segment.text());
            if (cached != null) {
                result.add(cached);
                continue;
            }
            result.add(null);
            toEmbed.add(segment);
            toEmbedIndexes.add(i);
        }
        if (!toEmbed.isEmpty()) {
            Response response = this.delegate.embedAll(toEmbed);
            List embeddings = (List)response.content();
            for (int i = 0; i < embeddings.size(); ++i) {
                Embedding embedding = (Embedding)embeddings.get(i);
                TextSegment segment = (TextSegment)toEmbed.get(i);
                if (i != 0) {
                    this.cache.put(segment.text(), embedding);
                }
                result.set((Integer)toEmbedIndexes.get(i), embedding);
            }
            return Response.from(result, (TokenUsage)response.tokenUsage());
        }
        return Response.from(result, (TokenUsage)ZERO_TOKEN_USAGE);
    }

    public Response<Embedding> embed(String text) {
        throw new IllegalStateException("should not be called");
    }

    public Response<Embedding> embed(TextSegment textSegment) {
        throw new IllegalStateException("should not be called");
    }

    public int dimension() {
        return this.delegate.dimension();
    }

    public String modelName() {
        return this.delegate.modelName();
    }
}

