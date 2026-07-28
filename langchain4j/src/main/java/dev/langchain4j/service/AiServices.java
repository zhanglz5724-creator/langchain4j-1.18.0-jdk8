/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.guardrail.InputGuardrail
 *  dev.langchain4j.guardrail.OutputGuardrail
 *  dev.langchain4j.guardrail.config.InputGuardrailsConfig
 *  dev.langchain4j.guardrail.config.OutputGuardrailsConfig
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.moderation.Moderation
 *  dev.langchain4j.model.moderation.ModerationModel
 *  dev.langchain4j.observability.api.event.AiServiceEvent
 *  dev.langchain4j.observability.api.listener.AiServiceListener
 *  dev.langchain4j.rag.DefaultRetrievalAugmentor
 *  dev.langchain4j.rag.RetrievalAugmentor
 *  dev.langchain4j.rag.content.retriever.ContentRetriever
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.DefaultAiServices;
import dev.langchain4j.service.IllegalConfigurationException;
import dev.langchain4j.service.ModerationException;
import dev.langchain4j.service.tool.AiServiceTool;
import dev.langchain4j.service.tool.BeforeToolExecution;
import dev.langchain4j.service.tool.ToolArgumentsErrorHandler;
import dev.langchain4j.service.tool.ToolExecution;
import dev.langchain4j.service.tool.ToolExecutionErrorHandler;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.service.tool.search.ToolSearchStrategy;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.services.AiServicesFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public abstract class AiServices<T> {
    protected final AiServiceContext context;
    private boolean contentRetrieverSet = false;
    private boolean retrievalAugmentorSet = false;

    protected AiServices(AiServiceContext context) {
        this.context = context;
    }

    public static <T> T create(Class<T> aiService, ChatModel chatModel) {
        return AiServices.builder(aiService).chatModel(chatModel).build();
    }

    public static <T> T create(Class<T> aiService, StreamingChatModel streamingChatModel) {
        return AiServices.builder(aiService).streamingChatModel(streamingChatModel).build();
    }

    public static <T> AiServices<T> builder(Class<T> aiService) {
        AiServiceContext context = AiServiceContext.create(aiService);
        return AiServices.builder(context);
    }

    @Internal
    public static <T> AiServices<T> builder(AiServiceContext context) {
        return FactoryHolder.aiServicesFactory != null ? FactoryHolder.aiServicesFactory.create(context) : new DefaultAiServices(context);
    }

    public AiServices<T> chatModel(ChatModel chatModel) {
        this.context.chatModel = chatModel;
        return this;
    }

    public AiServices<T> streamingChatModel(StreamingChatModel streamingChatModel) {
        this.context.streamingChatModel = streamingChatModel;
        return this;
    }

    public AiServices<T> systemMessage(String systemMessage) {
        return this.systemMessageProvider(ignore -> systemMessage);
    }

    public AiServices<T> systemMessageProvider(Function<Object, String> systemMessageProvider) {
        this.context.systemMessageProvider = systemMessageProvider.andThen(Optional::ofNullable);
        return this;
    }

    public AiServices<T> systemMessageProviderWithContext(Function<InvocationContext, String> systemMessageProvider) {
        this.context.systemMessageProviderWithContext = systemMessageProvider;
        return this;
    }

    public AiServices<T> systemMessageTransformer(UnaryOperator<String> systemMessageTransformer) {
        this.context.systemMessageTransformer = (msg, ctx) -> (String)systemMessageTransformer.apply((String)msg);
        return this;
    }

    public AiServices<T> systemMessageTransformer(BiFunction<String, InvocationContext, String> systemMessageTransformer) {
        this.context.systemMessageTransformer = systemMessageTransformer;
        return this;
    }

    public AiServices<T> userMessage(String userMessage) {
        return this.userMessageProvider(ignore -> userMessage);
    }

    public AiServices<T> userMessageProvider(Function<Object, String> userMessageProvider) {
        this.context.userMessageProvider = userMessageProvider.andThen(Optional::ofNullable);
        return this;
    }

    public AiServices<T> chatMemory(ChatMemory chatMemory) {
        if (chatMemory != null) {
            this.context.initChatMemories(chatMemory);
        }
        return this;
    }

    public AiServices<T> chatMemoryProvider(ChatMemoryProvider chatMemoryProvider) {
        if (chatMemoryProvider != null) {
            this.context.initChatMemories(chatMemoryProvider);
        }
        return this;
    }

    public AiServices<T> chatRequestTransformer(UnaryOperator<ChatRequest> chatRequestTransformer) {
        this.context.chatRequestTransformer = (req, memId) -> (ChatRequest)chatRequestTransformer.apply((ChatRequest)req);
        return this;
    }

    public AiServices<T> chatRequestTransformer(BiFunction<ChatRequest, Object, ChatRequest> chatRequestTransformer) {
        this.context.chatRequestTransformer = chatRequestTransformer;
        return this;
    }

    public AiServices<T> moderationModel(ModerationModel moderationModel) {
        this.context.moderationModel = moderationModel;
        return this;
    }

    public AiServices<T> tools(Object ... objectsWithTools) {
        return this.tools((Collection<Object>)Arrays.asList(objectsWithTools));
    }

    public AiServices<T> tools(Collection<Object> objectsWithTools) {
        this.context.toolService.tools(objectsWithTools);
        return this;
    }

    public AiServices<T> toolProvider(ToolProvider toolProvider) {
        this.context.toolService.toolProvider(toolProvider);
        return this;
    }

    public AiServices<T> toolProviders(Collection<ToolProvider> toolProviders) {
        this.context.toolService.toolProviders(toolProviders);
        return this;
    }

    public AiServices<T> toolProviders(ToolProvider ... toolProviders) {
        if (toolProviders != null && toolProviders.length > 0) {
            this.context.toolService.toolProviders(Arrays.asList(toolProviders));
        }
        return this;
    }

    public AiServices<T> tools(List<AiServiceTool> tools) {
        this.context.toolService.tools(tools);
        return this;
    }

    public AiServices<T> tools(Map<ToolSpecification, ToolExecutor> tools) {
        this.context.toolService.tools(tools);
        return this;
    }

    @Deprecated
    public AiServices<T> tools(Map<ToolSpecification, ToolExecutor> tools, Set<String> immediateReturnToolNames) {
        this.context.toolService.tools(tools, immediateReturnToolNames);
        return this;
    }

    public AiServices<T> executeToolsConcurrently() {
        this.context.toolService.executeToolsConcurrently();
        return this;
    }

    public AiServices<T> executeToolsConcurrently(Executor executor) {
        this.context.toolService.executeToolsConcurrently(executor);
        return this;
    }

    public AiServices<T> maxToolCallingRoundTrips(int maxToolCallingRoundTrips) {
        this.context.toolService.maxToolCallingRoundTrips(maxToolCallingRoundTrips);
        return this;
    }

    @Deprecated
    public AiServices<T> maxSequentialToolsInvocations(int maxSequentialToolsInvocations) {
        return this.maxToolCallingRoundTrips(maxSequentialToolsInvocations);
    }

    public AiServices<T> beforeToolExecution(Consumer<BeforeToolExecution> beforeToolExecution) {
        this.context.toolService.beforeToolExecution(beforeToolExecution);
        return this;
    }

    public AiServices<T> afterToolExecution(Consumer<ToolExecution> afterToolExecution) {
        this.context.toolService.afterToolExecution(afterToolExecution);
        return this;
    }

    public AiServices<T> hallucinatedToolNameStrategy(Function<ToolExecutionRequest, ToolExecutionResultMessage> hallucinatedToolNameStrategy) {
        this.context.toolService.hallucinatedToolNameStrategy(hallucinatedToolNameStrategy);
        return this;
    }

    public AiServices<T> toolArgumentsErrorHandler(ToolArgumentsErrorHandler handler) {
        this.context.toolService.argumentsErrorHandler(handler);
        return this;
    }

    public AiServices<T> toolExecutionErrorHandler(ToolExecutionErrorHandler handler) {
        this.context.toolService.executionErrorHandler(handler);
        return this;
    }

    public AiServices<T> toolSearchStrategy(ToolSearchStrategy toolSearchStrategy) {
        this.context.toolService.toolSearchStrategy(toolSearchStrategy);
        return this;
    }

    public AiServices<T> contentRetriever(ContentRetriever contentRetriever) {
        if (this.retrievalAugmentorSet) {
            throw IllegalConfigurationException.illegalConfiguration("Only one out of [retriever, contentRetriever, retrievalAugmentor] can be set");
        }
        this.contentRetrieverSet = true;
        this.context.retrievalAugmentor = DefaultRetrievalAugmentor.builder().contentRetriever((ContentRetriever)ValidationUtils.ensureNotNull((Object)contentRetriever, (String)"contentRetriever")).build();
        return this;
    }

    public AiServices<T> retrievalAugmentor(RetrievalAugmentor retrievalAugmentor) {
        if (this.contentRetrieverSet) {
            throw IllegalConfigurationException.illegalConfiguration("Only one out of [retriever, contentRetriever, retrievalAugmentor] can be set");
        }
        this.retrievalAugmentorSet = true;
        this.context.retrievalAugmentor = (RetrievalAugmentor)ValidationUtils.ensureNotNull((Object)retrievalAugmentor, (String)"retrievalAugmentor");
        return this;
    }

    public <I extends AiServiceEvent> AiServices<T> registerListener(AiServiceListener<I> listener) {
        this.context.eventListenerRegistrar.register((AiServiceListener)ValidationUtils.ensureNotNull(listener, (String)"listener"));
        return this;
    }

    public AiServices<T> registerListeners(AiServiceListener<?> ... listeners) {
        this.context.eventListenerRegistrar.register(listeners);
        return this;
    }

    public AiServices<T> registerListeners(Collection<? extends AiServiceListener<?>> listeners) {
        this.context.eventListenerRegistrar.register(listeners);
        return this;
    }

    public <I extends AiServiceEvent> AiServices<T> unregisterListener(AiServiceListener<I> listener) {
        this.context.eventListenerRegistrar.unregister((AiServiceListener)ValidationUtils.ensureNotNull(listener, (String)"listener"));
        return this;
    }

    public AiServices<T> unregisterListeners(AiServiceListener<?> ... listeners) {
        this.context.eventListenerRegistrar.unregister(listeners);
        return this;
    }

    public AiServices<T> unregisterListeners(Collection<? extends AiServiceListener<?>> listeners) {
        this.context.eventListenerRegistrar.unregister(listeners);
        return this;
    }

    public AiServices<T> inputGuardrailsConfig(InputGuardrailsConfig inputGuardrailsConfig) {
        this.context.guardrailServiceBuilder.inputGuardrailsConfig(inputGuardrailsConfig);
        return this;
    }

    public AiServices<T> outputGuardrailsConfig(OutputGuardrailsConfig outputGuardrailsConfig) {
        this.context.guardrailServiceBuilder.outputGuardrailsConfig(outputGuardrailsConfig);
        return this;
    }

    public <I extends InputGuardrail> AiServices<T> inputGuardrailClasses(List<Class<? extends I>> guardrailClasses) {
        this.context.guardrailServiceBuilder.inputGuardrailClasses(guardrailClasses);
        return this;
    }

    public <I extends InputGuardrail> AiServices<T> inputGuardrailClasses(Class<? extends I> ... guardrailClasses) {
        this.context.guardrailServiceBuilder.inputGuardrailClasses(guardrailClasses);
        return this;
    }

    public <I extends InputGuardrail> AiServices<T> inputGuardrails(List<I> guardrails) {
        this.context.guardrailServiceBuilder.inputGuardrails(guardrails);
        return this;
    }

    public <I extends InputGuardrail> AiServices<T> inputGuardrails(I ... guardrails) {
        this.context.guardrailServiceBuilder.inputGuardrails((InputGuardrail[])guardrails);
        return this;
    }

    public <O extends OutputGuardrail> AiServices<T> outputGuardrailClasses(List<Class<? extends O>> guardrailClasses) {
        this.context.guardrailServiceBuilder.outputGuardrailClasses(guardrailClasses);
        return this;
    }

    public <O extends OutputGuardrail> AiServices<T> outputGuardrailClasses(Class<? extends O> ... guardrailClasses) {
        this.context.guardrailServiceBuilder.outputGuardrailClasses(guardrailClasses);
        return this;
    }

    public <O extends OutputGuardrail> AiServices<T> outputGuardrails(List<O> guardrails) {
        this.context.guardrailServiceBuilder.outputGuardrails(guardrails);
        return this;
    }

    public <O extends OutputGuardrail> AiServices<T> outputGuardrails(O ... guardrails) {
        this.context.guardrailServiceBuilder.outputGuardrails((OutputGuardrail[])guardrails);
        return this;
    }

    public AiServices<T> storeRetrievedContentInChatMemory(boolean storeRetrievedContentInChatMemory) {
        this.context.storeRetrievedContentInChatMemory = storeRetrievedContentInChatMemory;
        return this;
    }

    public AiServices<T> compensateOnToolErrors(boolean compensateOnToolErrors) {
        this.context.toolService.compensateOnToolErrors(compensateOnToolErrors);
        return this;
    }

    public abstract T build();

    protected void performBasicValidation() {
        if (this.context.chatModel == null && this.context.streamingChatModel == null) {
            throw IllegalConfigurationException.illegalConfiguration("Please specify either chatModel or streamingChatModel");
        }
    }

    public static List<ChatMessage> removeToolMessages(List<ChatMessage> messages) {
        return messages.stream().filter(it -> !(it instanceof ToolExecutionResultMessage)).filter(it -> !(it instanceof AiMessage) || !((AiMessage)it).hasToolExecutionRequests()).collect(Collectors.toList());
    }

    public static void verifyModerationIfNeeded(Future<Moderation> moderationFuture) {
        if (moderationFuture != null) {
            try {
                Moderation moderation = moderationFuture.get();
                if (moderation.flagged()) {
                    throw new ModerationException(String.format("Text \"%s\" violates content policy", moderation.flaggedText()), moderation);
                }
            }
            catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class FactoryHolder {
        private static final AiServicesFactory aiServicesFactory = (AiServicesFactory)ServiceHelper.loadFactory(AiServicesFactory.class);

        private FactoryHolder() {
        }
    }
}

