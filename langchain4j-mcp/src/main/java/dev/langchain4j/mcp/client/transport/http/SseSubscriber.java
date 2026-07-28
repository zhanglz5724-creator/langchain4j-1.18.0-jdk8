package dev.langchain4j.mcp.client.transport.http;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.slf4j.Logger;

class SseSubscriber {

    private final Consumer<String> eventDataCallback;
    private final Consumer<Throwable> errorCallback;
    private final Logger logger;
    private final boolean logResponses;
    private final boolean subsidiary;
    private final AtomicReference<String> lastEventId;
    private final AtomicLong retryMs;
    private final Runnable onStreamEnd;
    private final AtomicBoolean transportClosed;

    /**
     * Constructor for a regular (non-subsidiary) SSE subscriber, used for POST response streams.
     */
    SseSubscriber(
            Consumer<String> eventDataCallback,
            Consumer<Throwable> errorCallback,
            boolean logResponses,
            Logger logger) {
        this.eventDataCallback = eventDataCallback;
        this.errorCallback = errorCallback;
        this.logResponses = logResponses;
        this.logger = logger;
        this.subsidiary = false;
        this.lastEventId = null;
        this.retryMs = null;
        this.onStreamEnd = null;
        this.transportClosed = new AtomicBoolean(false);
    }

    /**
     * Constructor for a subsidiary SSE subscriber, used for the long-lived GET SSE channel.
     */
    SseSubscriber(
            Consumer<String> eventDataCallback,
            Consumer<Throwable> onStreamError,
            boolean logResponses,
            Logger logger,
            AtomicReference<String> lastEventId,
            AtomicLong retryMs,
            Runnable onStreamEnd,
            AtomicBoolean transportClosed) {
        this.eventDataCallback = eventDataCallback;
        this.errorCallback = onStreamError;
        this.logResponses = logResponses;
        this.logger = logger;
        this.subsidiary = true;
        this.lastEventId = lastEventId;
        this.retryMs = retryMs;
        this.onStreamEnd = onStreamEnd;
        this.transportClosed = transportClosed;
    }

    void onLine(String item) {
        if (logResponses && !item.trim().isEmpty()) {
            logger.info("SSE event received: " + item);
        }
        if (item.startsWith("data:")) {
            String data = item.substring(5);
            eventDataCallback.accept(data);
        } else if (item.startsWith("id:") && lastEventId != null) {
            lastEventId.set(item.substring(3).trim());
        } else if (item.startsWith("retry:") && retryMs != null) {
            try {
                retryMs.set(Long.parseLong(item.substring(6).trim()));
            } catch (NumberFormatException e) {
                logger.warn("Failed to parse SSE retry value: " + item, e);
            }
        }
    }

    void onComplete() {
        if (subsidiary) {
            logger.debug("Subsidiary SSE channel closed");
            if (onStreamEnd != null && !transportClosed.get()) {
                onStreamEnd.run();
            }
        } else {
            logger.debug("SSE channel closed");
        }
    }

    void onError(Throwable throwable) {
        if (subsidiary && !transportClosed.get()) {
            logger.debug("Subsidiary SSE channel error", throwable);
            if (onStreamEnd != null) {
                onStreamEnd.run();
            }
        } else if (errorCallback != null) {
            errorCallback.accept(throwable);
        }
    }
}
