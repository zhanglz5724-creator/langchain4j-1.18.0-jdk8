/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.chat.request;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import java.util.List;

public interface ChatRequestParameters {
    public String modelName();

    public Double temperature();

    public Double topP();

    public Integer topK();

    public Double frequencyPenalty();

    public Double presencePenalty();

    public Integer maxOutputTokens();

    public List<String> stopSequences();

    public List<ToolSpecification> toolSpecifications();

    public ToolChoice toolChoice();

    public ResponseFormat responseFormat();

    public ChatRequestParameters overrideWith(ChatRequestParameters var1);

    default public ChatRequestParameters defaultedBy(ChatRequestParameters parameters) {
        throw new UnsupportedOperationException("Missing implementation, please override this method in " + this.getClass().getName());
    }

    public static DefaultChatRequestParameters.Builder<?> builder() {
        return new DefaultChatRequestParameters.Builder();
    }
}

