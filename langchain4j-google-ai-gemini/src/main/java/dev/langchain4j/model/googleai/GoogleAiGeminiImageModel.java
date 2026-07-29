/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.image.ImageModel
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  org.jspecify.annotations.NonNull
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.Experimental;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.googleai.FinishReasonMapper;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiGenerateContentRequest;
import dev.langchain4j.model.googleai.GeminiGenerateContentResponse;
import dev.langchain4j.model.googleai.GeminiGenerationConfig;
import dev.langchain4j.model.googleai.GeminiResponseModality;
import dev.langchain4j.model.googleai.GeminiRole;
import dev.langchain4j.model.googleai.GeminiSafetySetting;
import dev.langchain4j.model.googleai.GeminiService;
import dev.langchain4j.model.googleai.GoogleAiGeminiTokenUsage;
import dev.langchain4j.model.googleai.GroundingMetadata;
import dev.langchain4j.model.googleai.Json;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

@Experimental
public class GoogleAiGeminiImageModel
implements ImageModel {
    private final String modelName;
    private final GeminiGenerationConfig.GeminiImageConfig imageConfig;
    private final List<GeminiResponseModality> responseModalities;
    private final GeminiService geminiService;
    private final Integer maxRetries;
    private final List<GeminiSafetySetting> safetySettings;
    private final List<GeminiGenerateContentRequest.GeminiTool> tools;

    private GoogleAiGeminiImageModel(GoogleAiGeminiImageModelBuilder builder) {
        this.geminiService = new GeminiService(builder.httpClientBuilder, builder.apiKey, builder.baseUrl, (Boolean)Utils.getOrDefault((Object)builder.logRequestsAndResponses, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false), (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false), builder.logger, builder.timeout, null);
        this.modelName = (String)ValidationUtils.ensureNotNull((Object)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.responseModalities = Collections.singletonList(GeminiResponseModality.IMAGE);
        this.safetySettings = builder.safetySettings;
        this.tools = (Boolean)Utils.getOrDefault((Object)builder.useGoogleSearchGrounding, (Object)false) != false ? Collections.singletonList(new GeminiGenerateContentRequest.GeminiTool(null, null, new GeminiGenerateContentRequest.GeminiTool.GeminiGoogleSearchRetrieval(), null, null)) : null;
        this.imageConfig = builder.aspectRatio != null || builder.imageSize != null ? GeminiGenerationConfig.GeminiImageConfig.builder().aspectRatio(builder.aspectRatio).imageSize(builder.imageSize).build() : null;
    }

    public static GoogleAiGeminiImageModelBuilder builder() {
        return new GoogleAiGeminiImageModelBuilder();
    }

    public String modelName() {
        return this.modelName;
    }

    public Response<@NonNull Image> generate(String prompt) {
        GeminiGenerateContentRequest request = this.createGenerateRequest(prompt);
        GeminiGenerateContentResponse response = (GeminiGenerateContentResponse)RetryUtils.withRetryMappingExceptions(() -> this.geminiService.generateContent(this.modelName, request), (int)this.maxRetries);
        return this.toResponse(response);
    }

    public Response<@NonNull Image> edit(Image image, String prompt) {
        ValidationUtils.ensureNotNull((Object)image, (String)"image");
        ValidationUtils.ensureNotBlank((String)prompt, (String)"prompt");
        GeminiGenerateContentRequest request = this.createEditRequest(prompt, image, null);
        GeminiGenerateContentResponse response = (GeminiGenerateContentResponse)RetryUtils.withRetryMappingExceptions(() -> this.geminiService.generateContent(this.modelName, request), (int)this.maxRetries);
        return this.toResponse(response);
    }

    public Response<@NonNull Image> edit(Image image, Image mask, String prompt) {
        ValidationUtils.ensureNotNull((Object)image, (String)"image");
        ValidationUtils.ensureNotNull((Object)mask, (String)"mask");
        ValidationUtils.ensureNotBlank((String)prompt, (String)"prompt");
        GeminiGenerateContentRequest request = this.createEditRequest(prompt, image, mask);
        GeminiGenerateContentResponse response = (GeminiGenerateContentResponse)RetryUtils.withRetryMappingExceptions(() -> this.geminiService.generateContent(this.modelName, request), (int)this.maxRetries);
        return this.toResponse(response);
    }

    private Response<Image> toResponse(GeminiGenerateContentResponse response) {
        Image image = this.extractImage(response);
        GoogleAiGeminiTokenUsage tokenUsage = null;
        if (response.usageMetadata() != null) {
            tokenUsage = GoogleAiGeminiTokenUsage.builder().inputTokenCount(response.usageMetadata().promptTokenCount()).outputTokenCount(response.usageMetadata().candidatesTokenCount()).totalTokenCount(response.usageMetadata().totalTokenCount()).cachedContentTokenCount(response.usageMetadata().cachedContentTokenCount()).thoughtsTokenCount(response.usageMetadata().thoughtsTokenCount()).build();
        }
        FinishReason finishReason = null;
        if (response.candidates().get(0).finishReason() != null) {
            finishReason = FinishReasonMapper.fromGFinishReasonToFinishReason(response.candidates().get(0).finishReason());
        }
        Map<String, Object> metadata = new HashMap<>();
        GroundingMetadata groundingMetadata = response.groundingMetadata();
        if (groundingMetadata == null && !response.candidates().isEmpty()) {
            groundingMetadata = response.candidates().get(0).groundingMetadata();
        }
        if (groundingMetadata != null) {
            Map<String, Object> groundingMetadataMap = Json.convertValue(groundingMetadata, new TypeReference<Map<String, Object>>(){});
            metadata.put("groundingMetadata", groundingMetadataMap);
        }
        return Response.from(image, (TokenUsage)tokenUsage, (FinishReason)finishReason, metadata);
    }

    private GeminiGenerateContentRequest createGenerateRequest(String prompt) {
        GeminiContent content = new GeminiContent(Collections.singletonList(GeminiContent.GeminiPart.ofText(prompt)), GeminiRole.USER.toString());
        return this.createGenerateContentRequest(content);
    }

    private GeminiGenerateContentRequest createEditRequest(String prompt, Image image, Image mask) {
        ArrayList<GeminiContent.GeminiPart> parts = new ArrayList<GeminiContent.GeminiPart>();
        parts.add(GeminiContent.GeminiPart.ofText(prompt));
        parts.add(this.createImagePart(image));
        if (mask != null) {
            parts.add(this.createImagePart(mask));
        }
        GeminiContent content = new GeminiContent(parts, GeminiRole.USER.toString());
        return this.createGenerateContentRequest(content);
    }

    private GeminiGenerateContentRequest createGenerateContentRequest(GeminiContent content) {
        return GeminiGenerateContentRequest.builder().contents(Collections.singletonList(content)).generationConfig(this.createGenerationConfig()).safetySettings(this.safetySettings).tools(this.tools).build();
    }

    private GeminiContent.GeminiPart createImagePart(Image image) {
        String base64Data = image.base64Data();
        String mimeType = image.mimeType();
        if (mimeType == null || mimeType.trim().isEmpty()) {
            mimeType = "image/png";
        }
        if (base64Data == null && image.url() != null) {
            return GeminiContent.GeminiPart.builder().fileData(new GeminiContent.GeminiPart.GeminiFileData(mimeType, image.url().toString())).build();
        }
        return GeminiContent.GeminiPart.builder().inlineData(new GeminiContent.GeminiPart.GeminiBlob(mimeType, ValidationUtils.ensureNotBlank((String)base64Data, (String)"image.base64Data"))).build();
    }

    private GeminiGenerationConfig createGenerationConfig() {
        return GeminiGenerationConfig.builder().responseModalities(this.responseModalities).imageConfig(this.imageConfig).build();
    }

    private Image extractImage(GeminiGenerateContentResponse response) {
        if (response.candidates() == null || response.candidates().isEmpty()) {
            throw new GeminiImageGenerationException("No image generated in response");
        }
        GeminiGenerateContentResponse.GeminiCandidate candidate = response.candidates().get(0);
        if (candidate.content() == null || candidate.content().parts() == null) {
            throw new GeminiImageGenerationException("No content in response candidate");
        }
        for (GeminiContent.GeminiPart part : candidate.content().parts()) {
            if (part.inlineData() == null) continue;
            return Image.builder().base64Data(part.inlineData().data()).mimeType(part.inlineData().mimeType()).build();
        }
        throw new GeminiImageGenerationException("No image data found in response");
    }

    public static class GeminiImageGenerationException
    extends RuntimeException {
        public GeminiImageGenerationException(String message) {
            super(message);
        }
    }

    public static class GoogleAiGeminiImageModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String apiKey;
        private String baseUrl;
        private String modelName;
        private String aspectRatio;
        private String imageSize;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequestsAndResponses;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private List<GeminiSafetySetting> safetySettings;
        private Boolean useGoogleSearchGrounding;

        private GoogleAiGeminiImageModelBuilder() {
        }

        public GoogleAiGeminiImageModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder imageSize(String imageSize) {
            this.imageSize = imageSize;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder safetySettings(List<GeminiSafetySetting> safetySettings) {
            this.safetySettings = safetySettings;
            return this;
        }

        public GoogleAiGeminiImageModelBuilder useGoogleSearchGrounding(Boolean useGoogleSearchGrounding) {
            this.useGoogleSearchGrounding = useGoogleSearchGrounding;
            return this;
        }

        public GoogleAiGeminiImageModel build() {
            return new GoogleAiGeminiImageModel(this);
        }
    }
}

