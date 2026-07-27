package dev.langchain4j.store.embedding.infinispan;

import java.util.Map;
import java.util.Set;

/**
 * Langchain item that is serialized for the langchain integration use case
 *
 * @param id, the id of the item
 * @param embedding, the vector
 * @param text, associated text
 * @param metadata, additional set of metadata
 */
public class LangChainInfinispanItem {
    private final String id;
    private final float[] embedding;
    private final String text;
    private final Set<LangChainMetadata> metadata;
    private final Map<String, Object> metadataMap;

    public LangChainInfinispanItem(String id, float[] embedding, String text, Set<LangChainMetadata> metadata, Map<String, Object> metadataMap) {
        this.id = id;
        this.embedding = embedding;
        this.text = text;
        this.metadata = metadata;
        this.metadataMap = metadataMap;
    }

    public String getId() {
        return id;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public String getText() {
        return text;
    }

    public Set<LangChainMetadata> getMetadata() {
        return metadata;
    }

    public Map<String, Object> getMetadataMap() {
        return metadataMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LangChainInfinispanItem that = (LangChainInfinispanItem) o;
        return java.util.Objects.equals(this.id, that.id) && java.util.Objects.equals(this.embedding, that.embedding) && java.util.Objects.equals(this.text, that.text) && java.util.Objects.equals(this.metadata, that.metadata) && java.util.Objects.equals(this.metadataMap, that.metadataMap);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, embedding, text, metadata, metadataMap);
    }

    @Override
    public String toString() {
        return "LangChainInfinispanItem{"id=" + id + , "embedding=" + embedding + , "text=" + text + , "metadata=" + metadata + , "metadataMap=" + metadataMap + "}"";
    }

}
