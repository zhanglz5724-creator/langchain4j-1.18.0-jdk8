/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.model.bedrock.BedrockSystemContentType;

public interface BedrockSystemContent {
    public BedrockSystemContentType type();

    public boolean hasCachePoint();
}

