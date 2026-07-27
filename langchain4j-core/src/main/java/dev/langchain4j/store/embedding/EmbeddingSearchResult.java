/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import java.util.List;

public class EmbeddingSearchResult<Embedded> {
    private final List<EmbeddingMatch<Embedded>> matches;

    public EmbeddingSearchResult(List<EmbeddingMatch<Embedded>> matches) {
        this.matches = ValidationUtils.ensureNotNull(matches, "matches");
    }

    public List<EmbeddingMatch<Embedded>> matches() {
        return this.matches;
    }
}

