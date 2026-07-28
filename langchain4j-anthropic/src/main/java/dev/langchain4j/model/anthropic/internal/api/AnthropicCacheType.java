/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.anthropic.internal.api;

import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheControl;
import java.util.function.Supplier;

public enum AnthropicCacheType {
    NO_CACHE(() -> new AnthropicCacheControl("no_cache")),
    EPHEMERAL(() -> new AnthropicCacheControl("ephemeral"));

    private final Supplier<AnthropicCacheControl> value;

    private AnthropicCacheType(Supplier<AnthropicCacheControl> value) {
        this.value = value;
    }

    public AnthropicCacheControl cacheControl() {
        return this.value.get();
    }
}

