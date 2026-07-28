/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ReturnBehavior
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.guardrail.ChatExecutor
 *  dev.langchain4j.guardrail.GuardrailRequestParams
 *  dev.langchain4j.guardrail.OutputGuardrailRequest
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.CompleteToolCall
 *  dev.langchain4j.model.chat.response.PartialResponse
 *  dev.langchain4j.model.chat.response.PartialResponseContext
 *  dev.langchain4j.model.chat.response.PartialThinking
 *  dev.langchain4j.model.chat.response.PartialThinkingContext
 *  dev.langchain4j.model.chat.response.PartialToolCall
 *  dev.langchain4j.model.chat.response.PartialToolCallContext
 *  dev.langchain4j.model.chat.response.StreamingChatResponseHandler
 *  dev.langchain4j.model.chat.response.StreamingHandle
 *  dev.langchain4j.model.output.TokenUsage
 *  dev.langchain4j.observability.api.event.AiServiceCompletedEvent
 *  dev.langchain4j.observability.api.event.AiServiceErrorEvent
 *  dev.langchain4j.observability.api.event.AiServiceEvent
 *  dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent
 *  dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent
 *  dev.langchain4j.observability.api.event.ToolExecutedEvent
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.ChatExecutor;
import dev.langchain4j.guardrail.GuardrailRequestParams;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.PartialResponseContext;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.PartialThinkingContext;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.PartialToolCallContext;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.observability.api.event.AiServiceCompletedEvent;
import dev.langchain4j.observability.api.event.AiServiceErrorEvent;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.api.event.AiServiceRequestIssuedEvent;
import dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent;
import dev.langchain4j.observability.api.event.ToolExecutedEvent;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.AiServiceParamsUtil;
import dev.langchain4j.service.CancellationUnsupportedStreamingHandle;
import dev.langchain4j.service.ToolAwareRepromptExecutor;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolExecution;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolService;
import dev.langchain4j.service.tool.ToolServiceContext;
import dev.langchain4j.service.tool.search.ToolSearchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
class AiServiceStreamingResponseHandler
implements StreamingChatResponseHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AiServiceStreamingResponseHandler.class);
    private final ChatExecutor chatExecutor;
    private final ChatRequest chatRequest;
    private final AiServiceContext context;
    private final InvocationContext invocationContext;
    private final GuardrailRequestParams commonGuardrailParams;
    private final Object methodKey;
    private final Consumer<String> partialResponseHandler;
    private final BiConsumer<PartialResponse, PartialResponseContext> partialResponseWithContextHandler;
    private final Consumer<PartialThinking> partialThinkingHandler;
    private final BiConsumer<PartialThinking, PartialThinkingContext> partialThinkingWithContextHandler;
    private final Consumer<PartialToolCall> partialToolCallHandler;
    private final BiConsumer<PartialToolCall, PartialToolCallContext> partialToolCallWithContextHandler;
    private final Consumer<BeforeToolExecution> beforeToolExecutionHandler;
    private final Consumer<Object> rawEventHandler;
    private final Consumer<ToolExecution> toolExecutionHandler;
    private final Consumer<ChatResponse> intermediateResponseHandler;
    private final Consumer<ChatResponse> completeResponseHandler;
    private final Consumer<Throwable> errorHandler;
    private final ChatMemory temporaryMemory;
    private final TokenUsage tokenUsage;
    private final ToolServiceContext toolServiceContext;
    private final Map<String, ToolExecutor> toolExecutors;
    private final ToolArgumentsErrorHandler toolArgumentsErrorHandler;
    private final ToolExecutionErrorHandler toolExecutionErrorHandler;
    private final Executor toolExecutor;
    private final Queue<Future<ToolRequestResult>> toolExecutionFutures = new ConcurrentLinkedQueue<Future<ToolRequestResult>>();
    private final List<String> responseBuffer = new ArrayList<String>();
    private final boolean hasOutputGuardrails;
    private int toolCallingRoundTripsLeft;

    AiServiceStreamingResponseHandler(ChatRequest chatRequest, ChatExecutor chatExecutor, AiServiceContext context, InvocationContext invocationContext, Consumer<String> partialResponseHandler, BiConsumer<PartialResponse, PartialResponseContext> partialResponseWithContextHandler, Consumer<PartialThinking> partialThinkingHandler, BiConsumer<PartialThinking, PartialThinkingContext> partialThinkingWithContextHandler, Consumer<PartialToolCall> partialToolCallHandler, BiConsumer<PartialToolCall, PartialToolCallContext> partialToolCallWithContextHandler, Consumer<BeforeToolExecution> beforeToolExecutionHandler, Consumer<Object> rawEventHandler, Consumer<ToolExecution> toolExecutionHandler, Consumer<ChatResponse> intermediateResponseHandler, Consumer<ChatResponse> completeResponseHandler, Consumer<Throwable> errorHandler, ChatMemory temporaryMemory, TokenUsage tokenUsage, ToolServiceContext toolServiceContext, int toolCallingRoundTripsLeft, ToolArgumentsErrorHandler toolArgumentsErrorHandler, ToolExecutionErrorHandler toolExecutionErrorHandler, Executor toolExecutor, GuardrailRequestParams commonGuardrailParams, Object methodKey) {
        this.chatRequest = (ChatRequest)ValidationUtils.ensureNotNull((Object)chatRequest, (String)"chatRequest");
        this.chatExecutor = (ChatExecutor)ValidationUtils.ensureNotNull((Object)chatExecutor, (String)"chatExecutor");
        this.context = (AiServiceContext)ValidationUtils.ensureNotNull((Object)context, (String)"context");
        this.invocationContext = (InvocationContext)ValidationUtils.ensureNotNull((Object)invocationContext, (String)"invocationContext");
        this.methodKey = methodKey;
        this.partialResponseHandler = partialResponseHandler;
        this.partialResponseWithContextHandler = partialResponseWithContextHandler;
        this.partialThinkingHandler = partialThinkingHandler;
        this.partialThinkingWithContextHandler = partialThinkingWithContextHandler;
        this.partialToolCallHandler = partialToolCallHandler;
        this.partialToolCallWithContextHandler = partialToolCallWithContextHandler;
        this.intermediateResponseHandler = intermediateResponseHandler;
        this.completeResponseHandler = completeResponseHandler;
        this.beforeToolExecutionHandler = beforeToolExecutionHandler;
        this.rawEventHandler = rawEventHandler;
        this.toolExecutionHandler = toolExecutionHandler;
        this.errorHandler = errorHandler;
        this.temporaryMemory = temporaryMemory;
        this.tokenUsage = (TokenUsage)ValidationUtils.ensureNotNull((Object)tokenUsage, (String)"tokenUsage");
        this.commonGuardrailParams = commonGuardrailParams;
        this.toolServiceContext = toolServiceContext;
        this.toolExecutors = toolServiceContext != null ? toolServiceContext.toolExecutors() : Collections.emptyMap();
        this.toolArgumentsErrorHandler = (ToolArgumentsErrorHandler)ValidationUtils.ensureNotNull((Object)toolArgumentsErrorHandler, (String)"toolArgumentsErrorHandler");
        this.toolExecutionErrorHandler = (ToolExecutionErrorHandler)ValidationUtils.ensureNotNull((Object)toolExecutionErrorHandler, (String)"toolExecutionErrorHandler");
        this.toolExecutor = toolExecutor;
        this.hasOutputGuardrails = context.guardrailService().hasOutputGuardrails(methodKey);
        this.toolCallingRoundTripsLeft = toolCallingRoundTripsLeft;
    }

    public void onPartialResponse(String partialResponse) {
        if (this.hasOutputGuardrails) {
            this.responseBuffer.add(partialResponse);
        } else if (this.partialResponseHandler != null) {
            this.partialResponseHandler.accept(partialResponse);
        } else if (this.partialResponseWithContextHandler != null) {
            PartialResponseContext context = new PartialResponseContext((StreamingHandle)new CancellationUnsupportedStreamingHandle());
            this.partialResponseWithContextHandler.accept(new PartialResponse(partialResponse), context);
        }
    }

    public void onPartialResponse(PartialResponse partialResponse, PartialResponseContext context) {
        if (this.hasOutputGuardrails) {
            this.responseBuffer.add(partialResponse.text());
        } else if (this.partialResponseHandler != null) {
            this.partialResponseHandler.accept(partialResponse.text());
        } else if (this.partialResponseWithContextHandler != null) {
            this.partialResponseWithContextHandler.accept(partialResponse, context);
        }
    }

    public void onPartialThinking(PartialThinking partialThinking) {
        if (this.partialThinkingHandler != null) {
            this.partialThinkingHandler.accept(partialThinking);
        } else if (this.partialThinkingWithContextHandler != null) {
            PartialThinkingContext context = new PartialThinkingContext((StreamingHandle)new CancellationUnsupportedStreamingHandle());
            this.partialThinkingWithContextHandler.accept(partialThinking, context);
        }
    }

    public void onPartialThinking(PartialThinking partialThinking, PartialThinkingContext context) {
        if (this.partialThinkingHandler != null) {
            this.partialThinkingHandler.accept(partialThinking);
        } else if (this.partialThinkingWithContextHandler != null) {
            this.partialThinkingWithContextHandler.accept(partialThinking, context);
        }
    }

    public void onPartialToolCall(PartialToolCall partialToolCall) {
        if (this.partialToolCallHandler != null) {
            this.partialToolCallHandler.accept(partialToolCall);
        } else if (this.partialToolCallWithContextHandler != null) {
            PartialToolCallContext context = new PartialToolCallContext((StreamingHandle)new CancellationUnsupportedStreamingHandle());
            this.partialToolCallWithContextHandler.accept(partialToolCall, context);
        }
    }

    public void onPartialToolCall(PartialToolCall partialToolCall, PartialToolCallContext context) {
        if (this.partialToolCallHandler != null) {
            this.partialToolCallHandler.accept(partialToolCall);
        } else if (this.partialToolCallWithContextHandler != null) {
            this.partialToolCallWithContextHandler.accept(partialToolCall, context);
        }
    }

    public void onCompleteToolCall(CompleteToolCall completeToolCall) {
        if (this.toolExecutor != null) {
            ToolExecutionRequest toolRequest = completeToolCall.toolExecutionRequest();
            CompletableFuture<ToolRequestResult> future = CompletableFuture.supplyAsync(() -> {
                ToolExecutionResult toolResult = this.execute(toolRequest);
                return new ToolRequestResult(toolRequest, toolResult);
            }, this.toolExecutor);
            this.toolExecutionFutures.add(future);
        }
    }

    public void onUnmappedRawEvent(Object rawEvent) {
        if (this.rawEventHandler != null) {
            this.rawEventHandler.accept(rawEvent);
        }
    }

    private <T> void fireInvocationComplete(T result) {
        this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceCompletedEvent.builder().invocationContext(this.invocationContext).result(result).build());
    }

    private void fireToolExecutedEvent(ToolRequestResult toolRequestResult) {
        this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)ToolExecutedEvent.builder().invocationContext(this.invocationContext).request(toolRequestResult.request()).resultContents(toolRequestResult.result().resultContents()).build());
    }

    private void fireResponseReceivedEvent(ChatResponse chatResponse) {
        this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceResponseReceivedEvent.builder().invocationContext(this.invocationContext).request(this.chatRequest).response(chatResponse).build());
    }

    private void fireRequestIssuedEvent(ChatRequest chatRequest) {
        this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceRequestIssuedEvent.builder().invocationContext(this.invocationContext).request(chatRequest).build());
    }

    private void fireErrorReceived(Throwable error) {
        this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceErrorEvent.builder().invocationContext(this.invocationContext).error(error).build());
    }

    public void onCompleteResponse(ChatResponse chatResponse) {
        this.fireResponseReceivedEvent(chatResponse);
        AiMessage aiMessage = chatResponse.aiMessage();
        this.addToMemory((ChatMessage)aiMessage);
        if (aiMessage.hasToolExecutionRequests()) {
            if (this.toolCallingRoundTripsLeft-- == 0) {
                throw Exceptions.runtime((String)"Something is wrong, exceeded %s tool calling round trips (maxToolCallingRoundTrips)", (Object[])new Object[]{this.context.toolService.maxToolCallingRoundTrips()});
            }
            if (this.intermediateResponseHandler != null) {
                this.intermediateResponseHandler.accept(chatResponse);
            }
            ArrayList<ToolExecutionResult> toolResults = new ArrayList<ToolExecutionResult>();
            boolean anyToolErrored = false;
            ArrayList<ReturnBehavior> returnBehaviors = new ArrayList<ReturnBehavior>();
            if (this.toolExecutor != null) {
                for (Future future : this.toolExecutionFutures) {
                    try {
                        ToolRequestResult toolRequestResult = (ToolRequestResult)future.get();
                        this.fireToolExecutedEvent(toolRequestResult);
                        ToolExecutionRequest toolRequest = toolRequestResult.request();
                        ToolExecutionResult toolResult = toolRequestResult.result();
                        toolResults.add(toolResult);
                        ToolExecutionResultMessage toolExecutionResultMessage = AiServiceStreamingResponseHandler.toResultMessage(toolRequest, toolResult);
                        this.addToMemory((ChatMessage)toolExecutionResultMessage);
                        anyToolErrored = anyToolErrored || toolResult.isError();
                        returnBehaviors.add(this.toolServiceContext.returnBehavior(toolRequest.name()));
                    }
                    catch (ExecutionException e) {
                        if (e.getCause() instanceof RuntimeException) {
                            RuntimeException re = (RuntimeException)e.getCause();
                            throw re;
                        }
                        throw new RuntimeException(e.getCause());
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }
            } else {
                for (ToolExecutionRequest toolExecutionRequest : aiMessage.toolExecutionRequests()) {
                    ToolExecutionResult toolResult = this.execute(toolExecutionRequest);
                    toolResults.add(toolResult);
                    ToolRequestResult toolRequestResult = new ToolRequestResult(toolExecutionRequest, toolResult);
                    this.fireToolExecutedEvent(toolRequestResult);
                    ToolExecutionResultMessage toolExecutionResultMessage = AiServiceStreamingResponseHandler.toResultMessage(toolExecutionRequest, toolResult);
                    this.addToMemory((ChatMessage)toolExecutionResultMessage);
                    anyToolErrored = anyToolErrored || toolResult.isError();
                    returnBehaviors.add(this.toolServiceContext.returnBehavior(toolExecutionRequest.name()));
                }
            }
            if (ToolService.shouldReturnImmediately(anyToolErrored, returnBehaviors)) {
                ChatResponse finalChatResponse = this.finalResponse(chatResponse, aiMessage);
                this.fireInvocationComplete(finalChatResponse);
                if (this.completeResponseHandler != null) {
                    this.completeResponseHandler.accept(finalChatResponse);
                }
                return;
            }
            List<ChatMessage> messages = this.messagesToSend(this.invocationContext.chatMemoryId());
            ToolServiceContext toolServiceContext2 = ToolService.refreshDynamicProviders(this.toolServiceContext, messages, this.invocationContext);
            toolServiceContext2 = ToolSearchService.addFoundTools(toolServiceContext2, toolResults);
            ChatRequestParameters parameters = AiServiceParamsUtil.chatRequestParameters(this.invocationContext.methodArguments(), toolServiceContext2.effectiveTools());
            ChatRequest nextChatRequest = this.context.chatRequestTransformer.apply(ChatRequest.builder().messages(messages).parameters(parameters).build(), this.invocationContext.chatMemoryId());
            AiServiceStreamingResponseHandler handler = new AiServiceStreamingResponseHandler(nextChatRequest, this.chatExecutor, this.context, this.invocationContext, this.partialResponseHandler, this.partialResponseWithContextHandler, this.partialThinkingHandler, this.partialThinkingWithContextHandler, this.partialToolCallHandler, this.partialToolCallWithContextHandler, this.beforeToolExecutionHandler, this.rawEventHandler, this.toolExecutionHandler, this.intermediateResponseHandler, this.completeResponseHandler, this.errorHandler, this.temporaryMemory, TokenUsage.sum((TokenUsage)this.tokenUsage, (TokenUsage)chatResponse.metadata().tokenUsage()), toolServiceContext2, this.toolCallingRoundTripsLeft, this.toolArgumentsErrorHandler, this.toolExecutionErrorHandler, this.toolExecutor, this.commonGuardrailParams, this.methodKey);
            this.fireRequestIssuedEvent(nextChatRequest);
            this.context.streamingChatModel.chat(nextChatRequest, (StreamingChatResponseHandler)handler);
        } else {
            ChatResponse finalChatResponse = this.finalResponse(chatResponse, aiMessage);
            if (this.completeResponseHandler != null) {
                if (this.hasOutputGuardrails) {
                    if (this.commonGuardrailParams != null) {
                        GuardrailRequestParams newCommonParams = this.commonGuardrailParams.toBuilder().chatMemory(this.getMemory()).build();
                        OutputGuardrailRequest outputGuardrailParams = OutputGuardrailRequest.builder().responseFromLLM(finalChatResponse).chatExecutor(ToolAwareRepromptExecutor.wrap(this.chatExecutor, this.context, this.invocationContext.chatMemoryId(), this.chatRequest.parameters(), this.invocationContext, this.toolServiceContext, this::executeSynchronously)).requestParams(newCommonParams).build();
                        finalChatResponse = (ChatResponse)this.context.guardrailService().executeGuardrails(this.methodKey, outputGuardrailParams);
                    }
                    if (this.partialResponseHandler != null) {
                        this.responseBuffer.forEach(this.partialResponseHandler::accept);
                    } else if (this.partialResponseWithContextHandler != null) {
                        PartialResponseContext partialResponseContext = new PartialResponseContext((StreamingHandle)new CancellationUnsupportedStreamingHandle());
                        this.responseBuffer.forEach(s -> this.partialResponseWithContextHandler.accept(new PartialResponse(s), partialResponseContext));
                    }
                    this.responseBuffer.clear();
                }
                this.fireInvocationComplete(finalChatResponse);
                this.completeResponseHandler.accept(finalChatResponse);
            } else {
                this.fireInvocationComplete(finalChatResponse);
            }
        }
    }

    private ChatResponse executeSynchronously(ChatRequest request) {
        final CompletableFuture future = new CompletableFuture();
        this.context.streamingChatModel.chat(request, new StreamingChatResponseHandler(){

            public void onCompleteResponse(ChatResponse completeResponse) {
                future.complete(completeResponse);
            }

            public void onError(Throwable error) {
                future.completeExceptionally(error);
            }
        });
        try {
            return (ChatResponse)future.get();
        }
        catch (ExecutionException e) {
            if (e.getCause() instanceof RuntimeException) {
                RuntimeException re = (RuntimeException)e.getCause();
                throw re;
            }
            throw new RuntimeException(e.getCause());
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private ChatResponse finalResponse(ChatResponse completeResponse, AiMessage aiMessage) {
        return ChatResponse.builder().aiMessage(aiMessage).metadata(completeResponse.metadata().toBuilder().tokenUsage(this.tokenUsage.add(completeResponse.metadata().tokenUsage())).build()).build();
    }

    private ToolExecutionResult execute(ToolExecutionRequest toolRequest) {
        return this.context.toolService.executeTool(this.invocationContext, this.toolExecutors, toolRequest, this.beforeToolExecutionHandler, this.toolExecutionHandler);
    }

    private static ToolExecutionResultMessage toResultMessage(ToolExecutionRequest request, ToolExecutionResult result) {
        return ToolExecutionResultMessage.builder().id(request.id()).toolName(request.name()).contents(result.resultContents()).isError(Boolean.valueOf(result.isError())).attributes(result.attributes()).build();
    }

    private ChatMemory getMemory() {
        return this.getMemory(this.invocationContext.chatMemoryId());
    }

    private ChatMemory getMemory(Object memId) {
        return this.context.hasChatMemory() ? this.context.chatMemoryService.getOrCreateChatMemory(this.invocationContext.chatMemoryId()) : this.temporaryMemory;
    }

    private void addToMemory(ChatMessage chatMessage) {
        this.getMemory().add(chatMessage);
    }

    private List<ChatMessage> messagesToSend(Object memoryId) {
        List messages = this.getMemory(memoryId).messages();
        return this.context.storeRetrievedContentInChatMemory ? messages : UserMessage.replaceLast((List)messages, (UserMessage)this.invocationContext.userMessage());
    }

    public void onError(Throwable error) {
        if (this.errorHandler != null) {
            try {
                this.fireErrorReceived(error);
                this.errorHandler.accept(error);
            }
            catch (Exception e) {
                LOG.error("While handling the following error...", error);
                LOG.error("...the following error happened", (Throwable)e);
            }
        } else {
            LOG.warn("Ignored error", error);
        }
    }

    private static final class ToolRequestResult {
        final ToolExecutionRequest request;
        final ToolExecutionResult result;

        ToolRequestResult(ToolExecutionRequest request, ToolExecutionResult result) {
            this.request = request;
            this.result = result;
        }

        ToolExecutionRequest request() {
            return this.request;
        }

        ToolExecutionResult result() {
            return this.result;
        }
    }
}

