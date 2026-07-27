/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.web.search;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class WebSearchOrganicResult {
    private final String title;
    private final URI url;
    private final String snippet;
    private final String content;
    private final Map<String, String> metadata;

    public WebSearchOrganicResult(String title, URI url) {
        this(title, url, null, null);
    }

    public WebSearchOrganicResult(String title, URI url, String snippet, String content) {
        this(title, url, snippet, content, Collections.emptyMap());
    }

    public WebSearchOrganicResult(String title, URI url, String snippet, String content, Map<String, String> metadata) {
        this.title = ValidationUtils.ensureNotBlank(title, "title");
        this.url = ValidationUtils.ensureNotNull(url, "url");
        this.snippet = snippet;
        this.content = content;
        this.metadata = Utils.copy(metadata);
    }

    public String title() {
        return this.title;
    }

    public URI url() {
        return this.url;
    }

    public String snippet() {
        return this.snippet;
    }

    public String content() {
        return this.content;
    }

    public Map<String, String> metadata() {
        return this.metadata;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        WebSearchOrganicResult that = (WebSearchOrganicResult)o;
        return Objects.equals(this.title, that.title) && Objects.equals(this.url, that.url) && Objects.equals(this.snippet, that.snippet) && Objects.equals(this.content, that.content) && Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(this.title, this.url, this.snippet, this.content, this.metadata);
    }

    public String toString() {
        return "WebSearchOrganicResult{title='" + this.title + '\'' + ", url=" + this.url + ", snippet='" + this.snippet + '\'' + ", content='" + this.content + '\'' + ", metadata=" + this.metadata + '}';
    }

    public TextSegment toTextSegment() {
        return TextSegment.from(this.copyToText(), this.copyToMetadata());
    }

    public Document toDocument() {
        return Document.from(this.copyToText(), this.copyToMetadata());
    }

    private String copyToText() {
        StringBuilder text = new StringBuilder();
        text.append(this.title);
        text.append("\n");
        if (Utils.isNotNullOrBlank(this.content)) {
            text.append(this.content);
        } else if (Utils.isNotNullOrBlank(this.snippet)) {
            text.append(this.snippet);
        }
        return text.toString();
    }

    private Metadata copyToMetadata() {
        Metadata docMetadata = new Metadata();
        docMetadata.put("url", this.url.toString());
        for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
            docMetadata.put(entry.getKey(), entry.getValue());
        }
        return docMetadata;
    }

    public static WebSearchOrganicResult from(String title, URI url) {
        return new WebSearchOrganicResult(title, url);
    }

    public static WebSearchOrganicResult from(String title, URI url, String snippet, String content) {
        return new WebSearchOrganicResult(title, url, snippet, content);
    }

    public static WebSearchOrganicResult from(String title, URI url, String snippet, String content, Map<String, String> metadata) {
        return new WebSearchOrganicResult(title, url, snippet, content, metadata);
    }
}

