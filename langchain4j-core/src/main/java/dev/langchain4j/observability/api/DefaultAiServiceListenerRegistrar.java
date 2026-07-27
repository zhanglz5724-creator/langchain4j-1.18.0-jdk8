/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NonNull
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.observability.api;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.observability.api.AiServiceListenerRegistrar;
import dev.langchain4j.observability.api.event.AiServiceEvent;
import dev.langchain4j.observability.api.listener.AiServiceListener;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAiServiceListenerRegistrar
implements AiServiceListenerRegistrar {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAiServiceListenerRegistrar.class);
    private final Map<Class<? extends AiServiceEvent>, EventListeners<? extends AiServiceEvent>> listeners = new ConcurrentHashMap<Class<? extends AiServiceEvent>, EventListeners<? extends AiServiceEvent>>();
    private final AtomicBoolean shouldThrowExceptionOnEventError = new AtomicBoolean(false);

    @Override
    public <T extends AiServiceEvent> void register(AiServiceListener<T> listener) {
        ValidationUtils.ensureNotNull(listener, "listener");
        this.listeners.compute(listener.getEventClass(), (eventClass, eventListeners) -> this.addToExistingOrNewList((EventListeners<? extends AiServiceEvent>)eventListeners, listener));
    }

    @Override
    public <T extends AiServiceEvent> void unregister(AiServiceListener<T> listener) {
        ValidationUtils.ensureNotNull(listener, "listener");
        Optional.ofNullable(this.listeners.get(listener.getEventClass())).map(l -> l).ifPresent(eventListeners -> ((EventListeners)eventListeners).remove(listener));
    }

    @Override
    public <T extends AiServiceEvent> void fireEvent(T event) {
        ValidationUtils.ensureNotNull(event, "event");
        Optional.ofNullable(this.listeners.get(event.eventClass())).map(l -> l).ifPresent(l -> ((EventListeners)l).fireEvent(event));
    }

    @Override
    public void shouldThrowExceptionOnEventError(boolean shouldThrowExceptionOnEventError) {
        this.shouldThrowExceptionOnEventError.compareAndSet(!shouldThrowExceptionOnEventError, shouldThrowExceptionOnEventError);
    }

    private <T extends AiServiceEvent> EventListeners<T> addToExistingOrNewList(@Nullable EventListeners<? extends AiServiceEvent> listenersList, AiServiceListener<T> listener) {
        EventListeners list = Optional.ofNullable(listenersList).map(l -> l).orElseGet(() -> new EventListeners());
        list.add(listener);
        return list;
    }

    private class EventListeners<T extends AiServiceEvent> {
        private final Set<@NonNull AiServiceListener<T>> listeners = new LinkedHashSet<AiServiceListener<T>>();
        private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

        private EventListeners() {
        }

        private void add(AiServiceListener<T> listener) {
            ValidationUtils.ensureNotNull(listener, "listener");
            Lock writeLock = this.lock.writeLock();
            writeLock.lock();
            try {
                this.listeners.add(listener);
            }
            finally {
                writeLock.unlock();
            }
        }

        private void remove(AiServiceListener<T> listener) {
            Lock writeLock = this.lock.writeLock();
            writeLock.lock();
            try {
                this.listeners.remove(ValidationUtils.ensureNotNull(listener, "listener"));
            }
            finally {
                writeLock.unlock();
            }
        }

        private void fireEvent(T event) {
            ValidationUtils.ensureNotNull(event, "event");
            Lock readLock = this.lock.readLock();
            readLock.lock();
            try {
                this.listeners.forEach(listener -> {
                    block2: {
                        try {
                            listener.onEvent(event);
                        }
                        catch (Exception e) {
                            LOG.warn(String.format("An error occurred while firing event (%s) to listener (%s): %s", event.getClass().getName(), listener.getClass().getName(), e.getMessage()), (Throwable)e);
                            if (!DefaultAiServiceListenerRegistrar.this.shouldThrowExceptionOnEventError.get()) break block2;
                            throw e;
                        }
                    }
                });
            }
            finally {
                readLock.unlock();
            }
        }
    }
}

