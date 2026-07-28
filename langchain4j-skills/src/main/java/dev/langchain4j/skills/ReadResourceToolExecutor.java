/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.service.tool.ToolExecutionResult
 */
package dev.langchain4j.skills;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.skills.AbstractSkillToolExecutor;
import dev.langchain4j.skills.ReadResourceToolConfig;
import dev.langchain4j.skills.Skill;
import dev.langchain4j.skills.SkillResource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ReadResourceToolExecutor
extends AbstractSkillToolExecutor {
    private final ReadResourceToolConfig config;
    private final Map<String, Skill> skillsByName;

    ReadResourceToolExecutor(ReadResourceToolConfig config, Map<String, Skill> skillsByName) {
        super(config.throwToolArgumentsExceptions);
        this.config = (ReadResourceToolConfig)ValidationUtils.ensureNotNull((Object)config, (String)"config");
        this.skillsByName = Utils.copy(skillsByName);
    }

    public ToolExecutionResult executeWithContext(ToolExecutionRequest request, InvocationContext context) {
        List resources;
        Map<String, Object> arguments = this.parseArguments(request.arguments());
        String skillName = this.getRequiredArgument(this.config.skillNameParameterName, arguments);
        String relativePath = this.getRequiredArgument(this.config.relativePathParameterName, arguments);
        Skill skill = this.skillsByName.get(skillName);
        if (skill == null) {
            this.throwException(String.format("There is no skill with name '%s'", skillName));
        }
        if ((resources = skill.resources().stream().filter(resource -> resource.relativePath().equals(relativePath)).collect(Collectors.toList())).isEmpty()) {
            String availableResources = skill.resources().stream().map(resource -> "'" + resource.relativePath() + "'").collect(Collectors.joining(", "));
            this.throwException(String.format("There is no resource for skill '%s' with the path '%s'. Available resources: [%s]", skillName, relativePath, availableResources));
        }
        SkillResource resource2 = (SkillResource)resources.get(0);
        return ToolExecutionResult.builder().result((Object)resource2).resultText(resource2.content()).build();
    }
}

