/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding;

import dev.langchain4j.model.embedding.EmbeddingModel;
import java.util.Optional;

public abstract class DimensionAwareEmbeddingModel
implements EmbeddingModel {
    protected Integer dimension;

    protected Integer knownDimension() {
        return null;
    }

    @Override
    public int dimension() {
        if (this.dimension != null) {
            return this.dimension;
        }
        Integer knownDimension = this.knownDimension();
        this.dimension = Optional.ofNullable(knownDimension).orElseGet(() -> this.embed("test").content().dimension());
        return this.dimension;
    }
}

