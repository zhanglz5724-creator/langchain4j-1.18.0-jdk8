/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;

@Experimental
public class ActivateSkillToolConfig {
    static final String DEFAULT_NAME = "activate_skill";
    static final String DEFAULT_DESCRIPTION = "Returns the full instructions for a skill. Call this before following any skill-specific steps.";
    static final String DEFAULT_PARAMETER_NAME = "skill_name";
    static final String DEFAULT_PARAMETER_DESCRIPTION = "The name of the skill to activate";
    final String name;
    final String description;
    final String parameterName;
    final String parameterDescription;
    final boolean throwToolArgumentsExceptions;

    private ActivateSkillToolConfig(Builder builder) {
        this.name = (String)Utils.getOrDefault((Object)builder.name, (Object)DEFAULT_NAME);
        this.description = (String)Utils.getOrDefault((Object)builder.description, (Object)DEFAULT_DESCRIPTION);
        this.parameterName = (String)Utils.getOrDefault((Object)builder.parameterName, (Object)DEFAULT_PARAMETER_NAME);
        this.parameterDescription = (String)Utils.getOrDefault((Object)builder.parameterDescription, (Object)DEFAULT_PARAMETER_DESCRIPTION);
        this.throwToolArgumentsExceptions = (Boolean)Utils.getOrDefault((Object)builder.throwToolArgumentsExceptions, (Object)false);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private String parameterName;
        private String parameterDescription;
        private Boolean throwToolArgumentsExceptions;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder parameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public Builder parameterDescription(String parameterDescription) {
            this.parameterDescription = parameterDescription;
            return this;
        }

        public Builder throwToolArgumentsExceptions(Boolean throwToolArgumentsExceptions) {
            this.throwToolArgumentsExceptions = throwToolArgumentsExceptions;
            return this;
        }

        public ActivateSkillToolConfig build() {
            return new ActivateSkillToolConfig(this);
        }
    }
}

