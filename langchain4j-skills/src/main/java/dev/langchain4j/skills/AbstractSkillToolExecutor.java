/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.exception.ToolArgumentsException
 *  dev.langchain4j.exception.ToolExecutionException
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.service.tool.ToolExecutor
 */
package dev.langchain4j.skills;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.exception.ToolArgumentsException;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.service.tool.ToolExecutor;
import java.util.Map;

abstract class AbstractSkillToolExecutor
implements ToolExecutor {
    protected final boolean throwToolArgumentsExceptions;

    protected AbstractSkillToolExecutor(boolean throwToolArgumentsExceptions) {
        this.throwToolArgumentsExceptions = throwToolArgumentsExceptions;
    }

    protected Map<String, Object> parseArguments(String json) {
        try {
            return (Map)Json.fromJson((String)json, Map.class);
        }
        catch (Exception e) {
            String message = String.format("Failed to parse tool arguments: '%s' (base64: '%s')", json, Utils.toBase64((String)json));
            this.throwException(message, e);
            return null;
        }
    }

    protected String getRequiredArgument(String argumentName, Map<String, Object> arguments) {
        Object value;
        Object object = value = Utils.isNullOrEmpty(arguments) ? null : arguments.get(argumentName);
        if (value == null) {
            this.throwException(String.format("Missing required tool argument '%s'", argumentName));
        }
        return value.toString();
    }

    protected void throwException(String message) {
        this.throwException(message, null);
    }

    protected void throwException(String message, Exception e) {
        if (this.throwToolArgumentsExceptions) {
            throw e == null ? new ToolArgumentsException(message) : new ToolArgumentsException(message, (Throwable)e);
        }
        throw e == null ? new ToolExecutionException(message) : new ToolExecutionException(message, (Throwable)e);
    }

    public String execute(ToolExecutionRequest request, Object memoryId) {
        throw new IllegalStateException("executeWithContext must be called instead");
    }
}

