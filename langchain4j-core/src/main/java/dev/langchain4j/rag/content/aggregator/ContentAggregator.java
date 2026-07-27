/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.aggregator;

import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.query.Query;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ContentAggregator {
    public List<Content> aggregate(Map<Query, Collection<List<Content>>> var1);
}

