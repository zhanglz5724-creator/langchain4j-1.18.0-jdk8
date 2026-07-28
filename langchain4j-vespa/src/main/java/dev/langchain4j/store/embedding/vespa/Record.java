/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.store.embedding.vespa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public record Record(String id, Double relevance, Fields fields) {

    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Fields(String documentid, String textSegment, Vector vector) {

        @JsonIgnoreProperties(ignoreUnknown=true)
        public record Vector(List<Float> values) {
        }
    }
}

