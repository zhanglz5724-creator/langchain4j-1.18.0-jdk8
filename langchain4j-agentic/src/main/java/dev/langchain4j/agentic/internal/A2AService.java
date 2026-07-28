/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.internal;

import dev.langchain4j.agentic.internal.A2AClientBuilder;
import dev.langchain4j.agentic.internal.AgentExecutor;
import dev.langchain4j.agentic.internal.InternalAgent;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;

public interface A2AService {
    public <T> A2AClientBuilder<T> a2aBuilder(String var1, Class<T> var2);

    public Optional<AgentExecutor> methodToAgentExecutor(InternalAgent var1, Method var2);

    public static A2AService get() {
        return Provider.a2aService;
    }

    public static void setA2AService(A2AService a2aService) {
        Provider.a2aService = a2aService;
    }

    public static class DummyA2AService
    implements A2AService {
        private DummyA2AService() {
        }

        @Override
        public <T> A2AClientBuilder<T> a2aBuilder(String a2aServerUrl, Class<T> agentServiceClass) {
            throw DummyA2AService.noA2AException();
        }

        @Override
        public Optional<AgentExecutor> methodToAgentExecutor(InternalAgent agent, Method method) {
            return Optional.empty();
        }

        private static UnsupportedOperationException noA2AException() {
            return new UnsupportedOperationException("No A2A service implementation found. Please add 'langchain4j-agentic-a2a' to your dependencies.");
        }
    }

    public static class Provider {
        static A2AService a2aService = Provider.loadA2AService();

        private Provider() {
        }

        private static A2AService loadA2AService() {
            ServiceLoader<A2AService> loader = ServiceLoader.load(A2AService.class);
            Iterator<A2AService> iterator = loader.iterator();
            if (iterator.hasNext()) {
                A2AService service = iterator.next();
                return service;
            }
            return new DummyA2AService();
        }
    }
}

