/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonParser
 *  com.fasterxml.jackson.databind.DeserializationContext
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.deser.std.StdDeserializer
 */
package dev.langchain4j.model.voyageai;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.langchain4j.model.voyageai.EmbeddingResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

class VoyageAiEmbeddingDeserializer
extends StdDeserializer<List<EmbeddingResponse.EmbeddingData>> {
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    public VoyageAiEmbeddingDeserializer() {
        this(null);
    }

    protected VoyageAiEmbeddingDeserializer(Class<?> vc) {
        super(vc);
    }

    public List<EmbeddingResponse.EmbeddingData> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode dataNode = (JsonNode)p.getCodec().readTree(p);
        if (dataNode != null && dataNode.isArray()) {
            ArrayList<EmbeddingResponse.EmbeddingData> embeddings = new ArrayList<EmbeddingResponse.EmbeddingData>();
            for (JsonNode data : dataNode) {
                List<Float> embedding;
                JsonNode embeddingNode = data.get("embedding");
                if (embeddingNode != null && embeddingNode.isArray()) {
                    embedding = new ArrayList();
                    for (JsonNode embeddingValue : embeddingNode) {
                        embedding.add(Float.valueOf(embeddingValue.floatValue()));
                    }
                } else if (embeddingNode != null && embeddingNode.isTextual()) {
                    embedding = this.decodeBase64ToFloatList(embeddingNode.asText());
                } else {
                    throw new RuntimeException("Unexpected embedding " + embeddingNode);
                }
                embeddings.add(new EmbeddingResponse.EmbeddingData(data.get("object").asText(), embedding, data.get("index").asInt()));
            }
            return embeddings;
        }
        throw new RuntimeException("Expect data is ArrayNode, but is " + dataNode);
    }

    private List<Float> decodeBase64ToFloatList(String base64String) {
        byte[] bytes = BASE64_DECODER.decode(base64String);
        ArrayList<Float> embedding = new ArrayList<Float>();
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        while (byteBuffer.remaining() >= 4) {
            embedding.add(Float.valueOf(byteBuffer.getFloat()));
        }
        return embedding;
    }
}

