/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.model.embedding.request.DefaultEmbeddingRequestParameters
 *  dev.langchain4j.model.embedding.request.DefaultEmbeddingRequestParameters$Builder
 *  dev.langchain4j.model.embedding.request.EmbeddingParameter
 *  dev.langchain4j.model.embedding.request.EmbeddingRequestParameters
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.embedding.request.DefaultEmbeddingRequestParameters;
import dev.langchain4j.model.embedding.request.EmbeddingParameter;
import dev.langchain4j.model.embedding.request.EmbeddingRequestParameters;
import java.util.LinkedHashMap;
import java.util.Map;

@Experimental
public class OpenAiEmbeddingRequestParameters
extends DefaultEmbeddingRequestParameters {
    public static final EmbeddingParameter<String> USER = new EmbeddingParameter("openai.user", String.class);
    public static final EmbeddingParameter<String> ENCODING_FORMAT = new EmbeddingParameter("openai.encodingFormat", String.class);
    public static final EmbeddingParameter<Map> CUSTOM_PARAMETERS = new EmbeddingParameter("openai.customParameters", Map.class);

    protected OpenAiEmbeddingRequestParameters(Builder builder) {
        super((DefaultEmbeddingRequestParameters.Builder)builder);
    }

    public String user() {
        return (String)this.parameter(USER);
    }

    public String encodingFormat() {
        return (String)this.parameter(ENCODING_FORMAT);
    }

    public Map<String, Object> customParameters() {
        return (Map)this.parameter(CUSTOM_PARAMETERS);
    }

    public OpenAiEmbeddingRequestParameters overrideWith(EmbeddingRequestParameters that) {
        if (that == null || that.presentParameters().isEmpty()) {
            return this;
        }
        return ((Builder)((Builder)OpenAiEmbeddingRequestParameters.builder().overrideWith((EmbeddingRequestParameters)this)).overrideWith(that)).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends DefaultEmbeddingRequestParameters.Builder<Builder> {
        public Builder user(String user) {
            return (Builder)this.set(USER, user);
        }

        public Builder encodingFormat(String encodingFormat) {
            return (Builder)this.set(ENCODING_FORMAT, encodingFormat);
        }

        public Builder customParameters(Map<String, Object> customParameters) {
            return (Builder)this.set(CUSTOM_PARAMETERS, customParameters);
        }

        public Builder customParameter(String name, Object value) {
            Map current = (Map)this.values.get(CUSTOM_PARAMETERS);
            LinkedHashMap<String, Object> merged = current == null ? new LinkedHashMap<String, Object>() : new LinkedHashMap(current);
            merged.put(name, value);
            return (Builder)this.set(CUSTOM_PARAMETERS, merged);
        }

        public OpenAiEmbeddingRequestParameters build() {
            return new OpenAiEmbeddingRequestParameters(this);
        }
    }
}

