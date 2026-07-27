/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.observability;

import dev.langchain4j.observability.api.AiServiceListenerRegistrar;
import java.util.function.Supplier;

public interface AiServiceListenerRegistrarFactory
extends Supplier<AiServiceListenerRegistrar> {
}

