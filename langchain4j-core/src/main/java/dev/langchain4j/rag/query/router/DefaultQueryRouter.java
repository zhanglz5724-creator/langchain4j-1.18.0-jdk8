/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query.router;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.QueryRouter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DefaultQueryRouter
implements QueryRouter {
    private final Collection<ContentRetriever> contentRetrievers;

    public DefaultQueryRouter(ContentRetriever ... contentRetrievers) {
        this(Arrays.asList(contentRetrievers));
    }

    public DefaultQueryRouter(Collection<ContentRetriever> contentRetrievers) {
        this.contentRetrievers = Collections.unmodifiableCollection(ValidationUtils.ensureNotEmpty(contentRetrievers, "contentRetrievers"));
    }

    @Override
    public Collection<ContentRetriever> route(Query query) {
        return this.contentRetrievers;
    }
}

