package dev.langchain4j.store.embedding.infinispan;

/**
 * Langchain Metadata item that is serialized for the langchain integration use case
 * @param name, the name of the metadata
 * @param value, the value of the metadata
 */
public class LangChainMetadata {
    private final String name;
    private final Object value;

    public LangChainMetadata(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LangChainMetadata that = (LangChainMetadata) o;
        return java.util.Objects.equals(this.name, that.name) && java.util.Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "LangChainMetadata{"name=" + name + , "value=" + value + "}"";
    }

}
