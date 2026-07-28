/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.rerank.RerankParameters
 *  com.ibm.watsonx.ai.rerank.RerankResponse
 *  com.ibm.watsonx.ai.rerank.RerankResponse$RerankResult
 *  com.ibm.watsonx.ai.rerank.RerankService
 *  com.ibm.watsonx.ai.rerank.RerankService$Builder
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.model.scoring.ScoringModel
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.rerank.RerankParameters;
import com.ibm.watsonx.ai.rerank.RerankResponse;
import com.ibm.watsonx.ai.rerank.RerankService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.model.watsonx.WatsonxBuilder;
import dev.langchain4j.model.watsonx.WatsonxExceptionMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WatsonxScoringModel
implements ScoringModel {
    private final RerankService rerankService;

    private WatsonxScoringModel(Builder builder) {
        RerankService.Builder rerankServiceBuilder = Objects.nonNull(builder.authenticator) ? (RerankService.Builder)RerankService.builder().authenticator(builder.authenticator) : (RerankService.Builder)RerankService.builder().apiKey(builder.apiKey);
        this.rerankService = ((RerankService.Builder)((RerankService.Builder)((RerankService.Builder)((RerankService.Builder)((RerankService.Builder)((RerankService.Builder)((RerankService.Builder)((RerankService.Builder)((RerankService.Builder)((RerankService.Builder)rerankServiceBuilder.baseUrl(builder.baseUrl)).modelId(builder.modelName)).version(builder.version)).projectId(builder.projectId)).spaceId(builder.spaceId)).timeout(builder.timeout)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).httpClient(builder.httpClient)).verifySsl(builder.verifySsl)).build();
    }

    public Response<List<Double>> scoreAll(List<TextSegment> segments, String query) {
        return this.scoreAll(segments, query, null);
    }

    public Response<List<Double>> scoreAll(List<TextSegment> segments, String query, RerankParameters parameters) {
        if (Objects.isNull(segments) || segments.isEmpty()) {
            return Response.from((Object)List.of());
        }
        if (Objects.isNull(query) || query.isBlank()) {
            return Response.from((Object)List.of());
        }
        List inputs = segments.stream().map(TextSegment::text).toList();
        RerankResponse response = (RerankResponse)WatsonxExceptionMapper.INSTANCE.withExceptionMapper(() -> this.rerankService.rerank(query, inputs, parameters));
        Double[] content = new Double[response.results().size()];
        for (RerankResponse.RerankResult rerankResult : response.results()) {
            content[rerankResult.index()] = rerankResult.score();
        }
        return Response.from(Arrays.asList(content), (TokenUsage)new TokenUsage(Integer.valueOf(response.inputTokenCount())));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends WatsonxBuilder<Builder> {
        private String modelName;

        private Builder() {
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public WatsonxScoringModel build() {
            return new WatsonxScoringModel(this);
        }
    }
}

