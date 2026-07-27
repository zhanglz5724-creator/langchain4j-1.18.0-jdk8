/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.scoring;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.output.Response;
import java.util.Collections;
import java.util.List;

public interface ScoringModel {
    default public Response<Double> score(String text, String query) {
        return this.score(TextSegment.from(text), query);
    }

    default public Response<Double> score(TextSegment segment, String query) {
        Response<List<Double>> response = this.scoreAll(Collections.singletonList(segment), query);
        ValidationUtils.ensureEq(response.content().size(), 1, "Expected a single score, but received %d", response.content().size());
        return Response.from(response.content().get(0), response.tokenUsage(), response.finishReason());
    }

    public Response<List<Double>> scoreAll(List<TextSegment> var1, String var2);
}

