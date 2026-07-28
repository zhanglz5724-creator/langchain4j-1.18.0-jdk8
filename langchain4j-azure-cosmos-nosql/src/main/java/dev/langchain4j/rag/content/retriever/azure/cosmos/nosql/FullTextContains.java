/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.filter.Filter
 */
package dev.langchain4j.rag.content.retriever.azure.cosmos.nosql;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.filter.Filter;
import java.util.Objects;

public class FullTextContains
implements Filter {
    private final String key;
    private final String searchTerm;

    public FullTextContains(String key, String searchTerm) {
        this.key = ValidationUtils.ensureNotBlank((String)key, (String)"key");
        this.searchTerm = (String)ValidationUtils.ensureNotNull((Object)searchTerm, (String)("searchTerm with key '" + key + "'"));
    }

    public String key() {
        return this.key;
    }

    public String searchTerm() {
        return this.searchTerm;
    }

    public boolean test(Object object) {
        if (!(object instanceof Metadata)) {
            return false;
        }
        Metadata metadata = (Metadata)object;
        if (!metadata.containsKey(this.key)) {
            return false;
        }
        Object actualValue = metadata.toMap().get(this.key);
        if (actualValue instanceof String) {
            String str = (String)actualValue;
            return str.toLowerCase().contains(this.searchTerm.toLowerCase());
        }
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        FullTextContains that = (FullTextContains)o;
        return Objects.equals(this.key, that.key) && Objects.equals(this.searchTerm, that.searchTerm);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.searchTerm);
    }
}

