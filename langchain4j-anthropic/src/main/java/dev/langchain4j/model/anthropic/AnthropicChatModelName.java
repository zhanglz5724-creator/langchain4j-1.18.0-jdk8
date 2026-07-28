/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.anthropic;

public enum AnthropicChatModelName {
    CLAUDE_OPUS_4_8("claude-opus-4-8"),
    CLAUDE_OPUS_4_7("claude-opus-4-7"),
    CLAUDE_OPUS_4_6("claude-opus-4-6"),
    CLAUDE_SONNET_4_6("claude-sonnet-4-6"),
    CLAUDE_OPUS_4_5_20251101("claude-opus-4-5-20251101"),
    CLAUDE_SONNET_4_5_20250929("claude-sonnet-4-5-20250929"),
    CLAUDE_HAIKU_4_5_20251001("claude-haiku-4-5-20251001"),
    CLAUDE_OPUS_4_1_20250805("claude-opus-4-1-20250805");

    private final String stringValue;

    private AnthropicChatModelName(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}

