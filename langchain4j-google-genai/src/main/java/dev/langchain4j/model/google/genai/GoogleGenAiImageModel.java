/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.types.Blob
 *  com.google.genai.types.Candidate
 *  com.google.genai.types.Content
 *  com.google.genai.types.GenerateContentConfig
 *  com.google.genai.types.GenerateContentConfig$Builder
 *  com.google.genai.types.GenerateContentResponse
 *  com.google.genai.types.GoogleSearch
 *  com.google.genai.types.GroundingMetadata
 *  com.google.genai.types.ImageConfig
 *  com.google.genai.types.ImageConfig$Builder
 *  com.google.genai.types.Part
 *  com.google.genai.types.SafetySetting
 *  com.google.genai.types.Tool
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.internal.ExceptionMapper
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.image.ImageModel
 *  dev.langchain4j.model.output.Response
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.google.genai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.Blob;
import com.google.genai.types.Candidate;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GoogleSearch;
import com.google.genai.types.GroundingMetadata;
import com.google.genai.types.ImageConfig;
import com.google.genai.types.Part;
import com.google.genai.types.SafetySetting;
import com.google.genai.types.Tool;
import dev.langchain4j.Experimental;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.internal.ExceptionMapper;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.google.genai.GoogleGenAiClientFactory;
import dev.langchain4j.model.google.genai.GoogleGenAiExceptionMapper;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Experimental
public class GoogleGenAiImageModel
implements ImageModel {
    private static final Logger log = LoggerFactory.getLogger(GoogleGenAiImageModel.class);
    private final Client client;
    private final String modelName;
    private final Integer maxRetries;
    private final List<SafetySetting> safetySettings;
    private final boolean useGoogleSearchGrounding;
    private final String aspectRatio;
    private final String imageSize;
    private final String personGeneration;
    private final Map<String, String> labels;
    private final boolean logRequests;
    private final boolean logResponses;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private GoogleGenAiImageModel(Builder builder) {
        this.client = builder.client != null ? builder.client : GoogleGenAiClientFactory.createClient(builder.apiKey, builder.googleCredentials, builder.projectId, builder.location, builder.timeout, builder.customHeaders, builder.apiEndpoint);
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)3);
        this.safetySettings = Utils.copy((List)builder.safetySettings);
        this.useGoogleSearchGrounding = (Boolean)Utils.getOrDefault((Object)builder.useGoogleSearchGrounding, (Object)false);
        this.aspectRatio = builder.aspectRatio;
        this.imageSize = builder.imageSize;
        this.personGeneration = builder.personGeneration;
        this.labels = builder.labels != null ? new HashMap(builder.labels) : null;
        this.logRequests = (Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Response<Image> generate(String prompt) {
        ValidationUtils.ensureNotBlank((String)prompt, (String)"prompt");
        Content content = Content.builder().parts(Collections.singletonList(Part.fromText((String)prompt))).build();
        return this.generateImageResponse(Collections.singletonList(content));
    }

    public Response<Image> edit(Image image, String prompt) {
        ValidationUtils.ensureNotNull((Object)image, (String)"image");
        ValidationUtils.ensureNotBlank((String)prompt, (String)"prompt");
        ArrayList<Part> parts = new ArrayList<Part>();
        parts.add(Part.fromText((String)prompt));
        parts.add(this.createImagePart(image));
        Content content = Content.builder().parts(parts).build();
        return this.generateImageResponse(Collections.singletonList(content));
    }

    public Response<Image> edit(Image image, Image mask, String prompt) {
        ValidationUtils.ensureNotNull((Object)image, (String)"image");
        ValidationUtils.ensureNotNull((Object)mask, (String)"mask");
        ValidationUtils.ensureNotBlank((String)prompt, (String)"prompt");
        ArrayList<Part> parts = new ArrayList<Part>();
        parts.add(Part.fromText((String)prompt));
        parts.add(this.createImagePart(image));
        parts.add(this.createImagePart(mask));
        Content content = Content.builder().parts(parts).build();
        return this.generateImageResponse(Collections.singletonList(content));
    }

    private Response<Image> generateImageResponse(List<Content> contents) {
        GenerateContentConfig config = this.createGenerateContentConfig();
        if (this.logRequests) {
            log.info("Request:\n- model: {}\n- contents: {}\n- config: {}", new Object[]{this.modelName, contents, config});
        }
        GenerateContentResponse response = (GenerateContentResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.models.generateContent(this.modelName, contents, config), (int)this.maxRetries, (ExceptionMapper)GoogleGenAiExceptionMapper.INSTANCE);
        Response<Image> imageResponse = this.toResponse(response);
        if (this.logResponses) {
            log.info("Response:\n- model: {}\n- response: {}", (Object)this.modelName, imageResponse);
        }
        return imageResponse;
    }

    private GenerateContentConfig createGenerateContentConfig() {
        GenerateContentConfig.Builder configBuilder = GenerateContentConfig.builder().responseModalities(Collections.singletonList("IMAGE"));
        if (!this.safetySettings.isEmpty()) {
            configBuilder.safetySettings(this.safetySettings);
        }
        if (this.useGoogleSearchGrounding) {
            configBuilder.tools(Collections.singletonList(Tool.builder().googleSearch(GoogleSearch.builder().build()).build()));
        }
        if (this.aspectRatio != null || this.imageSize != null || this.personGeneration != null) {
            ImageConfig.Builder imageConfigBuilder = ImageConfig.builder();
            if (this.aspectRatio != null) {
                imageConfigBuilder.aspectRatio(this.aspectRatio);
            }
            if (this.imageSize != null) {
                imageConfigBuilder.imageSize(this.imageSize);
            }
            if (this.personGeneration != null) {
                imageConfigBuilder.personGeneration(this.personGeneration);
            }
            configBuilder.imageConfig(imageConfigBuilder.build());
        }
        if (this.labels != null && !this.labels.isEmpty()) {
            configBuilder.labels(this.labels);
        }
        return configBuilder.build();
    }

    private Part createImagePart(Image image) {
        String base64Data = image.base64Data();
        String mimeType = image.mimeType();
        if (mimeType == null || mimeType.trim().isEmpty()) {
            mimeType = "image/png";
        }
        if (base64Data == null && image.url() != null) {
            return Part.fromUri((String)image.url().toString(), (String)mimeType);
        }
        byte[] imageBytes = Base64.getDecoder().decode(ValidationUtils.ensureNotBlank((String)base64Data, (String)"image.base64Data"));
        return Part.fromBytes((byte[])imageBytes, (String)mimeType);
    }

    private Response<Image> toResponse(GenerateContentResponse response) {
        Candidate candidate;
        if (response.parts() == null || response.parts().isEmpty()) {
            throw new RuntimeException("No image generated in response");
        }
        HashMap<String, Map> metadata = new HashMap<String, Map>();
        if (response.candidates().isPresent() && !((List)response.candidates().get()).isEmpty() && (candidate = (Candidate)((List)response.candidates().get()).get(0)).groundingMetadata().isPresent()) {
            GroundingMetadata gm = (GroundingMetadata)candidate.groundingMetadata().get();
            try {
                Map groundingMap = (Map)OBJECT_MAPPER.readValue(gm.toJson(), (TypeReference)new TypeReference<Map<String, Object>>(){});
                metadata.put("groundingMetadata", groundingMap);
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to parse grounding metadata", e);
            }
        }
        for (Part part : response.parts()) {
            Blob blob;
            if (!part.inlineData().isPresent() || !(blob = (Blob)part.inlineData().get()).data().isPresent()) continue;
            byte[] bytes = (byte[])blob.data().get();
            String base64Data = Base64.getEncoder().encodeToString(bytes);
            String mimeType = blob.mimeType().orElse("image/png");
            Image image = Image.builder().base64Data(base64Data).mimeType(mimeType).build();
            return Response.from(image, null, null, metadata);
        }
        throw new RuntimeException("No image data found in response");
    }

    public static class Builder {
        private Client client;
        private String apiKey;
        private GoogleCredentials googleCredentials;
        private String projectId;
        private String location;
        private Duration timeout;
        private String modelName;
        private Integer maxRetries;
        private List<SafetySetting> safetySettings;
        private Boolean useGoogleSearchGrounding;
        private String aspectRatio;
        private String imageSize;
        private String personGeneration;
        private Boolean logRequests;
        private Boolean logResponses;
        private String apiEndpoint;
        private Map<String, String> customHeaders;
        private Map<String, String> labels;

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder googleCredentials(GoogleCredentials googleCredentials) {
            this.googleCredentials = googleCredentials;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder safetySettings(List<SafetySetting> safetySettings) {
            this.safetySettings = safetySettings;
            return this;
        }

        public Builder useGoogleSearchGrounding(Boolean useGoogleSearchGrounding) {
            this.useGoogleSearchGrounding = useGoogleSearchGrounding;
            return this;
        }

        public Builder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public Builder imageSize(String imageSize) {
            this.imageSize = imageSize;
            return this;
        }

        public Builder personGeneration(String personGeneration) {
            this.personGeneration = personGeneration;
            return this;
        }

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder logRequestsAndResponses(Boolean logRequestsAndResponses) {
            this.logRequests = logRequestsAndResponses;
            this.logResponses = logRequestsAndResponses;
            return this;
        }

        public Builder apiEndpoint(String apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder customHeaders(Map<String, String> customHeaders) {
            this.customHeaders = customHeaders;
            return this;
        }

        public Builder labels(Map<String, String> labels) {
            this.labels = labels;
            return this;
        }

        public GoogleGenAiImageModel build() {
            return new GoogleGenAiImageModel(this);
        }
    }
}

