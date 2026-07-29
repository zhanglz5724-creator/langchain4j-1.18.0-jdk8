/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.aiplatform.v1.EndpointName
 *  com.google.cloud.aiplatform.v1.PredictResponse
 *  com.google.cloud.aiplatform.v1.PredictionServiceClient
 *  com.google.cloud.aiplatform.v1.PredictionServiceSettings
 *  com.google.cloud.aiplatform.v1.PredictionServiceSettings$Builder
 *  com.google.protobuf.InvalidProtocolBufferException
 *  com.google.protobuf.Message$Builder
 *  com.google.protobuf.Value
 *  com.google.protobuf.Value$Builder
 *  com.google.protobuf.util.JsonFormat
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.image.ImageModel
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.vertexai;

import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictResponse;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1.PredictionServiceSettings;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.vertexai.Json;
import dev.langchain4j.model.vertexai.spi.VertexAiImageModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexAiImageModel
implements ImageModel {
    private final Long seed;
    private final String endpoint;
    private final MimeType mimeType;
    private final Integer compressionQuality;
    private final String cloudStorageBucket;
    private final EndpointName endpointName;
    private final String language;
    private final Integer guidanceScale;
    private final String negativePrompt;
    private final ImageStyle sampleImageStyle;
    private final Integer sampleImageSize;
    private final AspectRatio aspectRatio;
    private final PersonGeneration personGeneration;
    private final Boolean addWatermark;
    private final int maxRetries;
    private final Boolean withPersisting;
    private final String modelName;
    private Path tempDirectory;
    private final Boolean logRequests;
    private final Boolean logResponses;
    private static final Logger logger = LoggerFactory.getLogger(VertexAiImageModel.class);

    public VertexAiImageModel(String endpoint, String project, String location, String publisher, String modelName, Long seed, String language, Integer guidanceScale, String negativePrompt, ImageStyle sampleImageStyle, Integer sampleImageSize, AspectRatio aspectRatio, PersonGeneration personGeneration, Integer maxRetries, MimeType mimeType, Integer compressionQuality, Boolean addWatermark, String cloudStorageBucket, Boolean withPersisting, Path persistTo, Boolean logRequests, Boolean logResponses) {
        this.endpoint = ValidationUtils.ensureNotBlank((String)endpoint, (String)"endpoint");
        this.endpointName = EndpointName.ofProjectLocationPublisherModelName((String)ValidationUtils.ensureNotBlank((String)project, (String)"project"), (String)ValidationUtils.ensureNotBlank((String)location, (String)"location"), (String)ValidationUtils.ensureNotBlank((String)publisher, (String)"publisher"), (String)ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName"));
        this.seed = seed == null ? null : Long.valueOf(ValidationUtils.ensureBetween((Long)seed, (long)0L, (long)0xFFFFFFFFL, (String)"seed"));
        this.modelName = modelName;
        this.language = language;
        this.guidanceScale = guidanceScale;
        this.negativePrompt = negativePrompt;
        this.sampleImageStyle = sampleImageStyle;
        this.sampleImageSize = sampleImageSize;
        this.aspectRatio = aspectRatio;
        this.mimeType = mimeType;
        this.compressionQuality = compressionQuality;
        this.personGeneration = personGeneration;
        this.addWatermark = addWatermark;
        this.maxRetries = maxRetries == null ? 3 : maxRetries;
        this.cloudStorageBucket = cloudStorageBucket;
        this.withPersisting = withPersisting;
        if (this.withPersisting != null && this.withPersisting.booleanValue()) {
            try {
                if (persistTo != null) {
                    if (!persistTo.toFile().exists() && !persistTo.toFile().mkdirs()) {
                        throw new IOException("Impossible to create persistTo temporary directory");
                    }
                    this.tempDirectory = persistTo;
                } else {
                    this.tempDirectory = Files.createTempDirectory("imagen-directory-", new FileAttribute[0]);
                }
            }
            catch (IOException e) {
                throw new RuntimeException("Impossible to create persistence temporary directory", e);
            }
        }
        this.logRequests = logRequests != null ? logRequests : Boolean.valueOf(false);
        this.logResponses = logResponses != null ? logResponses : Boolean.valueOf(false);
    }

    public Response<Image> generate(String prompt) {
        Response<List<Image>> generatedImageResponse = this.generate(prompt, 1);
        return Response.from(((List)generatedImageResponse.content()).get(0), (TokenUsage)generatedImageResponse.tokenUsage(), (FinishReason)generatedImageResponse.finishReason());
    }

    public Response<List<Image>> generate(String prompt, int n) {
        return this.generate(prompt, null, null, n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Response<List<Image>> generate(String prompt, Image image, Image mask, int n) {
        try {
            PredictionServiceSettings serviceSettings = ((PredictionServiceSettings.Builder)PredictionServiceSettings.newBuilder().setEndpoint(this.endpoint)).build();
            try (PredictionServiceClient client = PredictionServiceClient.create((PredictionServiceSettings)serviceSettings);){
                List allImages;
                List<Value> instances = this.prepareInstance(prompt, image, mask);
                Value parameters = this.prepareParameters(n);
                if (this.logRequests.booleanValue() && logger.isDebugEnabled()) {
                    logger.debug("IMAGEN ({}) instances: {} parameters: {}", new Object[]{this.modelName, instances, parameters});
                }
                PredictResponse predictResponse = (PredictResponse)RetryUtils.withRetryMappingExceptions(() -> client.predict(this.endpointName, instances, parameters), (int)this.maxRetries);
                if (this.logResponses.booleanValue() && logger.isDebugEnabled()) {
                    logger.debug("IMAGEN ({}) response: {}", (Object)this.modelName, (Object)predictResponse);
                }
                if ((allImages = predictResponse.getPredictionsList().stream().filter(v -> !v.getStructValue().getFieldsMap().containsKey("raiFilteredReason")).map(v -> {
                    Map fieldsMap = v.getStructValue().getFieldsMap();
                    if (fieldsMap.containsKey("gcsUri")) {
                        String gcsUri = ((Value)fieldsMap.get("gcsUri")).getStringValue();
                        return Image.builder().url(gcsUri).build();
                    }
                    if (fieldsMap.containsKey("bytesBase64Encoded")) {
                        String bytesBase64Encoded = ((Value)fieldsMap.get("bytesBase64Encoded")).getStringValue();
                        return Image.builder().base64Data(bytesBase64Encoded).url(this.persistAndGetURI(bytesBase64Encoded)).build();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList())).isEmpty()) {
                    Optional<Value> raiFilteredReason = predictResponse.getPredictionsList().stream().filter(v -> v.getStructValue().getFieldsMap().containsKey("raiFilteredReason")).findFirst();
                    if (!raiFilteredReason.isPresent()) throw new RuntimeException("No image was generated. The image generation might have been blocked.");
                    String reason = ((Value)raiFilteredReason.get().getStructValue().getFieldsMap().get("raiFilteredReason")).getStringValue();
                    throw new RuntimeException("Image generation blocked for safaty reasons: " + reason);
                }
                Response response = Response.from(allImages);
                return response;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Value prepareParameters(int n) throws InvalidProtocolBufferException {
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("sampleCount", n);
        paramsMap.put("includeRaiReason", true);
        paramsMap.put("includeSafetyAttributes", true);
        if (this.seed != null) {
            paramsMap.put("seed", this.seed);
        }
        if (this.sampleImageStyle != null) {
            paramsMap.put("sampleImageStyle", this.sampleImageStyle.toString());
        }
        if (this.sampleImageSize != null) {
            paramsMap.put("mode", "upscale");
            paramsMap.put("sampleImageSize", this.sampleImageSize.toString());
        }
        if (this.guidanceScale != null) {
            paramsMap.put("guidanceScale", this.guidanceScale);
        }
        if (this.negativePrompt != null) {
            paramsMap.put("negativePrompt", this.negativePrompt);
        }
        if (this.language != null) {
            paramsMap.put("language", this.language);
        }
        if (this.aspectRatio != null) {
            paramsMap.put("aspectRatio", this.aspectRatio.toString());
        }
        if (this.mimeType != null) {
            HashMap<String, Object> outputOptions = new HashMap<String, Object>();
            outputOptions.put("mimeType", this.mimeType.toString());
            if (this.mimeType == MimeType.JPEG && this.compressionQuality != null) {
                outputOptions.put("compressionQuality", this.compressionQuality);
            }
            paramsMap.put("outputOptions", outputOptions);
        }
        if (this.personGeneration != null) {
            paramsMap.put("personGeneration", this.personGeneration.toString());
        }
        if (this.addWatermark != null) {
            paramsMap.put("addWatermark", this.addWatermark);
        }
        if (this.cloudStorageBucket != null) {
            paramsMap.put("storageUri", this.cloudStorageBucket);
        }
        Value.Builder parametersBuilder = Value.newBuilder();
        JsonFormat.parser().merge(Json.toJson(paramsMap), (Message.Builder)parametersBuilder);
        return parametersBuilder.build();
    }

    private List<Value> prepareInstance(String prompt, Image image, Image mask) throws InvalidProtocolBufferException {
        HashMap<String, String> imageMap;
        HashMap<String, Object> promptMap = new HashMap<String, Object>();
        promptMap.put("prompt", prompt);
        if (image != null && image.base64Data() != null) {
            imageMap = new HashMap<String, String>();
            imageMap.put("bytesBase64Encoded", image.base64Data());
            promptMap.put("image", imageMap);
        }
        if (mask != null && mask.base64Data() != null) {
            imageMap = new HashMap();
            imageMap.put("bytesBase64Encoded", mask.base64Data());
            HashMap<String, HashMap<String, String>> maskMap = new HashMap<String, HashMap<String, String>>();
            maskMap.put("image", imageMap);
            promptMap.put("mask", maskMap);
        }
        Value.Builder instanceBuilder = Value.newBuilder();
        JsonFormat.parser().merge(Json.toJson(promptMap), (Message.Builder)instanceBuilder);
        return Collections.singletonList(instanceBuilder.build());
    }

    public Response<Image> edit(Image image, String prompt) {
        Response<Image> generatedImageResponse = this.edit(image, null, prompt);
        return Response.from(generatedImageResponse.content(), (TokenUsage)generatedImageResponse.tokenUsage(), (FinishReason)generatedImageResponse.finishReason());
    }

    public Response<Image> edit(Image image, Image mask, String prompt) {
        Response<List<Image>> generatedImageResponse = this.generate(prompt, image, mask, 1);
        return Response.from(((List)generatedImageResponse.content()).get(0), (TokenUsage)generatedImageResponse.tokenUsage(), (FinishReason)generatedImageResponse.finishReason());
    }

    private URI persistAndGetURI(String bytesBase64Encoded) {
        if (this.withPersisting != null && this.withPersisting.booleanValue()) {
            try {
                String suffix = ".png";
                if (this.mimeType == MimeType.JPEG) {
                    suffix = ".jpg";
                }
                Path tempFile = Files.createTempFile(this.tempDirectory, "imagen-image-", suffix, new FileAttribute[0]);
                Files.write(tempFile, Base64.getDecoder().decode(bytesBase64Encoded), new OpenOption[0]);
                return tempFile.toUri();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(VertexAiImageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            VertexAiImageModelBuilderFactory factory = (VertexAiImageModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static class Builder {
        private String endpoint;
        private String project;
        private String location;
        private String publisher;
        private String modelName;
        private Long seed;
        private String language;
        private String negativePrompt;
        private ImageStyle sampleImageStyle;
        private AspectRatio aspectRatio;
        private Integer sampleImageSize;
        private Integer maxRetries;
        private Integer guidanceScale;
        private MimeType mimeType;
        private PersonGeneration personGeneration;
        private Boolean watermark;
        private Boolean withPersisting;
        private Path persistTo;
        private Integer compressionQuality;
        private String cloudStorageBucket;
        private Boolean logRequests;
        private Boolean logResponses;

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder project(String project) {
            this.project = project;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder seed(Long seed) {
            this.seed = seed;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder guidanceScale(Integer guidanceScale) {
            this.guidanceScale = guidanceScale;
            return this;
        }

        public Builder negativePrompt(String negativePrompt) {
            this.negativePrompt = negativePrompt;
            return this;
        }

        public Builder sampleImageStyle(ImageStyle sampleImageStyle) {
            this.sampleImageStyle = sampleImageStyle;
            return this;
        }

        public Builder sampleImageSize(Integer sampleImageSize) {
            this.sampleImageSize = sampleImageSize;
            return this;
        }

        public Builder aspectRatio(AspectRatio aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public Builder mimeType(MimeType mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder compressionQuality(Integer compressionQuality) {
            this.compressionQuality = compressionQuality;
            return this;
        }

        public Builder personGeneration(PersonGeneration personGeneration) {
            this.personGeneration = personGeneration;
            return this;
        }

        public Builder watermark(Boolean watermark) {
            this.watermark = watermark;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder persistToCloudStorage(String gcsUri) {
            this.cloudStorageBucket = gcsUri;
            return this;
        }

        public Builder withPersisting() {
            this.withPersisting = Boolean.TRUE;
            return this;
        }

        public Builder persistTo(Path persistTo) {
            this.persistTo = persistTo;
            return this.withPersisting();
        }

        public Builder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public Builder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public VertexAiImageModel build() {
            return new VertexAiImageModel(this.endpoint, this.project, this.location, this.publisher, this.modelName, this.seed, this.language, this.guidanceScale, this.negativePrompt, this.sampleImageStyle, this.sampleImageSize, this.aspectRatio, this.personGeneration, this.maxRetries, this.mimeType, this.compressionQuality, this.watermark, this.cloudStorageBucket, this.withPersisting, this.persistTo, this.logRequests, this.logResponses);
        }
    }

    public static enum PersonGeneration {
        DONT_ALLOW("dont_allow"),
        ALLOW_ADULT("allow_adult"),
        ALLOW_ALL("allow_all");

        private final String personGeneration;

        private PersonGeneration(String value) {
            this.personGeneration = value;
        }

        public String toString() {
            return this.personGeneration;
        }
    }

    public static enum MimeType {
        PNG("image/png"),
        JPEG("image/jpeg");

        private final String mimeType;

        private MimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String toString() {
            return this.mimeType;
        }
    }

    public static enum AspectRatio {
        SQUARE("1:1"),
        PORTRAIT("9:16"),
        LANDSCAPE("16:9"),
        THREE_FOURTHS("3:4"),
        FOUR_THIRDS("4:3");

        private final String ratio;

        private AspectRatio(String ratio) {
            this.ratio = ratio;
        }

        public String toString() {
            return this.ratio;
        }
    }

    public static enum ImageStyle {
        PHOTOGRAPH("photograph"),
        DIGITAL_ART("digital_art"),
        LANDSCAPE("landscape"),
        SKETCH("sketch"),
        WATERCOLOR("watercolor"),
        CYBERPUNK("cyberpunk"),
        POP_ART("pop_art");

        private final String style;

        private ImageStyle(String style) {
            this.style = style;
        }

        public String toString() {
            return this.style;
        }
    }
}

