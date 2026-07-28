/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.skills.DefaultSkillResource;

@Experimental
public interface SkillResource {
    public String relativePath();

    public String content();

    public static DefaultSkillResource.Builder builder() {
        return new DefaultSkillResource.Builder();
    }
}

