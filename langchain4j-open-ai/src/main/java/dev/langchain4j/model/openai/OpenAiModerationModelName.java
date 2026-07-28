/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai;

public enum OpenAiModerationModelName {
    TEXT_MODERATION_STABLE("text-moderation-stable"),
    TEXT_MODERATION_LATEST("text-moderation-latest"),
    OMNI_MODERATION_LATEST("omni-moderation-latest"),
    OMNI_MODERATION_2024_09_26("omni-moderation-2024-09-26");

    private final String stringValue;

    private OpenAiModerationModelName(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}

