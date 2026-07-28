/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.azure;

public enum AzureOpenAiImageModelName {
    DALL_E_3("dall-e-3", "dall-e-3"),
    DALL_E_3_30("dall-e-3-30", "dall-e-3", "30");

    private final String modelName;
    private final String modelType;
    private final String modelVersion;

    private AzureOpenAiImageModelName(String modelName, String modelType) {
        this.modelName = modelName;
        this.modelType = modelType;
        this.modelVersion = null;
    }

    private AzureOpenAiImageModelName(String modelName, String modelType, String modelVersion) {
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

