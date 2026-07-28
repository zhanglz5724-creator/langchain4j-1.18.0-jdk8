/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ContentType
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.request.EmbeddingInput
 *  dev.langchain4j.model.embedding.request.EmbeddingRequest
 *  dev.langchain4j.model.embedding.response.EmbeddingResponse
 *  dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.bedrock.AbstractBedrockEmbeddingModel;
import dev.langchain4j.model.bedrock.BedrockExceptionMapper;
import dev.langchain4j.model.bedrock.BedrockTitanEmbeddingResponse;
import dev.langchain4j.model.bedrock.Json;
import dev.langchain4j.model.embedding.request.EmbeddingInput;
import dev.langchain4j.model.embedding.request.EmbeddingRequest;
import dev.langchain4j.model.embedding.response.EmbeddingResponse;
import dev.langchain4j.model.embedding.response.EmbeddingResponseMetadata;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

public class BedrockTitanEmbeddingModel
extends AbstractBedrockEmbeddingModel<BedrockTitanEmbeddingResponse> {
    private final String model;
    private final Integer dimensions;
    private final Boolean normalize;

    protected BedrockTitanEmbeddingModel(BedrockTitanEmbeddingModelBuilder<?, ?> builder) {
        super(builder);
        this.model = ValidationUtils.ensureNotBlank((String)((BedrockTitanEmbeddingModelBuilder)builder).model, (String)"model");
        this.dimensions = ((BedrockTitanEmbeddingModelBuilder)builder).dimensions;
        this.normalize = ((BedrockTitanEmbeddingModelBuilder)builder).normalize;
    }

    @Override
    protected String getModelId() {
        return this.model;
    }

    protected Integer knownDimension() {
        return this.dimensions;
    }

    @Override
    protected List<Map<String, Object>> getRequestParameters(List<TextSegment> textSegments) {
        if (this.isMultimodal()) {
            ArrayList<Map<String, Object>> bodies = new ArrayList<Map<String, Object>>();
            for (TextSegment textSegment : textSegments) {
                HashMap<String, Object> body = new HashMap<String, Object>();
                body.put("inputText", textSegment.text());
                if (this.dimensions != null) {
                    body.put("embeddingConfig", BedrockTitanEmbeddingModel.of("outputEmbeddingLength", this.dimensions));
                }
                bodies.add(body);
            }
            return bodies;
        }
        if (Types.TitanEmbedTextV1.getValue().equals(this.model)) {
            if (this.dimensions != null || this.normalize != null) {
                throw new IllegalArgumentException("Dimensions and normalize are not supported for Titan Embedding model V1");
            }
            return textSegments.stream().map(TextSegment::text).map(text -> BedrockTitanEmbeddingModel.of("inputText", text)).collect(Collectors.toList());
        }
        ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (TextSegment textSegment : textSegments) {
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("inputText", textSegment.text());
            parameters.put("dimensions", this.dimensions);
            parameters.put("normalize", this.normalize);
            result.add(parameters);
        }
        return result;
    }

    @Override
    protected Class<BedrockTitanEmbeddingResponse> getResponseClassType() {
        return BedrockTitanEmbeddingResponse.class;
    }

    public Set<ContentType> supportedContentTypes() {
        return this.isMultimodal() ? new HashSet<ContentType>(Arrays.asList(ContentType.TEXT, ContentType.IMAGE)) : Collections.singleton(ContentType.TEXT);
    }

    public EmbeddingResponse doEmbed(EmbeddingRequest request) {
        if (!this.isMultimodal()) {
            Response<List<Embedding>> legacy = this.doEmbedAll(request.inputs().stream().map(input -> TextSegment.from((String)input.text())).collect(Collectors.toList()));
            return EmbeddingResponse.builder().embeddings((List)legacy.content()).metadata(EmbeddingResponseMetadata.builder().modelName(this.model).tokenUsage(legacy.tokenUsage()).build()).build();
        }
        ArrayList<Embedding> embeddings = new ArrayList<Embedding>();
        int totalTokens = 0;
        for (EmbeddingInput input2 : request.inputs()) {
            String imageBase64;
            String text = input2.text();
            HashMap<String, Object> body = new HashMap<String, Object>();
            if (!text.isEmpty()) {
                body.put("inputText", text);
            }
            if ((imageBase64 = this.extractImageBase64(input2)) != null) {
                body.put("inputImage", imageBase64);
            }
            if (this.dimensions != null) {
                body.put("embeddingConfig", BedrockTitanEmbeddingModel.of("outputEmbeddingLength", this.dimensions));
            }
            BedrockTitanEmbeddingResponse response = Json.fromJson(((InvokeModelResponse)RetryUtils.withRetryMappingExceptions(() -> this.invoke(Json.toJson(body)), (int)this.getMaxRetries(), (ExceptionMapper)BedrockExceptionMapper.INSTANCE)).body().asUtf8String(), BedrockTitanEmbeddingResponse.class);
            embeddings.add(response.toEmbedding());
            totalTokens += response.getInputTextTokenCount();
        }
        return EmbeddingResponse.builder().embeddings(embeddings).metadata(EmbeddingResponseMetadata.builder().modelName(this.model).tokenUsage(new TokenUsage(Integer.valueOf(totalTokens))).build()).build();
    }

    private boolean isMultimodal() {
        return Types.TitanEmbedImageV1.getValue().equals(this.model) || this.model.contains("embed-image");
    }

    private String extractImageBase64(EmbeddingInput input) {
        String base64 = null;
        for (Content content : input.contents()) {
            if (!(content instanceof ImageContent)) continue;
            ImageContent imageContent = (ImageContent)content;
            if (base64 != null) {
                throw new UnsupportedFeatureException("Amazon Titan supports at most one image per input");
            }
            if (imageContent.image().base64Data() == null) {
                throw new UnsupportedFeatureException("Amazon Titan requires base64 image data (a URL is not supported)");
            }
            base64 = imageContent.image().base64Data();
        }
        return base64;
    }

    public String getModel() {
        return this.model;
    }

    public Integer getDimensions() {
        return this.dimensions;
    }

    public Boolean getNormalize() {
        return this.normalize;
    }

    public static BedrockTitanEmbeddingModelBuilder<?, ?> builder() {
        return new BedrockTitanEmbeddingModelBuilderImpl();
    }

    private static final class BedrockTitanEmbeddingModelBuilderImpl
    extends BedrockTitanEmbeddingModelBuilder<BedrockTitanEmbeddingModel, BedrockTitanEmbeddingModelBuilderImpl> {
        private BedrockTitanEmbeddingModelBuilderImpl() {
        }

        @Override
        protected BedrockTitanEmbeddingModelBuilderImpl self() {
            return this;
        }

        @Override
        public BedrockTitanEmbeddingModel build() {
            return new BedrockTitanEmbeddingModel(this);
        }
    }

    public static abstract class BedrockTitanEmbeddingModelBuilder<C extends BedrockTitanEmbeddingModel, B extends BedrockTitanEmbeddingModelBuilder<C, B>>
    extends AbstractBedrockEmbeddingModel.AbstractBedrockEmbeddingModelBuilder<BedrockTitanEmbeddingResponse, C, B> {
        private String model;
        private Integer dimensions;
        private Boolean normalize;

        public B model(String model) {
            this.model = model;
            return (B)this.self();
        }

        public B dimensions(Integer dimensions) {
            this.dimensions = dimensions;
            return (B)this.self();
        }

        public B normalize(Boolean normalize) {
            this.normalize = normalize;
            return (B)this.self();
        }

        @Override
        protected abstract B self();

        @Override
        public abstract C build();
    }

    public static enum Types {
        TitanEmbedTextV1("amazon.titan-embed-text-v1"),
        TitanEmbedTextV2("amazon.titan-embed-text-v2:0"),
        TitanEmbedImageV1("amazon.titan-embed-image-v1");

        private final String value;

        private Types(String modelID) {
            this.value = modelID;
        }

        public String getValue() {
            return this.value;
        }
    }
}

