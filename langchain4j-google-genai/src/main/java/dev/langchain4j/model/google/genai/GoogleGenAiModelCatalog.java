/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.types.ListModelsConfig
 *  com.google.genai.types.Model
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.catalog.ModelCatalog
 *  dev.langchain4j.model.catalog.ModelDescription
 *  dev.langchain4j.model.catalog.ModelDescription$Builder
 *  dev.langchain4j.model.catalog.ModelType
 */
package dev.langchain4j.model.google.genai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.ListModelsConfig;
import com.google.genai.types.Model;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.catalog.ModelCatalog;
import dev.langchain4j.model.catalog.ModelDescription;
import dev.langchain4j.model.catalog.ModelType;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoogleGenAiModelCatalog
implements ModelCatalog {
    private final Client client;

    private GoogleGenAiModelCatalog(Builder builder) {
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, builder.credentials, builder.projectId, builder.location, builder.timeout, builder.customHeaders, builder.apiEndpoint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<ModelDescription> listModels() {
        ArrayList<ModelDescription> allModels = new ArrayList<ModelDescription>();
        this.client.models.list(ListModelsConfig.builder().build()).forEach(modelInfo -> allModels.add(this.mapToModelDescription((Model)modelInfo)));
        return allModels;
    }

    public ModelProvider provider() {
        return ModelProvider.GOOGLE_GENAI;
    }

    private ModelDescription mapToModelDescription(Model modelInfo) {
        ModelDescription.Builder builder = ModelDescription.builder().provider(ModelProvider.GOOGLE_GENAI);
        if (modelInfo.name().isPresent()) {
            String name = (String)modelInfo.name().get();
            String id = name.startsWith("models/") ? name.substring(7) : name;
            builder.name(id);
        }
        if (modelInfo.displayName().isPresent() && !((String)modelInfo.displayName().get()).isEmpty()) {
            builder.displayName((String)modelInfo.displayName().get());
        }
        if (modelInfo.description().isPresent()) {
            builder.description((String)modelInfo.description().get());
        }
        if (modelInfo.inputTokenLimit().isPresent()) {
            builder.maxInputTokens((Integer)modelInfo.inputTokenLimit().get());
        }
        if (modelInfo.outputTokenLimit().isPresent()) {
            builder.maxOutputTokens((Integer)modelInfo.outputTokenLimit().get());
        }
        if (modelInfo.supportedActions().isPresent()) {
            List actions = (List)modelInfo.supportedActions().get();
            if (actions.contains("generateContent")) {
                builder.type(ModelType.CHAT);
            } else if (actions.contains("embedContent")) {
                builder.type(ModelType.EMBEDDING);
            }
        }
        return builder.build();
    }

    public static class Builder {
        private String apiKey;
        private GoogleCredentials credentials;
        private String projectId;
        private String location;
        private Duration timeout;
        private String apiEndpoint;
        private Map<String, String> customHeaders;
        private Client client;

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder credentials(GoogleCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder apiEndpoint(String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public GoogleGenAiModelCatalog build() {
            return new GoogleGenAiModelCatalog(this);
        }
    }
}

