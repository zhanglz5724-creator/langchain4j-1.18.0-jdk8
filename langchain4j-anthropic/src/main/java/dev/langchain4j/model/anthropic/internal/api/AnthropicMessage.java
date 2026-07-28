/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.model.anthropic.internal.api.AnthropicMessageContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicRole;
import java.util.List;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicMessage {
    public AnthropicRole role;
    public List<AnthropicMessageContent> content;

    public AnthropicMessage() {
    }

    public AnthropicMessage(AnthropicRole role, List<AnthropicMessageContent> content) {
        this.role = role;
        this.content = content;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicMessage that = (AnthropicMessage)o;
        return this.role == that.role && Objects.equals(this.content, that.content);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.role, this.content});
    }

    public String toString() {
        return "AnthropicMessage{role=" + (Object)((Object)this.role) + ", content=" + this.content + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AnthropicRole role;
        private List<AnthropicMessageContent> content;

        public Builder role(AnthropicRole role) {
            this.role = role;
            return this;
        }

        public Builder content(List<AnthropicMessageContent> content) {
            this.content = content;
            return this;
        }

        public AnthropicMessage build() {
            return new AnthropicMessage(this.role, this.content);
        }
    }
}

