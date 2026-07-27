/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AiMessage
implements ChatMessage {
    public static final String GENERATED_IMAGES_KEY = "generated_images";
    private final String text;
    private final String thinking;
    private final List<ToolExecutionRequest> toolExecutionRequests;
    private final Map<String, Object> attributes;

    public AiMessage(String text) {
        this.text = ValidationUtils.ensureNotNull(text, "text");
        this.thinking = null;
        this.toolExecutionRequests = Collections.emptyList();
        this.attributes = Collections.emptyMap();
    }

    public AiMessage(List<ToolExecutionRequest> toolExecutionRequests) {
        this.text = null;
        this.thinking = null;
        this.toolExecutionRequests = ValidationUtils.ensureNotEmpty(toolExecutionRequests, "toolExecutionRequests");
        this.attributes = Collections.emptyMap();
    }

    public AiMessage(String text, List<ToolExecutionRequest> toolExecutionRequests) {
        this.text = text;
        this.thinking = null;
        this.toolExecutionRequests = Utils.copy(toolExecutionRequests);
        this.attributes = Collections.emptyMap();
    }

    public AiMessage(Builder builder) {
        this.text = builder.text;
        this.thinking = builder.thinking;
        this.toolExecutionRequests = Utils.copy(builder.toolExecutionRequests);
        this.attributes = Utils.copy(builder.attributes);
    }

    public AiMessage withText(String text) {
        return AiMessage.builder().text(text).thinking(this.thinking).toolExecutionRequests(this.toolExecutionRequests).attributes(this.attributes).build();
    }

    public String text() {
        return this.text;
    }

    @Experimental
    public String thinking() {
        return this.thinking;
    }

    public List<Image> images() {
        return (List<Image>) this.attributes.getOrDefault(GENERATED_IMAGES_KEY, Collections.emptyList());
    }

    public List<ToolExecutionRequest> toolExecutionRequests() {
        return this.toolExecutionRequests;
    }

    public boolean hasToolExecutionRequests() {
        return !Utils.isNullOrEmpty(this.toolExecutionRequests);
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
        return ChatMessageType.AI;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AiMessage that = (AiMessage)o;
        return Objects.equals(this.text, that.text) && Objects.equals(this.thinking, that.thinking) && Objects.equals(this.toolExecutionRequests, that.toolExecutionRequests) && Objects.equals(this.attributes, that.attributes);
    }

    public int hashCode() {
        return Objects.hash(this.text, this.thinking, this.toolExecutionRequests, this.attributes);
    }

    public String toString() {
        return "AiMessage { text = " + Utils.quoted(this.text) + ", thinking = " + Utils.quoted(this.thinking) + ", toolExecutionRequests = " + this.toolExecutionRequests + ", attributes = " + this.attributes + " }";
    }

    public Builder toBuilder() {
        return AiMessage.builder().text(this.text).thinking(this.thinking).toolExecutionRequests(this.toolExecutionRequests).attributes(this.attributes);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static AiMessage from(String text) {
        return new AiMessage(text);
    }

    public static AiMessage from(ToolExecutionRequest ... toolExecutionRequests) {
        return AiMessage.from(Arrays.asList(toolExecutionRequests));
    }

    public static AiMessage from(List<ToolExecutionRequest> toolExecutionRequests) {
        return new AiMessage(toolExecutionRequests);
    }

    public static AiMessage from(String text, List<ToolExecutionRequest> toolExecutionRequests) {
        return new AiMessage(text, toolExecutionRequests);
    }

    public static AiMessage aiMessage(String text) {
        return AiMessage.from(text);
    }

    public static AiMessage aiMessage(ToolExecutionRequest ... toolExecutionRequests) {
        return AiMessage.aiMessage(Arrays.asList(toolExecutionRequests));
    }

    public static AiMessage aiMessage(List<ToolExecutionRequest> toolExecutionRequests) {
        return AiMessage.from(toolExecutionRequests);
    }

    public static AiMessage aiMessage(String text, List<ToolExecutionRequest> toolExecutionRequests) {
        return AiMessage.from(text, toolExecutionRequests);
    }

    public static class Builder {
        private String text;
        private String thinking;
        private List<ToolExecutionRequest> toolExecutionRequests;
        private Map<String, Object> attributes;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        @Experimental
        public Builder thinking(String thinking) {
            this.thinking = thinking;
            return this;
        }

        public Builder toolExecutionRequests(List<ToolExecutionRequest> toolExecutionRequests) {
            this.toolExecutionRequests = toolExecutionRequests;
            return this;
        }

        @Experimental
        public Builder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public AiMessage build() {
            return new AiMessage(this);
        }
    }
}

