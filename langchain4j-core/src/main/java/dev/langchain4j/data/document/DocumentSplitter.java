/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.document;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface DocumentSplitter {
    public List<TextSegment> split(Document var1);

    default public List<TextSegment> splitAll(List<Document> documents) {
        return documents.stream().flatMap(document -> this.split((Document)document).stream()).collect(Collectors.toList());
    }

    default public List<TextSegment> splitAll(Document ... documents) {
        if (Utils.isNullOrEmpty(documents)) {
            return Collections.emptyList();
        }
        return this.splitAll(Arrays.asList(documents));
    }
}

