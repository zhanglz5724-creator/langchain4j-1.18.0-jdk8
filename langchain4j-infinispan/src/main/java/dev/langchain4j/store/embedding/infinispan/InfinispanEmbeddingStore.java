/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
 *  dev.langchain4j.store.embedding.filter.Filter
 *  org.infinispan.client.hotrod.RemoteCache
 *  org.infinispan.client.hotrod.RemoteCacheManager
 *  org.infinispan.client.hotrod.configuration.ConfigurationBuilder
 *  org.infinispan.commons.api.query.Query
 *  org.infinispan.commons.configuration.BasicConfiguration
 *  org.infinispan.commons.configuration.StringConfiguration
 *  org.infinispan.commons.marshall.Marshaller
 *  org.infinispan.commons.marshall.ProtoStreamMarshaller
 *  org.infinispan.protostream.BaseMarshaller
 *  org.infinispan.protostream.FileDescriptorSource
 *  org.infinispan.protostream.SerializationContext
 *  org.infinispan.protostream.schema.Schema
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.infinispan;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.infinispan.InfinispanMetadataFilterMapper;
import dev.langchain4j.store.embedding.infinispan.InfinispanStoreConfiguration;
import dev.langchain4j.store.embedding.infinispan.LangChainInfinispanItem;
import dev.langchain4j.store.embedding.infinispan.LangChainItemMarshaller;
import dev.langchain4j.store.embedding.infinispan.LangChainMetadata;
import dev.langchain4j.store.embedding.infinispan.LangChainMetadataMarshaller;
import dev.langchain4j.store.embedding.infinispan.LangchainSchemaCreator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.commons.api.query.Query;
import org.infinispan.commons.configuration.BasicConfiguration;
import org.infinispan.commons.configuration.StringConfiguration;
import org.infinispan.commons.marshall.Marshaller;
import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.BaseMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanEmbeddingStore
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(InfinispanEmbeddingStore.class);
    private final RemoteCache<String, LangChainInfinispanItem> remoteCache;
    private final InfinispanStoreConfiguration storeConfiguration;

    public InfinispanEmbeddingStore(RemoteCacheManager remoteCacheManager, InfinispanStoreConfiguration storeConfiguration) {
        ValidationUtils.ensureNotNull((Object)remoteCacheManager, (String)"remoteCacheManager");
        ValidationUtils.ensureNotNull((Object)((Object)storeConfiguration), (String)"storeConfiguration");
        ValidationUtils.ensureNotNull((Object)storeConfiguration.dimension(), (String)"dimension");
        ValidationUtils.ensureNotBlank((String)storeConfiguration.cacheName(), (String)"cacheName");
        this.storeConfiguration = storeConfiguration;
        this.remoteCache = storeConfiguration.createCache() ? remoteCacheManager.administration().getOrCreateCache(storeConfiguration.cacheName(), (BasicConfiguration)new StringConfiguration(this.computeCacheConfiguration(storeConfiguration))) : remoteCacheManager.getCache(storeConfiguration.cacheName());
    }

    public InfinispanEmbeddingStore(ConfigurationBuilder builder, InfinispanStoreConfiguration storeConfiguration) {
        ValidationUtils.ensureNotNull((Object)builder, (String)"builder");
        ValidationUtils.ensureNotNull((Object)((Object)storeConfiguration), (String)"storeConfiguration");
        ValidationUtils.ensureNotBlank((String)storeConfiguration.cacheName(), (String)"cacheName");
        ValidationUtils.ensureNotNull((Object)storeConfiguration.dimension(), (String)"dimension");
        this.storeConfiguration = storeConfiguration;
        Schema schema = LangchainSchemaCreator.buildSchema(storeConfiguration);
        if (storeConfiguration.createCache()) {
            String remoteCacheConfig = this.computeCacheConfiguration(storeConfiguration);
            builder.remoteCache(storeConfiguration.cacheName()).configuration(remoteCacheConfig);
        }
        ProtoStreamMarshaller marshaller = new ProtoStreamMarshaller();
        SerializationContext serializationContext = marshaller.getSerializationContext();
        String schemaContent = schema.toString();
        FileDescriptorSource fileDescriptorSource = FileDescriptorSource.fromString((String)storeConfiguration.fileName(), (String)schemaContent);
        serializationContext.registerProtoFiles(fileDescriptorSource);
        serializationContext.registerMarshaller((BaseMarshaller)new LangChainItemMarshaller(storeConfiguration.langchainItemFullType()));
        serializationContext.registerMarshaller((BaseMarshaller)new LangChainMetadataMarshaller(storeConfiguration.metadataFullType()));
        builder.marshaller((Marshaller)marshaller);
        RemoteCacheManager rmc = new RemoteCacheManager(builder.build());
        if (storeConfiguration.registerSchema()) {
            rmc.administration().schemas().createOrUpdate(schema);
        }
        this.remoteCache = rmc.getCache(storeConfiguration.cacheName());
    }

    public RemoteCache<String, LangChainInfinispanItem> getRemoteCache() {
        return this.remoteCache;
    }

    private String computeCacheConfiguration(InfinispanStoreConfiguration storeConfiguration) {
        String remoteCacheConfig = storeConfiguration.cacheConfig();
        if (remoteCacheConfig == null) {
            remoteCacheConfig = "<distributed-cache name=\"CACHE_NAME\">\n<indexing storage=\"local-heap\">\n<indexed-entities>\n<indexed-entity>LANGCHAINITEM</indexed-entity>\n</indexed-entities>\n</indexing>\n</distributed-cache>".replace("CACHE_NAME", storeConfiguration.cacheName()).replace("LANGCHAINITEM", storeConfiguration.langchainItemFullType()).replace("LANGCHAIN_METADATA", storeConfiguration.metadataFullType());
        }
        return remoteCacheConfig;
    }

    public String add(Embedding embedding) {
        String id = Utils.randomUUID();
        this.add(id, embedding);
        return id;
    }

    public void add(String id, Embedding embedding) {
        this.addInternal(id, embedding, null);
    }

    public String add(Embedding embedding, TextSegment textSegment) {
        String id = Utils.randomUUID();
        this.addInternal(id, embedding, textSegment);
        return id;
    }

    public List<String> addAll(List<Embedding> embeddings) {
        List<String> ids = embeddings.stream().map(ignored -> Utils.randomUUID()).collect(Collectors.toList());
        this.addAll(ids, embeddings, null);
        return ids;
    }

    public void removeAll() {
        this.remoteCache.clear();
    }

    public void removeAll(Filter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("filter cannot be null");
        }
        InfinispanMetadataFilterMapper.FilterResult filterResult = new InfinispanMetadataFilterMapper().map(filter);
        String deleteQuery = "DELETE FROM " + this.storeConfiguration.langchainItemFullType() + " i " + filterResult.join + " where " + filterResult.query;
        Query query = this.remoteCache.query(deleteQuery);
        query.execute();
    }

    public void removeAll(Collection<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ids cannot be null or empty");
        }
        for (String id : ids) {
            this.remoteCache.remove((Object)id);
        }
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        InfinispanMetadataFilterMapper.FilterResult filteringQuery = new InfinispanMetadataFilterMapper().map(request.filter());
        String joinPart = "";
        Object filteringPart = "";
        if (filteringQuery != null) {
            joinPart = filteringQuery.join;
            filteringPart = " filtering(" + filteringQuery.query + ")";
        }
        String vectorQuery = "select i, score(i) from " + this.storeConfiguration.langchainItemFullType() + " i " + joinPart + " where i.embedding <-> " + Arrays.toString(request.queryEmbedding().vector()) + "~" + this.storeConfiguration.distance() + (String)filteringPart;
        Query query = this.remoteCache.query(vectorQuery);
        List hits = query.maxResults(request.maxResults()).list();
        List matches = hits.stream().map(obj -> {
            LangChainInfinispanItem item = (LangChainInfinispanItem)((Object)((Object)obj[0]));
            Float score = (Float)obj[1];
            if (score.doubleValue() < request.minScore()) {
                return null;
            }
            TextSegment embedded = null;
            if (item.text() != null) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                for (LangChainMetadata metadata : item.metadata()) {
                    map.put(metadata.name(), metadata.value());
                }
                embedded = new TextSegment(item.text(), new Metadata(map));
            }
            Embedding embedding = new Embedding(item.embedding());
            return new EmbeddingMatch(Double.valueOf(score.doubleValue()), item.id(), embedding, embedded);
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return new EmbeddingSearchResult(matches);
    }

    private void addInternal(String id, Embedding embedding, TextSegment embedded) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    public void addAll(List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("do not add empty embeddings to infinispan");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        int size = ids.size();
        HashMap<String, LangChainInfinispanItem> elements = new HashMap<String, LangChainInfinispanItem>(size);
        for (int i = 0; i < size; ++i) {
            TextSegment textSegment;
            String id = ids.get(i);
            Embedding embedding = embeddings.get(i);
            TextSegment textSegment2 = textSegment = embedded == null ? null : embedded.get(i);
            if (textSegment != null) {
                Set<LangChainMetadata> metadata = textSegment.metadata().toMap().entrySet().stream().map(e -> new LangChainMetadata((String)e.getKey(), e.getValue())).collect(Collectors.toSet());
                elements.put(id, new LangChainInfinispanItem(id, embedding.vector(), textSegment.text(), metadata, textSegment.metadata().toMap()));
                continue;
            }
            elements.put(id, new LangChainInfinispanItem(id, embedding.vector(), null, null, null));
        }
        this.remoteCache.putAll(elements);
    }

    public static Builder builder() {
        return new Builder();
    }

    public RemoteCache<String, LangChainInfinispanItem> remoteCache() {
        return this.remoteCache;
    }

    public void clearCache() {
        this.remoteCache.clear();
    }

    public static class Builder {
        private ConfigurationBuilder configurationBuilder;
        private String cacheName;
        private Integer dimension;
        private Integer distance;
        private String similarity;
        private String cacheConfig;
        private String packageName;
        private String fileName;
        private String langchainItemName;
        private String metadataItemName;
        private boolean registerSchema = true;
        private boolean createCache = true;

        public Builder cacheName(String name) {
            this.cacheName = name;
            return this;
        }

        public Builder cacheConfig(String cacheConfig) {
            this.cacheConfig = cacheConfig;
            return this;
        }

        public Builder dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder distance(Integer distance) {
            this.distance = distance;
            return this;
        }

        public Builder similarity(String similarity) {
            this.similarity = similarity;
            return this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder langchainItemName(String langchainItemName) {
            this.langchainItemName = langchainItemName;
            return this;
        }

        public Builder metadataItemName(String metadataItemName) {
            this.metadataItemName = metadataItemName;
            return this;
        }

        public Builder registerSchema(boolean registerSchema) {
            this.registerSchema = registerSchema;
            return this;
        }

        public Builder createCache(boolean createCache) {
            this.createCache = createCache;
            return this;
        }

        public Builder infinispanConfigBuilder(ConfigurationBuilder builder) {
            this.configurationBuilder = builder;
            return this;
        }

        public InfinispanEmbeddingStore build() {
            return new InfinispanEmbeddingStore(this.configurationBuilder, new InfinispanStoreConfiguration(this.cacheName, this.dimension, this.distance, this.similarity, this.cacheConfig, this.packageName, this.fileName, this.langchainItemName, this.metadataItemName, this.createCache, this.registerSchema));
        }
    }
}

