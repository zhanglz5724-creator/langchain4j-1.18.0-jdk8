/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ToolExecutionResultMessage
implements ChatMessage {
    private final String id;
    private final String toolName;
    private final List<Content> contents;
    private final Boolean isError;
    private final Map<String, Object> attributes;

    public ToolExecutionResultMessage(Builder builder) {
        boolean hasContents;
        this.id = builder.id;
        this.toolName = builder.toolName;
        boolean hasText = builder.text != null;
        boolean bl = hasContents = builder.contents != null && !builder.contents.isEmpty();
        if (hasText && hasContents) {
            throw new IllegalArgumentException("Either text or contents must be provided, not both");
        }
        if (hasText) {
            this.contents = Collections.singletonList(TextContent.from(builder.text));
        } else if (hasContents) {
            this.contents = Utils.copy(builder.contents);
        } else {
            throw new IllegalArgumentException("Either text or contents must be provided");
        }
        this.isError = builder.isError;
        this.attributes = Utils.copy(builder.attributes);
    }

    public ToolExecutionResultMessage(String id, String toolName, String text) {
        this.id = id;
        this.toolName = toolName;
        this.contents = Collections.singletonList(TextContent.from(ValidationUtils.ensureNotNull(text, "text")));
        this.isError = null;
        this.attributes = Collections.emptyMap();
    }

    public String id() {
        return this.id;
    }

    public String toolName() {
        return this.toolName;
    }

    public String text() {
        if (this.contents.size() == 1 && this.contents.get(0) instanceof TextContent) {
            return ((TextContent)this.contents.get(0)).text();
        }
        throw new IllegalStateException("text() cannot be called when contents contains non-text or multiple content elements. Use contents() instead.");
    }

    @Experimental
    public List<Content> contents() {
        return this.contents;
    }

    @Experimental
    public boolean hasSingleText() {
        return this.contents.size() == 1 && this.contents.get(0) instanceof TextContent;
    }

    public Boolean isError() {
        return this.isError;
    }

    public Map<String, Object> attributes() {
        return this.attributes;
    }

    @Override
    public ChatMessageType type() {
        return ChatMessageType.TOOL_EXECUTION_RESULT;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ToolExecutionResultMessage that = (ToolExecutionResultMessage)o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.toolName, that.toolName) && Objects.equals(this.contents, that.contents) && Objects.equals(this.isError, that.isError) && Objects.equals(this.attributes, that.attributes);
    }

    public int hashCode() {
        return Objects.hash(this.id, this.toolName, this.contents, this.isError, this.attributes);
    }

    public String toString() {
        return "ToolExecutionResultMessage{id='" + this.id + '\'' + ", toolName='" + this.toolName + '\'' + ", contents=" + this.contents + ", isError=" + this.isError + ", attributes=" + this.attributes + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return ToolExecutionResultMessage.builder().id(this.id).toolName(this.toolName).contents(this.contents).isError(this.isError).attributes(this.attributes);
    }

    public static ToolExecutionResultMessage from(ToolExecutionRequest request, String toolExecutionResult) {
        return new ToolExecutionResultMessage(request.id(), request.name(), toolExecutionResult);
    }

    public static ToolExecutionResultMessage from(String id, String toolName, String toolExecutionResult) {
        return new ToolExecutionResultMessage(id, toolName, toolExecutionResult);
    }

    public static ToolExecutionResultMessage toolExecutionResultMessage(ToolExecutionRequest request, String toolExecutionResult) {
        return ToolExecutionResultMessage.from(request, toolExecutionResult);
    }

    public static ToolExecutionResultMessage toolExecutionResultMessage(String id, String toolName, String toolExecutionResult) {
        return ToolExecutionResultMessage.from(id, toolName, toolExecutionResult);
    }

    public static class Builder {
        private String id;
        private String toolName;
        private String text;
        private List<Content> contents;
        private Boolean isError;
        private Map<String, Object> attributes;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder toolName(String toolName) {
            this.toolName = toolName;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        @Experimental
        public Builder contents(List<Content> contents) {
            this.contents = contents;
            return this;
        }

        @Experimental
        public Builder contents(Content ... contents) {
            return this.contents(Arrays.asList(contents));
        }

        public Builder isError(Boolean isError) {
            this.isError = isError;
            return this;
        }

        public Builder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public ToolExecutionResultMessage build() {
            return new ToolExecutionResultMessage(this);
        }
    }
}

