/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.skills.DefaultFileSystemSkill;
import dev.langchain4j.skills.Skill;
import java.nio.file.Path;

@Experimental
public interface FileSystemSkill
extends Skill {
    public Path basePath();

    public static DefaultFileSystemSkill.Builder builder() {
        return new DefaultFileSystemSkill.Builder();
    }
}

