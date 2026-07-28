/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.SearchBehavior
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.service.tool.ToolProvider
 *  dev.langchain4j.service.tool.ToolProviderRequest
 *  dev.langchain4j.service.tool.ToolProviderResult
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.SearchBehavior;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.service.tool.ToolProviderRequest;
import dev.langchain4j.service.tool.ToolProviderResult;
import dev.langchain4j.skills.AbstractSkillToolExecutor;
import dev.langchain4j.skills.ActivateSkillToolConfig;
import dev.langchain4j.skills.ActivateSkillToolExecutor;
import dev.langchain4j.skills.ReadResourceToolConfig;
import dev.langchain4j.skills.ReadResourceToolExecutor;
import dev.langchain4j.skills.Skill;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Experimental
public class Skills {
    private final List<Skill> skills;
    private final ToolProvider toolProvider;
    private final String formattedAvailableSkills;

    public Skills(Builder builder) {
        this.skills = Utils.copy((Collection)ValidationUtils.ensureNotEmpty(builder.skills, (String)"skills"));
        this.toolProvider = this.createToolProvider(builder);
        this.formattedAvailableSkills = Skills.formatAvailableSkills(builder.skills);
    }

    public ToolProvider toolProvider() {
        return this.toolProvider;
    }

    public String formatAvailableSkills() {
        return this.formattedAvailableSkills;
    }

    public static Skills from(Collection<? extends Skill> skills) {
        return Skills.builder().skills(skills).build();
    }

    public static Skills from(Skill ... skills) {
        return Skills.builder().skills(skills).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    private ToolProvider createToolProvider(Builder builder) {
        LinkedHashMap<String, Skill> skillsByName = new LinkedHashMap<String, Skill>();
        this.skills.forEach(skill -> skillsByName.put(skill.name(), (Skill)skill));
        HashMap<ToolSpecification, AbstractSkillToolExecutor> skillManagementTools = new HashMap<ToolSpecification, AbstractSkillToolExecutor>();
        ActivateSkillToolConfig asc = (ActivateSkillToolConfig)Utils.getOrDefault((Object)builder.activateSkillToolConfig, (Object)ActivateSkillToolConfig.builder().build());
        ToolSpecification activateSkillTool = ToolSpecification.builder().name(asc.name).description(asc.description).parameters(JsonObjectSchema.builder().addStringProperty(asc.parameterName, asc.parameterDescription).required(new String[]{asc.parameterName}).build()).addMetadata("searchBehavior", (Object)SearchBehavior.ALWAYS_VISIBLE).build();
        skillManagementTools.put(activateSkillTool, new ActivateSkillToolExecutor(asc, skillsByName));
        boolean hasResources = this.skills.stream().anyMatch(skill -> !skill.resources().isEmpty());
        if (hasResources) {
            ReadResourceToolConfig rrc = (ReadResourceToolConfig)Utils.getOrDefault((Object)builder.readResourceToolConfig, (Object)ReadResourceToolConfig.builder().build());
            ToolSpecification readResourceTool = ToolSpecification.builder().name(rrc.name).description(rrc.description).parameters(JsonObjectSchema.builder().addStringProperty(rrc.skillNameParameterName, rrc.skillNameParameterDescription).addStringProperty(rrc.relativePathParameterName, this.resolveRelativePathParameterDescription(rrc)).required(new String[]{rrc.skillNameParameterName, rrc.relativePathParameterName}).build()).addMetadata("searchBehavior", (Object)SearchBehavior.ALWAYS_VISIBLE).build();
            skillManagementTools.put(readResourceTool, new ReadResourceToolExecutor(rrc, skillsByName));
        }
        final ToolProviderResult skillManagementResult = ToolProviderResult.builder().addAll(skillManagementTools).build();
        final LinkedHashMap skillScopedProviders = new LinkedHashMap();
        for (Map.Entry entry : skillsByName.entrySet()) {
            Skill skill2 = (Skill)entry.getValue();
            List<ToolProvider> delegates = skill2.toolProviders();
            if (delegates == null || delegates.isEmpty()) continue;
            skillScopedProviders.put(entry.getKey(), delegates);
        }
        return new ToolProvider(){

            public ToolProviderResult provideTools(ToolProviderRequest request) {
                if (skillScopedProviders.isEmpty()) {
                    return skillManagementResult;
                }
                Set activatedSkillNames = Skills.getActivatedSkillNames(request.messages());
                if (activatedSkillNames.isEmpty()) {
                    return skillManagementResult;
                }
                LinkedHashMap toolsBySpec = new LinkedHashMap();
                skillManagementResult.aiServiceTools().forEach(tool -> toolsBySpec.put(tool.toolSpecification(), tool));
                for (String skillName : activatedSkillNames) {
                    List delegates = (List)skillScopedProviders.get(skillName);
                    if (delegates == null) continue;
                    for (ToolProvider delegate : delegates) {
                        ToolProviderResult delegateResult = delegate.provideTools(request);
                        if (delegateResult == null) continue;
                        delegateResult.aiServiceTools().forEach(tool -> toolsBySpec.put(tool.toolSpecification(), tool));
                    }
                }
                return ToolProviderResult.builder().addAll(toolsBySpec.values()).build();
            }

            public boolean isDynamic() {
                return !skillScopedProviders.isEmpty();
            }
        };
    }

    private static Set<String> getActivatedSkillNames(List<ChatMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return Collections.emptySet();
        }
        HashSet<String> activated = new HashSet<String>();
        for (ChatMessage message : messages) {
            ToolExecutionResultMessage toolResult;
            Object skillName;
            if (!(message instanceof ToolExecutionResultMessage) || !((skillName = (toolResult = (ToolExecutionResultMessage)message).attributes().get("activated_skill")) instanceof String)) continue;
            activated.add((String)skillName);
        }
        return activated;
    }

    private String resolveRelativePathParameterDescription(ReadResourceToolConfig rrc) {
        if (rrc.relativePathParameterDescription != null) {
            return rrc.relativePathParameterDescription;
        }
        return rrc.relativePathParameterDescriptionProvider.apply(this.skills);
    }

    private static String formatAvailableSkills(Collection<? extends Skill> skills) {
        StringBuilder sb = new StringBuilder();
        sb.append("<available_skills>\n");
        for (Skill skill : skills) {
            sb.append("<skill>\n").append("<name>").append(Skills.escapeXml(skill.name())).append("</name>\n").append("<description>").append(Skills.escapeXml(skill.description())).append("</description>\n").append("</skill>\n");
        }
        sb.append("</available_skills>");
        return sb.toString();
    }

    private static String escapeXml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
    }

    public static class Builder {
        Collection<? extends Skill> skills;
        ActivateSkillToolConfig activateSkillToolConfig;
        ReadResourceToolConfig readResourceToolConfig;

        public Builder skills(Collection<? extends Skill> skills) {
            this.skills = skills;
            return this;
        }

        public Builder skills(Skill ... skills) {
            return this.skills(Arrays.asList(skills));
        }

        public Builder activateSkillToolConfig(ActivateSkillToolConfig activateSkillToolConfig) {
            this.activateSkillToolConfig = activateSkillToolConfig;
            return this;
        }

        public Builder readResourceToolConfig(ReadResourceToolConfig readResourceToolConfig) {
            this.readResourceToolConfig = readResourceToolConfig;
            return this;
        }

        public Skills build() {
            return new Skills(this);
        }
    }
}

