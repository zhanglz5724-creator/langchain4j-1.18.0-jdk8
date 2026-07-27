/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class UserMessage
implements ChatMessage {
    private final String name;
    private final List<Content> contents;
    private final Map<String, Object> attributes;

    public UserMessage(Builder builder) {
        this.name = builder.name;
        this.contents = Utils.copy(ValidationUtils.ensureNotEmpty(builder.contents, "contents"));
        this.attributes = Utils.mutableCopy(builder.attributes);
    }

    public UserMessage(String text) {
        this(TextContent.from(text));
    }

    public UserMessage(String name, String text) {
        this(name, TextContent.from(text));
    }

    public UserMessage(Content ... contents) {
        this(Arrays.asList(contents));
    }

    public UserMessage(String name, Content ... contents) {
        this(name, Arrays.asList(contents));
    }

    public UserMessage(List<Content> contents) {
        this.name = null;
        this.contents = Utils.copy(ValidationUtils.ensureNotEmpty(contents, "contents"));
        this.attributes = new HashMap<String, Object>();
    }

    public UserMessage(String name, List<Content> contents) {
        this.name = name;
        this.contents = Utils.copy(ValidationUtils.ensureNotEmpty(contents, "contents"));
        this.attributes = new HashMap<String, Object>();
    }

    public String name() {
        return this.name;
    }

    public List<Content> contents() {
        return this.contents;
    }

    public String singleText() {
        if (this.hasSingleText()) {
            return ((TextContent)this.contents.get(0)).text();
        }
        throw Exceptions.runtime("Expecting single text content, but got: " + this.contents, new Object[0]);
    }

    public boolean hasSingleText() {
        return this.contents.size() == 1 && this.contents.get(0) instanceof TextContent;
    }

    @Experimental
    public Map<String, Object> attributes() {
        return this.attributes;
    }

    @Experimental
    public <T> T attribute(String key, Class<T> type) {
        return (T)this.attributes.get(key);
    }

    @Override
    public ChatMessageType type() {
        return ChatMessageType.USER;
    }

    public Builder toBuilder() {
        return UserMessage.builder().name(this.name).contents(Utils.mutableCopy(this.contents)).attributes(this.attributes);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        UserMessage that = (UserMessage)o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.contents, that.contents) && Objects.equals(this.attributes, that.attributes);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.contents, this.attributes);
    }

    public String toString() {
        return "UserMessage { name = " + Utils.quoted(this.name) + ", contents = " + this.contents + ", attributes = " + this.attributes + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static UserMessage from(String text) {
        return new UserMessage(text);
    }

    public static UserMessage from(String name, String text) {
        return new UserMessage(name, text);
    }

    public static UserMessage from(Content ... contents) {
        return new UserMessage(contents);
    }

    public static UserMessage from(String name, Content ... contents) {
        return new UserMessage(name, contents);
    }

    public static UserMessage from(List<Content> contents) {
        return new UserMessage(contents);
    }

    public static UserMessage from(String name, List<Content> contents) {
        return new UserMessage(name, contents);
    }

    public static UserMessage userMessage(String text) {
        return UserMessage.from(text);
    }

    public static UserMessage userMessage(String name, String text) {
        return UserMessage.from(name, text);
    }

    public static UserMessage userMessage(Content ... contents) {
        return UserMessage.from(contents);
    }

    public static UserMessage userMessage(String name, Content ... contents) {
        return UserMessage.from(name, contents);
    }

    public static UserMessage userMessage(List<Content> contents) {
        return UserMessage.from(contents);
    }

    public static UserMessage userMessage(String name, List<Content> contents) {
        return UserMessage.from(name, contents);
    }

    public static Optional<UserMessage> findLast(Collection<ChatMessage> messages) {
        return messages.stream().filter(UserMessage.class::isInstance).map(UserMessage.class::cast).reduce((first, second) -> second);
    }

    public static List<ChatMessage> replaceLast(List<ChatMessage> messages, UserMessage replacement) {
        if (replacement == null) {
            return messages;
        }
        for (int i = messages.size() - 1; i >= 0; --i) {
            if (!(messages.get(i) instanceof UserMessage)) continue;
            UserMessage existing = (UserMessage)messages.get(i);
            if (existing.equals(replacement)) {
                return messages;
            }
            ArrayList<ChatMessage> result = new ArrayList<ChatMessage>(messages);
            result.set(i, replacement);
            return result;
        }
        return messages;
    }

    public static class Builder {
        private String name;
        private List<Content> contents;
        private Map<String, Object> attributes;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder contents(List<Content> contents) {
            this.contents = contents;
            return this;
        }

        public Builder addContent(Content content) {
            if (this.contents == null) {
                this.contents = new ArrayList<Content>();
            }
            this.contents.add(content);
            return this;
        }

        public Builder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public UserMessage build() {
            return new UserMessage(this);
        }
    }
}

