/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai.spi;

import dev.langchain4j.model.workersai.WorkersAiImageModel;
import java.util.function.Supplier;

public interface WorkersAiImageModelBuilderFactory
extends Supplier<WorkersAiImageModel.Builder> {
}

