/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure;

public enum AzureOpenAiChatModelName {
    GPT_3_5_TURBO("gpt-35-turbo", "gpt-3.5-turbo"),
    GPT_3_5_TURBO_0301("gpt-35-turbo-0301", "gpt-3.5-turbo", "0301"),
    GPT_3_5_TURBO_0613("gpt-35-turbo-0613", "gpt-3.5-turbo", "0613"),
    GPT_3_5_TURBO_1106("gpt-35-turbo-1106", "gpt-3.5-turbo", "1106"),
    GPT_3_5_TURBO_16K("gpt-35-turbo-16k", "gpt-3.5-turbo-16k"),
    GPT_3_5_TURBO_16K_0613("gpt-35-turbo-16k-0613", "gpt-3.5-turbo-16k", "0613"),
    GPT_4("gpt-4", "gpt-4"),
    GPT_4_0613("gpt-4-0613", "gpt-4", "0613"),
    GPT_4_0125_PREVIEW("gpt-4-0125-preview", "gpt-4", "0125-preview"),
    GPT_4_1106_PREVIEW("gpt-4-1106-preview", "gpt-4", "1106-preview"),
    GPT_4_TURBO("gpt-4-turbo", "gpt-4-turbo"),
    GPT_4_TURBO_2024_04_09("gpt-4-turbo-2024-04-09", "gpt-4-turbo", "2024-04-09"),
    GPT_4_32K("gpt-4-32k", "gpt-4-32k"),
    GPT_4_32K_0613("gpt-4-32k-0613", "gpt-4-32k", "0613"),
    GPT_4_VISION_PREVIEW("gpt-4-vision-preview", "gpt-4-vision", "preview"),
    GPT_4_O("gpt-4o", "gpt-4o");

    private final String modelName;
    private final String modelType;
    private final String modelVersion;

    private AzureOpenAiChatModelName(String modelName, String modelType) {
        this.modelName = modelName;
        this.modelType = modelType;
        this.modelVersion = null;
    }

    private AzureOpenAiChatModelName(String modelName, String modelType, String modelVersion) {
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

