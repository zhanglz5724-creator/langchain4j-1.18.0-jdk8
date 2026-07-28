/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.skills.SkillResource;
import java.util.Objects;

@Experimental
public class DefaultSkillResource
implements SkillResource {
    private final String relativePath;
    private final String content;

    public DefaultSkillResource(Builder builder) {
        this.relativePath = ValidationUtils.ensureNotBlank((String)builder.relativePath, (String)"relativePath");
        this.content = ValidationUtils.ensureNotBlank((String)builder.content, (String)"content");
    }

    @Override
    public String relativePath() {
        return this.relativePath;
    }

    @Override
    public String content() {
        return this.content;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSkillResource)) {
            return false;
        }
        DefaultSkillResource that = (DefaultSkillResource)o;
        return Objects.equals(this.relativePath, that.relativePath) && Objects.equals(this.content, that.content);
    }

    public int hashCode() {
        return Objects.hash(this.relativePath, this.content);
    }

    public String toString() {
        return "DefaultSkillResource { relativePath = " + this.relativePath + ", content = " + this.content + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String relativePath;
        private String content;

        public Builder relativePath(String relativePath) {
            this.relativePath = relativePath;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public DefaultSkillResource build() {
            return new DefaultSkillResource(this);
        }
    }
}

