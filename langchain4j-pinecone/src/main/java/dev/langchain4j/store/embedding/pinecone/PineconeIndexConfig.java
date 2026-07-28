/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.pinecone.clients.Pinecone
 */
package dev.langchain4j.store.embedding.pinecone;

import io.pinecone.clients.Pinecone;

public interface PineconeIndexConfig {
    public void createIndex(Pinecone var1, String var2);
}

