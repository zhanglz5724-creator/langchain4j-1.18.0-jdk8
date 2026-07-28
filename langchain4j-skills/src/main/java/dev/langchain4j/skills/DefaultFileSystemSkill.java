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
import dev.langchain4j.skills.AbstractSkill;
import dev.langchain4j.skills.FileSystemSkill;
import java.nio.file.Path;
import java.util.Objects;

@Experimental
public class DefaultFileSystemSkill
extends AbstractSkill
implements FileSystemSkill {
    private final Path basePath;

    public DefaultFileSystemSkill(Builder builder) {
        super(builder);
        this.basePath = (Path)ValidationUtils.ensureNotNull((Object)builder.basePath, (String)"basePath");
    }

    @Override
    public Path basePath() {
        return this.basePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultFileSystemSkill)) {
            return false;
        }
        DefaultFileSystemSkill that = (DefaultFileSystemSkill)o;
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(this.basePath, that.basePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.basePath);
    }

    @Override
    public String toString() {
        return "DefaultFileSystemSkill { name = " + this.name() + ", description = " + this.description() + ", content = " + this.content() + ", resources = " + this.resources() + ", toolProviders = " + this.toolProviders() + ", basePath = " + this.basePath + " }";
    }

    public Builder toBuilder() {
        return ((Builder)DefaultFileSystemSkill.builder().copyFrom(this)).basePath(this.basePath());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends AbstractSkill.BaseBuilder<Builder> {
        private Path basePath;

        public Builder basePath(Path basePath) {
            this.basePath = basePath;
            return this;
        }

        public DefaultFileSystemSkill build() {
            return new DefaultFileSystemSkill(this);
        }
    }
}

