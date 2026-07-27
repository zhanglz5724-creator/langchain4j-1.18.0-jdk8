/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.retriever;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.WebSearchRequest;
import dev.langchain4j.web.search.WebSearchResults;
import java.util.List;
import java.util.stream.Collectors;

public class WebSearchContentRetriever
implements ContentRetriever {
    private final WebSearchEngine webSearchEngine;
    private final int maxResults;

    public WebSearchContentRetriever(WebSearchEngine webSearchEngine, Integer maxResults) {
        this.webSearchEngine = ValidationUtils.ensureNotNull(webSearchEngine, "webSearchEngine");
        this.maxResults = Utils.getOrDefault(maxResults, 5);
    }

    public static WebSearchContentRetrieverBuilder builder() {
        return new WebSearchContentRetrieverBuilder();
    }

    @Override
    public List<Content> retrieve(Query query) {
        WebSearchRequest webSearchRequest = WebSearchRequest.builder().searchTerms(query.text()).maxResults(this.maxResults).build();
        WebSearchResults webSearchResults = this.webSearchEngine.search(webSearchRequest);
        return webSearchResults.toTextSegments().stream().map(Content::from).collect(Collectors.toList());
    }

    public static class WebSearchContentRetrieverBuilder {
        private WebSearchEngine webSearchEngine;
        private Integer maxResults;

        WebSearchContentRetrieverBuilder() {
        }

        public WebSearchContentRetrieverBuilder webSearchEngine(WebSearchEngine webSearchEngine) {
            this.webSearchEngine = webSearchEngine;
            return this;
        }

        public WebSearchContentRetrieverBuilder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public WebSearchContentRetriever build() {
            return new WebSearchContentRetriever(this.webSearchEngine, this.maxResults);
        }
    }
}

