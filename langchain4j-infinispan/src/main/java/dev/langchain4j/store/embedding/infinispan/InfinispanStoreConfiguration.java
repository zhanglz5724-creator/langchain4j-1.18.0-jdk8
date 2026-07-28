/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.store.embedding.infinispan;

public record InfinispanStoreConfiguration(String cacheName, Integer dimension, Integer distance, String similarity, String cacheConfig, String packageItem, String fileName, String langchainItemName, String metadataItemName, boolean createCache, boolean registerSchema) {
    public static final String DEFAULT_CACHE_CONFIG = "<distributed-cache name=\"CACHE_NAME\">\n<indexing storage=\"local-heap\">\n<indexed-entities>\n<indexed-entity>LANGCHAINITEM</indexed-entity>\n</indexed-entities>\n</indexing>\n</distributed-cache>";
    public static final String DEFAULT_ITEM_PACKAGE = "dev.langchain4j";
    public static final String DEFAULT_LANGCHAIN_ITEM = "LangChainItem";
    public static final String DEFAULT_METADATA_ITEM = "LangChainMetadata";
    public static final int DEFAULT_DISTANCE = 3;
    public static final String DEFAULT_SIMILARITY = "COSINE";

    public InfinispanStoreConfiguration(String cacheName, Integer dimension, Integer distance, String similarity, String cacheConfig, String packageItem, String fileName, String langchainItemName, String metadataItemName, boolean createCache, boolean registerSchema) {
        this.cacheName = cacheName;
        this.dimension = dimension;
        this.cacheConfig = cacheConfig;
        this.distance = distance != null ? distance : 3;
        this.similarity = similarity != null ? similarity : DEFAULT_SIMILARITY;
        this.packageItem = packageItem != null ? packageItem : DEFAULT_ITEM_PACKAGE;
        this.fileName = fileName != null ? fileName : InfinispanStoreConfiguration.computeFileName(this.packageItem, dimension);
        this.langchainItemName = langchainItemName != null ? langchainItemName : DEFAULT_LANGCHAIN_ITEM + dimension;
        this.metadataItemName = metadataItemName != null ? metadataItemName : DEFAULT_METADATA_ITEM + dimension;
        this.createCache = createCache;
        this.registerSchema = registerSchema;
    }

    public String langchainItemFullType() {
        return this.packageItem + "." + this.langchainItemName;
    }

    public String metadataFullType() {
        return this.packageItem + "." + this.metadataItemName;
    }

    private static String computeFileName(String itemPackage, int dimension) {
        return itemPackage + ".dimension." + dimension + ".proto";
    }
}

