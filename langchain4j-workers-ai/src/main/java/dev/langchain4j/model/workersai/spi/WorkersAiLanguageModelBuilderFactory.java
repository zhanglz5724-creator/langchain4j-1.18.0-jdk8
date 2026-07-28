/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.spi;

import dev.langchain4j.model.workersai.WorkersAiLanguageModel;
import java.util.function.Supplier;

public interface WorkersAiLanguageModelBuilderFactory
extends Supplier<WorkersAiLanguageModel.Builder> {
}

