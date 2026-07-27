/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.request;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.embedding.request.DefaultEmbeddingRequestParameters;
import dev.langchain4j.model.embedding.request.EmbeddingInputType;
import dev.langchain4j.model.embedding.request.EmbeddingParameter;
import java.util.Set;

@Experimental
public interface EmbeddingRequestParameters {
    public static final EmbeddingParameter<String> MODEL_NAME = new EmbeddingParameter<String>("modelName", String.class);
    public static final EmbeddingParameter<Integer> DIMENSIONS = new EmbeddingParameter<Integer>("dimensions", Integer.class);
    public static final EmbeddingParameter<EmbeddingInputType> INPUT_TYPE = new EmbeddingParameter<EmbeddingInputType>("inputType", EmbeddingInputType.class);
    public static final EmbeddingRequestParameters EMPTY = DefaultEmbeddingRequestParameters.builder().build();

    public String modelName();

    public Integer dimensions();

    public EmbeddingInputType inputType();

    public <T> T parameter(EmbeddingParameter<T> var1);

    public Set<EmbeddingParameter<?>> presentParameters();

    public EmbeddingRequestParameters overrideWith(EmbeddingRequestParameters var1);

    public static DefaultEmbeddingRequestParameters.Builder<?> builder() {
        return new DefaultEmbeddingRequestParameters.Builder();
    }
}

