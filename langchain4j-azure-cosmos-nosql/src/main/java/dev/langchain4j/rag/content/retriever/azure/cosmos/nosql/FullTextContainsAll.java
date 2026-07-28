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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FullTextContainsAll
implements Filter {
    private final String key;
    private final List<String> searchTerms;

    public FullTextContainsAll(String key, String ... searchTerms) {
        this(key, Arrays.asList(searchTerms));
    }

    public FullTextContainsAll(String key, Collection<String> searchTerms) {
        this.key = ValidationUtils.ensureNotBlank((String)key, (String)"key");
        this.searchTerms = ((Collection)ValidationUtils.ensureNotNull(searchTerms, (String)("searchTerms with key '" + key + "'"))).stream().collect(Collectors.toList());
        if (this.searchTerms.isEmpty()) {
            throw new IllegalArgumentException("searchTerms cannot be empty");
        }
    }

    public String key() {
        return this.key;
    }

    public List<String> searchTerms() {
        return this.searchTerms;
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
            String lowerStr = str.toLowerCase();
            return this.searchTerms.stream().allMatch(term -> lowerStr.contains(term.toLowerCase()));
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
        FullTextContainsAll that = (FullTextContainsAll)o;
        return Objects.equals(this.key, that.key) && Objects.equals(this.searchTerms, that.searchTerms);
    }

    public int hashCode() {
        return Objects.hash(this.key, this.searchTerms);
    }
}

