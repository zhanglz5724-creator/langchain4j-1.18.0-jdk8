/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.service.MemoryId
 *  dev.langchain4j.service.ParameterNameResolver
 *  dev.langchain4j.service.UserMessage
 *  dev.langchain4j.service.V
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.declarative.K;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.ParameterNameResolver;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import java.lang.reflect.Parameter;

public class AgenticParameterNameResolver
implements ParameterNameResolver {
    public boolean hasVariableName(Parameter parameter) {
        return this.getVariableName(parameter) != null;
    }

    public String getVariableName(Parameter parameter) {
        V annotation = parameter.getAnnotation(V.class);
        if (annotation != null) {
            return annotation.value();
        }
        K k = parameter.getAnnotation(K.class);
        if (k != null) {
            return AgentUtil.keyName(k.value());
        }
        if (parameter.getAnnotation(MemoryId.class) != null) {
            return "@MemoryId";
        }
        if (parameter.getAnnotation(UserMessage.class) != null) {
            return "@UserMessage";
        }
        if (parameter.isNamePresent()) {
            return parameter.getName();
        }
        return null;
    }
}

