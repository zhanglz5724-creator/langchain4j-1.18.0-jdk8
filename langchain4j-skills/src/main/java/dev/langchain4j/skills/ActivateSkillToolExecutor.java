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
import dev.langchain4j.skills.ActivateSkillToolConfig;
import dev.langchain4j.skills.Skill;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

class ActivateSkillToolExecutor
extends AbstractSkillToolExecutor {
    static final String ACTIVATED_SKILL_ATTRIBUTE = "activated_skill";
    private final ActivateSkillToolConfig config;
    private final Map<String, Skill> skillsByName;

    ActivateSkillToolExecutor(ActivateSkillToolConfig config, Map<String, Skill> skillsByName) {
        super(config.throwToolArgumentsExceptions);
        this.config = (ActivateSkillToolConfig)ValidationUtils.ensureNotNull((Object)config, (String)"config");
        this.skillsByName = Utils.copy(skillsByName);
    }

    public ToolExecutionResult executeWithContext(ToolExecutionRequest request, InvocationContext context) {
        Map<String, Object> arguments = this.parseArguments(request.arguments());
        String skillName = this.getRequiredArgument(this.config.parameterName, arguments);
        Skill skill = this.skillsByName.get(skillName);
        if (skill == null) {
            String availableSkillNames = this.skillsByName.keySet().stream().map(name -> "'" + name + "'").collect(Collectors.joining(", "));
            this.throwException(String.format("There is no skill with name '%s'. Available skills: [%s]", skillName, availableSkillNames));
        }
        return ToolExecutionResult.builder().result((Object)skill).resultText(skill.content()).attributes(Collections.singletonMap(ACTIVATED_SKILL_ATTRIBUTE, skillName)).build();
    }
}

