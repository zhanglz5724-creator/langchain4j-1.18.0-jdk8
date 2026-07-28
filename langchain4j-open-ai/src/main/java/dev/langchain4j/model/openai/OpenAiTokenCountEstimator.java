/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.knuddels.jtokkit.Encodings
 *  com.knuddels.jtokkit.api.Encoding
 *  com.knuddels.jtokkit.api.EncodingRegistry
 *  com.knuddels.jtokkit.api.EncodingType
 *  com.knuddels.jtokkit.api.IntArrayList
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.TokenCountEstimator
 */
package dev.langchain4j.model.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.IntArrayList;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.model.openai.OpenAiLanguageModelName;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class OpenAiTokenCountEstimator
implements TokenCountEstimator {
    private static final EncodingRegistry ENCODING_REGISTRY = Encodings.newDefaultEncodingRegistry();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String modelName;
    private final Encoding encoding;

    public OpenAiTokenCountEstimator(OpenAiChatModelName modelName) {
        this(modelName.toString());
    }

    public OpenAiTokenCountEstimator(OpenAiEmbeddingModelName modelName) {
        this(modelName.toString());
    }

    public OpenAiTokenCountEstimator(OpenAiLanguageModelName modelName) {
        this(modelName.toString());
    }

    public OpenAiTokenCountEstimator(String modelName) {
        this.modelName = ValidationUtils.ensureNotBlank((String)modelName, (String)"modelName");
        this.encoding = modelName.startsWith("o") || modelName.startsWith("gpt-4.") || modelName.startsWith("gpt-5") ? ENCODING_REGISTRY.getEncoding(EncodingType.O200K_BASE) : (Encoding)ENCODING_REGISTRY.getEncodingForModel(modelName).orElseThrow(this.unknownModelException());
    }

    public int estimateTokenCountInText(String text) {
        return this.encoding.countTokensOrdinary(text);
    }

    public int estimateTokenCountInMessage(ChatMessage message) {
        int tokenCount = 1;
        tokenCount += 3;
        if (message instanceof SystemMessage) {
            tokenCount += this.estimateTokenCountIn((SystemMessage)message);
        } else if (message instanceof UserMessage) {
            tokenCount += this.estimateTokenCountIn((UserMessage)message);
        } else if (message instanceof AiMessage) {
            tokenCount += this.estimateTokenCountIn((AiMessage)message);
        } else if (message instanceof ToolExecutionResultMessage) {
            tokenCount += this.estimateTokenCountIn((ToolExecutionResultMessage)message);
        } else {
            throw new IllegalArgumentException("Unknown message type: " + message);
        }
        return tokenCount;
    }

    private int estimateTokenCountIn(SystemMessage systemMessage) {
        return this.estimateTokenCountInText(systemMessage.text());
    }

    private int estimateTokenCountIn(UserMessage userMessage) {
        int tokenCount = 0;
        for (Content content : userMessage.contents()) {
            if (content instanceof TextContent) {
                tokenCount += this.estimateTokenCountInText(((TextContent)content).text());
                continue;
            }
            throw Exceptions.illegalArgument((String)("Unknown content type: " + content), (Object[])new Object[0]);
        }
        if (userMessage.name() != null) {
            ++tokenCount;
            tokenCount += this.estimateTokenCountInText(userMessage.name());
        }
        return tokenCount;
    }

    private int estimateTokenCountIn(AiMessage aiMessage) {
        int tokenCount = 0;
        if (aiMessage.text() != null) {
            tokenCount += this.estimateTokenCountInText(aiMessage.text());
        }
        if (aiMessage.hasToolExecutionRequests()) {
            tokenCount += 6;
            if (aiMessage.toolExecutionRequests().size() == 1) {
                --tokenCount;
                ToolExecutionRequest toolExecutionRequest = (ToolExecutionRequest)aiMessage.toolExecutionRequests().get(0);
                tokenCount += this.estimateTokenCountInText(toolExecutionRequest.name()) * 2;
                tokenCount += this.estimateTokenCountInText(toolExecutionRequest.arguments());
            } else {
                tokenCount += 15;
                for (ToolExecutionRequest toolExecutionRequest : aiMessage.toolExecutionRequests()) {
                    tokenCount += 7;
                    tokenCount += this.estimateTokenCountInText(toolExecutionRequest.name());
                    if (Utils.isNullOrBlank((String)toolExecutionRequest.arguments())) continue;
                    try {
                        Map arguments = (Map)OBJECT_MAPPER.readValue(toolExecutionRequest.arguments(), Map.class);
                        for (Map.Entry argument : arguments.entrySet()) {
                            tokenCount += 2;
                            tokenCount += this.estimateTokenCountInText(String.valueOf(argument.getKey()));
                            tokenCount += this.estimateTokenCountInText(String.valueOf(argument.getValue()));
                        }
                    }
                    catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if (this.modelName.startsWith("o4")) {
            tokenCount += 2;
        }
        return tokenCount;
    }

    private int estimateTokenCountIn(ToolExecutionResultMessage toolExecutionResultMessage) {
        return this.estimateTokenCountInText(toolExecutionResultMessage.text());
    }

    public int estimateTokenCountInMessages(Iterable<ChatMessage> messages) {
        int tokenCount = 3;
        for (ChatMessage message : messages) {
            tokenCount += this.estimateTokenCountInMessage(message);
        }
        if (this.modelName.startsWith("o")) {
            --tokenCount;
        }
        return tokenCount;
    }

    public List<Integer> encode(String text) {
        return this.encoding.encodeOrdinary(text).boxed();
    }

    public List<Integer> encode(String text, int maxTokensToEncode) {
        return this.encoding.encodeOrdinary(text, maxTokensToEncode).getTokens().boxed();
    }

    public String decode(List<Integer> tokens) {
        IntArrayList intArrayList = new IntArrayList();
        for (Integer token : tokens) {
            intArrayList.add(token.intValue());
        }
        return this.encoding.decode(intArrayList);
    }

    private Supplier<IllegalArgumentException> unknownModelException() {
        return () -> Exceptions.illegalArgument((String)"Model '%s' is unknown to jtokkit", (Object[])new Object[]{this.modelName});
    }
}

