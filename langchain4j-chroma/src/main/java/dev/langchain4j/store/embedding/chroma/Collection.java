/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.Internal;
import java.util.Map;

@Internal
class Collection {
    private String id;
    private String name;
    private Map<String, Object> metadata;

    Collection() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Object> getMetadata() {
        return this.metadata;
    }
}

