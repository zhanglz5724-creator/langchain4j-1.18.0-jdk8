/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.Internal;
import java.util.HashMap;
import java.util.Map;

@Internal
class CreateCollectionRequest {
    private final String name;
    private final Map<String, Object> metadata;

    public CreateCollectionRequest(String name) {
        this.name = name;
        HashMap<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("hnsw:space", "cosine");
        this.metadata = metadata;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Object> getMetadata() {
        return this.metadata;
    }
}

