/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.huggingface.spi;

import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import java.util.function.Supplier;

@Deprecated
public interface HuggingFaceChatModelBuilderFactory
extends Supplier<HuggingFaceChatModel.Builder> {
}

