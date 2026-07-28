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

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@Deprecated
public final class FunctionMessage
implements Message {
    @JsonProperty
    private final Role role = Role.FUNCTION;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String content;

    public FunctionMessage(Builder builder) {
        this.name = builder.name;
        this.content = builder.content;
    }

    @Override
    public Role role() {
        return this.role;
    }

    public String name() {
        return this.name;
    }

    public String content() {
        return this.content;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof FunctionMessage && this.equalTo((FunctionMessage)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(FunctionMessage another) {
        return Objects.equals((Object)this.role, (Object)another.role) && Objects.equals(this.name, another.name) && Objects.equals(this.content, another.content);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode((Object)this.role);
        h += (h << 5) + Objects.hashCode(this.name);
        h += (h << 5) + Objects.hashCode(this.content);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "FunctionMessage{role=" + (Object)((Object)this.role) + ", name=" + this.name + ", content=" + this.content + "}";
    }

    @Deprecated
    public static FunctionMessage from(String name, String content) {
        return FunctionMessage.builder().name(name).content(content).build();
    }

    @Deprecated
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String name;
        private String content;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public FunctionMessage build() {
            return new FunctionMessage(this);
        }
    }
}

