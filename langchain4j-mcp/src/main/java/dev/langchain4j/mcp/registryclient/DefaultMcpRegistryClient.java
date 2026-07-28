/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility
 *  com.fasterxml.jackson.annotation.PropertyAccessor
 *  com.fasterxml.jackson.core.JsonParser
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.DeserializationContext
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.JsonDeserializer
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.Module
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  com.fasterxml.jackson.databind.module.SimpleModule
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpClientBuilderLoader
 *  dev.langchain4j.http.client.HttpMethod
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.log.LoggingHttpClient
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.mcp.registryclient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.HttpMethod;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.LoggingHttpClient;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.registryclient.McpRegistryClient;
import dev.langchain4j.mcp.registryclient.McpRegistryClientException;
import dev.langchain4j.mcp.registryclient.model.McpGetServerResponse;
import dev.langchain4j.mcp.registryclient.model.McpRegistryHealth;
import dev.langchain4j.mcp.registryclient.model.McpRegistryPong;
import dev.langchain4j.mcp.registryclient.model.McpServerList;
import dev.langchain4j.mcp.registryclient.model.McpServerListRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DefaultMcpRegistryClient
implements McpRegistryClient {
    private static final String OFFICIAL_REGISTRY_URL = "https://registry.modelcontextprotocol.io";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE_TIME).parseLenient().appendLiteral('Z').toFormatter();
    private static final SimpleModule JACKSON_MODULE = new SimpleModule("mcp-registry-client-module").addDeserializer(LocalDateTime.class, (JsonDeserializer)new JsonDeserializer<LocalDateTime>(){

        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = (JsonNode)p.getCodec().readTree(p);
            return LocalDateTime.parse(node.asText(), DateTimeFormatter.ISO_DATE_TIME);
        }
    });
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).registerModule((Module)JACKSON_MODULE).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).enable(SerializationFeature.INDENT_OUTPUT);
    private final String baseUrl;
    private final HttpClient httpClient;
    private final Supplier<Map<String, String>> headers;
    private static final Pattern ALLOWED_URL_CHARACTERS = Pattern.compile("[a-zA-Z0-9\\-_./]+");

    private DefaultMcpRegistryClient(String baseUrl, HttpClient httpClient, Supplier<Map<String, String>> headers, boolean logRequests, boolean logResponses) {
        this.baseUrl = (String)Utils.getOrDefault((Object)baseUrl, (Object)OFFICIAL_REGISTRY_URL);
        this.headers = (Supplier)Utils.getOrDefault(headers, () -> HashMap::new);
        HttpClient httpClientToUse = (HttpClient)Utils.getOrDefault((Object)httpClient, () -> HttpClientBuilderLoader.loadHttpClientBuilder().build());
        this.httpClient = logRequests || logResponses ? new LoggingHttpClient(httpClientToUse, Boolean.valueOf(logRequests), Boolean.valueOf(logResponses)) : httpClientToUse;
    }

    @Override
    public McpServerList listServers(McpServerListRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        String params = this.processServerListRequestPathParams(request);
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, !params.isEmpty() ? "/v0/servers?" + params : "/v0/servers").addHeaders(this.currentHeaders()).build();
        return this.sendAndProcessResponse(httpRequest, McpServerList.class);
    }

    @Override
    public McpGetServerResponse getServerDetails(String serverName) {
        Objects.requireNonNull(serverName, "serverName cannot be null");
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "/v0/servers/" + this.encode(serverName)).addHeaders(this.currentHeaders()).build();
        return this.sendAndProcessResponse(httpRequest, McpGetServerResponse.class);
    }

    @Override
    public McpGetServerResponse getSpecificServerVersion(String serverName, String version) {
        Objects.requireNonNull(serverName, "serverName cannot be null");
        Objects.requireNonNull(version, "version cannot be null");
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "/v0.1/servers/" + this.encode(serverName) + "/versions/" + this.encode(version)).addHeaders(this.currentHeaders()).build();
        return this.sendAndProcessResponse(httpRequest, McpGetServerResponse.class);
    }

    @Override
    public McpServerList getAllVersionsOfServer(String serverName) {
        Objects.requireNonNull(serverName, "serverName cannot be null");
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "/v0.1/servers/" + this.encode(serverName) + "/versions").addHeaders(this.currentHeaders()).build();
        return this.sendAndProcessResponse(httpRequest, McpServerList.class);
    }

    @Override
    public McpRegistryHealth healthCheck() {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "/v0/health").addHeaders(this.currentHeaders()).build();
        return this.sendAndProcessResponse(httpRequest, McpRegistryHealth.class);
    }

    @Override
    public McpRegistryPong ping() {
        HttpRequest httpRequest = HttpRequest.builder().method(HttpMethod.GET).url(this.baseUrl, "/v0/ping").addHeaders(this.currentHeaders()).build();
        return this.sendAndProcessResponse(httpRequest, McpRegistryPong.class);
    }

    private Map<String, String> currentHeaders() {
        Map<String, String> map = this.headers.get();
        map.put("Content-Type", "application/json");
        map.put("Accept", "application/json, application/problem+json");
        return map;
    }

    private String encode(String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            throw new IllegalArgumentException("Parameter must not be null or empty");
        }
        if (!ALLOWED_URL_CHARACTERS.matcher(parameter).matches()) {
            throw new IllegalArgumentException("Parameter contains unsafe characters");
        }
        try {
            return URLEncoder.encode(parameter, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String processServerListRequestPathParams(McpServerListRequest request) {
        ArrayList<String> params = new ArrayList<String>();
        if (request.getCursor() != null) {
            params.add("cursor=" + request.getCursor());
        }
        if (request.getLimit() != null) {
            params.add("limit=" + request.getLimit());
        }
        if (request.getSearch() != null) {
            params.add("search=" + request.getSearch());
        }
        if (request.getUpdatedSince() != null) {
            params.add("updated_since=" + request.getUpdatedSince().format(DATE_TIME_FORMATTER));
        }
        if (request.getVersion() != null) {
            params.add("version=" + request.getVersion());
        }
        return params.stream().collect(Collectors.joining("&"));
    }

    private <T> T sendAndProcessResponse(HttpRequest httpRequest, Class<T> returnType) {
        SuccessfulHttpResponse response = this.httpClient.execute(httpRequest);
        try {
            return (T)OBJECT_MAPPER.readValue(response.body(), returnType);
        }
        catch (JsonProcessingException e) {
            throw new McpRegistryClientException(e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String baseUrl;
        private HttpClient httpClient;
        private Supplier<Map<String, String>> headers;
        private boolean logRequests = false;
        private boolean logResponses = false;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = () -> headers;
            return this;
        }

        public Builder headersSupplier(Supplier<Map<String, String>> headers) {
            this.headers = headers;
            return this;
        }

        public Builder logRequests(boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public DefaultMcpRegistryClient build() {
            return new DefaultMcpRegistryClient(this.baseUrl, this.httpClient, this.headers, this.logRequests, this.logResponses);
        }
    }
}

