/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.document;

import dev.langchain4j.data.document.Document;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface DocumentTransformer {
    public Document transform(Document var1);

    default public List<Document> transformAll(List<Document> documents) {
        return documents.stream().map(this::transform).filter(Objects::nonNull).collect(Collectors.toList());
    }
}

