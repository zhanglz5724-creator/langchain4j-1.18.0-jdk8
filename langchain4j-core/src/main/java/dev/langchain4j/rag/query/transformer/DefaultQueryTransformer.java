/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.query.transformer;

import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import java.util.Collection;
import java.util.Collections;

public class DefaultQueryTransformer
implements QueryTransformer {
    @Override
    public Collection<Query> transform(Query query) {
        return Collections.singletonList(query);
    }
}

