/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query.router;

import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import java.util.Collection;

public interface QueryRouter {
    public Collection<ContentRetriever> route(Query var1);
}

