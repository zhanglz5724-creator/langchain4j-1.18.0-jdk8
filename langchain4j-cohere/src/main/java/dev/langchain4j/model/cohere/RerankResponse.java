/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.cohere;

import dev.langchain4j.model.cohere.Meta;
import dev.langchain4j.model.cohere.Result;
import java.util.List;

class RerankResponse {
    private List<Result> results;
    private Meta meta;

    RerankResponse() {
    }

    public List<Result> getResults() {
        return this.results;
    }

    public Meta getMeta() {
        return this.meta;
    }
}

