/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.workersai.client;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.workersai.client.WorkersAiClient;

public abstract class AbstractWorkersAIModel {
    protected String accountId;
    protected String modelName;
    protected WorkersAiClient client;

    public AbstractWorkersAIModel(String accountId, String modelName, String apiToken) {
        this(accountId, modelName, apiToken, null);
    }

    public AbstractWorkersAIModel(String accountId, String modelName, String apiToken, HttpClientBuilder httpClientBuilder) {
        ValidationUtils.ensureNotEmpty((String)accountId, (String)"%s", (Object[])new Object[]{"Account identifier should not be null or empty"});
        this.accountId = accountId;
        ValidationUtils.ensureNotEmpty((String)modelName, (String)"%s", (Object[])new Object[]{"Model name should not be null or empty"});
        this.modelName = modelName;
        ValidationUtils.ensureNotEmpty((String)apiToken, (String)"%s", (Object[])new Object[]{"Token should not be null or empty"});
        this.client = WorkersAiClient.builder().apiToken(apiToken).httpClientBuilder(httpClientBuilder).build();
    }
}

