/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.genai.Client
 *  com.google.genai.types.CachedContent
 *  com.google.genai.types.Content
 *  com.google.genai.types.CreateCachedContentConfig
 *  com.google.genai.types.CreateCachedContentConfig$Builder
 *  com.google.genai.types.DeleteCachedContentConfig
 *  com.google.genai.types.GetCachedContentConfig
 *  com.google.genai.types.ListCachedContentsConfig
 *  com.google.genai.types.UpdateCachedContentConfig
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.google.genai;

import com.google.genai.Client;
import com.google.genai.types.CachedContent;
import com.google.genai.types.Content;
import com.google.genai.types.CreateCachedContentConfig;
import com.google.genai.types.DeleteCachedContentConfig;
import com.google.genai.types.GetCachedContentConfig;
import com.google.genai.types.ListCachedContentsConfig;
import com.google.genai.types.UpdateCachedContentConfig;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiContentMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GoogleGenAiCaches {
    private final Client client;

    private GoogleGenAiCaches(Builder builder) {
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, null, null, null, null, builder.customHeaders, builder.apiEndpoint);
    }

    public static Builder builder() {
        return new Builder();
    }

    public CachedContent createCache(String modelName, List<ChatMessage> messages, Duration ttl) {
        ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        ValidationUtils.ensureNotEmpty(messages, (String)"messages");
        return this.client.caches.create(modelName, GoogleGenAiCaches.toCreateConfig(messages, ttl));
    }

    public CachedContent getCache(String name) {
        ValidationUtils.ensureNotBlank((String)name, (String)"name");
        return this.client.caches.get(name, GetCachedContentConfig.builder().build());
    }

    public List<CachedContent> listCaches() {
        ArrayList<CachedContent> caches = new ArrayList<CachedContent>();
        this.client.caches.list(ListCachedContentsConfig.builder().build()).forEach(caches::add);
        return caches;
    }

    public CachedContent updateCacheTtl(String name, Duration ttl) {
        ValidationUtils.ensureNotBlank((String)name, (String)"name");
        ValidationUtils.ensureNotNull((Object)ttl, (String)"ttl");
        return this.client.caches.update(name, UpdateCachedContentConfig.builder().ttl(ttl).build());
    }

    public void deleteCache(String name) {
        ValidationUtils.ensureNotBlank((String)name, (String)"name");
        this.client.caches.delete(name, DeleteCachedContentConfig.builder().build());
    }

    static CreateCachedContentConfig toCreateConfig(List<ChatMessage> messages, Duration ttl) {
        List<Content> contents = GoogleGenAiContentMapper.toContents(messages);
        Content systemInstruction = GoogleGenAiContentMapper.toSystemInstruction(messages);
        CreateCachedContentConfig.Builder config = CreateCachedContentConfig.builder();
        if (!contents.isEmpty()) {
            config.contents(contents);
        }
        if (systemInstruction != null) {
            config.systemInstruction(systemInstruction);
        }
        if (ttl != null) {
            config.ttl(ttl);
        }
        return config.build();
    }

    public static class Builder {
        private String apiKey;
        private String apiEndpoint;
        private Map<String, String> customHeaders;
        private Client client;

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
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

        public GoogleGenAiCaches build() {
            return new GoogleGenAiCaches(this);
        }
    }
}

