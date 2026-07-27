/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.Internal;
import dev.langchain4j.guardrail.AbstractChatExecutor;
import dev.langchain4j.guardrail.ChatExecutor;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
final class StreamingToSynchronousChatExecutor
extends AbstractChatExecutor {
    private final StreamingChatModel streamingChatModel;
    private final Consumer<Throwable> errorHandler;

    protected StreamingToSynchronousChatExecutor(ChatExecutor.StreamingToSynchronousBuilder builder) {
        super(builder);
        this.streamingChatModel = ValidationUtils.ensureNotNull(builder.streamingChatModel, "streamingChatModel");
        this.errorHandler = builder.errorHandler;
    }

    @Override
    protected ChatResponse execute(ChatRequest chatRequest) {
        StreamingToSyncResponseHandler responseHandler = new StreamingToSyncResponseHandler(this.errorHandler);
        this.streamingChatModel.chat(chatRequest, (StreamingChatResponseHandler)responseHandler);
        return Optional.ofNullable(responseHandler.getResponse()).orElseGet(ChatResponse.builder()::build);
    }

    private static class StreamingToSyncResponseHandler
    implements StreamingChatResponseHandler {
        private static final Logger LOG = LoggerFactory.getLogger(StreamingToSyncResponseHandler.class);
        private final Consumer<Throwable> errorHandler;
        private final CountDownLatch latch = new CountDownLatch(1);
        private AtomicReference<ChatResponse> response = new AtomicReference();

        StreamingToSyncResponseHandler(Consumer<Throwable> errorHandler) {
            this.errorHandler = errorHandler;
        }

        @Override
        public void onPartialResponse(String partialResponse) {
        }

        @Override
        public void onCompleteResponse(ChatResponse completeResponse) {
            this.response.set(completeResponse);
            this.latch.countDown();
        }

        private void waitForCompletion() {
            try {
                this.latch.await();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        ChatResponse getResponse() {
            this.waitForCompletion();
            return this.response.get();
        }

        @Override
        public void onError(Throwable error) {
            if (this.errorHandler != null) {
                try {
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
    }
}

