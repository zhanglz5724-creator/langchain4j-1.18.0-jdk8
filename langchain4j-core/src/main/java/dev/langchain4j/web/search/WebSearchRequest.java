/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.web.search;

import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Map;
import java.util.Objects;

public class WebSearchRequest {
    private final String searchTerms;
    private final Integer maxResults;
    private final String language;
    private final String geoLocation;
    private final Integer startPage;
    private final Integer startIndex;
    private final Boolean safeSearch;
    private final Map<String, Object> additionalParams;

    private WebSearchRequest(Builder builder) {
        this.searchTerms = ValidationUtils.ensureNotBlank(builder.searchTerms, "searchTerms");
        this.maxResults = builder.maxResults;
        this.language = builder.language;
        this.geoLocation = builder.geoLocation;
        this.startPage = Utils.getOrDefault(builder.startPage, 1);
        this.startIndex = builder.startIndex;
        this.safeSearch = Utils.getOrDefault(builder.safeSearch, true);
        this.additionalParams = Utils.copy(builder.additionalParams);
    }

    public String searchTerms() {
        return this.searchTerms;
    }

    public Integer maxResults() {
        return this.maxResults;
    }

    public String language() {
        return this.language;
    }

    public String geoLocation() {
        return this.geoLocation;
    }

    public Integer startPage() {
        return this.startPage;
    }

    public Integer startIndex() {
        return this.startIndex;
    }

    public Boolean safeSearch() {
        return this.safeSearch;
    }

    public Map<String, Object> additionalParams() {
        return this.additionalParams;
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (!(another instanceof WebSearchRequest)) {
            return false;
        }
        WebSearchRequest wsr = (WebSearchRequest)another;
        return this.equalTo(wsr);
    }

    private boolean equalTo(WebSearchRequest another) {
        return Objects.equals(this.searchTerms, another.searchTerms) && Objects.equals(this.maxResults, another.maxResults) && Objects.equals(this.language, another.language) && Objects.equals(this.geoLocation, another.geoLocation) && Objects.equals(this.startPage, another.startPage) && Objects.equals(this.startIndex, another.startIndex) && Objects.equals(this.safeSearch, another.safeSearch) && Objects.equals(this.additionalParams, another.additionalParams);
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.searchTerms);
        h += (h << 5) + Objects.hashCode(this.maxResults);
        h += (h << 5) + Objects.hashCode(this.language);
        h += (h << 5) + Objects.hashCode(this.geoLocation);
        h += (h << 5) + Objects.hashCode(this.startPage);
        h += (h << 5) + Objects.hashCode(this.startIndex);
        h += (h << 5) + Objects.hashCode(this.safeSearch);
        h += (h << 5) + Objects.hashCode(this.additionalParams);
        return h;
    }

    public String toString() {
        return "WebSearchRequest{searchTerms='" + this.searchTerms + '\'' + ", maxResults=" + this.maxResults + ", language='" + this.language + '\'' + ", geoLocation='" + this.geoLocation + '\'' + ", startPage=" + this.startPage + ", startIndex=" + this.startIndex + ", siteRestrict=" + this.safeSearch + ", additionalParams=" + this.additionalParams + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static WebSearchRequest from(String searchTerms) {
        return WebSearchRequest.builder().searchTerms(searchTerms).build();
    }

    public static WebSearchRequest from(String searchTerms, Integer maxResults) {
        return WebSearchRequest.builder().searchTerms(searchTerms).maxResults(maxResults).build();
    }

    public static final class Builder {
        private String searchTerms;
        private Integer maxResults;
        private String language;
        private String geoLocation;
        private Integer startPage;
        private Integer startIndex;
        private Boolean safeSearch;
        private Map<String, Object> additionalParams;

        private Builder() {
        }

        public Builder searchTerms(String searchTerms) {
            this.searchTerms = searchTerms;
            return this;
        }

        public Builder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder geoLocation(String geoLocation) {
            this.geoLocation = geoLocation;
            return this;
        }

        public Builder startPage(Integer startPage) {
            this.startPage = startPage;
            return this;
        }

        public Builder startIndex(Integer startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public Builder safeSearch(Boolean safeSearch) {
            this.safeSearch = safeSearch;
            return this;
        }

        public Builder additionalParams(Map<String, Object> additionalParams) {
            this.additionalParams = additionalParams;
            return this;
        }

        public WebSearchRequest build() {
            return new WebSearchRequest(this);
        }
    }
}

