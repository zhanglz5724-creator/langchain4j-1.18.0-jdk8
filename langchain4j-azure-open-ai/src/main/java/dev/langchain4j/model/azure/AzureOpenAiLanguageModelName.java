/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure;

public enum AzureOpenAiLanguageModelName {
    GPT_3_5_TURBO_INSTRUCT("gpt-35-turbo-instruct", "gpt-3.5-turbo"),
    GPT_3_5_TURBO_INSTRUCT_0914("gpt-35-turbo-instruct-0914", "gpt-3.5-turbo", "0914"),
    TEXT_DAVINCI_002("davinci-002", "text-davinci-002"),
    TEXT_DAVINCI_002_1("davinci-002-1", "text-davinci-002", "1");

    private final String modelName;
    private final String modelType;
    private final String modelVersion;

    private AzureOpenAiLanguageModelName(String modelName, String modelType) {
        this.modelName = modelName;
        this.modelType = modelType;
        this.modelVersion = null;
    }

    private AzureOpenAiLanguageModelName(String modelName, String modelType, String modelVersion) {
        this.modelName = modelName;
        this.modelType = modelType;
        this.modelVersion = modelVersion;
    }

    public String modelName() {
        return this.modelName;
    }

    public String modelType() {
        return this.modelType;
    }

    public String modelVersion() {
        return this.modelVersion;
    }

    public String toString() {
        return this.modelName;
    }
}

