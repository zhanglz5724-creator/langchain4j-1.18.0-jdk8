import java.util.Arrays;

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.embedding.EmbeddingParameters
 *  com.ibm.watsonx.ai.embedding.EmbeddingResponse
 *  com.ibm.watsonx.ai.embedding.EmbeddingResponse$Result
 *  com.ibm.watsonx.ai.embedding.EmbeddingService
 *  com.ibm.watsonx.ai.embedding.EmbeddingService$Builder
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.model.output.Response
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.embedding.EmbeddingParameters;
import com.ibm.watsonx.ai.embedding.EmbeddingResponse;
import com.ibm.watsonx.ai.embedding.EmbeddingService;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.watsonx.WatsonxBuilder;
import dev.langchain4j.model.watsonx.WatsonxExceptionMapper;
import java.util.List;
import java.util.Objects;

public class WatsonxEmbeddingModel
implements EmbeddingModel {
    private final EmbeddingService embeddingService;
    private final String modelName;

    private WatsonxEmbeddingModel(Builder builder) {
        EmbeddingService.Builder embeddingServiceBuilder = Objects.nonNull(builder.authenticator) ? (EmbeddingService.Builder)EmbeddingService.builder().authenticator(builder.authenticator) : (EmbeddingService.Builder)EmbeddingService.builder().apiKey(builder.apiKey);
        this.embeddingService = ((EmbeddingService.Builder)((EmbeddingService.Builder)((EmbeddingService.Builder)((EmbeddingService.Builder)((EmbeddingService.Builder)((EmbeddingService.Builder)((EmbeddingService.Builder)((EmbeddingService.Builder)((EmbeddingService.Builder)((EmbeddingService.Builder)embeddingServiceBuilder.baseUrl(builder.baseUrl)).modelId(builder.modelName)).version(builder.version)).projectId(builder.projectId)).spaceId(builder.spaceId)).timeout(builder.timeout)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).httpClient(builder.httpClient)).verifySsl(builder.verifySsl)).build();
        this.modelName = builder.modelName;
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments) {
        return this.embedAll(textSegments, null);
    }

    public String modelName() {
        return this.modelName;
    }

    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments, EmbeddingParameters parameters) {
        if (Objects.isNull(textSegments) || textSegments.isEmpty()) {
            return Response.from((Object)Arrays.asList());
        }
        List inputs = textSegments.stream().map(TextSegment::text).toList();
        EmbeddingResponse response = (EmbeddingResponse)WatsonxExceptionMapper.INSTANCE.withExceptionMapper(() -> this.embeddingService.embedding(inputs, parameters));
        return Response.from((Object)response.results().stream().map(EmbeddingResponse.Result::embedding).map(Embedding::from).toList());
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

        public WatsonxEmbeddingModel build() {
            return new WatsonxEmbeddingModel(this);
        }
    }
}

