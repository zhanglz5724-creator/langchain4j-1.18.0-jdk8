/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.language.LanguageModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.huggingface;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.huggingface.FactoryCreator;
import dev.langchain4j.model.huggingface.client.HuggingFaceClient;
import dev.langchain4j.model.huggingface.client.Options;
import dev.langchain4j.model.huggingface.client.Parameters;
import dev.langchain4j.model.huggingface.client.TextGenerationRequest;
import dev.langchain4j.model.huggingface.client.TextGenerationResponse;
import dev.langchain4j.model.huggingface.spi.HuggingFaceClientFactory;
import dev.langchain4j.model.huggingface.spi.HuggingFaceLanguageModelBuilderFactory;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;

@Deprecated
public class HuggingFaceLanguageModel
implements LanguageModel {
    private final HuggingFaceClient client;
    private final Double temperature;
    private final Integer maxNewTokens;
    private final Boolean returnFullText;
    private final Boolean waitForModel;

    public HuggingFaceLanguageModel(String accessToken, String modelId, Duration timeout, Double temperature, Integer maxNewTokens, Boolean returnFullText, Boolean waitForModel) {
        this(HuggingFaceLanguageModel.builder().accessToken(accessToken).modelId(modelId).timeout(timeout).temperature(temperature).maxNewTokens(maxNewTokens).returnFullText(returnFullText).waitForModel(waitForModel));
    }

    public HuggingFaceLanguageModel(String baseUrl, String accessToken, String modelId, Duration timeout, Double temperature, Integer maxNewTokens, Boolean returnFullText, Boolean waitForModel) {
        this(HuggingFaceLanguageModel.builder().baseUrl(baseUrl).accessToken(accessToken).modelId(modelId).timeout(timeout).temperature(temperature).maxNewTokens(maxNewTokens).returnFullText(returnFullText).waitForModel(waitForModel));
    }

    public HuggingFaceLanguageModel(final Builder builder) {
        this.client = FactoryCreator.FACTORY.create(new HuggingFaceClientFactory.Input(){

            @Override
            public String baseUrl() {
                return builder.baseUrl;
            }

            @Override
            public String apiKey() {
                return builder.accessToken;
            }

            @Override
            public String modelId() {
                return builder.modelId;
            }

            @Override
            public Duration timeout() {
                return builder.timeout;
            }

            @Override
            public HttpClientBuilder httpClientBuilder() {
                return builder.httpClientBuilder;
            }
        });
        this.temperature = builder.temperature;
        this.maxNewTokens = builder.maxNewTokens;
        this.returnFullText = builder.returnFullText;
        this.waitForModel = builder.waitForModel;
    }

    public Response<String> generate(String prompt) {
        TextGenerationRequest request = TextGenerationRequest.builder().inputs(prompt).parameters(Parameters.builder().temperature(this.temperature).maxNewTokens(this.maxNewTokens).returnFullText(this.returnFullText).build()).options(Options.builder().waitForModel(this.waitForModel).build()).build();
        TextGenerationResponse response = this.client.generate(request);
        return Response.from(response.getGeneratedText());
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(HuggingFaceLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            HuggingFaceLanguageModelBuilderFactory factory = (HuggingFaceLanguageModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public static HuggingFaceLanguageModel withAccessToken(String accessToken) {
        return HuggingFaceLanguageModel.builder().accessToken(accessToken).build();
    }

    public static final class Builder {
        private HttpClientBuilder httpClientBuilder;
        private String baseUrl;
        private String accessToken;
        private String modelId;
        private Duration timeout = Duration.ofSeconds(15L);
        private Double temperature;
        private Integer maxNewTokens;
        private Boolean returnFullText = false;
        private Boolean waitForModel = true;

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder modelId(String modelId) {
            if (modelId != null) {
                this.modelId = modelId;
            }
            return this;
        }

        public Builder timeout(Duration timeout) {
            if (timeout != null) {
                this.timeout = timeout;
            }
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxNewTokens(Integer maxNewTokens) {
            this.maxNewTokens = maxNewTokens;
            return this;
        }

        public Builder returnFullText(Boolean returnFullText) {
            if (returnFullText != null) {
                this.returnFullText = returnFullText;
            }
            return this;
        }

        public Builder waitForModel(Boolean waitForModel) {
            if (waitForModel != null) {
                this.waitForModel = waitForModel;
            }
            return this;
        }

        public HuggingFaceLanguageModel build() {
            ValidationUtils.ensureNotBlank((String)this.accessToken, (String)"%s", (Object[])new Object[]{"HuggingFace access token must be defined. It can be generated here: https://huggingface.co/settings/tokens"});
            return new HuggingFaceLanguageModel(this);
        }
    }
}

