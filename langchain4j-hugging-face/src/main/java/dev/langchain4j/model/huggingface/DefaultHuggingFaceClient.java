/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.huggingface;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.huggingface.HuggingFaceJsonUtils;
import dev.langchain4j.model.huggingface.client.EmbeddingRequest;
import dev.langchain4j.model.huggingface.client.HuggingFaceClient;
import dev.langchain4j.model.huggingface.client.TextGenerationRequest;
import dev.langchain4j.model.huggingface.client.TextGenerationResponse;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

class DefaultHuggingFaceClient
implements HuggingFaceClient {
    private static final String BASE_URL = "https://router.huggingface.co/hf-inference/";
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;
    private final String modelId;

    DefaultHuggingFaceClient(HttpClientBuilder httpClientBuilder, String baseUrl, String apiKey, String modelId, Duration timeout) {
        HttpClientBuilder builder = (HttpClientBuilder)Utils.getOrDefault((Object)httpClientBuilder, HttpClientBuilderLoader::loadHttpClientBuilder);
        this.httpClient = builder.connectTimeout(timeout).readTimeout(timeout).build();
        this.baseUrl = Utils.ensureTrailingForwardSlash((String)(Objects.isNull(baseUrl) ? BASE_URL : baseUrl));
        this.apiKey = ValidationUtils.ensureNotBlank((String)apiKey, (String)"apiKey");
        this.modelId = ValidationUtils.ensureNotBlank((String)modelId, (String)"modelId");
    }

    @Override
    public TextGenerationResponse chat(TextGenerationRequest request) {
        return this.generate(request);
    }

    @Override
    public TextGenerationResponse generate(TextGenerationRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "models/" + this.modelId).addHeader("Content-Type", new String[]{"application/json"}).addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).body(HuggingFaceJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse httpResponse = this.httpClient.execute(httpRequest);
        List<TextGenerationResponse> responses = HuggingFaceJsonUtils.fromJson(httpResponse.body(), new TypeReference<List<TextGenerationResponse>>(){});
        return DefaultHuggingFaceClient.toOneResponse(responses);
    }

    private static TextGenerationResponse toOneResponse(List<TextGenerationResponse> responses) {
        if (responses != null && responses.size() == 1) {
            return responses.get(0);
        }
        throw new RuntimeException("Expected only one generated_text, but was: " + (responses == null ? 0 : responses.size()));
    }

    @Override
    public List<float[]> embed(EmbeddingRequest request) {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.POST).url(this.baseUrl + "models/" + this.modelId + "/pipeline/feature-extraction").addHeader("Content-Type", new String[]{"application/json"}).addHeader("Authorization", new String[]{"Bearer " + this.apiKey}).body(HuggingFaceJsonUtils.toJson(request)).build();
        SuccessfulHttpResponse httpResponse = this.httpClient.execute(httpRequest);
        return HuggingFaceJsonUtils.fromJson(httpResponse.body(), new TypeReference<List<float[]>>(){});
    }
}

