/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai.spi;

import dev.langchain4j.model.mistralai.MistralAiChatModel;
import java.util.function.Supplier;

public interface MistralAiChatModelBuilderFactory
extends Supplier<MistralAiChatModel.MistralAiChatModelBuilder> {
}

