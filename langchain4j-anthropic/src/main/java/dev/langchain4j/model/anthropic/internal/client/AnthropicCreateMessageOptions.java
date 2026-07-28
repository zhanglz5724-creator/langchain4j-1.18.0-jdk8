/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.model.anthropic.internal.client;

import dev.langchain4j.Internal;

@Internal
public class AnthropicCreateMessageOptions {
    private final boolean returnThinking;
    private final boolean returnServerToolResults;

    public AnthropicCreateMessageOptions(boolean returnThinking) {
        this(returnThinking, false);
    }

    public AnthropicCreateMessageOptions(boolean returnThinking, boolean returnServerToolResults) {
        this.returnThinking = returnThinking;
        this.returnServerToolResults = returnServerToolResults;
    }

    public boolean returnThinking() {
        return this.returnThinking;
    }

    public boolean returnServerToolResults() {
        return this.returnServerToolResults;
    }
}

