/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.language;

import dev.langchain4j.model.ModelDisabledException;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.output.Response;

public class DisabledLanguageModel
implements LanguageModel {
    @Override
    public Response<String> generate(String prompt) {
        throw new ModelDisabledException("LanguageModel is disabled");
    }

    @Override
    public Response<String> generate(Prompt prompt) {
        throw new ModelDisabledException("LanguageModel is disabled");
    }
}

