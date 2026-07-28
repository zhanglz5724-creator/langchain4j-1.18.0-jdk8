/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.spi;

import dev.langchain4j.model.workersai.WorkersAiChatModel;
import java.util.function.Supplier;

public interface WorkersAiChatModelBuilderFactory
extends Supplier<WorkersAiChatModel.Builder> {
}

