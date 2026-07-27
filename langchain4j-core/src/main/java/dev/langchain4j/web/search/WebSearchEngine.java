/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.web.search;

import dev.langchain4j.web.search.WebSearchRequest;
import dev.langchain4j.web.search.WebSearchResults;

public interface WebSearchEngine {
    default public WebSearchResults search(String query) {
        return this.search(WebSearchRequest.from(query));
    }

    public WebSearchResults search(WebSearchRequest var1);
}

