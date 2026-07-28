/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonSerialize
 */
package dev.langchain4j.model.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.langchain4j.model.ollama.FormatSerializer;
import dev.langchain4j.model.ollama.Message;
import dev.langchain4j.model.ollama.Options;
import dev.langchain4j.model.ollama.Tool;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
class OllamaChatRequest {
    private String model;
    private List<Message> messages;
    private Options options;
    @JsonSerialize(using=FormatSerializer.class)
    private String format;
    private Boolean stream;
    private List<Tool> tools;
    private Integer keepAlive;
    private Boolean think;

    OllamaChatRequest() {
    }

    OllamaChatRequest(Builder builder) {
        this.model = builder.model;
        this.messages = builder.messages;
        this.options = builder.options;
        this.stream = builder.stream;
        this.tools = builder.tools;
        this.format = builder.format;
        this.keepAlive = builder.keepAlive;
        this.think = builder.think;
    }

    static Builder builder() {
        return new Builder();
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Options getOptions() {
        return this.options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Boolean getStream() {
        return this.stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public List<Tool> getTools() {
        return this.tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public Integer getKeepAlive() {
        return this.keepAlive;
    }

    public void setKeepAlive(Integer keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Boolean getThink() {
        return this.think;
    }

    public void setThink(Boolean think) {
        this.think = think;
    }

    static class Builder {
        private String model;
        private List<Message> messages;
        private Options options;
        private String format;
        private Boolean stream;
        private List<Tool> tools;
        private Integer keepAlive;
        private Boolean think;

        Builder() {
        }

        Builder model(String model) {
            this.model = model;
            return this;
        }

        Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        Builder options(Options options) {
            this.options = options;
            return this;
        }

        Builder format(String format) {
            this.format = format;
            return this;
        }

        Builder stream(Boolean stream) {
            this.stream = stream;
            return this;
        }

        Builder tools(List<Tool> tools) {
            this.tools = tools;
            return this;
        }

        Builder keepAlive(Integer keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        Builder think(Boolean think) {
            this.think = think;
            return this;
        }

        OllamaChatRequest build() {
            return new OllamaChatRequest(this);
        }
    }
}

