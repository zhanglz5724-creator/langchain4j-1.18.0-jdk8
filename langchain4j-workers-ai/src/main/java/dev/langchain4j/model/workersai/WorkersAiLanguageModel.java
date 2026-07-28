/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.model.input.Prompt
 *  dev.langchain4j.model.language.LanguageModel
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.model.workersai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.workersai.client.AbstractWorkersAIModel;
import dev.langchain4j.model.workersai.client.WorkersAiTextCompletionRequest;
import dev.langchain4j.model.workersai.client.WorkersAiTextCompletionResponse;
import dev.langchain4j.model.workersai.spi.WorkersAiLanguageModelBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkersAiLanguageModel
extends AbstractWorkersAIModel
implements LanguageModel {
    private static final Logger log = LoggerFactory.getLogger(WorkersAiLanguageModel.class);

    public WorkersAiLanguageModel(Builder builder) {
        super(builder.accountId, builder.modelName, builder.apiToken, builder.httpClientBuilder);
    }

    public WorkersAiLanguageModel(String accountId, String modelName, String apiToken) {
        super(accountId, modelName, apiToken);
    }

    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(WorkersAiLanguageModelBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            WorkersAiLanguageModelBuilderFactory factory = (WorkersAiLanguageModelBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return new Builder();
    }

    public Response<String> generate(String prompt) {
        WorkersAiTextCompletionResponse response = this.client.generateText(new WorkersAiTextCompletionRequest(prompt), this.accountId, this.modelName);
        if (response == null || response.getResult() == null) {
            throw new RuntimeException("Empty response");
        }
        return new Response((Object)((WorkersAiTextCompletionResponse.TextResponse)response.getResult()).getResponse());
    }

    public Response<String> generate(Prompt prompt) {
        return this.generate(prompt.text());
    }

    public static class Builder {
        public String accountId;
        public String apiToken;
        public String modelName;
        public HttpClientBuilder httpClientBuilder;

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder apiToken(String apiToken) {
            this.apiToken = apiToken;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this;
        }

        public WorkersAiLanguageModel build() {
            return new WorkersAiLanguageModel(this);
        }
    }
}

