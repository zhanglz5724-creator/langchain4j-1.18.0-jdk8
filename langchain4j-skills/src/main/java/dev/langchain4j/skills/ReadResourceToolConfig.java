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
import dev.langchain4j.skills.Skill;
import dev.langchain4j.skills.SkillResource;
import java.util.List;
import java.util.function.Function;

@Experimental
public class ReadResourceToolConfig {
    static final String DEFAULT_NAME = "read_skill_resource";
    static final String DEFAULT_DESCRIPTION = "Returns the content of a resource referenced in the skill";
    static final String DEFAULT_SKILL_NAME_PARAMETER_NAME = "skill_name";
    static final String DEFAULT_SKILL_NAME_PARAMETER_DESCRIPTION = "The name of the skill for which the resource should be read";
    static final String DEFAULT_RELATIVE_PATH_PARAMETER_NAME = "relative_path";
    static final Function<List<? extends Skill>, String> DEFAULT_RELATIVE_PATH_PARAMETER_DESCRIPTION_PROVIDER = skills -> "Relative path to the resource. For example: " + skills.stream().flatMap(skill -> skill.resources().stream()).findFirst().map(SkillResource::relativePath).orElseThrow(() -> new IllegalStateException("No skill resources found to use for the relative path description"));
    final String name;
    final String description;
    final String skillNameParameterName;
    final String skillNameParameterDescription;
    final String relativePathParameterName;
    final String relativePathParameterDescription;
    final Function<List<? extends Skill>, String> relativePathParameterDescriptionProvider;
    final boolean throwToolArgumentsExceptions;

    private ReadResourceToolConfig(Builder builder) {
        this.name = (String)Utils.getOrDefault((Object)builder.name, (Object)DEFAULT_NAME);
        this.description = (String)Utils.getOrDefault((Object)builder.description, (Object)DEFAULT_DESCRIPTION);
        this.skillNameParameterName = (String)Utils.getOrDefault((Object)builder.skillNameParameterName, (Object)DEFAULT_SKILL_NAME_PARAMETER_NAME);
        this.skillNameParameterDescription = (String)Utils.getOrDefault((Object)builder.skillNameParameterDescription, (Object)DEFAULT_SKILL_NAME_PARAMETER_DESCRIPTION);
        this.relativePathParameterName = (String)Utils.getOrDefault((Object)builder.relativePathParameterName, (Object)DEFAULT_RELATIVE_PATH_PARAMETER_NAME);
        this.relativePathParameterDescription = builder.relativePathParameterDescription;
        this.relativePathParameterDescriptionProvider = (Function)Utils.getOrDefault((Object)builder.relativePathParameterDescriptionProvider, DEFAULT_RELATIVE_PATH_PARAMETER_DESCRIPTION_PROVIDER);
        this.throwToolArgumentsExceptions = (Boolean)Utils.getOrDefault((Object)builder.throwToolArgumentsExceptions, (Object)false);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private String skillNameParameterName;
        private String skillNameParameterDescription;
        private String relativePathParameterName;
        private String relativePathParameterDescription;
        private Function<List<? extends Skill>, String> relativePathParameterDescriptionProvider;
        private Boolean throwToolArgumentsExceptions;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder skillNameParameterName(String skillNameParameterName) {
            this.skillNameParameterName = skillNameParameterName;
            return this;
        }

        public Builder skillNameParameterDescription(String skillNameParameterDescription) {
            this.skillNameParameterDescription = skillNameParameterDescription;
            return this;
        }

        public Builder relativePathParameterName(String relativePathParameterName) {
            this.relativePathParameterName = relativePathParameterName;
            return this;
        }

        public Builder relativePathParameterDescription(String relativePathParameterDescription) {
            this.relativePathParameterDescription = relativePathParameterDescription;
            return this;
        }

        public Builder relativePathParameterDescriptionProvider(Function<List<? extends Skill>, String> relativePathParameterDescriptionProvider) {
            this.relativePathParameterDescriptionProvider = relativePathParameterDescriptionProvider;
            return this;
        }

        public Builder throwToolArgumentsExceptions(Boolean throwToolArgumentsExceptions) {
            this.throwToolArgumentsExceptions = throwToolArgumentsExceptions;
            return this;
        }

        public ReadResourceToolConfig build() {
            return new ReadResourceToolConfig(this);
        }
    }
}

