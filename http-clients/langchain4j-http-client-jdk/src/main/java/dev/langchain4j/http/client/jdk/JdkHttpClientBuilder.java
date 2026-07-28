package dev.langchain4j.http.client.jdk;

import dev.langchain4j.http.client.HttpClientBuilder;
import java.time.Duration;

public class JdkHttpClientBuilder implements HttpClientBuilder {

    private okhttp3.OkHttpClient.Builder okHttpClientBuilder;
    private Duration connectTimeout;
    private Duration readTimeout;

    public okhttp3.OkHttpClient.Builder okHttpClientBuilder() {
        return okHttpClientBuilder;
    }

    public JdkHttpClientBuilder okHttpClientBuilder(okhttp3.OkHttpClient.Builder okHttpClientBuilder) {
        this.okHttpClientBuilder = okHttpClientBuilder;
        return this;
    }

    @Override
    public Duration connectTimeout() {
        return connectTimeout;
    }

    @Override
    public JdkHttpClientBuilder connectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    @Override
    public Duration readTimeout() {
        return readTimeout;
    }

    @Override
    public JdkHttpClientBuilder readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    @Override
    public JdkHttpClient build() {
        return new JdkHttpClient(this);
    }
}
