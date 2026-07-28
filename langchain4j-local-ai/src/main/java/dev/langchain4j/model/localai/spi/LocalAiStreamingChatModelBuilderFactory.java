/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.localai.spi;

import dev.langchain4j.model.localai.LocalAiStreamingChatModel;
import java.util.function.Supplier;

public interface LocalAiStreamingChatModelBuilderFactory
extends Supplier<LocalAiStreamingChatModel.LocalAiStreamingChatModelBuilder> {
}

