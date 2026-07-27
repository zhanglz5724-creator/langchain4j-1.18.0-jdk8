/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.retriever.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import java.util.Map;

@Experimental
public class ContentRetrieverErrorContext {
    private final Throwable error;
    private final Query query;
    private final ContentRetriever contentRetriever;
    private final Map<Object, Object> attributes;

    public ContentRetrieverErrorContext(Builder builder) {
        this.error = ValidationUtils.ensureNotNull(builder.error, "error");
        this.query = ValidationUtils.ensureNotNull(builder.query, "query");
        this.contentRetriever = ValidationUtils.ensureNotNull(builder.contentRetriever, "contentRetriever");
        this.attributes = ValidationUtils.ensureNotNull(builder.attributes, "attributes");
    }

    public static Builder builder() {
        return new Builder();
    }

    public Throwable error() {
        return this.error;
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
        private Throwable error;
        private Query query;
        private ContentRetriever contentRetriever;
        private Map<Object, Object> attributes;

        Builder() {
        }

        public Builder error(Throwable error) {
            this.error = error;
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

        public ContentRetrieverErrorContext build() {
            return new ContentRetrieverErrorContext(this);
        }
    }
}

