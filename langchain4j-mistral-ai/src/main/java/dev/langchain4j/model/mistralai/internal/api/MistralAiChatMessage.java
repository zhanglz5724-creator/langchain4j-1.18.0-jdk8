/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContentDeserializer;
import dev.langchain4j.model.mistralai.internal.api.MistralAiRole;
import dev.langchain4j.model.mistralai.internal.api.MistralAiTextContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolCall;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiChatMessageBuilder.class)
public class MistralAiChatMessage {
    private MistralAiRole role;
    private List<MistralAiMessageContent> content;
    private String name;
    private List<MistralAiToolCall> toolCalls;
    private String toolCallId;

    private MistralAiChatMessage(MistralAiChatMessageBuilder builder) {
        this.role = builder.role;
        this.content = builder.content;
        this.name = builder.name;
        this.toolCalls = builder.toolCalls;
        this.toolCallId = builder.toolCallId;
    }

    public MistralAiRole getRole() {
        return this.role;
    }

    public List<MistralAiMessageContent> getContent() {
        return this.content;
    }

    public String asText() {
        if (this.content == null || this.content.isEmpty()) {
            return "";
        }
        if (this.content.size() > 1) {
            throw new UnsupportedOperationException("Cannot convert message with multiple content parts to text");
        }
        return this.content.get(0).asText();
    }

    public String getName() {
        return this.name;
    }

    public String getToolCallId() {
        return this.toolCallId;
    }

    public List<MistralAiToolCall> getToolCalls() {
        return this.toolCalls;
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode((Object)this.role);
        hash = 97 * hash + Objects.hashCode(this.content);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.toolCallId);
        hash = 97 * hash + Objects.hashCode(this.toolCalls);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiChatMessage other = (MistralAiChatMessage)obj;
        return Objects.equals(this.content, other.content) && Objects.equals(this.name, other.name) && this.role == other.role && Objects.equals(this.toolCallId, other.toolCallId) && Objects.equals(this.toolCalls, other.toolCalls);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiChatMessage [", "]").add("role=" + (Object)((Object)this.getRole())).add("content=" + this.getContent()).add("name=" + this.getName()).add("toolCallId=" + this.getToolCallId()).add("toolCalls=" + this.getToolCalls()).toString();
    }

    public static MistralAiChatMessageBuilder builder() {
        return new MistralAiChatMessageBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiChatMessageBuilder {
        private MistralAiRole role;
        private List<MistralAiMessageContent> content;
        private String name;
        private String toolCallId;
        private List<MistralAiToolCall> toolCalls;

        private MistralAiChatMessageBuilder() {
        }

        public MistralAiChatMessageBuilder role(MistralAiRole role) {
            this.role = role;
            return this;
        }

        public MistralAiChatMessageBuilder content(String content) {
            return this.content(Collections.singletonList(new MistralAiTextContent(content)));
        }

        public MistralAiChatMessageBuilder content(MistralAiMessageContent ... content) {
            return this.content(Arrays.asList(content));
        }

        @JsonDeserialize(using=MistralAiMessageContentDeserializer.class)
        public MistralAiChatMessageBuilder content(List<MistralAiMessageContent> content) {
            this.content = content;
            return this;
        }

        public MistralAiChatMessageBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MistralAiChatMessageBuilder toolCallId(String toolCallId) {
            this.toolCallId = toolCallId;
            return this;
        }

        public MistralAiChatMessageBuilder toolCalls(List<MistralAiToolCall> toolCalls) {
            this.toolCalls = toolCalls;
            return this;
        }

        public MistralAiChatMessage build() {
            return new MistralAiChatMessage(this);
        }
    }
}

