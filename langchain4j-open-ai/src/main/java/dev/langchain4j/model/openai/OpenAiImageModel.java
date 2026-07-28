/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.image.Image$Builder
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.RetryUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.image.ImageModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.openai;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.RetryUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModelName;
import dev.langchain4j.model.openai.internal.OpenAiClient;
import dev.langchain4j.model.openai.internal.SyncOrAsync;
import dev.langchain4j.model.openai.internal.image.EditImageRequest;
import dev.langchain4j.model.openai.internal.image.GenerateImagesRequest;
import dev.langchain4j.model.openai.internal.image.GenerateImagesResponse;
import dev.langchain4j.model.openai.internal.image.ImageData;
import dev.langchain4j.model.openai.internal.image.ImageFile;
import dev.langchain4j.model.openai.spi.OpenAiImageModelBuilderFactory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;

public class OpenAiImageModel
implements ImageModel {
    private final String modelName;
    private final String size;
    private final String quality;
    private final String user;
    private final String background;
    private final String outputFormat;
    private final Integer outputCompression;
    private final String moderation;
    private final OpenAiClient client;
    private final Integer maxRetries;

    public OpenAiImageModel(OpenAiImageModelBuilder builder) {
        Object cBuilder = ((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)((OpenAiClient.Builder)OpenAiClient.builder().httpClientBuilder(builder.httpClientBuilder)).baseUrl((String)Utils.getOrDefault((Object)builder.baseUrl, (Object)"https://api.openai.com/v1"))).apiKey(builder.apiKey)).organizationId(builder.organizationId)).projectId(builder.projectId)).connectTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(15L)))).readTimeout((Duration)Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofSeconds(60L)))).logRequests((Boolean)Utils.getOrDefault((Object)builder.logRequests, (Object)false))).logResponses((Boolean)Utils.getOrDefault((Object)builder.logResponses, (Object)false))).logger(builder.logger)).userAgent("langchain4j-openai")).customHeaders(builder.customHeadersSupplier)).customQueryParams(builder.customQueryParams);
        this.client = ((OpenAiClient.Builder)cBuilder).build();
        this.maxRetries = (Integer)Utils.getOrDefault((Object)builder.maxRetries, (Object)2);
        this.modelName = builder.modelName;
        this.size = builder.size;
        this.quality = builder.quality;
        this.user = builder.user;
        this.background = builder.background;
        this.outputFormat = builder.outputFormat;
        this.outputCompression = builder.outputCompression;
        this.moderation = builder.moderation;
    }

    public String modelName() {
        return this.modelName;
    }

    public Response<Image> generate(String prompt) {
        GenerateImagesRequest request = this.requestBuilder(prompt).build();
        GenerateImagesResponse response = (GenerateImagesResponse)((SyncOrAsync)RetryUtils.withRetryMappingExceptions(() -> this.client.imagesGeneration(request), (int)this.maxRetries)).execute();
        return Response.from(OpenAiImageModel.fromImageData(response.data().get(0), response.outputFormat()));
    }

    public Response<List<Image>> generate(String prompt, int n) {
        GenerateImagesRequest request = this.requestBuilder(prompt).n(n).build();
        GenerateImagesResponse response = (GenerateImagesResponse)((SyncOrAsync)RetryUtils.withRetryMappingExceptions(() -> this.client.imagesGeneration(request), (int)this.maxRetries)).execute();
        String responseOutputFormat = response.outputFormat();
        return Response.from(response.data().stream().map(data -> OpenAiImageModel.fromImageData(data, responseOutputFormat)).collect(Collectors.toList()));
    }

    public Response<Image> edit(Image image, String prompt) {
        EditImageRequest request = this.editRequestBuilder(image, prompt).build();
        GenerateImagesResponse response = (GenerateImagesResponse)((SyncOrAsync)RetryUtils.withRetryMappingExceptions(() -> this.client.imagesEdit(request), (int)this.maxRetries)).execute();
        return Response.from(OpenAiImageModel.fromImageData(response.data().get(0), response.outputFormat()));
    }

    public Response<Image> edit(Image image, Image mask, String prompt) {
        EditImageRequest request = this.editRequestBuilder(image, prompt).mask(ImageFile.from(mask)).build();
        GenerateImagesResponse response = (GenerateImagesResponse)((SyncOrAsync)RetryUtils.withRetryMappingExceptions(() -> this.client.imagesEdit(request), (int)this.maxRetries)).execute();
        return Response.from(OpenAiImageModel.fromImageData(response.data().get(0), response.outputFormat()));
    }

    public static OpenAiImageModelBuilder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OpenAiImageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OpenAiImageModelBuilderFactory factory = (OpenAiImageModelBuilderFactory)iterator.next();
            return (OpenAiImageModelBuilder)factory.get();
        }
        return new OpenAiImageModelBuilder();
    }

    private static Image fromImageData(ImageData data, String outputFormat) {
        Image.Builder imageBuilder = Image.builder().url(data.url()).base64Data(data.b64Json()).revisedPrompt(data.revisedPrompt());
        if (outputFormat != null) {
            imageBuilder.mimeType("image/" + outputFormat);
        }
        return imageBuilder.build();
    }

    private GenerateImagesRequest.Builder requestBuilder(String prompt) {
        return GenerateImagesRequest.builder().model(this.modelName).prompt(prompt).size(this.size).quality(this.quality).user(this.user).background(this.background).outputFormat(this.outputFormat).outputCompression(this.outputCompression).moderation(this.moderation);
    }

    private EditImageRequest.Builder editRequestBuilder(Image image, String prompt) {
        return EditImageRequest.builder().image(ImageFile.from(image)).model(this.modelName).prompt(prompt).size(this.size).quality(this.quality).user(this.user).background(this.background).outputFormat(this.outputFormat).outputCompression(this.outputCompression);
    }

    public static class OpenAiImageModelBuilder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String apiKey;
        private String organizationId;
        private String projectId;
        private String modelName;
        private String size;
        private String quality;
        private String user;
        private String background;
        private String outputFormat;
        private Integer outputCompression;
        private String moderation;
        private Duration timeout;
        private Integer maxRetries;
        private Boolean logRequests;
        private Boolean logResponses;
        private Logger logger;
        private Supplier<Map<String, String>> customHeadersSupplier;
        private Map<String, String> customQueryParams;

        public OpenAiImageModelBuilder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public OpenAiImageModelBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public OpenAiImageModelBuilder modelName(OpenAiImageModelName modelName) {
            this.modelName = modelName.toString();
            return this;
        }

        public OpenAiImageModelBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OpenAiImageModelBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public OpenAiImageModelBuilder organizationId(String organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public OpenAiImageModelBuilder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public OpenAiImageModelBuilder size(String size) {
            this.size = size;
            return this;
        }

        public OpenAiImageModelBuilder quality(String quality) {
            this.quality = quality;
            return this;
        }

        public OpenAiImageModelBuilder user(String user) {
            this.user = user;
            return this;
        }

        public OpenAiImageModelBuilder background(String background) {
            this.background = background;
            return this;
        }

        public OpenAiImageModelBuilder outputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public OpenAiImageModelBuilder outputCompression(Integer outputCompression) {
            this.outputCompression = outputCompression;
            return this;
        }

        public OpenAiImageModelBuilder moderation(String moderation) {
            this.moderation = moderation;
            return this;
        }

        public OpenAiImageModelBuilder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public OpenAiImageModelBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public OpenAiImageModelBuilder logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this;
        }

        public OpenAiImageModelBuilder logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public OpenAiImageModelBuilder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public OpenAiImageModelBuilder customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this;
        }

        public OpenAiImageModelBuilder customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this;
        }

        public OpenAiImageModelBuilder customQueryParams(Map<String, String> customQueryParams) {
            this.customQueryParams = customQueryParams;
            return this;
        }

        public OpenAiImageModel build() {
            return new OpenAiImageModel(this);
        }
    }
}

