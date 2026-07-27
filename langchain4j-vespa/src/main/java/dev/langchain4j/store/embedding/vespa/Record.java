package dev.langchain4j.store.embedding.vespa;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true) public class Record {
    private final String id;
    private final Double relevance;
    private final Fields fields;

    public Record(String id, Double relevance, Fields fields) {
        this.id = id;
        this.relevance = relevance;
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public Double getRelevance() {
        return relevance;
    }

    public Fields getFields() {
        return fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record that = (Record) o;
        return java.util.Objects.equals(this.id, that.id) && java.util.Objects.equals(this.relevance, that.relevance) && java.util.Objects.equals(this.fields, that.fields);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, relevance, fields);
    }

    @Override
    public String toString() {
        return "Record{"id=" + id + , "relevance=" + relevance + , "fields=" + fields + "}"";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(NON_NULL)
    @JsonNaming(SnakeCaseStrategy.class)
    public record Fields(String documentid, String textSegment, Vector vector) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Vector(List<Float> values) {}
    }
}
