/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ReturnBehavior
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.guardrail.ChatExecutor
 *  dev.langchain4j.guardrail.GuardrailRequestParams
 *  dev.langchain4j.guardrail.InputGuardrailRequest
 *  dev.langchain4j.guardrail.OutputGuardrailRequest
 *  dev.langchain4j.internal.DefaultExecutorProvider
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.invocation.InvocationContext
 *  dev.langchain4j.invocation.InvocationParameters
 *  dev.langchain4j.invocation.LangChain4jManaged
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.model.ModelProvider
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.input.Prompt
 *  dev.langchain4j.model.input.PromptTemplate
 *  dev.langchain4j.model.moderation.Moderation
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.observability.api.event.AiServiceCompletedEvent
 *  dev.langchain4j.observability.api.event.AiServiceErrorEvent
 *  dev.langchain4j.observability.api.event.AiServiceEvent
 *  dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent
 *  dev.langchain4j.observability.api.event.AiServiceStartedEvent
 *  dev.langchain4j.rag.AugmentationRequest
 *  dev.langchain4j.rag.AugmentationResult
 *  dev.langchain4j.rag.query.Metadata
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.service;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.guardrail.ChatExecutor;
import dev.langchain4j.guardrail.GuardrailRequestParams;
import dev.langchain4j.guardrail.InputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.internal.DefaultExecutorProvider;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.observability.api.event.AiServiceCompletedEvent;
import dev.langchain4j.observability.api.event.AiServiceErrorEvent;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent;
import dev.langchain4j.observability.api.event.AiServiceStartedEvent;
import dev.langchain4j.rag.AugmentationRequest;
import dev.langchain4j.rag.AugmentationResult;
import dev.langchain4j.rag.query.Metadata;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.AiServiceParamsUtil;
import dev.langchain4j.service.AiServiceTokenStream;
import dev.langchain4j.service.AiServiceTokenStreamParameters;
import dev.langchain4j.service.AiServiceValidation;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.IllegalConfigurationException;
import dev.langchain4j.service.InternalReflectionVariableResolver;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Moderate;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.ToolAwareRepromptExecutor;
import dev.langchain4j.service.TypeUtils;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.UserName;
import dev.langchain4j.service.V;
import dev.langchain4j.service.guardrail.GuardrailService;
import dev.langchain4j.service.memory.ChatMemoryAccess;
import dev.langchain4j.service.output.ServiceOutputParser;
import dev.langchain4j.service.tool.ToolExecution;
import dev.langchain4j.service.tool.ToolServiceContext;
import dev.langchain4j.service.tool.ToolServiceResult;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.services.TokenStreamAdapter;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Internal
class DefaultAiServices<T>
extends AiServices<T> {
    private final ServiceOutputParser serviceOutputParser = new ServiceOutputParser();
    private final Collection<TokenStreamAdapter> tokenStreamAdapters = ServiceHelper.loadFactories(TokenStreamAdapter.class);
    private static final Set<Class<? extends Annotation>> VALID_PARAM_ANNOTATIONS = new HashSet<Class>(Arrays.asList(UserMessage.class, V.class, MemoryId.class, UserName.class));

    DefaultAiServices(AiServiceContext context) {
        super(context);
    }

    protected void validate() {
        this.performBasicValidation();
        AiServiceValidation.validate(this.context);
    }

    private Object handleChatMemoryAccess(Method method, Object[] args) {
        String methodName = method.getName();
        if ("getChatMemory".equals(methodName)) {
            return this.context.chatMemoryService.getChatMemory(args[0]);
        }
        if ("evictChatMemory".equals(methodName)) {
            return this.context.chatMemoryService.evictChatMemory(args[0]) != null;
        }
        throw new UnsupportedOperationException("Unknown method on ChatMemoryAccess class : " + method.getName());
    }

    @Override
    public T build() {
        this.validate();
        Object proxyInstance = Proxy.newProxyInstance(this.context.aiServiceClass.getClassLoader(), new Class[]{this.context.aiServiceClass}, new InvocationHandler(){

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.isDefault()) {
                    return MethodHandles.lookup().unreflectSpecial(method, method.getDeclaringClass()).bindTo(proxy).invokeWithArguments(args);
                }
                if (method.getDeclaringClass() == Object.class) {
                    switch (method.getName()) {
                        case "equals": {
                            return proxy == args[0];
                        }
                        case "hashCode": {
                            return System.identityHashCode(proxy);
                        }
                        case "toString": {
                            return DefaultAiServices.this.context.aiServiceClass.getName() + "@" + Integer.toHexString(System.identityHashCode(proxy));
                        }
                    }
                    throw new IllegalStateException("Unexpected Object method: " + method);
                }
                if (method.getDeclaringClass() == ChatMemoryAccess.class) {
                    return DefaultAiServices.this.handleChatMemoryAccess(method, args);
                }
                AiServiceValidation.validateParameters(DefaultAiServices.this.context.aiServiceClass, method);
                InvocationParameters invocationParameters = AiServiceParamsUtil.findArgumentOfType(InvocationParameters.class, args, method.getParameters()).orElseGet(InvocationParameters::new);
                InvocationContext invocationContext = InvocationContext.builder().invocationId(UUID.randomUUID()).interfaceName(DefaultAiServices.this.context.aiServiceClass.getName()).methodName(method.getName()).methodArguments(args != null ? Arrays.asList(args) : Collections.emptyList()).chatMemoryId((Object)DefaultAiServices.findMemoryId(method, args).orElse("default")).defaultRequestParameters(this.determineChatRequestParameters(DefaultAiServices.this.context)).modelProvider(this.determineModelProvider(DefaultAiServices.this.context)).invocationParameters(invocationParameters).managedParameters(LangChain4jManaged.current()).timestampNow().build();
                try {
                    return this.invoke(method, args, invocationContext);
                }
                catch (Exception ex) {
                    DefaultAiServices.this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceErrorEvent.builder().invocationContext(invocationContext).error((Throwable)ex).build());
                    throw ex;
                }
            }

            private ChatRequestParameters determineChatRequestParameters(AiServiceContext context) {
                if (context.chatModel != null) {
                    return context.chatModel.defaultRequestParameters();
                }
                return context.streamingChatModel != null ? context.streamingChatModel.defaultRequestParameters() : null;
            }

            private ModelProvider determineModelProvider(AiServiceContext context) {
                if (context.chatModel != null) {
                    return context.chatModel.provider();
                }
                return context.streamingChatModel != null ? context.streamingChatModel.provider() : null;
            }

            public Object invoke(Method method, Object[] args, InvocationContext invocationContext) {
                Object memoryId = invocationContext.chatMemoryId();
                ChatMemory chatMemory = DefaultAiServices.this.context.hasChatMemory() ? DefaultAiServices.this.context.chatMemoryService.getOrCreateChatMemory(memoryId) : null;
                Optional<dev.langchain4j.data.message.SystemMessage> systemMessage = DefaultAiServices.this.prepareSystemMessage(invocationContext, method, args);
                if (DefaultAiServices.this.context.systemMessageTransformer != null) {
                    String transformedSystemMessage = DefaultAiServices.this.context.systemMessageTransformer.apply(systemMessage.map(dev.langchain4j.data.message.SystemMessage::text).orElse(null), invocationContext);
                    systemMessage = transformedSystemMessage != null ? Optional.of(dev.langchain4j.data.message.SystemMessage.from((String)transformedSystemMessage)) : Optional.empty();
                }
                String userMessageTemplate = DefaultAiServices.this.getUserMessageTemplate(memoryId, method, args);
                Map<String, Object> variables = InternalReflectionVariableResolver.findTemplateVariables(userMessageTemplate, method, args);
                dev.langchain4j.data.message.UserMessage originalUserMessage = DefaultAiServices.prepareUserMessage(method, args, userMessageTemplate, variables);
                DefaultAiServices.this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceStartedEvent.builder().invocationContext(invocationContext).systemMessage(systemMessage).userMessage(originalUserMessage).build());
                dev.langchain4j.data.message.UserMessage userMessageForAugmentation = originalUserMessage;
                AugmentationResult augmentationResult = null;
                if (DefaultAiServices.this.context.retrievalAugmentor != null) {
                    List chatMemoryMessages = chatMemory != null ? chatMemory.messages() : null;
                    Metadata metadata = Metadata.builder().chatMessage((ChatMessage)userMessageForAugmentation).systemMessage((dev.langchain4j.data.message.SystemMessage)systemMessage.orElse(null)).chatMemory(chatMemoryMessages).invocationContext(invocationContext).build();
                    AugmentationRequest augmentationRequest = new AugmentationRequest((ChatMessage)userMessageForAugmentation, metadata);
                    augmentationResult = DefaultAiServices.this.context.retrievalAugmentor.augment(augmentationRequest);
                    userMessageForAugmentation = (dev.langchain4j.data.message.UserMessage)augmentationResult.chatMessage();
                }
                dev.langchain4j.data.message.UserMessage userMessage = DefaultAiServices.addContentsToUserMessage(method, args, userMessageForAugmentation);
                GuardrailRequestParams commonGuardrailParam = GuardrailRequestParams.builder().chatMemory(chatMemory).augmentationResult(augmentationResult).userMessageTemplate(userMessageTemplate).invocationContext(invocationContext).aiServiceListenerRegistrar(DefaultAiServices.this.context.eventListenerRegistrar).variables(variables).build();
                userMessage = DefaultAiServices.this.invokeInputGuardrails(DefaultAiServices.this.context.guardrailService(), method, userMessage, commonGuardrailParam);
                Class<?> returnType = DefaultAiServices.this.context.returnType != null ? DefaultAiServices.this.context.returnType : method.getGenericReturnType();
                boolean streaming = returnType == TokenStream.class || this.canAdaptTokenStreamTo(returnType);
                boolean supportsJsonSchema = this.supportsJsonSchema();
                Optional<Object> jsonSchema = Optional.empty();
                boolean returnsImage = this.isImage(returnType);
                if (supportsJsonSchema && !streaming && !returnsImage) {
                    jsonSchema = DefaultAiServices.this.serviceOutputParser.jsonSchema(returnType);
                }
                if (!(supportsJsonSchema && jsonSchema.isPresent() || streaming || returnsImage)) {
                    userMessage = this.appendOutputFormatInstructions(returnType, userMessage);
                }
                ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
                if (DefaultAiServices.this.context.hasChatMemory()) {
                    systemMessage.ifPresent(arg_0 -> ((ChatMemory)chatMemory).add(arg_0));
                    messages.addAll(chatMemory.messages());
                    if (DefaultAiServices.this.context.storeRetrievedContentInChatMemory) {
                        chatMemory.add((ChatMessage)userMessage);
                    } else {
                        chatMemory.add((ChatMessage)originalUserMessage);
                    }
                    messages.add((ChatMessage)userMessage);
                } else {
                    systemMessage.ifPresent(messages::add);
                    messages.add((ChatMessage)userMessage);
                }
                invocationContext = invocationContext.toBuilder().userMessage(userMessage).build();
                Future<Moderation> moderationFuture = this.triggerModerationIfNeeded(method, messages);
                ToolServiceContext toolServiceContext = DefaultAiServices.this.context.toolService.createContext(invocationContext, userMessage, messages);
                if (streaming) {
                    AiServiceTokenStreamParameters tokenStreamParameters = AiServiceTokenStreamParameters.builder().messages(messages).toolServiceContext(toolServiceContext).toolArgumentsErrorHandler(DefaultAiServices.this.context.toolService.argumentsErrorHandler()).toolExecutionErrorHandler(DefaultAiServices.this.context.toolService.executionErrorHandler()).toolExecutor(DefaultAiServices.this.context.toolService.executor()).retrievedContents(augmentationResult != null ? augmentationResult.contents() : null).context(DefaultAiServices.this.context).invocationContext(invocationContext).commonGuardrailParams(commonGuardrailParam).methodKey(method).build();
                    AiServiceTokenStream tokenStream = new AiServiceTokenStream(tokenStreamParameters);
                    if (returnType == TokenStream.class) {
                        return tokenStream;
                    }
                    return this.adapt(tokenStream, returnType);
                }
                ResponseFormat responseFormat = null;
                if (supportsJsonSchema && jsonSchema.isPresent()) {
                    responseFormat = ResponseFormat.builder().type(ResponseFormatType.JSON).jsonSchema((JsonSchema)jsonSchema.get()).build();
                }
                ChatRequestParameters parameters = AiServiceParamsUtil.chatRequestParameters(method, args, toolServiceContext, responseFormat);
                ChatRequest chatRequest = DefaultAiServices.this.context.chatRequestTransformer.apply(ChatRequest.builder().messages(messages).parameters(parameters).build(), memoryId);
                ChatExecutor chatExecutor = ChatExecutor.builder((ChatModel)DefaultAiServices.this.context.chatModel).chatRequest(chatRequest).invocationContext(invocationContext).eventListenerRegistrar(DefaultAiServices.this.context.eventListenerRegistrar).build();
                ChatResponse chatResponse = chatExecutor.execute();
                DefaultAiServices.this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceResponseReceivedEvent.builder().invocationContext(invocationContext).response(chatResponse).request(chatRequest).build());
                AiServices.verifyModerationIfNeeded(moderationFuture);
                boolean isReturnTypeResult = TypeUtils.typeHasRawClass(returnType, Result.class);
                ToolServiceResult toolServiceResult = DefaultAiServices.this.context.toolService.executeInferenceAndToolsLoop(DefaultAiServices.this.context, memoryId, chatResponse, parameters, messages, chatMemory, invocationContext, toolServiceContext, arg_0 -> ((ChatModel)DefaultAiServices.this.context.chatModel).chat(arg_0));
                if (toolServiceResult.immediateToolReturn()) {
                    if (isReturnTypeResult) {
                        Result<Object> result = Result.builder().content(null).tokenUsage(toolServiceResult.aggregateTokenUsage()).sources(augmentationResult == null ? null : augmentationResult.contents()).finishReason(FinishReason.TOOL_EXECUTION).toolExecutions(toolServiceResult.toolExecutions()).intermediateResponses(toolServiceResult.intermediateResponses()).finalResponse(toolServiceResult.finalResponse()).build();
                        return this.fireEventAndReturn(invocationContext, result);
                    }
                    if (returnType == Void.TYPE) {
                        return this.fireEventAndReturn(invocationContext, null);
                    }
                    Set returnBehaviors = toolServiceResult.toolExecutions().stream().map(execution -> toolServiceContext.returnBehavior(execution.request().name())).collect(Collectors.toSet());
                    if (returnBehaviors.stream().allMatch(returnBehavior -> returnBehavior == ReturnBehavior.IMMEDIATE || returnBehavior == ReturnBehavior.IMMEDIATE_IF_LAST)) {
                        int numNullResults = 0;
                        ToolExecution lastNonNull = null;
                        for (ToolExecution execution2 : toolServiceResult.toolExecutions()) {
                            if (execution2.resultObject() == null) {
                                ++numNullResults;
                                continue;
                            }
                            lastNonNull = execution2;
                        }
                        if (numNullResults == toolServiceResult.toolExecutions().size()) {
                            return this.fireEventAndReturn(invocationContext, null);
                        }
                        if (numNullResults + 1 == toolServiceResult.toolExecutions().size() && DefaultAiServices.resolvesToType(lastNonNull.resultObject(), returnType)) {
                            return this.fireEventAndReturn(invocationContext, lastNonNull.resultObject());
                        }
                        throw IllegalConfigurationException.illegalConfiguration("AI Service method '%s' call cannot resolve return type from tool executions with ReturnBehavior.%s/%s. Use %s as your return type.", method.getName(), ReturnBehavior.IMMEDIATE, ReturnBehavior.IMMEDIATE_IF_LAST, Result.class.getName());
                    }
                }
                ChatResponse aggregateResponse = toolServiceResult.aggregateResponse();
                ChatExecutor toolAwareRepromptExecutor = ToolAwareRepromptExecutor.wrap(chatExecutor, DefaultAiServices.this.context, memoryId, parameters, invocationContext, toolServiceContext, arg_0 -> ((ChatModel)DefaultAiServices.this.context.chatModel).chat(arg_0));
                Object response = DefaultAiServices.this.invokeOutputGuardrails(DefaultAiServices.this.context.guardrailService(), method, aggregateResponse, toolAwareRepromptExecutor, commonGuardrailParam);
                if (response != null) {
                    if (returnsImage && response instanceof ChatResponse) {
                        ChatResponse cResponse = (ChatResponse)response;
                        return this.fireEventAndReturn(invocationContext, this.parseImages(cResponse, returnType));
                    }
                    if (TypeUtils.typeHasRawClass(returnType, response.getClass())) {
                        return this.fireEventAndReturn(invocationContext, response);
                    }
                }
                Result<Object> parsedResponse = DefaultAiServices.this.serviceOutputParser.parse((ChatResponse)response, returnType);
                Result<Object> actualResponse = isReturnTypeResult ? Result.builder().content(parsedResponse).tokenUsage(toolServiceResult.aggregateTokenUsage()).sources(augmentationResult == null ? null : augmentationResult.contents()).finishReason(toolServiceResult.finalResponse().finishReason()).toolExecutions(toolServiceResult.toolExecutions()).intermediateResponses(toolServiceResult.intermediateResponses()).finalResponse(toolServiceResult.finalResponse()).build() : parsedResponse;
                return this.fireEventAndReturn(invocationContext, actualResponse);
            }

            private Object fireEventAndReturn(InvocationContext invocationContext, Object result) {
                DefaultAiServices.this.context.eventListenerRegistrar.fireEvent((AiServiceEvent)AiServiceCompletedEvent.builder().invocationContext(invocationContext).result(result).build());
                return result;
            }

            private boolean isImage(Type returnType) {
                Class<?> rawReturnType = TypeUtils.getRawClass(returnType);
                if (TypeUtils.isImageType(rawReturnType)) {
                    return true;
                }
                if (Collection.class.isAssignableFrom(rawReturnType)) {
                    Class<?> genericParam = TypeUtils.resolveFirstGenericParameterClass(returnType);
                    return genericParam != null && TypeUtils.isImageType(genericParam);
                }
                return false;
            }

            private Object parseImages(ChatResponse response, Type returnType) {
                List images = response.aiMessage().images();
                Class<?> rawReturnType = TypeUtils.getRawClass(returnType);
                if (this.isImage(rawReturnType)) {
                    if (rawReturnType == ImageContent.class) {
                        List<ImageContent> imageContents = this.toImageContents(images);
                        return imageContents.isEmpty() ? null : imageContents.get(0);
                    }
                    if (rawReturnType == Image.class) {
                        return images.isEmpty() ? null : images.get(0);
                    }
                }
                if (Collection.class.isAssignableFrom(rawReturnType)) {
                    Class<?> genericParam = TypeUtils.resolveFirstGenericParameterClass(returnType);
                    if (genericParam == ImageContent.class) {
                        return this.toImageContents(images);
                    }
                    if (genericParam == Image.class) {
                        return images;
                    }
                }
                throw new UnsupportedOperationException("Unsupported return type " + rawReturnType);
            }

            private List<ImageContent> toImageContents(List<Image> images) {
                return images.stream().map(ImageContent::from).collect(Collectors.toList());
            }

            private boolean canAdaptTokenStreamTo(Type returnType) {
                for (TokenStreamAdapter tokenStreamAdapter : DefaultAiServices.this.tokenStreamAdapters) {
                    if (!tokenStreamAdapter.canAdaptTokenStreamTo(returnType)) continue;
                    return true;
                }
                return false;
            }

            private Object adapt(TokenStream tokenStream, Type returnType) {
                for (TokenStreamAdapter tokenStreamAdapter : DefaultAiServices.this.tokenStreamAdapters) {
                    if (!tokenStreamAdapter.canAdaptTokenStreamTo(returnType)) continue;
                    return tokenStreamAdapter.adapt(tokenStream);
                }
                throw new IllegalStateException("Can't find suitable TokenStreamAdapter");
            }

            private boolean supportsJsonSchema() {
                return DefaultAiServices.this.context.chatModel != null && DefaultAiServices.this.context.chatModel.supportedCapabilities().contains(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
            }

            private dev.langchain4j.data.message.UserMessage appendOutputFormatInstructions(Type returnType, dev.langchain4j.data.message.UserMessage userMessage) {
                String outputFormatInstructions = DefaultAiServices.this.serviceOutputParser.outputFormatInstructions(returnType);
                if (Utils.isNullOrEmpty((String)outputFormatInstructions)) {
                    return userMessage;
                }
                ArrayList<TextContent> contents = new ArrayList<TextContent>(userMessage.contents());
                boolean appended = false;
                for (int i = contents.size() - 1; i >= 0; --i) {
                    if (!(contents.get(i) instanceof TextContent)) continue;
                    TextContent lastTextContent = (TextContent)contents.get(i);
                    String newText = lastTextContent.text() + outputFormatInstructions;
                    contents.set(i, TextContent.from((String)newText));
                    appended = true;
                    break;
                }
                if (!appended) {
                    contents.add(TextContent.from((String)outputFormatInstructions));
                }
                return userMessage.toBuilder().contents(contents).build();
            }

            private Future<Moderation> triggerModerationIfNeeded(Method method, List<ChatMessage> messages) {
                if (method.isAnnotationPresent(Moderate.class)) {
                    ExecutorService executor = DefaultExecutorProvider.getDefaultExecutorService();
                    return executor.submit(() -> {
                        List<ChatMessage> messagesToModerate = AiServices.removeToolMessages(messages);
                        return (Moderation)DefaultAiServices.this.context.moderationModel.moderate(messagesToModerate).content();
                    });
                }
                return null;
            }
        });
        return (T)proxyInstance;
    }

    private static boolean resolvesToType(Object o, Type returnType) {
        return o != null && returnType instanceof Class && ((Class)returnType).isAssignableFrom(o.getClass());
    }

    private dev.langchain4j.data.message.UserMessage invokeInputGuardrails(GuardrailService guardrailService, Method method, dev.langchain4j.data.message.UserMessage userMessage, GuardrailRequestParams commonGuardrailParams) {
        if (guardrailService.hasInputGuardrails(method)) {
            InputGuardrailRequest inputGuardrailRequest = InputGuardrailRequest.builder().userMessage(userMessage).commonParams(commonGuardrailParams).build();
            return guardrailService.executeGuardrails(method, inputGuardrailRequest);
        }
        return userMessage;
    }

    private <T> T invokeOutputGuardrails(GuardrailService guardrailService, Method method, ChatResponse responseFromLLM, ChatExecutor chatExecutor, GuardrailRequestParams commonGuardrailParams) {
        if (guardrailService.hasOutputGuardrails(method)) {
            OutputGuardrailRequest outputGuardrailRequest = OutputGuardrailRequest.builder().responseFromLLM(responseFromLLM).chatExecutor(chatExecutor).requestParams(commonGuardrailParams).build();
            return guardrailService.executeGuardrails(method, outputGuardrailRequest);
        }
        return (T)responseFromLLM;
    }

    private Optional<dev.langchain4j.data.message.SystemMessage> prepareSystemMessage(InvocationContext invocationContext, Method method, Object[] args) {
        return this.findSystemMessageTemplate(invocationContext, method).map(systemMessageTemplate -> PromptTemplate.from((String)systemMessageTemplate).apply(InternalReflectionVariableResolver.findTemplateVariables(systemMessageTemplate, method, args)).toSystemMessage());
    }

    private Optional<String> findSystemMessageTemplate(InvocationContext invocationContext, Method method) {
        SystemMessage annotation = method.getAnnotation(SystemMessage.class);
        if (annotation != null) {
            return Optional.of(DefaultAiServices.getTemplate(method, "System", annotation.fromResource(), annotation.value(), annotation.delimiter()));
        }
        if (this.context.systemMessageProviderWithContext != null) {
            return Optional.of(this.context.systemMessageProviderWithContext.apply(invocationContext));
        }
        return this.context.systemMessageProvider.apply(invocationContext.chatMemoryId());
    }

    private static dev.langchain4j.data.message.UserMessage prepareUserMessage(Method method, Object[] args, String userMessageTemplate, Map<String, Object> variables) {
        Optional<String> maybeUserName = DefaultAiServices.findUserName(method.getParameters(), args);
        if (userMessageTemplate.isEmpty()) {
            ArrayList<Content> contents = new ArrayList<Content>();
            for (Object arg : args) {
                if (arg instanceof Content) {
                    Content content = (Content)arg;
                    contents.add(content);
                    continue;
                }
                if (!DefaultAiServices.isListOfContents(arg)) continue;
                contents.addAll((List)arg);
            }
            if (!contents.isEmpty()) {
                return maybeUserName.map(userName -> dev.langchain4j.data.message.UserMessage.from((String)userName, (List)contents)).orElseGet(() -> dev.langchain4j.data.message.UserMessage.from((List)contents));
            }
            throw IllegalConfigurationException.illegalConfiguration("Error: The method '%s' does not have a user message defined.", method.getName());
        }
        Prompt prompt = PromptTemplate.from((String)userMessageTemplate).apply(variables);
        return maybeUserName.map(userName -> dev.langchain4j.data.message.UserMessage.from((String)userName, (String)prompt.text())).orElseGet(() -> ((Prompt)prompt).toUserMessage());
    }

    private String getUserMessageTemplate(Object memoryId, Method method, Object[] args) {
        Optional<String> templateFromMethodAnnotation = DefaultAiServices.findUserMessageTemplateFromMethodAnnotation(method);
        Optional<String> templateFromParameterAnnotation = DefaultAiServices.findUserMessageTemplateFromAnnotatedParameter(method.getParameters(), args);
        if (templateFromMethodAnnotation.isPresent() && templateFromParameterAnnotation.isPresent()) {
            throw IllegalConfigurationException.illegalConfiguration("Error: The method '%s' has multiple @UserMessage annotations. Please use only one.", method.getName());
        }
        if (templateFromMethodAnnotation.isPresent()) {
            return templateFromMethodAnnotation.get();
        }
        if (templateFromParameterAnnotation.isPresent()) {
            return templateFromParameterAnnotation.get();
        }
        Optional<String> templateFromTheOnlyArgument = DefaultAiServices.findUserMessageTemplateFromTheOnlyArgument(method.getParameters(), args);
        if (templateFromTheOnlyArgument.isPresent()) {
            return templateFromTheOnlyArgument.get();
        }
        if (DefaultAiServices.hasContentArgument(method, args)) {
            return "";
        }
        return this.context.userMessageProvider.apply(memoryId).orElseThrow(() -> IllegalConfigurationException.illegalConfiguration("Error: The method '%s' does not have a user message defined.", method.getName()));
    }

    private static boolean hasContentArgument(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            if (!parameters[i].isAnnotationPresent(UserMessage.class) || !(args[i] instanceof Content) && !DefaultAiServices.isListOfContents(args[i])) continue;
            return true;
        }
        if (parameters.length == 1 && !DefaultAiServices.hasAnyValidAnnotation(parameters[0])) {
            return args[0] instanceof Content || DefaultAiServices.isListOfContents(args[0]);
        }
        return false;
    }

    private static Optional<String> findUserMessageTemplateFromMethodAnnotation(Method method) {
        return Optional.ofNullable(method.getAnnotation(UserMessage.class)).map(a -> DefaultAiServices.getTemplate(method, "User", a.fromResource(), a.value(), a.delimiter()));
    }

    private static Optional<String> findUserMessageTemplateFromAnnotatedParameter(Parameter[] parameters, Object[] args) {
        for (int i = 0; i < parameters.length; ++i) {
            if (!parameters[i].isAnnotationPresent(UserMessage.class) || args[i] instanceof Content || DefaultAiServices.isListOfContents(args[i])) continue;
            return Optional.of(InternalReflectionVariableResolver.asString(args[i]));
        }
        return Optional.empty();
    }

    private static boolean hasAnyValidAnnotation(Parameter parameter) {
        for (Class<? extends Annotation> a : VALID_PARAM_ANNOTATIONS) {
            if (parameter.getAnnotation(a) == null) continue;
            return true;
        }
        return false;
    }

    private static Optional<String> findUserMessageTemplateFromTheOnlyArgument(Parameter[] parameters, Object[] args) {
        if (parameters != null && parameters.length == 1 && !DefaultAiServices.hasAnyValidAnnotation(parameters[0])) {
            if (args[0] instanceof Content || DefaultAiServices.isListOfContents(args[0]) || DefaultAiServices.isMapOfContents(args[0])) {
                return Optional.empty();
            }
            return Optional.of(InternalReflectionVariableResolver.asString(args[0]));
        }
        return Optional.empty();
    }

    private static Optional<String> findUserName(Parameter[] parameters, Object[] args) {
        for (int i = 0; i < parameters.length; ++i) {
            if (!parameters[i].isAnnotationPresent(UserName.class)) continue;
            return Optional.of(args[i].toString());
        }
        return Optional.empty();
    }

    private static dev.langchain4j.data.message.UserMessage addContentsToUserMessage(Method method, Object[] args, dev.langchain4j.data.message.UserMessage userMessage) {
        Map map;
        boolean hasTextContent = false;
        ArrayList<Content> contents = new ArrayList<Content>();
        if (args != null && args.length == 1 && args[0] instanceof Map && !(map = (Map)args[0]).isEmpty()) {
            for (Object value : map.values()) {
                if (value instanceof Content) {
                    Content content = (Content)value;
                    hasTextContent |= value instanceof TextContent;
                    contents.add(content);
                    continue;
                }
                if (!DefaultAiServices.isListOfContents(value)) continue;
                hasTextContent |= ((List)value).stream().anyMatch(TextContent.class::isInstance);
                contents.addAll((List)value);
            }
            if (!contents.isEmpty()) {
                DefaultAiServices.prependTextContentsToUserMessage(userMessage, contents);
                return userMessage.toBuilder().contents(contents).build();
            }
        }
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            if (!parameters[i].isAnnotationPresent(UserMessage.class)) continue;
            if (args[i] instanceof Content) {
                Content content = (Content)args[i];
                contents.add(content);
                continue;
            }
            if (DefaultAiServices.isListOfContents(args[i])) {
                hasTextContent |= ((List)args[i]).stream().anyMatch(TextContent.class::isInstance);
                contents.addAll((List)args[i]);
                continue;
            }
            if (hasTextContent) {
                throw IllegalConfigurationException.illegalConfiguration("Error: The method '%s' has multiple @UserMessage annotations. Please use only one.", method.getName());
            }
            contents.addAll(userMessage.contents());
            hasTextContent = true;
        }
        if (contents.isEmpty() && parameters.length == 1 && !DefaultAiServices.hasAnyValidAnnotation(parameters[0])) {
            if (args[0] instanceof Content) {
                hasTextContent |= args[0] instanceof TextContent;
                contents.add((Content)args[0]);
            } else if (DefaultAiServices.isListOfContents(args[0])) {
                hasTextContent |= ((List)args[0]).stream().anyMatch(TextContent.class::isInstance);
                contents.addAll((List)args[0]);
            }
        }
        if (!hasTextContent) {
            DefaultAiServices.prependTextContentsToUserMessage(userMessage, contents);
        }
        return userMessage.contents().size() == contents.size() ? userMessage : userMessage.toBuilder().contents(contents).build();
    }

    private static void prependTextContentsToUserMessage(dev.langchain4j.data.message.UserMessage userMessage, List<Content> contents) {
        List originalContent = userMessage.contents();
        for (int i = originalContent.size() - 1; i >= 0; --i) {
            if (!(originalContent.get(i) instanceof TextContent)) continue;
            TextContent textContent = (TextContent)originalContent.get(i);
            contents.add(0, (Content)textContent);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean isMapOfContents(Object o) {
        if (!(o instanceof Map)) return false;
        if (!((Map)o).values().stream().allMatch(Content.class::isInstance)) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean isListOfContents(Object o) {
        if (!(o instanceof List)) return false;
        if (!((List)o).stream().allMatch(Content.class::isInstance)) return false;
        return true;
    }

    private static String getTemplate(Method method, String type, String resource, String[] value, String delimiter) {
        String messageTemplate;
        if (!resource.trim().isEmpty()) {
            messageTemplate = DefaultAiServices.getResourceText(method.getDeclaringClass(), resource);
            if (messageTemplate == null) {
                throw IllegalConfigurationException.illegalConfiguration("@%sMessage's resource '%s' not found", type, resource);
            }
        } else {
            messageTemplate = String.join((CharSequence)delimiter, value);
        }
        if (messageTemplate.trim().isEmpty()) {
            throw IllegalConfigurationException.illegalConfiguration("@%sMessage's template cannot be empty", type);
        }
        return messageTemplate;
    }

    private static String getResourceText(Class<?> clazz, String resource) {
        InputStream inputStream = clazz.getResourceAsStream(resource);
        if (inputStream == null) {
            inputStream = clazz.getResourceAsStream("/" + resource);
        }
        return DefaultAiServices.getText(inputStream);
    }

    /*
     * Exception decompiling
     */
    private static String getText(InputStream inputStream) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static Optional<Object> findMemoryId(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            if (!parameters[i].isAnnotationPresent(MemoryId.class)) continue;
            Object memoryId = args[i];
            if (memoryId == null) {
                throw Exceptions.illegalArgument((String)"The value of parameter '%s' annotated with @MemoryId in method '%s' must not be null", (Object[])new Object[]{parameters[i].getName(), method.getName()});
            }
            return Optional.of(memoryId);
        }
        return Optional.empty();
    }
}

