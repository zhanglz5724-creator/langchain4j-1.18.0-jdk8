/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query.transformer;

import dev.langchain4j.rag.query.Query;
import java.util.Collection;

public interface QueryTransformer {
    public Collection<Query> transform(Query var1);
}

