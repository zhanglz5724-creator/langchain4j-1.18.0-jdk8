/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.huggingface.spi;

import dev.langchain4j.model.huggingface.HuggingFaceLanguageModel;
import java.util.function.Supplier;

@Deprecated
public interface HuggingFaceLanguageModelBuilderFactory
extends Supplier<HuggingFaceLanguageModel.Builder> {
}

