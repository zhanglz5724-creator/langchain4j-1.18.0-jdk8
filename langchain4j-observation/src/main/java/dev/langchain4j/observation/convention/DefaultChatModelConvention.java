/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.listener.ChatModelErrorContext
 *  dev.langchain4j.model.chat.listener.ChatModelRequestContext
 *  dev.langchain4j.model.chat.listener.ChatModelResponseContext
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.TokenUsage
 *  io.micrometer.common.KeyValue
 *  io.micrometer.common.KeyValues
 *  io.micrometer.common.docs.KeyName
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.observation.convention;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.observation.context.ChatModelObservationContext;
import dev.langchain4j.observation.convention.ChatModelConvention;
import dev.langchain4j.observation.convention.ChatModelDocumentation;
import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import io.micrometer.common.docs.KeyName;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public class DefaultChatModelConvention
implements ChatModelConvention {
    private static final String OUTCOME_SUCCESS = "SUCCESS";
    private static final String OUTCOME_ERROR = "ERROR";
    static final String UNKNOWN = "unknown";
    public static final String OPERATION_VALUE_CHAT = "chat";

    public @Nullable String getName() {
        return "gen_ai.client.operation.duration";
    }

    public @Nullable String getContextualName(ChatModelObservationContext context) {
        return "chat " + Optional.ofNullable(context.getRequestContext()).map(ChatModelRequestContext::chatRequest).map(ChatRequest::parameters).map(ChatRequestParameters::modelName).orElse(UNKNOWN);
    }

    public KeyValues getLowCardinalityKeyValues(ChatModelObservationContext context) {
        ChatModelRequestContext requestContext = context.getRequestContext();
        ChatModelResponseContext responseContext = context.getResponseContext();
        ChatModelErrorContext errorContext = context.getErrorContext();
        KeyValues result = KeyValues.of((KeyValue[])new KeyValue[]{KeyValue.of((KeyName)ChatModelDocumentation.LowCardinalityValues.OPERATION_NAME, (String)OPERATION_VALUE_CHAT)});
        result = Optional.ofNullable(requestContext).map(ChatModelRequestContext::modelProvider).map(p -> KeyValue.of((KeyName)ChatModelDocumentation.LowCardinalityValues.PROVIDER_NAME, (String)p.name())).map(xva$0 -> result.and(new KeyValue[]{xva$0})).orElse(result.and(new KeyValue[]{KeyValue.of((KeyName)ChatModelDocumentation.LowCardinalityValues.PROVIDER_NAME, (String)UNKNOWN)}));
        result = Optional.ofNullable(requestContext).map(ChatModelRequestContext::chatRequest).map(ChatRequest::parameters).map(ChatRequestParameters::modelName).map(m -> KeyValue.of((KeyName)ChatModelDocumentation.LowCardinalityValues.REQUEST_MODEL, (String)m)).map(xva$0 -> result.and(new KeyValue[]{xva$0})).orElse(result.and(new KeyValue[]{KeyValue.of((KeyName)ChatModelDocumentation.LowCardinalityValues.REQUEST_MODEL, (String)UNKNOWN)}));
        result = Optional.ofNullable(responseContext).map(ChatModelResponseContext::chatResponse).map(ChatResponse::metadata).map(ChatResponseMetadata::modelName).map(m -> KeyValue.of((KeyName)ChatModelDocumentation.LowCardinalityValues.RESPONSE_MODEL, (String)m)).map(xva$0 -> result.and(new KeyValue[]{xva$0})).orElse(result.and(new KeyValue[]{KeyValue.of((KeyName)ChatModelDocumentation.LowCardinalityValues.RESPONSE_MODEL, (String)UNKNOWN)}));
        result = errorContext != null && errorContext.error() != null ? result.and(new KeyValue[]{KeyValue.of((String)ChatModelDocumentation.LowCardinalityValues.OUTCOME.asString(), (String)OUTCOME_ERROR)}) : result.and(new KeyValue[]{KeyValue.of((String)ChatModelDocumentation.LowCardinalityValues.OUTCOME.asString(), (String)OUTCOME_SUCCESS)});
        return result;
    }

    public KeyValues getHighCardinalityKeyValues(ChatModelObservationContext context) {
        ChatModelResponseContext responseContext = context.getResponseContext();
        KeyValues result = KeyValues.empty();
        result = Optional.ofNullable(responseContext).map(ChatModelResponseContext::chatResponse).map(ChatResponse::tokenUsage).map(TokenUsage::outputTokenCount).map(tokens -> KeyValue.of((String)ChatModelDocumentation.HighCardinalityValues.OUTPUT_TOKENS.asString(), (String)("" + tokens))).map(xva$0 -> result.and(new KeyValue[]{xva$0})).orElse(result);
        result = Optional.ofNullable(responseContext).map(ChatModelResponseContext::chatResponse).map(ChatResponse::tokenUsage).map(TokenUsage::inputTokenCount).map(tokens -> KeyValue.of((String)ChatModelDocumentation.HighCardinalityValues.INPUT_TOKENS.asString(), (String)("" + tokens))).map(xva$0 -> result.and(new KeyValue[]{xva$0})).orElse(result);
        return result;
    }
}

