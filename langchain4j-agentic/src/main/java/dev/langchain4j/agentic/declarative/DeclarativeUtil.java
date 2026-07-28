/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.memory.ChatMemory
 *  dev.langchain4j.model.chat.ChatModel
 *  dev.langchain4j.model.chat.StreamingChatModel
 *  dev.langchain4j.rag.RetrievalAugmentor
 *  dev.langchain4j.rag.content.retriever.ContentRetriever
 *  dev.langchain4j.service.tool.ToolProvider
 */
package dev.langchain4j.agentic.declarative;

import dev.langchain4j.Internal;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.agent.AgentBuilder;
import dev.langchain4j.agentic.agent.ErrorContext;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.declarative.AgentListenerSupplier;
import dev.langchain4j.agentic.declarative.BeforeCall;
import dev.langchain4j.agentic.declarative.ChatMemoryProviderSupplier;
import dev.langchain4j.agentic.declarative.ChatMemorySupplier;
import dev.langchain4j.agentic.declarative.ChatModelSupplier;
import dev.langchain4j.agentic.declarative.ContentRetrieverSupplier;
import dev.langchain4j.agentic.declarative.ErrorHandler;
import dev.langchain4j.agentic.declarative.Output;
import dev.langchain4j.agentic.declarative.ParallelExecutor;
import dev.langchain4j.agentic.declarative.RetrievalAugmentorSupplier;
import dev.langchain4j.agentic.declarative.StreamingChatModelSupplier;
import dev.langchain4j.agentic.declarative.SupplierParameterResolver;
import dev.langchain4j.agentic.declarative.SystemMessageProviderSupplier;
import dev.langchain4j.agentic.declarative.ToolProviderSupplier;
import dev.langchain4j.agentic.declarative.ToolsSupplier;
import dev.langchain4j.agentic.declarative.UserMessageProviderSupplier;
import dev.langchain4j.agentic.internal.AgentUtil;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgenticService;
import dev.langchain4j.agentic.planner.AgenticSystemConfigurationException;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.tool.ToolProvider;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Internal
public class DeclarativeUtil {
    private static final List<SupplierParameterResolver> supplierParameterResolvers = new CopyOnWriteArrayList<SupplierParameterResolver>();

    private DeclarativeUtil() {
    }

    public static void configureAgent(Class<?> agentType, AgentBuilder<?, ?> agentBuilder) {
        DeclarativeUtil.configureAgent(agentType, null, true, agentBuilder, AgenticServices.AgentConfigurator.empty());
    }

    public static void configureAgent(Class<?> agentType, ChatModel chatModel, AgentBuilder<?, ?> agentBuilder, AgenticServices.AgentConfigurator agentConfigurator) {
        DeclarativeUtil.configureAgent(agentType, chatModel, false, agentBuilder, agentConfigurator);
    }

