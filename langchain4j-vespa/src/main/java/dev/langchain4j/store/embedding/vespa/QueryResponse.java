/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 */
package dev.langchain4j.store.embedding.vespa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.langchain4j.store.embedding.vespa.Record;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
record QueryResponse(RootNode root) {

    @JsonIgnoreProperties(ignoreUnknown=true)
    record RootNode(List<Record> children) {
    }
}

