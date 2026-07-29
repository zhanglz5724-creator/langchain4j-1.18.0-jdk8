/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.image.ImageModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.ollama.CompletionRequest;
import dev.langchain4j.model.ollama.CompletionResponse;
import dev.langchain4j.model.ollama.OllamaClient;
import dev.langchain4j.model.ollama.Options;
import dev.langchain4j.model.ollama.spi.OllamaImageModelBuilderFactory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

@Experimental
public class OllamaImageModel
implements ImageModel {
    private static final String IMAGE_PNG = "image/png";
    private static final int MAX_IMAGE_DIMENSION = 4096;
    private final OllamaClient client;
    private final String modelName;
    private final Options options;
    private final Integer width;
    private final Integer height;
    private final Integer steps;
    private final Integer maxRetries;

    public OllamaImageModel(OllamaImageModelBuilder builder) {
        this.client = OllamaClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl(builder.baseUrl).timeout(builder.timeout).logRequests(builder.logRequests).logResponses(builder.logResponses).customHeaders(builder.customHeadersSupplier).build();
        this.modelName = ValidationUtils.ensureNotBlank((String)builder.modelName, (String)"modelName");
        this.options = Options.builder().seed(builder.seed).build();
        this.width = OllamaImageModel.ensureDimension(builder.width, "width");
        this.height = OllamaImageModel.ensureDimension(builder.height, "height");
        this.steps = OllamaImageModel.ensureSteps(builder.steps);
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
    }

    public static OllamaImageModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OllamaImageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OllamaImageModelBuilderFactory factory = (OllamaImageModelBuilderFactory)iterator.next();
            return (OllamaImageModelBuilder)factory.get();
        }
        return new OllamaImageModelBuilder();
    }

    public String modelName() {
        return this.modelName;
    }

    public Response<Image> generate(String prompt) {
        CompletionRequest request = CompletionRequest.builder().model(this.modelName).prompt(ValidationUtils.ensureNotBlank((String)prompt, (String)"prompt")).options(this.options).width(this.width).height(this.height).steps(this.steps).stream(false).build();
        CompletionResponse response = (CompletionResponse)RetryUtils.withRetryMappingExceptions(() -> this.client.completion(request), (int)this.maxRetries);
        return Response.from(OllamaImageModel.fromResponse(response));
    }

    private static Image fromResponse(CompletionResponse response) {
        String image = response.getImage();
        if (Utils.isNullOrBlank((String)image)) {
            throw new OllamaImageGenerationException("No image was returned by Ollama");
        }
        return Image.builder().base64Data(image).mimeType(IMAGE_PNG).build();
    }

    private static Integer ensureDimension(Integer dimension, String name) {
        if (dimension == null || dimension == 0) {
            return null;
        }
        return ValidationUtils.ensureBetween((Integer)dimension, (int)0, (int)4096, (String)name);
    }

    private static Integer ensureSteps(Integer steps) {
        if (steps == null || steps == 0) {
            return null;
        }
        return ValidationUtils.ensureGreaterThanZeroIfNotNull((Integer)steps, (String)"steps");
    }

    public static class OllamaImageGenerationException
    extends RuntimeException {
        public OllamaImageGenerationException(String message) {
            super(message);
        }
    }

    public static class OllamaImageModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String modelName;
        private Integer width;
        private Integer height;
        private Integer steps;
        private Integer seed;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Supplier<Map<String, String>> customHeadersSupplier;

        public OllamaImageModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OllamaImageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OllamaImageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OllamaImageModelBuilder width(Integer width) {
            this.width = width;
            return this;
        }

        public OllamaImageModelBuilder height(Integer height) {
            this.height = height;
            return this;
        }

        public OllamaImageModelBuilder steps(Integer steps) {
            this.steps = steps;
            return this;
        }

        public OllamaImageModelBuilder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public OllamaImageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OllamaImageModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public OllamaImageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OllamaImageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OllamaImageModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OllamaImageModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OllamaImageModel build() {
            return new OllamaImageModel(this);
        }
    }
}

