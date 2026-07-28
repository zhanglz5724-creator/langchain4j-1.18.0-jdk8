/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai.spi;

import dev.langchain4j.model.mistralai.MistralAiStreamingChatModel;
import java.util.function.Supplier;

public interface MistralAiStreamingChatModelBuilderFactory
extends Supplier<MistralAiStreamingChatModel.MistralAiStreamingChatModelBuilder> {
}

