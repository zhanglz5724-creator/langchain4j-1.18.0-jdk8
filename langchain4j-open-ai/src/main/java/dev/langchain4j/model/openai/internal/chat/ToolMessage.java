/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.model.openai.internal.chat.Role;
import java.util.Objects;

@JsonDeserialize(builder=ToolMessage.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class ToolMessage
implements Message {
    @JsonProperty
    private final Role role = Role.TOOL;
    @JsonProperty
    private final String toolCallId;
    @JsonProperty
    private final String content;

    public ToolMessage(Builder builder) {
        this.toolCallId = builder.toolCallId;
        this.content = builder.content;
    }

    @Override
    public Role role() {
        return this.role;
    }

    public String toolCallId() {
        return this.toolCallId;
    }

    public String content() {
        return this.content;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof ToolMessage && this.equalTo((ToolMessage)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(ToolMessage another) {
        return Objects.equals((Object)this.role, (Object)another.role) && Objects.equals(this.toolCallId, another.toolCallId) && Objects.equals(this.content, another.content);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode((Object)this.role);
        h += (h << 5) + Objects.hashCode(this.toolCallId);
        h += (h << 5) + Objects.hashCode(this.content);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ToolMessage{role=" + (Object)((Object)this.role) + ", toolCallId=" + this.toolCallId + ", content=" + this.content + "}";
    }

    public static ToolMessage from(String toolCallId, String content) {
        return ToolMessage.builder().toolCallId(toolCallId).content(content).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String toolCallId;
        private String content;

        public Builder toolCallId(String toolCallId) {
            this.toolCallId = toolCallId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public ToolMessage build() {
            return new ToolMessage(this);
        }
    }
}

