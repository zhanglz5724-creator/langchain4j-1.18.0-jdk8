/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContentDeserializer;
import dev.langchain4j.model.mistralai.internal.api.MistralAiRole;
import dev.langchain4j.model.mistralai.internal.api.MistralAiToolCall;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MistralAiDeltaMessage {
    private MistralAiRole role;
    @JsonDeserialize(using=MistralAiMessageContentDeserializer.class)
    private List<MistralAiMessageContent> content;
    private List<MistralAiToolCall> toolCalls;

    @JsonCreator
    public MistralAiDeltaMessage(@JsonProperty(value="role") MistralAiRole role, @JsonProperty(value="content") List<MistralAiMessageContent> content, @JsonProperty(value="tool_calls") List<MistralAiToolCall> toolCalls) {
        this.role = role;
        this.content = content;
        this.toolCalls = toolCalls;
    }

    public MistralAiRole getRole() {
        return this.role;
    }

    public List<MistralAiMessageContent> getContent() {
        return this.content;
    }

    public List<MistralAiToolCall> getToolCalls() {
        return this.toolCalls;
    }

    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode((Object)this.role);
        hash = 23 * hash + Objects.hashCode(this.content);
        hash = 23 * hash + Objects.hashCode(this.toolCalls);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiDeltaMessage other = (MistralAiDeltaMessage)obj;
        return Objects.equals(this.content, other.content) && this.role == other.role && Objects.equals(this.toolCalls, other.toolCalls);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiDeltaMessage [", "]").add("role=" + (Object)((Object)this.getRole())).add("content=" + this.getContent()).add("toolCalls=" + this.getToolCalls()).toString();
    }
}

