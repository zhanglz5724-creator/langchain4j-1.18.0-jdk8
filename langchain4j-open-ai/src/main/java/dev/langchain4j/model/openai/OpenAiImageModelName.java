/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai;

public enum OpenAiImageModelName {
    GPT_IMAGE_1("gpt-image-1"),
    GPT_IMAGE_1_MINI("gpt-image-1-mini"),
    GPT_IMAGE_1_5("gpt-image-1.5"),
    GPT_IMAGE_2("gpt-image-2"),
    CHATGPT_IMAGE_LATEST("chatgpt-image-latest"),
    DALL_E_2("dall-e-2"),
    DALL_E_3("dall-e-3");

    private final String stringValue;

    private OpenAiImageModelName(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}

