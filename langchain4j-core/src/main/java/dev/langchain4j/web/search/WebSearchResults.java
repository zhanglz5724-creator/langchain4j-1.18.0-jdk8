/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.web.search;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.web.search.WebSearchInformationResult;
import dev.langchain4j.web.search.WebSearchOrganicResult;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WebSearchResults {
    private final Map<String, Object> searchMetadata;
    private final WebSearchInformationResult searchInformation;
    private final List<WebSearchOrganicResult> results;

    public WebSearchResults(WebSearchInformationResult searchInformation, List<WebSearchOrganicResult> results) {
        this(Collections.emptyMap(), searchInformation, results);
    }

    public WebSearchResults(Map<String, Object> searchMetadata, WebSearchInformationResult searchInformation, List<WebSearchOrganicResult> results) {
        this.searchMetadata = Utils.copy(searchMetadata);
        this.searchInformation = ValidationUtils.ensureNotNull(searchInformation, "searchInformation");
        this.results = Utils.copy(results);
    }

    public Map<String, Object> searchMetadata() {
        return this.searchMetadata;
    }

    public WebSearchInformationResult searchInformation() {
        return this.searchInformation;
    }

    public List<WebSearchOrganicResult> results() {
        return this.results;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        WebSearchResults that = (WebSearchResults)o;
        return Objects.equals(this.searchMetadata, that.searchMetadata) && Objects.equals(this.searchInformation, that.searchInformation) && Objects.equals(this.results, that.results);
    }

    public int hashCode() {
        return Objects.hash(this.searchMetadata, this.searchInformation, this.results);
    }

    public String toString() {
        return "WebSearchResults{searchMetadata=" + this.searchMetadata + ", searchInformation=" + this.searchInformation + ", results=" + this.results + '}';
    }

    public List<TextSegment> toTextSegments() {
        return this.results.stream().map(WebSearchOrganicResult::toTextSegment).collect(Collectors.toList());
    }

    public List<Document> toDocuments() {
        return this.results.stream().map(WebSearchOrganicResult::toDocument).collect(Collectors.toList());
    }

    public static WebSearchResults from(WebSearchInformationResult searchInformation, List<WebSearchOrganicResult> results) {
        return new WebSearchResults(searchInformation, results);
    }

    public static WebSearchResults from(Map<String, Object> searchMetadata, WebSearchInformationResult searchInformation, List<WebSearchOrganicResult> results) {
        return new WebSearchResults(searchMetadata, searchInformation, results);
    }
}

