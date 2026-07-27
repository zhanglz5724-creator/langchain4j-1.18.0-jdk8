/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.input;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Objects;

public class Prompt {
    private final String text;

    public Prompt(String text) {
        this.text = ValidationUtils.ensureNotBlank(text, "text");
    }

    public String text() {
        return this.text;
    }

    public SystemMessage toSystemMessage() {
        return SystemMessage.systemMessage(this.text);
    }

    public UserMessage toUserMessage(String userName) {
        return UserMessage.userMessage(userName, this.text);
    }

    public UserMessage toUserMessage() {
        return UserMessage.userMessage(this.text);
    }

    public AiMessage toAiMessage() {
        return AiMessage.aiMessage(this.text);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Prompt that = (Prompt)o;
        return Objects.equals(this.text, that.text);
    }

    public int hashCode() {
        return Objects.hash(this.text);
    }

    public String toString() {
        return "Prompt { text = " + Utils.quoted(this.text) + " }";
    }

    public static Prompt from(String text) {
        return new Prompt(text);
    }
}

