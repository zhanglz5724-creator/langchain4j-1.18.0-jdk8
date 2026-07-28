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
import java.util.List;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicContainer {
    public List<AnthropicContainerSkill> skills;

    public AnthropicContainer() {
    }

    public AnthropicContainer(List<AnthropicContainerSkill> skills) {
        this.skills = skills;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AnthropicContainer that = (AnthropicContainer)o;
        return Objects.equals(this.skills, that.skills);
    }

    public int hashCode() {
        return Objects.hash(this.skills);
    }

    public String toString() {
        return "AnthropicContainer{skills=" + this.skills + '}';
    }

    @JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AnthropicContainerSkill {
        public String type;
        public String skillId;
        public String version;

        public AnthropicContainerSkill() {
        }

        public AnthropicContainerSkill(String type, String skillId, String version) {
            this.type = type;
            this.skillId = skillId;
            this.version = version;
        }

        public boolean equals(Object o) {
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            AnthropicContainerSkill that = (AnthropicContainerSkill)o;
            return Objects.equals(this.type, that.type) && Objects.equals(this.skillId, that.skillId) && Objects.equals(this.version, that.version);
        }

        public int hashCode() {
            return Objects.hash(this.type, this.skillId, this.version);
        }

        public String toString() {
            return "AnthropicContainerSkill{type='" + this.type + "', skillId='" + this.skillId + "', version='" + this.version + "'}";
        }
    }
}

