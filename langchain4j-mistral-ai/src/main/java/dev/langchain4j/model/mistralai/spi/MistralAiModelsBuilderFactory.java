/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai.spi;

import dev.langchain4j.model.mistralai.MistralAiModels;
import java.util.function.Supplier;

public interface MistralAiModelsBuilderFactory
extends Supplier<MistralAiModels.MistralAiModelsBuilder> {
}

