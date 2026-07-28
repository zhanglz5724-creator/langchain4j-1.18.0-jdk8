/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 */
package dev.langchain4j.model.huggingface.spi;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.huggingface.client.HuggingFaceClient;
import java.time.Duration;

public interface HuggingFaceClientFactory {
    public HuggingFaceClient create(Input var1);

    public static interface Input {
        default public String baseUrl() {
            return null;
        }

        public String apiKey();

        public String modelId();

        public Duration timeout();

        default public HttpClientBuilder httpClientBuilder() {
            return null;
        }
    }
}

