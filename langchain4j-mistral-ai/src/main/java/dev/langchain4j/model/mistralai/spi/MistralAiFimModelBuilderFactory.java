/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.mistralai.spi;

import dev.langchain4j.model.mistralai.MistralAiFimModel;
import java.util.function.Supplier;

public interface MistralAiFimModelBuilderFactory
extends Supplier<MistralAiFimModel.Builder> {
}

