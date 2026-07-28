/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.guardrail.ChatExecutor
 *  dev.langchain4j.guardrail.GuardrailRequestParams
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.PartialResponse
 *  dev.langchain4j.model.chat.response.PartialResponseContext
 *  dev.langchain4j.model.chat.response.PartialThinking
 *  dev.langchain4j.model.chat.response.PartialThinkingContext
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCallContext
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.observability.api.event.AiServiceEvent
 *  dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent
 *  dev.langchain4j.rag.content.Content
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.guardrail.ChatExecutor;
import dev.langchain4j.guardrail.GuardrailRequestParams;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.PartialResponseContext;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.PartialThinkingContext;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.PartialToolCallContext;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.AiServiceParamsUtil;
import dev.langchain4j.service.AiServiceStreamingResponseHandler;
import dev.langchain4j.service.AiServiceTokenStreamParameters;
import dev.langchain4j.service.IllegalConfigurationException;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolExecution;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;
import dev.langchain4j.service.tool.ToolServiceContext;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Internal
public class AiServiceTokenStream
implements TokenStream {
    private final List<ChatMessage> messages;
    private final ToolServiceContext toolServiceContext;
    private final ToolArgumentsErrorHandler toolArgumentsErrorHandler;
    private final ToolExecutionErrorHandler toolExecutionErrorHandler;
    private final Executor toolExecutor;
    private final List<Content> retrievedContents;
    private final AiServiceContext context;
    private final InvocationContext invocationContext;
    private final GuardrailRequestParams commonGuardrailParams;
    private final Object methodKey;
    private Consumer<String> partialResponseHandler;
    private BiConsumer<PartialResponse, PartialResponseContext> partialResponseWithContextHandler;
    private Consumer<PartialThinking> partialThinkingHandler;
    private BiConsumer<PartialThinking, PartialThinkingContext> partialThinkingWithContextHandler;
    private Consumer<PartialToolCall> partialToolCallHandler;
    private BiConsumer<PartialToolCall, PartialToolCallContext> partialToolCallWithContextHandler;
    private Consumer<List<Content>> contentsHandler;
    private Consumer<ChatResponse> intermediateResponseHandler;
    private Consumer<BeforeToolExecution> beforeToolExecutionHandler;
    private Consumer<Object> rawEventHandler;
    private Consumer<ToolExecution> toolExecutionHandler;
    private Consumer<ChatResponse> completeResponseHandler;
    private Consumer<Throwable> errorHandler;
    private int onPartialResponseInvoked;
    private int onPartialResponseWithContextInvoked;
    private int onPartialThinkingInvoked;
    private int onPartialThinkingWithContextInvoked;
    private int onPartialToolCallInvoked;
    private int onPartialToolCallWithContextInvoked;
    private int onIntermediateResponseInvoked;
    private int onCompleteResponseInvoked;
    private int onRetrievedInvoked;
    private int beforeToolExecutionInvoked;
    private int onUnmappedRawEventInvoked;
    private int onToolExecutedInvoked;
    private int onErrorInvoked;
    private int ignoreErrorsInvoked;

    public AiServiceTokenStream(AiServiceTokenStreamParameters parameters) {
        ValidationUtils.ensureNotNull((Object)parameters, (String)"parameters");
        this.messages = Utils.copy((List)((List)ValidationUtils.ensureNotEmpty(parameters.messages(), (String)"messages")));
        this.toolServiceContext = parameters.toolServiceContext();
        this.toolArgumentsErrorHandler = parameters.toolArgumentsErrorHandler();
        this.toolExecutionErrorHandler = parameters.toolExecutionErrorHandler();
        this.toolExecutor = parameters.toolExecutor();
        this.retrievedContents = Utils.copy(parameters.retrievedContents());
        this.context = (AiServiceContext)ValidationUtils.ensureNotNull((Object)parameters.context(), (String)"context");
        ValidationUtils.ensureNotNull((Object)this.context.streamingChatModel, (String)"streamingChatModel");
        this.invocationContext = parameters.invocationContext();
        this.commonGuardrailParams = parameters.commonGuardrailParams();
        this.methodKey = parameters.methodKey();
    }

    @Override
    public TokenStream onPartialResponse(Consumer<String> partialResponseHandler) {
        this.partialResponseHandler = partialResponseHandler;
        ++this.onPartialResponseInvoked;
        return this;
    }

    @Override
    public TokenStream onPartialResponseWithContext(BiConsumer<PartialResponse, PartialResponseContext> handler) {
        this.partialResponseWithContextHandler = handler;
        ++this.onPartialResponseWithContextInvoked;
        return this;
    }

    @Override
    public TokenStream onPartialThinking(Consumer<PartialThinking> partialThinkingHandler) {
        this.partialThinkingHandler = partialThinkingHandler;
        ++this.onPartialThinkingInvoked;
        return this;
    }

    @Override
    public TokenStream onPartialThinkingWithContext(BiConsumer<PartialThinking, PartialThinkingContext> handler) {
        this.partialThinkingWithContextHandler = handler;
        ++this.onPartialThinkingWithContextInvoked;
        return this;
    }

    @Override
    public TokenStream onPartialToolCall(Consumer<PartialToolCall> partialToolCallHandler) {
        this.partialToolCallHandler = partialToolCallHandler;
        ++this.onPartialToolCallInvoked;
        return this;
    }

    @Override
    public TokenStream onPartialToolCallWithContext(BiConsumer<PartialToolCall, PartialToolCallContext> handler) {
        this.partialToolCallWithContextHandler = handler;
        ++this.onPartialToolCallWithContextInvoked;
        return this;
    }

    @Override
    public TokenStream onRetrieved(Consumer<List<Content>> contentsHandler) {
        this.contentsHandler = contentsHandler;
        ++this.onRetrievedInvoked;
        return this;
    }

    @Override
    public TokenStream onIntermediateResponse(Consumer<ChatResponse> intermediateResponseHandler) {
        this.intermediateResponseHandler = intermediateResponseHandler;
        ++this.onIntermediateResponseInvoked;
        return this;
    }

    @Override
    public TokenStream beforeToolExecution(Consumer<BeforeToolExecution> beforeToolExecutionHandler) {
        this.beforeToolExecutionHandler = beforeToolExecutionHandler;
        ++this.beforeToolExecutionInvoked;
        return this;
    }

    @Override
    public TokenStream onUnmappedRawEvent(Consumer<Object> rawEventHandler) {
        this.rawEventHandler = rawEventHandler;
        ++this.onUnmappedRawEventInvoked;
        return this;
    }

    @Override
    public TokenStream onToolExecuted(Consumer<ToolExecution> toolExecutionHandler) {
        this.toolExecutionHandler = toolExecutionHandler;
        ++this.onToolExecutedInvoked;
        return this;
    }

    @Override
    public TokenStream onCompleteResponse(Consumer<ChatResponse> completionHandler) {
        this.completeResponseHandler = completionHandler;
        ++this.onCompleteResponseInvoked;
        return this;
    }

    @Override
    public TokenStream onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        ++this.onErrorInvoked;
        return this;
    }

    @Override
    public TokenStream ignoreErrors() {
        this.errorHandler = null;
        ++this.ignoreErrorsInvoked;
        return this;
    }

    @Override
    public void start() {
        this.validateConfiguration();
        List<ToolSpecification> effectiveTools = this.toolServiceContext != null ? this.toolServiceContext.effectiveTools() : null;
        ChatRequest chatRequest = this.context.chatRequestTransformer.apply(ChatRequest.builder().messages(this.messages).parameters(AiServiceParamsUtil.chatRequestParameters(this.invocationContext.methodArguments(), effectiveTools)).build(), this.invocationContext.chatMemoryId());
        ChatExecutor chatExecutor = ChatExecutor.builder((StreamingChatModel)this.context.streamingChatModel).errorHandler(this.errorHandler).chatRequest(chatRequest).invocationContext(this.invocationContext).eventListenerRegistrar(this.context.eventListenerRegistrar).build();
        AiServiceStreamingResponseHandler handler = new AiServiceStreamingResponseHandler(chatRequest, chatExecutor, this.context, this.invocationContext, this.partialResponseHandler, this.partialResponseWithContextHandler, this.partialThinkingHandler, this.partialThinkingWithContextHandler, this.partialToolCallHandler, this.partialToolCallWithContextHandler, this.beforeToolExecutionHandler, this.rawEventHandler, this.toolExecutionHandler, this.intermediateResponseHandler, this.completeResponseHandler, this.errorHandler, this.initTemporaryMemory(this.context, this.messages), new TokenUsage(), this.toolServiceContext, this.context.toolService.maxToolCallingRoundTrips(), this.toolArgumentsErrorHandler, this.toolExecutionErrorHandler, this.toolExecutor, this.commonGuardrailParams, this.methodKey);
        if (this.contentsHandler != null && this.retrievedContents != null) {
            this.contentsHandler.accept(this.retrievedContents);
        }
        this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceRequestIssuedEvent.builder().invocationContext(this.invocationContext).request(chatRequest).build());
        this.context.streamingChatModel.chat(chatRequest, (StreamingChatResponseHandler)handler);
    }

    private void validateConfiguration() {
        if (this.onPartialResponseInvoked + this.onPartialResponseWithContextInvoked > 1) {
            throw new IllegalConfigurationException("One of [onPartialResponse, onPartialResponseWithContext] can be invoked on TokenStream at most 1 time");
        }
        if (this.onPartialThinkingInvoked + this.onPartialThinkingWithContextInvoked > 1) {
            throw new IllegalConfigurationException("One of [onPartialThinking, onPartialThinkingWithContext] can be invoked on TokenStream at most 1 time");
        }
        if (this.onPartialToolCallInvoked + this.onPartialToolCallWithContextInvoked > 1) {
            throw new IllegalConfigurationException("One of [onPartialToolCall, onPartialToolCallWithContext] can be invoked on TokenStream at most 1 time");
        }
        if (this.onIntermediateResponseInvoked > 1) {
            throw new IllegalConfigurationException("onIntermediateResponse can be invoked on TokenStream at most 1 time");
        }
        if (this.onCompleteResponseInvoked > 1) {
            throw new IllegalConfigurationException("onCompleteResponse can be invoked on TokenStream at most 1 time");
        }
        if (this.onRetrievedInvoked > 1) {
            throw new IllegalConfigurationException("onRetrieved can be invoked on TokenStream at most 1 time");
        }
        if (this.beforeToolExecutionInvoked > 1) {
            throw new IllegalConfigurationException("beforeToolExecution can be invoked on TokenStream at most 1 time");
        }
        if (this.onUnmappedRawEventInvoked > 1) {
            throw new IllegalConfigurationException("onUnmappedRawEvent can be invoked on TokenStream at most 1 time");
        }
        if (this.onToolExecutedInvoked > 1) {
            throw new IllegalConfigurationException("onToolExecuted can be invoked on TokenStream at most 1 time");
        }
        if (this.onErrorInvoked + this.ignoreErrorsInvoked != 1) {
            throw new IllegalConfigurationException("One of [onError, ignoreErrors] must be invoked on TokenStream exactly 1 time");
        }
    }

    private ChatMemory initTemporaryMemory(AiServiceContext context, List<ChatMessage> messagesToSend) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(Integer.MAX_VALUE);
        if (!context.hasChatMemory()) {
            chatMemory.add(messagesToSend);
        }
        return chatMemory;
    }
}

