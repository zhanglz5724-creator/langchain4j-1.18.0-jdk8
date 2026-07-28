/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai.spi;

import dev.langchain4j.model.mistralai.MistralAiStreamingFimModel;
import java.util.function.Supplier;

public interface MistralAiStreamingFimModelBuilderFactory
extends Supplier<MistralAiStreamingFimModel.Builder> {
}

