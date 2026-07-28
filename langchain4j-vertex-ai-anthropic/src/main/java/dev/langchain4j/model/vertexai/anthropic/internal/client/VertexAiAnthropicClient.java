/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  com.google.api.HttpBody
 *  com.google.api.gax.core.CredentialsProvider
 *  com.google.api.gax.core.FixedCredentialsProvider
 *  com.google.api.gax.rpc.UnavailableException
 *  com.google.auth.Credentials
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.cloud.aiplatform.v1.EndpointName
 *  com.google.cloud.aiplatform.v1.PredictionServiceClient
 *  com.google.cloud.aiplatform.v1.PredictionServiceSettings
 *  com.google.cloud.aiplatform.v1.PredictionServiceSettings$Builder
 *  com.google.cloud.aiplatform.v1.RawPredictRequest
 *  com.google.protobuf.ByteString
 *  io.grpc.Status$Code
 *  io.grpc.StatusRuntimeException
 */
package dev.langchain4j.model.vertexai.anthropic.internal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.HttpBody;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.UnavailableException;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1.PredictionServiceSettings;
import com.google.cloud.aiplatform.v1.RawPredictRequest;
import com.google.protobuf.ByteString;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicContent;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicRequest;
import dev.langchain4j.model.vertexai.anthropic.internal.api.AnthropicResponse;
import dev.langchain4j.model.vertexai.anthropic.internal.client.StreamingResponseHandler;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VertexAiAnthropicClient {
    private static final String PUBLISHER = "anthropic";
    private static final String CREDENTIALS_ENDPOINT_TEMPLATE = "%s-aiplatform.googleapis.com:443";
    private volatile PredictionServiceClient predictionServiceClient;
    private final String project;
    private final String location;
    private final GoogleCredentials credentials;
    private final ObjectMapper objectMapper;

    public VertexAiAnthropicClient(String project, String location, String model) {
        this(project, location, model, null);
    }

    public VertexAiAnthropicClient(String project, String location, String model, GoogleCredentials credentials) {
        if (project == null || project.trim().isEmpty()) {
            throw new IllegalArgumentException("project cannot be null or empty");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("location cannot be null or empty");
        }
        String ignoredModel = model;
        this.project = project;
        this.location = location;
        this.credentials = credentials;
        this.objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        this.predictionServiceClient = this.createClient();
    }

    private PredictionServiceClient createClient() {
        try {
            PredictionServiceSettings.Builder settingsBuilder = PredictionServiceSettings.newBuilder();
            settingsBuilder.setEndpoint(String.format(CREDENTIALS_ENDPOINT_TEMPLATE, this.location.toLowerCase()));
            if (this.credentials != null) {
                GoogleCredentials scopedCredentials = this.credentials.createScoped(new String[]{"https://www.googleapis.com/auth/cloud-platform"});
                settingsBuilder.setCredentialsProvider((CredentialsProvider)FixedCredentialsProvider.create((Credentials)scopedCredentials));
            }
            return PredictionServiceClient.create((PredictionServiceSettings)settingsBuilder.build());
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to initialize Vertex AI client", e);
        }
    }

    public AnthropicResponse generateContent(AnthropicRequest request, String modelName) throws IOException {
        return this.generateContentWithRetry(request, modelName, 1);
    }

    public void generateContentStreaming(AnthropicRequest request, String modelName, StreamingResponseHandler handler) throws IOException {
        try {
            AnthropicResponse response = this.generateContent(request, modelName);
            handler.onResponse(response);
            if (response.content != null && !response.content.isEmpty()) {
                this.processContentForStreaming(response.content, handler);
            }
            handler.onComplete();
        }
        catch (Exception e) {
            handler.onError(e);
        }
    }

    private void processContentForStreaming(List<AnthropicContent> contentList, StreamingResponseHandler handler) {
        for (AnthropicContent content : contentList) {
            if ("text".equals(content.type) && content.text != null) {
                this.processTextContentStreaming(content.text, handler);
                continue;
            }
            if (!"tool_use".equals(content.type)) continue;
            this.processToolContentStreaming(content, handler);
        }
    }

    private void processTextContentStreaming(String text, StreamingResponseHandler handler) {
        int chunkSize = 10;
        for (int i = 0; i < text.length(); i += chunkSize) {
            int end = Math.min(i + chunkSize, text.length());
            String chunk = text.substring(i, end);
            String streamChunk = String.format("{\"type\":\"content_block_delta\",\"delta\":{\"type\":\"text_delta\",\"text\":\"%s\"}}", chunk.replace("\"", "\\\"").replace("\n", "\\n"));
            handler.onChunk(streamChunk);
            this.simulateStreamingDelay(handler);
        }
    }

    private void processToolContentStreaming(AnthropicContent content, StreamingResponseHandler handler) {
        String toolChunk = String.format("{\"type\":\"content_block_start\",\"content_block\":{\"type\":\"tool_use\",\"id\":\"%s\",\"name\":\"%s\"}}", content.id, content.name);
        handler.onChunk(toolChunk);
    }

    private void simulateStreamingDelay(StreamingResponseHandler handler) {
        try {
            Thread.sleep(10L);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            handler.onError(e);
        }
    }

    private AnthropicResponse generateContentWithRetry(AnthropicRequest request, String modelName, int attempt) throws IOException {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        try {
            Map<String, Object> requestMap = this.buildRequestMap(request);
            String requestJson = this.objectMapper.writeValueAsString(requestMap);
            RawPredictRequest rawPredictRequest = this.buildRawPredictRequest(requestJson, modelName);
            HttpBody response = this.predictionServiceClient.rawPredict(rawPredictRequest);
            String responseJson = response.getData().toStringUtf8();
            return (AnthropicResponse)this.objectMapper.readValue(responseJson, AnthropicResponse.class);
        }
        catch (UnavailableException e) {
            return this.handleUnavailableException(e, request, modelName, attempt);
        }
        catch (Exception e) {
            throw new IOException("Failed to generate content using Vertex AI rawPredict", e);
        }
    }

    private Map<String, Object> buildRequestMap(AnthropicRequest request) {
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("anthropic_version", "vertex-2023-10-16");
        requestMap.put("messages", request.messages);
        requestMap.put("max_tokens", request.maxTokens);
        this.addOptionalParameter(requestMap, "temperature", request.temperature);
        this.addOptionalParameter(requestMap, "system", request.system);
        this.addOptionalParameter(requestMap, "top_p", request.topP);
        this.addOptionalParameter(requestMap, "top_k", request.topK);
        this.addOptionalParameter(requestMap, "stop_sequences", request.stopSequences);
        this.addOptionalParameter(requestMap, "tools", request.tools);
        this.addOptionalParameter(requestMap, "tool_choice", request.toolChoice);
        return requestMap;
    }

    private void addOptionalParameter(Map<String, Object> requestMap, String key, Object value) {
        if (value != null) {
            requestMap.put(key, value);
        }
    }

    private RawPredictRequest buildRawPredictRequest(String requestJson, String modelName) {
        String endpoint = EndpointName.ofProjectLocationPublisherModelName((String)this.project, (String)this.location, (String)PUBLISHER, (String)modelName).toString();
        HttpBody httpBody = HttpBody.newBuilder().setContentType("application/json").setData(ByteString.copyFromUtf8((String)requestJson)).build();
        return RawPredictRequest.newBuilder().setEndpoint(endpoint).setHttpBody(httpBody).build();
    }

    private AnthropicResponse handleUnavailableException(UnavailableException e, AnthropicRequest request, String modelName, int attempt) throws IOException {
        if (this.isChannelShutdownError(e) && attempt < 3) {
            return this.retryAfterDelay(request, modelName, attempt);
        }
        throw new IOException("Failed to generate content using Vertex AI rawPredict", e);
    }

    private boolean isChannelShutdownError(UnavailableException e) {
        return e.getCause() instanceof StatusRuntimeException && ((StatusRuntimeException)e.getCause()).getStatus().getCode() == Status.Code.UNAVAILABLE && ((StatusRuntimeException)e.getCause()).getMessage().contains("Channel shutdown invoked");
    }

    private AnthropicResponse retryAfterDelay(AnthropicRequest request, String modelName, int attempt) throws IOException {
        try {
            Thread.sleep(100L * (long)attempt);
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", ie);
        }
        this.recreateClient();
        return this.generateContentWithRetry(request, modelName, attempt + 1);
    }

    private void recreateClient() {
        try {
            if (this.predictionServiceClient != null) {
                this.predictionServiceClient.close();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.predictionServiceClient = this.createClient();
    }

    public void close() {
        if (this.predictionServiceClient != null && !this.predictionServiceClient.isShutdown()) {
            try {
                this.predictionServiceClient.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

