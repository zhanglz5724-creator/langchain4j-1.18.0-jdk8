/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.Internal;

@Internal
class Tenant {
    private String name;

    public Tenant() {
    }

    public Tenant(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

