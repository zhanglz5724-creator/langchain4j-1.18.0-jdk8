/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.model.openai.internal.Json;
import dev.langchain4j.model.openai.internal.ParsedAndRawResponse;
import java.util.function.Function;

class SyncRequestExecutor<Response> {
    private final HttpClient httpClient;
    private final HttpRequest httpRequest;
    private final Class<Response> responseClass;
    private final Function<SuccessfulHttpResponse, Response> responseMapper;

    SyncRequestExecutor(HttpClient httpClient, HttpRequest httpRequest, Class<Response> responseClass) {
        this(httpClient, httpRequest, responseClass, null);
    }

    SyncRequestExecutor(HttpClient httpClient, HttpRequest httpRequest, Class<Response> responseClass, Function<SuccessfulHttpResponse, Response> responseMapper) {
        this.httpClient = httpClient;
        this.httpRequest = httpRequest;
        this.responseClass = responseClass;
        this.responseMapper = responseMapper;
    }

    ParsedAndRawResponse<Response> execute() {
        SuccessfulHttpResponse rawHttpResponse = this.httpClient.execute(this.httpRequest);
        Response parsedResponse = this.responseMapper != null ? this.responseMapper.apply(rawHttpResponse) : Json.fromJson(rawHttpResponse.body(), this.responseClass);
        return new ParsedAndRawResponse<Response>(parsedResponse, rawHttpResponse);
    }
}

