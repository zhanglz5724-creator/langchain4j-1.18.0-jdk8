/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.http.client;

import dev.langchain4j.http.client.HttpClient;
import java.time.Duration;

public interface HttpClientBuilder {
    public Duration connectTimeout();

    public HttpClientBuilder connectTimeout(Duration var1);

    public Duration readTimeout();

    public HttpClientBuilder readTimeout(Duration var1);

    public HttpClient build();
}

