/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.ModelProvider
 */
package dev.langchain4j.micrometer.metrics.conventions;

import dev.langchain4j.model.ModelProvider;

public enum OTelGenAiProviderName {
    ANTHROPIC(ModelProvider.ANTHROPIC, "anthropic"),
    AMAZON_BEDROCK(ModelProvider.AMAZON_BEDROCK, "aws.bedrock"),
    AZURE_OPEN_AI(ModelProvider.AZURE_OPEN_AI, "azure.ai.openai"),
    COHERE(ModelProvider.COHERE, "cohere"),
    GITHUB_MODELS(ModelProvider.GITHUB_MODELS, "github"),
    GOOGLE_AI_GEMINI(ModelProvider.GOOGLE_AI_GEMINI, "gcp.gemini"),
    GOOGLE_GENAI(ModelProvider.GOOGLE_GENAI, "gcp.gen_ai"),
    GOOGLE_VERTEX_AI_GEMINI(ModelProvider.GOOGLE_VERTEX_AI_GEMINI, "gcp.vertex_ai"),
    GOOGLE_VERTEX_AI_ANTHROPIC(ModelProvider.GOOGLE_VERTEX_AI_ANTHROPIC, "gcp.vertex_ai"),
    JINA(ModelProvider.JINA, "jina"),
    MICROSOFT_FOUNDRY(ModelProvider.MICROSOFT_FOUNDRY, "azure.ai.inference"),
    MISTRAL_AI(ModelProvider.MISTRAL_AI, "mistral_ai"),
    OLLAMA(ModelProvider.OLLAMA, "ollama"),
    OPEN_AI(ModelProvider.OPEN_AI, "openai"),
    VOYAGE_AI(ModelProvider.VOYAGE_AI, "voyage_ai"),
    WATSONX(ModelProvider.WATSONX, "ibm.watsonx.ai"),
    OTHER(ModelProvider.OTHER, "unknown");

    private final ModelProvider modelProvider;
    private final String value;

    private OTelGenAiProviderName(ModelProvider modelProvider, String value) {
        this.modelProvider = modelProvider;
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static String fromModelProvider(ModelProvider modelProvider) {
        if (modelProvider == null) {
            return "unknown";
        }
        for (OTelGenAiProviderName entry : OTelGenAiProviderName.values()) {
            if (entry.modelProvider != modelProvider) continue;
            return entry.value;
        }
        return "unknown";
    }
}

