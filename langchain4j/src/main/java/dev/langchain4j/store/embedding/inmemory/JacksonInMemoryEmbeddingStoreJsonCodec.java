/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.PropertyAccessor
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.json.JsonMapper
 *  com.fasterxml.jackson.databind.json.JsonMapper$Builder
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 */
package dev.langchain4j.store.embedding.inmemory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.langchain4j.Internal;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStoreJsonCodec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Internal
class JacksonInMemoryEmbeddingStoreJsonCodec
implements InMemoryEmbeddingStoreJsonCodec {
    private static final ObjectMapper OBJECT_MAPPER = ((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)JsonMapper.builder().visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)).addMixIn(InMemoryEmbeddingStore.Entry.class, EntryMixIn.class)).addMixIn(Embedding.class, EmbeddingMixIn.class)).addMixIn(TextSegment.class, TextSegmentMixin.class)).build();
    private static final TypeReference<InMemoryEmbeddingStore<TextSegment>> TYPE_REFERENCE = new TypeReference<InMemoryEmbeddingStore<TextSegment>>(){};

    JacksonInMemoryEmbeddingStoreJsonCodec() {
    }

    @Override
    public InMemoryEmbeddingStore<TextSegment> fromJson(String json) {
        try {
            return (InMemoryEmbeddingStore)OBJECT_MAPPER.readValue(json, TYPE_REFERENCE);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson(InMemoryEmbeddingStore<?> store) {
        try {
            return OBJECT_MAPPER.writeValueAsString(store);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void toJson(OutputStream outputStream, InMemoryEmbeddingStore<?> store) {
        try {
            OBJECT_MAPPER.writeValue(outputStream, store);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InMemoryEmbeddingStore<TextSegment> fromJson(InputStream inputStream) {
        try {
            return (InMemoryEmbeddingStore)OBJECT_MAPPER.readValue(inputStream, TYPE_REFERENCE);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static abstract class TextSegmentMixin {
        @JsonCreator
        public TextSegmentMixin(@JsonProperty(value="text") String text, @JsonProperty(value="metadata") Metadata metadata) {
        }
    }

    private static abstract class EmbeddingMixIn {
        @JsonCreator
        EmbeddingMixIn(@JsonProperty(value="vector") float[] vector) {
        }

        @JsonProperty(value="vector")
        abstract float[] vector();
    }

    private static abstract class EntryMixIn<T> {
        @JsonCreator
        EntryMixIn(@JsonProperty(value="id") String id, @JsonProperty(value="embedding") Embedding embedding, @JsonProperty(value="embedded") T embedded) {
        }
    }
}

