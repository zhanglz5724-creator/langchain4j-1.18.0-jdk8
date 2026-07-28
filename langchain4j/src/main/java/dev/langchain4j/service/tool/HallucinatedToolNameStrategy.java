/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.internal.Exceptions
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.internal.Exceptions;
import java.util.function.Function;

public enum HallucinatedToolNameStrategy implements Function<ToolExecutionRequest, ToolExecutionResultMessage>
{
    THROW_EXCEPTION;


    @Override
    public ToolExecutionResultMessage apply(ToolExecutionRequest toolExecutionRequest) {
        switch (this) {
            case THROW_EXCEPTION: {
                throw Exceptions.runtime((String)"The LLM is trying to execute the '%s' tool, but no such tool exists. Most likely, it is a hallucination. You can override this default strategy by setting the hallucinatedToolNameStrategy on the AiService", (Object[])new Object[]{toolExecutionRequest.name()});
            }
        }
        throw new UnsupportedOperationException();
    }
}

