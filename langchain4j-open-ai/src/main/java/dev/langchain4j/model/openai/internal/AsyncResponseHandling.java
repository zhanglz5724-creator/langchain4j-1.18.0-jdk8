/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.model.openai.internal.ErrorHandling;
import java.util.function.Consumer;

public interface AsyncResponseHandling {
    public ErrorHandling onError(Consumer<Throwable> var1);

    public ErrorHandling ignoreErrors();
}

