/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai;

public enum OpenAiLanguageModelName {
    GPT_3_5_TURBO_INSTRUCT("gpt-3.5-turbo-instruct");

    private final String stringValue;

    private OpenAiLanguageModelName(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}

