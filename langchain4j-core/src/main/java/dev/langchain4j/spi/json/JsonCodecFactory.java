/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.json;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Json;

@Internal
public interface JsonCodecFactory {
    public Json.JsonCodec create();
}

