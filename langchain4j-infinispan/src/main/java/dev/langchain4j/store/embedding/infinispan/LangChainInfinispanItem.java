/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.infinispan;

import dev.langchain4j.store.embedding.infinispan.LangChainMetadata;
import java.util.Map;
import java.util.Set;

public record LangChainInfinispanItem(String id, float[] embedding, String text, Set<LangChainMetadata> metadata, Map<String, Object> metadataMap) {
}

