/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.localai.spi;

import dev.langchain4j.model.localai.LocalAiLanguageModel;
import java.util.function.Supplier;

public interface LocalAiLanguageModelBuilderFactory
extends Supplier<LocalAiLanguageModel.LocalAiLanguageModelBuilder> {
}

