/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  co.elastic.clients.elasticsearch.ElasticsearchClient
 *  co.elastic.clients.elasticsearch._types.ElasticsearchException
 *  co.elastic.clients.elasticsearch.core.SearchResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import dev.langchain4j.store.embedding.elasticsearch.Document;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchConfiguration;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchConfigurationFullText
implements ElasticsearchConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ElasticsearchConfigurationFullText.class);

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SearchResponse<Document> fullTextSearch(ElasticsearchClient client, String indexName, String textQuery) throws ElasticsearchException, IOException {
        log.trace("Searching for text matches in index [{}] with query [{}].", (Object)indexName, (Object)textQuery);
        return client.search(s -> s.index(indexName, new String[0]).query(q -> q.match(m -> m.field("text").query(textQuery))), Document.class);
    }

    public static class Builder {
        public ElasticsearchConfigurationFullText build() {
            return new ElasticsearchConfigurationFullText();
        }
    }
}

