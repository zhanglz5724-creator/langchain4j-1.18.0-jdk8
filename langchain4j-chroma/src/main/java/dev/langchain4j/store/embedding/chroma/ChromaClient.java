/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.Internal;
import dev.langchain4j.store.embedding.chroma.AddEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.Collection;
import dev.langchain4j.store.embedding.chroma.CreateCollectionRequest;
import dev.langchain4j.store.embedding.chroma.DeleteEmbeddingsRequest;
import dev.langchain4j.store.embedding.chroma.QueryRequest;
import dev.langchain4j.store.embedding.chroma.QueryResponse;

@Internal
interface ChromaClient {
    public Collection createCollection(CreateCollectionRequest var1);

    public Collection collection(String var1);

    public boolean addEmbeddings(String var1, AddEmbeddingsRequest var2);

    public QueryResponse queryCollection(String var1, QueryRequest var2);

    public void deleteEmbeddings(String var1, DeleteEmbeddingsRequest var2);

    public void deleteCollection(String var1);
}

