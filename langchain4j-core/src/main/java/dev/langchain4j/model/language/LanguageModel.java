/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.language;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.output.Response;

public interface LanguageModel {
    public Response<String> generate(String var1);

    default public Response<String> generate(Prompt prompt) {
        return this.generate(prompt.text());
    }
}

