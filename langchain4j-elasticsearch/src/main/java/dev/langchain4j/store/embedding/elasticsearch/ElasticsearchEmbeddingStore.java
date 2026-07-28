/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch.ElasticsearchClient
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  org.apache.http.Header
 *  org.apache.http.HttpHost
 *  org.apache.http.auth.AuthScope
 *  org.apache.http.auth.Credentials
 *  org.apache.http.auth.UsernamePasswordCredentials
 *  org.apache.http.client.CredentialsProvider
 *  org.apache.http.impl.client.BasicCredentialsProvider
 *  org.apache.http.impl.nio.client.HttpAsyncClientBuilder
 *  org.apache.http.message.BasicHeader
 *  org.elasticsearch.client.RestClient
 *  org.elasticsearch.client.RestClientBuilder
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.AbstractElasticsearchEmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfiguration;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfigurationKnn;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchEmbeddingStore
extends AbstractElasticsearchEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchEmbeddingStore.class);

    @Deprecated(forRemoval=true)
    public ElasticsearchEmbeddingStore(ElasticsearchConfiguration configuration, String serverUrl, String apiKey, String userName, String password, String indexName, Integer dimension) {
        this(configuration, serverUrl, apiKey, userName, password, indexName);
        log.warn("Setting the dimension is deprecated.");
    }

    @Deprecated(forRemoval=true)
    public ElasticsearchEmbeddingStore(ElasticsearchConfiguration configuration, String serverUrl, String apiKey, String userName, String password, String indexName) {
        RestClientBuilder restClientBuilder = RestClient.builder((HttpHost[])new HttpHost[]{HttpHost.create((String)((String)ValidationUtils.ensureNotNull((Object)serverUrl, (String)"serverUrl")))});
        if (!Utils.isNullOrBlank((String)userName)) {
            BasicCredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(AuthScope.ANY, (Credentials)new UsernamePasswordCredentials(userName, password));
            restClientBuilder.setHttpClientConfigCallback(arg_0 -> ElasticsearchEmbeddingStore.lambda$new$0((CredentialsProvider)provider, arg_0));
        }
        if (!Utils.isNullOrBlank((String)apiKey)) {
            restClientBuilder.setDefaultHeaders(new Header[]{new BasicHeader("Authorization", "Apikey " + apiKey)});
        }
        this.initialize(configuration, restClientBuilder.build(), (String)ValidationUtils.ensureNotNull((Object)indexName, (String)"indexName"));
    }

    @Deprecated(forRemoval=true)
    public ElasticsearchEmbeddingStore(ElasticsearchConfiguration configuration, RestClient restClient, String indexName) {
        this.initialize(configuration, restClient, indexName);
    }

    public ElasticsearchEmbeddingStore(ElasticsearchConfiguration configuration, ElasticsearchClient client, String indexName) {
        this.initialize(configuration, client, indexName);
    }

    public static Builder builder() {
        return new Builder();
    }

    private static /* synthetic */ HttpAsyncClientBuilder lambda$new$0(CredentialsProvider provider, HttpAsyncClientBuilder httpClientBuilder) {
        return httpClientBuilder.setDefaultCredentialsProvider(provider);
    }

    public static class Builder {
        private String serverUrl;
        private String apiKey;
        private String userName;
        private String password;
        private ElasticsearchClient client;
        private RestClient restClient;
        private String indexName = "default";
        private ElasticsearchConfiguration configuration = ElasticsearchConfigurationKnn.builder().build();

        @Deprecated(forRemoval=true)
        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        @Deprecated(forRemoval=true)
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        @Deprecated(forRemoval=true)
        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        @Deprecated(forRemoval=true)
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        @Deprecated(forRemoval=true)
        public Builder restClient(RestClient restClient) {
            this.restClient = restClient;
            return this;
        }

        public Builder client(ElasticsearchClient client) {
            this.client = client;
            return this;
        }

        public Builder indexName(String indexName) {
            this.indexName = indexName;
            return this;
        }

        @Deprecated(forRemoval=true)
        public Builder dimension(Integer dimension) {
            log.warn("Setting the dimension is deprecated. This value is ignored.");
            return this;
        }

        public Builder configuration(ElasticsearchConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public ElasticsearchEmbeddingStore build() {
            if (this.client != null) {
                return new ElasticsearchEmbeddingStore(this.configuration, this.client, this.indexName);
            }
            log.warn("This is deprecated. You should provide an ElasticsearchClient instead and use client(ElasticsearchClient) instead.");
            if (this.restClient != null) {
                return new ElasticsearchEmbeddingStore(this.configuration, this.restClient, this.indexName);
            }
            return new ElasticsearchEmbeddingStore(this.configuration, this.serverUrl, this.apiKey, this.userName, this.password, this.indexName);
        }
    }
}

