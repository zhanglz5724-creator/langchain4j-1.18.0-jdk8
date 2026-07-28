/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.service.tool.AiServiceTool
 *  dev.langchain4j.service.tool.ToolExecutor
 *  dev.langchain4j.service.tool.ToolProvider
 *  dev.langchain4j.service.tool.ToolProviderResult
 *  dev.langchain4j.service.tool.ToolService
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.service.tool.AiServiceTool;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.service.tool.ToolProviderResult;
import dev.langchain4j.service.tool.ToolService;
import dev.langchain4j.skills.Skill;
import dev.langchain4j.skills.SkillResource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Experimental
public abstract class AbstractSkill
implements Skill {
    private final String name;
    private final String description;
    private final String content;
    private final List<SkillResource> resources;
    private final List<AiServiceTool> annotatedTools;
    private final List<AiServiceTool> mapTools;
    private final List<ToolProvider> toolProviders;
    private final List<ToolProvider> aggregatedToolProviders;

    protected AbstractSkill(BaseBuilder<?> builder) {
        this.name = ValidationUtils.ensureNotBlank((String)((BaseBuilder)builder).name, (String)"name");
        this.description = ValidationUtils.ensureNotBlank((String)((BaseBuilder)builder).description, (String)"description");
        this.content = ValidationUtils.ensureNotBlank((String)((BaseBuilder)builder).content, (String)"content");
        this.resources = Utils.copy((Collection)((BaseBuilder)builder).resources);
        AbstractSkill.validateUniquePaths(this.resources);
        this.annotatedTools = Utils.copy((List)((BaseBuilder)builder).annotatedTools);
        this.mapTools = Utils.copy((List)((BaseBuilder)builder).mapTools);
        this.toolProviders = Utils.copy((List)((BaseBuilder)builder).toolProviders);
        this.aggregatedToolProviders = AbstractSkill.aggregate(this.annotatedTools, this.mapTools, this.toolProviders);
    }

    private static List<ToolProvider> aggregate(List<AiServiceTool> annotatedTools, List<AiServiceTool> mapTools, List<ToolProvider> toolProviders) {
        ArrayList<AiServiceTool> staticTools = new ArrayList<AiServiceTool>();
        staticTools.addAll(annotatedTools);
        staticTools.addAll(mapTools);
        ArrayList<ToolProvider> result = new ArrayList<ToolProvider>();
        if (!staticTools.isEmpty()) {
            ToolProviderResult staticResult = ToolProviderResult.builder().addAll(staticTools).build();
            result.add(request -> staticResult);
        }
        result.addAll(toolProviders);
        return result;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public String content() {
        return this.content;
    }

    @Override
    public List<SkillResource> resources() {
        return this.resources;
    }

    @Override
    public List<ToolProvider> toolProviders() {
        return this.aggregatedToolProviders;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractSkill)) {
            return false;
        }
        AbstractSkill that = (AbstractSkill)o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.description, that.description) && Objects.equals(this.content, that.content) && Objects.equals(this.resources, that.resources) && Objects.equals(this.annotatedTools, that.annotatedTools) && Objects.equals(this.mapTools, that.mapTools) && Objects.equals(this.toolProviders, that.toolProviders);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.description, this.content, this.resources, this.annotatedTools, this.mapTools, this.toolProviders);
    }

    public String toString() {
        return this.getClass().getSimpleName() + " { name = " + this.name + ", description = " + this.description + ", content = " + this.content + ", resources = " + this.resources + ", toolProviders = " + this.aggregatedToolProviders + " }";
    }

    private static void validateUniquePaths(List<SkillResource> resources) {
        HashSet<String> seenPaths = new HashSet<String>();
        for (SkillResource resource : resources) {
            String path = resource.relativePath();
            if (seenPaths.add(path)) continue;
            throw new IllegalStateException(String.format("Duplicate skill resource path detected: '%s'", path));
        }
    }

    public static abstract class BaseBuilder<B extends BaseBuilder<B>> {
        private String name;
        private String description;
        private String content;
        private Collection<? extends SkillResource> resources;
        private List<AiServiceTool> annotatedTools;
        private List<ToolProvider> toolProviders;
        private List<AiServiceTool> mapTools;

        public B name(String name) {
            this.name = name;
            return (B)this;
        }

        public B description(String description) {
            this.description = description;
            return (B)this;
        }

        public B content(String content) {
            this.content = content;
            return (B)this;
        }

        public B resources(Collection<? extends SkillResource> resources) {
            this.resources = resources;
            return (B)this;
        }

        public B tools(Object ... objectsWithTools) {
            this.annotatedTools = new ArrayList<AiServiceTool>();
            for (Object objectWithTools : objectsWithTools) {
                this.annotatedTools.addAll(ToolService.findTools((Object)objectWithTools));
            }
            return (B)this;
        }

        public B toolProviders(Collection<? extends ToolProvider> toolProviders) {
            this.toolProviders = new ArrayList<ToolProvider>(toolProviders);
            return (B)this;
        }

        public B toolProviders(ToolProvider ... toolProviders) {
            return this.toolProviders(Arrays.asList(toolProviders));
        }

        public B tools(Map<ToolSpecification, ToolExecutor> tools) {
            this.mapTools = new ArrayList<AiServiceTool>();
            for (Map.Entry<ToolSpecification, ToolExecutor> entry : tools.entrySet()) {
                this.mapTools.add(AiServiceTool.builder().toolSpecification(entry.getKey()).toolExecutor(entry.getValue()).build());
            }
            return (B)this;
        }

        protected B copyFrom(AbstractSkill skill) {
            this.name = skill.name;
            this.description = skill.description;
            this.content = skill.content;
            this.resources = skill.resources.isEmpty() ? null : new ArrayList(skill.resources);
            this.annotatedTools = skill.annotatedTools.isEmpty() ? null : new ArrayList(skill.annotatedTools);
            this.mapTools = skill.mapTools.isEmpty() ? null : new ArrayList(skill.mapTools);
            this.toolProviders = skill.toolProviders.isEmpty() ? null : new ArrayList(skill.toolProviders);
            return (B)this;
        }
    }
}