    private static void configureAgent(Class<?> agentType, ChatModel chatModel, boolean allowNullChatModel, AgentBuilder<?, ?> agentBuilder, AgenticServices.AgentConfigurator agentConfigurator) {
        AgentUtil.getAnnotatedMethodOnClass(agentType, ToolsSupplier.class).ifPresent(method -> {
            Object tools = DeclarativeUtil.invokeSupplierWithResolvers(agentType, method, Object.class);
            if (tools instanceof Map) {
                agentBuilder.tools((Map)tools);
            } else if (tools.getClass().isArray()) {
                agentBuilder.tools((Object[])tools);
            } else {
                agentBuilder.tools(tools);
            }
        });
        AgentUtil.getAnnotatedMethodOnClass(agentType, ToolProviderSupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, ToolProvider.class);
            agentBuilder.toolProvider(DeclarativeUtil.invokeSupplierWithResolvers(agentType, method, ToolProvider.class));
        });
        AgentUtil.getAnnotatedMethodOnClass(agentType, ContentRetrieverSupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, ContentRetriever.class);
            agentBuilder.contentRetriever(DeclarativeUtil.invokeSupplierWithResolvers(agentType, method, ContentRetriever.class));
        });
        AgentUtil.getAnnotatedMethodOnClass(agentType, RetrievalAugmentorSupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, RetrievalAugmentor.class);
            agentBuilder.retrievalAugmentor(DeclarativeUtil.invokeSupplierWithResolvers(agentType, method, RetrievalAugmentor.class));
        });
        AgentUtil.getAnnotatedMethodOnClass(agentType, ChatMemoryProviderSupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, ChatMemory.class);
            if (method.getParameterCount() == 0) {
                throw new IllegalArgumentException("Method " + method + " must have at least 1 argument: [class java.lang.Object]");
            }
            if (method.getParameterCount() == 1) {
                DeclarativeUtil.checkArguments(method, Object.class);
                agentBuilder.chatMemoryProvider(memoryId -> (ChatMemory)DeclarativeUtil.invokeStatic(method, memoryId));
            } else {
                agentBuilder.chatMemoryProvider(memoryId -> {
                    Function<AgenticScope, ChatMemory> fn = DeclarativeUtil.agenticScopeFunctionWithSupplierParameterResolver(agentType, method, ChatMemory.class);
                    return fn.apply(null);
                });
            }
        });
        AgentUtil.getAnnotatedMethodOnClass(agentType, ChatMemorySupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, ChatMemory.class);
            agentBuilder.chatMemory(DeclarativeUtil.invokeSupplierWithResolvers(agentType, method, ChatMemory.class));
        });
        Optional<Method> suppliedChatModel = AgentUtil.getAnnotatedMethodOnClass(agentType, ChatModelSupplier.class);
        if (suppliedChatModel.isPresent()) {
            Method method2 = suppliedChatModel.get();
            if (method2.getParameterCount() > 0) {
                Function<AgenticScope, ChatModel> scopeFunction = DeclarativeUtil.agenticScopeFunctionWithSupplierParameterResolver(agentType, method2, ChatModel.class);
                Function<AgenticScope, ChatModel> provider = scope -> {
                    if (scope == null) {
                        return (ChatModel)DeclarativeUtil.invokeStatic(method2, new Object[method2.getParameterCount()]);
                    }
                    return (ChatModel)scopeFunction.apply((AgenticScope)scope);
                };
                agentBuilder.chatModel(provider);
            } else {
                agentBuilder.chatModel((ChatModel)DeclarativeUtil.invokeStatic(method2, new Object[0]));
            }
        } else {
            Optional<Method> suppliedStreamingChatModel = AgentUtil.getAnnotatedMethodOnClass(agentType, StreamingChatModelSupplier.class);
            if (suppliedStreamingChatModel.isPresent()) {
                Method method3 = suppliedStreamingChatModel.get();
                if (method3.getParameterCount() > 0) {
                    Function<AgenticScope, StreamingChatModel> scopeFunction = DeclarativeUtil.agenticScopeFunctionWithSupplierParameterResolver(agentType, method3, StreamingChatModel.class);
                    Function<AgenticScope, StreamingChatModel> provider = scope -> {
                        if (scope == null) {
                            return (StreamingChatModel)DeclarativeUtil.invokeStatic(method3, new Object[method3.getParameterCount()]);
                        }
                        return (StreamingChatModel)scopeFunction.apply((AgenticScope)scope);
                    };
                    agentBuilder.streamingChatModel(provider);
                } else {
                    agentBuilder.streamingChatModel((StreamingChatModel)DeclarativeUtil.invokeStatic(method3, new Object[0]));
                }
            } else {
                if (chatModel == null && !allowNullChatModel) {
                    throw new IllegalArgumentException("ChatModel not provided for subagent " + agentType.getName() + ". Please provide one either with a static method annotated with @ChatModelSupplier or @StreamingChatModelSupplier, or through the parent agent's chatModel parameter.");
                }
                agentBuilder.chatModel(chatModel);
            }
        }
        AgentUtil.getAnnotatedMethodOnClass(agentType, AgentListenerSupplier.class).ifPresent(listenerMethod -> {
            DeclarativeUtil.checkReturnType(listenerMethod, AgentListener.class);
            agentBuilder.listener(DeclarativeUtil.invokeSupplierWithResolvers(agentType, listenerMethod, AgentListener.class));
        });
        AgentUtil.getAnnotatedMethodOnClass(agentType, SystemMessageProviderSupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, String.class);
            DeclarativeUtil.checkArguments(method, Object.class);
            agentBuilder.systemMessageProvider(memoryId -> (String)DeclarativeUtil.invokeStatic(method, memoryId));
        });
        AgentUtil.getAnnotatedMethodOnClass(agentType, UserMessageProviderSupplier.class).ifPresent(method -> {
            DeclarativeUtil.checkReturnType(method, String.class);
            DeclarativeUtil.checkArguments(method, Object.class);
            agentBuilder.userMessageProvider(memoryId -> (String)DeclarativeUtil.invokeStatic(method, memoryId));
        });
        if (agentConfigurator.agentInstanceFactory() != null) {
            agentBuilder.agentInstanceFactory(agentConfigurator.agentInstanceFactory());
        }
        agentConfigurator.configurator().accept(new AgenticServices.DefaultDeclarativeAgentCreationContext(agentType, agentBuilder));
    }

    public static void checkArguments(Method method, Class<?> ... expected) {
        Class<?>[] actual = method.getParameterTypes();
        if (actual.length != expected.length) {
            throw new IllegalArgumentException("Method " + method + " must have " + expected.length + " arguments: " + Arrays.toString(expected));
        }
        for (int i = 0; i < expected.length; ++i) {
            if (expected[i].isAssignableFrom(actual[i])) continue;
            throw new IllegalArgumentException("Method " + method + " argument " + (i + 1) + " must be of type " + expected[i].getName());
        }
    }

    public static void checkReturnType(Method method, Class<?> expected) {
        if (!method.getReturnType().isAssignableFrom(expected)) {
            throw new IllegalArgumentException("Method " + method + " must return " + expected.getName());
        }
    }

    public static <T> T invokeStatic(Method method, Object ... args) {
        try {
            return (T)method.invoke(null, args);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void configureOutput(Class<T> agentServiceClass, AgenticService<?, ?> builder) {
        DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(Output.class)).map(m -> DeclarativeUtil.agenticScopeFunction(m, Object.class)).ifPresent(builder::output);
    }

    public static Optional<Method> predicateMethod(Class<?> agentServiceClass, Predicate<Method> methodSelector) {
        return DeclarativeUtil.selectMethod(agentServiceClass, methodSelector.and(m -> m.getReturnType() == Boolean.TYPE || m.getReturnType() == Boolean.class));
    }

    public static void buildAgentFeatures(Class<?> agentServiceClass, AgenticService<?, ?> builder) {
        DeclarativeUtil.buildBeforeCall(agentServiceClass).ifPresent(builder::beforeCall);
        DeclarativeUtil.buildErrorHandler(agentServiceClass).ifPresent(builder::errorHandler);
        DeclarativeUtil.buildListener(agentServiceClass, builder);
    }

    private static Optional<Consumer<AgenticScope>> buildBeforeCall(Class<?> agentServiceClass) {
        return DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(BeforeCall.class)).map(m -> {
            DeclarativeUtil.checkReturnType(m, Void.TYPE);
            return DeclarativeUtil.agenticScopeFunction(m, Object.class)::apply;
        });
    }

    public static Optional<Executor> parallelExecutor(Class<?> agentServiceClass) {
        return DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(ParallelExecutor.class) && Executor.class.isAssignableFrom(method.getReturnType())).map(method -> DeclarativeUtil.invokeParallelExecutor(agentServiceClass, method));
    }

    private static <T> Optional<Function<ErrorContext, ErrorRecoveryResult>> buildErrorHandler(Class<T> agentServiceClass) {
        return DeclarativeUtil.selectMethod(agentServiceClass, method -> method.isAnnotationPresent(ErrorHandler.class)).map(m -> errorContext -> (ErrorRecoveryResult)DeclarativeUtil.invokeStatic(m, errorContext));
    }

    private static void buildListener(Class<?> agentServiceClass, AgenticService<?, ?> builder) {
        AgentUtil.getAnnotatedMethodOnClass(agentServiceClass, AgentListenerSupplier.class).ifPresent(listenerMethod -> {
            DeclarativeUtil.checkReturnType(listenerMethod, AgentListener.class);
            builder.listener((AgentListener)DeclarativeUtil.invokeStatic(listenerMethod, new Object[0]));
        });
    }

    public static Optional<Method> selectMethod(Class<?> agentServiceClass, Predicate<Method> methodSelector) {
        Optional<Method> method;
        for (Method method2 : agentServiceClass.getMethods()) {
            if (!methodSelector.test(method2) || !Modifier.isStatic(method2.getModifiers())) continue;
            return Optional.of(method2);
        }
        if (agentServiceClass.getSuperclass() != null && (method = DeclarativeUtil.selectMethod(agentServiceClass.getSuperclass(), methodSelector)).isPresent()) {
            return method;
        }
        for (GenericDeclaration genericDeclaration : agentServiceClass.getInterfaces()) {
            Optional<Method> method3 = DeclarativeUtil.selectMethod(genericDeclaration, methodSelector);
            if (!method3.isPresent()) continue;
            return method3;
        }
        return Optional.empty();
    }

    public static Predicate<AgenticScope> agenticScopePredicate(Method predicateMethod) {
        return agenticScope -> DeclarativeUtil.agenticScopeFunction(predicateMethod, Boolean.TYPE).apply((AgenticScope)agenticScope);
    }

    private static <T> T invokeSupplierWithResolvers(Class<?> agentType, Method method, Class<T> targetClass) {
        if (method.getParameterCount() == 0) {
            return DeclarativeUtil.invokeStatic(method, new Object[0]);
        }
        List<SupplierParameterResolver> resolvers = DeclarativeUtil.getSupplierParameterResolvers();
        if (resolvers.isEmpty()) {
            return DeclarativeUtil.invokeStatic(method, new Object[method.getParameterCount()]);
        }
        Function<AgenticScope, Object> fn = DeclarativeUtil.agenticScopeFunctionWithSupplierParameterResolver(agentType, method, targetClass);
        return fn.apply(null);
    }

    private static <T> Function<AgenticScope, T> agenticScopeFunctionWithSupplierParameterResolver(Class<?> agentType, Method functionMethod, Class<T> targetClass) {
        List<SupplierParameterResolver> resolvers = DeclarativeUtil.getSupplierParameterResolvers();
        if (resolvers.isEmpty()) {
            return DeclarativeUtil.agenticScopeFunction(functionMethod, targetClass);
        }
        Parameter[] parameters = functionMethod.getParameters();
        ArrayList<AgentArgument> unresolvedAgentArguments = new ArrayList<AgentArgument>(parameters.length);
        ArrayList<Integer> unresolvedParameterIndexes = new ArrayList<Integer>(parameters.length);
        SupplierParameterResolver.Context[] contexts = new SupplierParameterResolver.Context[parameters.length];
        SupplierParameterResolver[] paramResolvers = new SupplierParameterResolver[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            DefaultSupplierParameterResolverContext ctx = new DefaultSupplierParameterResolverContext(agentType, functionMethod, parameters[i]);
            for (SupplierParameterResolver resolver : resolvers) {
                if (!resolver.supports(ctx)) continue;
                contexts[i] = ctx;
                paramResolvers[i] = resolver;
                break;
            }
            if (paramResolvers[i] != null) continue;
            unresolvedAgentArguments.add(AgentUtil.argumentFromParameter(parameters[i]));
            unresolvedParameterIndexes.add(i);
        }
        return agenticScope -> {
            try {
                Object[] args = new Object[parameters.length];
                for (int i = 0; i < paramResolvers.length; ++i) {
                    if (paramResolvers[i] == null) continue;
                    args[i] = paramResolvers[i].resolve(contexts[i]);
                }
                HashMap<String, Object> additionalArgs = new HashMap<String, Object>();
                additionalArgs.put("@AgenticScope", agenticScope);
                Object[] unresolvedArgs = AgentUtil.agentInvocationArguments(agenticScope, unresolvedAgentArguments, additionalArgs).positionalArgs();
                for (int i = 0; i < unresolvedArgs.length; ++i) {
                    args[((Integer)unresolvedParameterIndexes.get((int)i)).intValue()] = unresolvedArgs[i];
                }
                return functionMethod.invoke(null, args);
            }
            catch (Exception e) {
                throw new RuntimeException("Error invoking method: " + functionMethod.getName(), e);
            }
        };
    }

    private static Executor invokeParallelExecutor(Class<?> agentType, Method method) {
        if (method.getParameterCount() == 0) {
            return (Executor)DeclarativeUtil.invokeStatic(method, new Object[0]);
        }
        List<SupplierParameterResolver> resolvers = DeclarativeUtil.getSupplierParameterResolvers();
        if (resolvers.isEmpty()) {
            throw DeclarativeUtil.missingSupplierParameterResolver(method, method.getParameters()[0]);
        }
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            DefaultSupplierParameterResolverContext ctx = new DefaultSupplierParameterResolverContext(agentType, method, parameters[i]);
            SupplierParameterResolver resolver = null;
            for (SupplierParameterResolver candidate : resolvers) {
                if (!candidate.supports(ctx)) continue;
                resolver = candidate;
                break;
            }
            if (resolver == null) {
                throw DeclarativeUtil.missingSupplierParameterResolver(method, parameters[i]);
            }
            args[i] = resolver.resolve(ctx);
        }
        return (Executor)DeclarativeUtil.invokeStatic(method, args);
    }

    private static AgenticSystemConfigurationException missingSupplierParameterResolver(Method method, Parameter parameter) {
        return new AgenticSystemConfigurationException("No SupplierParameterResolver is registered for parameter " + parameter + " of @ParallelExecutor method " + method + ".");
    }

    public static <T> Function<AgenticScope, T> agenticScopeFunction(Method functionMethod, Class<T> targetClass) {
        List<AgentArgument> agentArguments = AgentUtil.argumentsFromMethod(functionMethod);
        return agenticScope -> {
            try {
                Object[] args = AgentUtil.agentInvocationArguments(agenticScope, agentArguments, Collections.singletonMap("@AgenticScope", agenticScope)).positionalArgs();
                return functionMethod.invoke(null, args);
            }
            catch (Exception e) {
                throw new RuntimeException("Error invoking method: " + functionMethod.getName(), e);
            }
        };
    }

    public static void addSupplierParameterResolver(SupplierParameterResolver resolver) {
        supplierParameterResolvers.add(resolver);
    }

    public static List<SupplierParameterResolver> getSupplierParameterResolvers() {
        return supplierParameterResolvers;
    }

    private static class DefaultSupplierParameterResolverContext
    implements SupplierParameterResolver.Context {
        private final Class<?> declaringAgentClass;
        private final Method supplierMethod;
        private final Parameter parameter;

        DefaultSupplierParameterResolverContext(Class<?> declaringAgentClass, Method supplierMethod, Parameter parameter) {
            this.declaringAgentClass = declaringAgentClass;
            this.supplierMethod = supplierMethod;
            this.parameter = parameter;
        }

        @Override
        public Class<?> declaringAgentClass() {
            return this.declaringAgentClass;
        }

        @Override
        public Method supplierMethod() {
            return this.supplierMethod;
        }

        @Override
        public Parameter parameter() {
            return this.parameter;
        }
    }
}

