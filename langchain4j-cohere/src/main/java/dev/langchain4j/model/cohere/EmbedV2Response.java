/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.cohere;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.cohere.Meta;
import java.util.List;

class EmbedV2Response {
    private String id;
    private Embeddings embeddings;
    private Meta meta;

    EmbedV2Response() {
    }

    public String getId() {
        return this.id;
    }

    public Embeddings getEmbeddings() {
        return this.embeddings;
    }

    public Meta getMeta() {
        return this.meta;
    }

    static class Embeddings {
        @JsonProperty(value="float")
        private List<List<Float>> floatEmbeddings;

        Embeddings() {
        }

        public List<List<Float>> getFloatEmbeddings() {
            return this.floatEmbeddings;
        }
    }
}

