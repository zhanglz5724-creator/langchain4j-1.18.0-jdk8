/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.tokenization.TokenizationParameters
 *  com.ibm.watsonx.ai.tokenization.TokenizationResponse
 *  com.ibm.watsonx.ai.tokenization.TokenizationResponse$Result
 *  com.ibm.watsonx.ai.tokenization.TokenizationService
 *  com.ibm.watsonx.ai.tokenization.TokenizationService$Builder
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageType
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.TokenCountEstimator
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.tokenization.TokenizationParameters;
import com.ibm.watsonx.ai.tokenization.TokenizationResponse;
import com.ibm.watsonx.ai.tokenization.TokenizationService;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.watsonx.WatsonxBuilder;
import dev.langchain4j.model.watsonx.WatsonxExceptionMapper;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class WatsonxTokenCountEstimator
implements TokenCountEstimator {
    private final TokenizationService tokenizationService;

    private WatsonxTokenCountEstimator(Builder builder) {
        TokenizationService.Builder tokenizationServiceBuilder = Objects.nonNull(builder.authenticator) ? (TokenizationService.Builder)TokenizationService.builder().authenticator(builder.authenticator) : (TokenizationService.Builder)TokenizationService.builder().apiKey(builder.apiKey);
        this.tokenizationService = ((TokenizationService.Builder)((TokenizationService.Builder)((TokenizationService.Builder)((TokenizationService.Builder)((TokenizationService.Builder)((TokenizationService.Builder)((TokenizationService.Builder)((TokenizationService.Builder)((TokenizationService.Builder)((TokenizationService.Builder)tokenizationServiceBuilder.baseUrl(builder.baseUrl)).modelId(builder.modelName)).version(builder.version)).projectId(builder.projectId)).spaceId(builder.spaceId)).timeout(builder.timeout)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).httpClient(builder.httpClient)).verifySsl(builder.verifySsl)).build();
    }

    public int estimateTokenCountInText(String text) {
        return this.estimateTokenCountInText(text, null);
    }

    public int estimateTokenCountInText(String text, TokenizationParameters parameters) {
        return (Integer)WatsonxExceptionMapper.INSTANCE.withExceptionMapper(() -> this.tokenizationService.tokenize(text, parameters).result().tokenCount());
    }

    public int estimateTokenCountInMessage(ChatMessage message) {
        return (Integer)WatsonxExceptionMapper.INSTANCE.withExceptionMapper(() -> switch (message.type()) {
            default -> throw new IncompatibleClassChangeError();
            case ChatMessageType.SYSTEM -> {
                SystemMessage systemMessage = (SystemMessage)message;
                yield this.estimateTokenCountInText(systemMessage.text());
            }
            case ChatMessageType.AI -> {
                AiMessage aiMessage = (AiMessage)message;
                ArrayList<CompletableFuture> futures = new ArrayList<CompletableFuture>();
                if (Utils.isNotNullOrEmpty((String)aiMessage.thinking())) {
                    futures.add(this.tokenizationService.tokenizeAsync(aiMessage.thinking()));
                }
                if (Utils.isNotNullOrEmpty((String)aiMessage.text())) {
                    futures.add(this.tokenizationService.tokenizeAsync(aiMessage.text()));
                }
                if (aiMessage.hasToolExecutionRequests()) {
                    for (ToolExecutionRequest toolExecutionRequest : aiMessage.toolExecutionRequests()) {
                        futures.add(this.tokenizationService.tokenizeAsync(toolExecutionRequest.id()));
                        futures.add(this.tokenizationService.tokenizeAsync(toolExecutionRequest.name()));
                        if (Utils.isNullOrBlank((String)toolExecutionRequest.arguments())) continue;
                        futures.add(this.tokenizationService.tokenizeAsync(toolExecutionRequest.arguments()));
                    }
                }
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                yield futures.stream().map(CompletableFuture::join).map(TokenizationResponse::result).mapToInt(TokenizationResponse.Result::tokenCount).sum();
            }
            case ChatMessageType.USER -> {
                UserMessage userMessage = (UserMessage)message;
                ArrayList<CompletableFuture> futures = new ArrayList<CompletableFuture>();
                if (Utils.isNotNullOrBlank((String)userMessage.name())) {
                    futures.add(this.tokenizationService.tokenizeAsync(userMessage.name()));
                }
                for (Content content : userMessage.contents()) {
                    switch (content.type()) {
                        case TEXT: {
                            futures.add(this.tokenizationService.tokenizeAsync(((TextContent)content).text()));
                            break;
                        }
                        case AUDIO: 
                        case IMAGE: 
                        case PDF: 
                        case VIDEO: {
                            throw new UnsupportedOperationException("The " + content.type().name() + " content type is not supported in WatsonxTokenCountEstimator");
                        }
                    }
                }
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                yield futures.stream().map(CompletableFuture::join).map(TokenizationResponse::result).mapToInt(TokenizationResponse.Result::tokenCount).sum();
            }
            case ChatMessageType.TOOL_EXECUTION_RESULT -> {
                ToolExecutionResultMessage toolExecutionResult = (ToolExecutionResultMessage)message;
                yield this.estimateTokenCountInText(toolExecutionResult.text());
            }
            case ChatMessageType.CUSTOM -> throw new UnsupportedOperationException("The custom message type is not supported in WatsonxTokenCountEstimator");
        });
    }

    public int estimateTokenCountInMessages(Iterable<ChatMessage> messages) {
        int tokenCount = 0;
        for (ChatMessage chatMessage : messages) {
            tokenCount += this.estimateTokenCountInMessage(chatMessage);
        }
        return tokenCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends WatsonxBuilder<Builder> {
        private String modelName;

        private Builder() {
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        @Override
        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        @Override
        public Builder spaceId(String spaceId) {
            this.spaceId = spaceId;
            return this;
        }

        public WatsonxTokenCountEstimator build() {
            return new WatsonxTokenCountEstimator(this);
        }
    }
}

