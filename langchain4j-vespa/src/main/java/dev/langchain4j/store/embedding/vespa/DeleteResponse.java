package dev.langchain4j.store.embedding.vespa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties(ignoreUnknown = true) public class DeleteResponse {
    private final String pathId;
    private final Long documentCount;

    public DeleteResponse(String pathId, Long documentCount) {
        this.pathId = pathId;
        this.documentCount = documentCount;
    }

    public String getPathId() {
        return pathId;
    }

    public Long getDocumentCount() {
        return documentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteResponse that = (DeleteResponse) o;
        return java.util.Objects.equals(this.pathId, that.pathId) && java.util.Objects.equals(this.documentCount, that.documentCount);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(pathId, documentCount);
    }

    @Override
    public String toString() {
        return "DeleteResponse{"pathId=" + pathId + , "documentCount=" + documentCount + "}"";
    }

}
