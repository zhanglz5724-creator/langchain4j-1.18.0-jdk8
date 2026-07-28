/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.segment.TextSegment
 */
package dev.langchain4j.store.embedding.inmemory;

import dev.langchain4j.Internal;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Internal
public interface InMemoryEmbeddingStoreJsonCodec {
    public InMemoryEmbeddingStore<TextSegment> fromJson(String var1);

    public String toJson(InMemoryEmbeddingStore<?> var1);

    default public InMemoryEmbeddingStore<TextSegment> fromJson(InputStream in) throws IOException {
        int n;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        while ((n = in.read(data)) != -1) {
            buffer.write(data, 0, n);
        }
        String json = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        return this.fromJson(json);
    }

    default public void toJson(OutputStream out, InMemoryEmbeddingStore<?> store) throws IOException {
        out.write(this.toJson(store).getBytes(StandardCharsets.UTF_8));
    }
}

