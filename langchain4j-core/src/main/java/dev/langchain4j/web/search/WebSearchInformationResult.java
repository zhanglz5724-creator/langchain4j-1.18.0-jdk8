/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.web.search;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Map;
import java.util.Objects;

public class WebSearchInformationResult {
    private final Long totalResults;
    private final Integer pageNumber;
    private final Map<String, Object> metadata;

    public WebSearchInformationResult(Long totalResults) {
        this(totalResults, null, null);
    }

    public WebSearchInformationResult(Long totalResults, Integer pageNumber, Map<String, Object> metadata) {
        this.totalResults = ValidationUtils.ensureNotNull(totalResults, "totalResults");
        this.pageNumber = pageNumber;
        this.metadata = Utils.copy(metadata);
    }

    public Long totalResults() {
        return this.totalResults;
    }

    public Integer pageNumber() {
        return this.pageNumber;
    }

    public Map<String, Object> metadata() {
        return this.metadata;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        WebSearchInformationResult that = (WebSearchInformationResult)o;
        return Objects.equals(this.totalResults, that.totalResults) && Objects.equals(this.pageNumber, that.pageNumber) && Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(this.totalResults, this.pageNumber, this.metadata);
    }

    public String toString() {
        return "WebSearchInformationResult{totalResults=" + this.totalResults + ", pageNumber=" + this.pageNumber + ", metadata=" + this.metadata + '}';
    }

    public static WebSearchInformationResult from(Long totalResults) {
        return new WebSearchInformationResult(totalResults);
    }

    public static WebSearchInformationResult from(Long totalResults, Integer pageNumber, Map<String, Object> metadata) {
        return new WebSearchInformationResult(totalResults, pageNumber, metadata);
    }
}

