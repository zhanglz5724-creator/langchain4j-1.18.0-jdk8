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

@JsonDeserialize(builder=SystemMessage.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class SystemMessage
implements Message {
    @JsonProperty
    private final Role role = Role.SYSTEM;
    @JsonProperty
    private final String content;
    @JsonProperty
    private final String name;

    public SystemMessage(Builder builder) {
        this.content = builder.content;
        this.name = builder.name;
    }

    @Override
    public Role role() {
        return this.role;
    }

    public String content() {
        return this.content;
    }

    public String name() {
        return this.name;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof SystemMessage && this.equalTo((SystemMessage)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(SystemMessage another) {
        return Objects.equals((Object)this.role, (Object)another.role) && Objects.equals(this.content, another.content) && Objects.equals(this.name, another.name);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode((Object)this.role);
        h += (h << 5) + Objects.hashCode(this.content);
        h += (h << 5) + Objects.hashCode(this.name);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "SystemMessage{role=" + (Object)((Object)this.role) + ", content=" + this.content + ", name=" + this.name + "}";
    }

    public static SystemMessage from(String content) {
        return SystemMessage.builder().content(content).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String content;
        private String name;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public SystemMessage build() {
            return new SystemMessage(this);
        }
    }
}

