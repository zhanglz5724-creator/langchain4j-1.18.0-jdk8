/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.service.tool.ToolProvider
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.skills.DefaultSkill;
import dev.langchain4j.skills.SkillResource;
import java.util.Arrays;
import java.util.List;

@Experimental
public interface Skill {
    public String name();

    public String description();

    public String content();

    default public List<SkillResource> resources() {
        return Arrays.asList(new SkillResource[0]);
    }

    default public List<ToolProvider> toolProviders() {
        return Arrays.asList(new ToolProvider[0]);
    }

    public static DefaultSkill.Builder builder() {
        return new DefaultSkill.Builder();
    }
}

