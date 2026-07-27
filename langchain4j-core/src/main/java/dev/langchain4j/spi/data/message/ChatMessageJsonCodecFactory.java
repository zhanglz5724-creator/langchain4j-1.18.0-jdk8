/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi.data.message;

import dev.langchain4j.Internal;
import dev.langchain4j.data.message.ChatMessageJsonCodec;

@Internal
public interface ChatMessageJsonCodecFactory {
    public ChatMessageJsonCodec create();
}

