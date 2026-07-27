package dev.langchain4j.store.embedding.vespa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties(ignoreUnknown = true) class QueryResponse {
    private final RootNode root;

    public QueryResponse(RootNode root) {
        this.root = root;
    }

    public RootNode getRoot() {
        return root;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryResponse that = (QueryResponse) o;
        return java.util.Objects.equals(this.root, that.root);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(root);
    }

    @Override
    public String toString() {
        return "QueryResponse{"root=" + root + "}"";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record RootNode(List<Record> children) {}
}
