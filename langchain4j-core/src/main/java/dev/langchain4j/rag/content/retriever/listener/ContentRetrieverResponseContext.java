/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.retriever.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import java.util.List;
import java.util.Map;

@Experimental
public class ContentRetrieverResponseContext {
    private final List<Content> contents;
    private final Query query;
    private final ContentRetriever contentRetriever;
    private final Map<Object, Object> attributes;

    public ContentRetrieverResponseContext(Builder builder) {
        this.contents = Utils.copy(ValidationUtils.ensureNotNull(builder.contents, "contents"));
        this.query = ValidationUtils.ensureNotNull(builder.query, "query");
        this.contentRetriever = ValidationUtils.ensureNotNull(builder.contentRetriever, "contentRetriever");
        this.attributes = ValidationUtils.ensureNotNull(builder.attributes, "attributes");
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Content> contents() {
        return this.contents;
    }

    public Query query() {
        return this.query;
    }

    public ContentRetriever contentRetriever() {
        return this.contentRetriever;
    }

    public Map<Object, Object> attributes() {
        return this.attributes;
    }

    @Experimental
    public static class Builder {
        private List<Content> contents;
        private Query query;
        private ContentRetriever contentRetriever;
        private Map<Object, Object> attributes;

        Builder() {
        }

        public Builder contents(List<Content> contents) {
            this.contents = contents;
            return this;
        }

        public Builder query(Query query) {
            this.query = query;
            return this;
        }

        public Builder contentRetriever(ContentRetriever contentRetriever) {
            this.contentRetriever = contentRetriever;
            return this;
        }

        public Builder attributes(Map<Object, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public ContentRetrieverResponseContext build() {
            return new ContentRetrieverResponseContext(this);
        }
    }
}

