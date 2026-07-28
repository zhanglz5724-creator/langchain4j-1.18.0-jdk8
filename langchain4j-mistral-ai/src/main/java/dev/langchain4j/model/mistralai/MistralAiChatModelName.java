/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai;

public enum MistralAiChatModelName {
    OPEN_MISTRAL_7B("open-mistral-7b"),
    OPEN_MIXTRAL_8x7B("open-mixtral-8x7b"),
    OPEN_MIXTRAL_8X22B("open-mixtral-8x22b"),
    MISTRAL_SMALL_LATEST("mistral-small-latest"),
    MISTRAL_MEDIUM_LATEST("mistral-medium-latest"),
    MISTRAL_LARGE_LATEST("mistral-large-latest"),
    MAGISTRAL_SMALL_LATEST("magistral-small-latest"),
    MAGISTRAL_MEDIUM_LATEST("magistral-medium-latest"),
    MISTRAL_MODERATION_LATEST("mistral-moderation-latest"),
    OPEN_MISTRAL_NEMO("open-mistral-nemo"),
    CODESTRAL_LATEST("codestral-latest"),
    VOXTRAL_SMALL_LATEST("voxtral-small-latest");

    private final String value;

    private MistralAiChatModelName(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}

