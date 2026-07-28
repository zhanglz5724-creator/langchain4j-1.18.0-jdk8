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
import dev.langchain4j.model.anthropic.internal.api.AnthropicMessage;
import dev.langchain4j.model.anthropic.internal.api.AnthropicTextContent;
import dev.langchain4j.model.anthropic.internal.api.AnthropicThinking;
import dev.langchain4j.model.anthropic.internal.api.AnthropicTool;
import java.util.List;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicCountTokensRequest {
    private String model;
    private List<AnthropicMessage> messages;
    private List<AnthropicTextContent> system;
    private List<AnthropicTool> tools;
    private AnthropicThinking thinking;

    public AnthropicCountTokensRequest() {
    }

    private AnthropicCountTokensRequest(Builder builder) {
        this.model = builder.model;
        this.messages = builder.messages;
        this.system = builder.system;
        this.tools = builder.tools;
        this.thinking = builder.thinking;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<AnthropicMessage> getMessages() {
        return this.messages;
    }

    public void setMessages(List<AnthropicMessage> messages) {
        this.messages = messages;
    }

    public List<AnthropicTextContent> getSystem() {
        return this.system;
    }

    public void setSystem(List<AnthropicTextContent> system) {
        this.system = system;
    }

    public List<AnthropicTool> getTools() {
        return this.tools;
    }

    public void setTools(List<AnthropicTool> tools) {
        this.tools = tools;
    }

    public AnthropicThinking getThinking() {
        return this.thinking;
    }

    public void setThinking(AnthropicThinking thinking) {
        this.thinking = thinking;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int hashCode() {
        return Objects.hash(this.model, this.messages, this.system, this.tools, this.thinking);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AnthropicCountTokensRequest)) {
            return false;
        }
        AnthropicCountTokensRequest that = (AnthropicCountTokensRequest)obj;
        return Objects.equals(this.model, that.model) && Objects.equals(this.messages, that.messages) && Objects.equals(this.system, that.system) && Objects.equals(this.tools, that.tools) && Objects.equals(this.thinking, that.thinking);
    }

    public String toString() {
        return "AnthropicCountTokensRequest{model='" + this.model + '\'' + ", messages=" + this.messages + ", system=" + this.system + ", tools=" + this.tools + ", thinking=" + this.thinking + '}';
    }

    public static class Builder {
        private String model;
        private List<AnthropicMessage> messages;
        private List<AnthropicTextContent> system;
        private List<AnthropicTool> tools;
        private AnthropicThinking thinking;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder messages(List<AnthropicMessage> messages) {
            this.messages = messages;
            return this;
        }

        public Builder system(List<AnthropicTextContent> system) {
            this.system = system;
            return this;
        }

        public Builder tools(List<AnthropicTool> tools) {
            this.tools = tools;
            return this;
        }

        public Builder thinking(AnthropicThinking thinking) {
            this.thinking = thinking;
            return this;
        }

        public AnthropicCountTokensRequest build() {
            return new AnthropicCountTokensRequest(this);
        }
    }
}

