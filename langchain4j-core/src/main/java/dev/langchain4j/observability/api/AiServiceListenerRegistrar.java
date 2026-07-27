/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.observability.api;

import dev.langchain4j.observability.api.DefaultAiServiceListenerRegistrar;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;
import dev.langchain4j.spi.observability.AiServiceListenerRegistrarFactory;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;

public interface AiServiceListenerRegistrar {
    public <T extends AiServiceEvent> void register(AiServiceListener<T> var1);

    default public void register(AiServiceListener<?> ... listeners) {
        if (listeners != null) {
            this.register(Arrays.asList(listeners));
        }
    }

    default public void register(Iterable<? extends AiServiceListener<?>> listeners) {
        if (listeners != null) {
            listeners.forEach(this::register);
        }
    }

    public <T extends AiServiceEvent> void unregister(AiServiceListener<T> var1);

    default public void unregister(AiServiceListener<?> ... listeners) {
        if (listeners != null) {
            this.unregister(Arrays.asList(listeners));
        }
    }

    default public void unregister(Iterable<? extends AiServiceListener<?>> listeners) {
        if (listeners != null) {
            listeners.forEach(this::unregister);
        }
    }

    public <T extends AiServiceEvent> void fireEvent(T var1);

    public void shouldThrowExceptionOnEventError(boolean var1);

    public static AiServiceListenerRegistrar newInstance() {
        return AiServiceListenerRegistrar.newInstance(false);
    }

    public static AiServiceListenerRegistrar newInstance(boolean shouldThrowExceptionOnEventError) {
        Iterator<AiServiceListenerRegistrarFactory> iterator = ServiceLoader.load(AiServiceListenerRegistrarFactory.class).iterator();
        if (iterator.hasNext()) {
            AiServiceListenerRegistrarFactory factory = iterator.next();
            AiServiceListenerRegistrar registrar = (AiServiceListenerRegistrar)factory.get();
            registrar.shouldThrowExceptionOnEventError(shouldThrowExceptionOnEventError);
            return registrar;
        }
        DefaultAiServiceListenerRegistrar registrar = new DefaultAiServiceListenerRegistrar();
        registrar.shouldThrowExceptionOnEventError(shouldThrowExceptionOnEventError);
        return registrar;
    }
}

