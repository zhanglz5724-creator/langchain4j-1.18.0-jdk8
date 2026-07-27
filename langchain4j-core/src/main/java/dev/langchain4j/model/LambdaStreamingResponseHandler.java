/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class LambdaStreamingResponseHandler {
    public static StreamingChatResponseHandler onPartialResponse(final Consumer<String> onPartialResponse) {
        return new StreamingChatResponseHandler(){

            @Override
            public void onPartialResponse(String partialResponse) {
                onPartialResponse.accept(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
            }

            @Override
            public void onError(Throwable error) {
                throw new RuntimeException(error);
            }
        };
    }

    public static StreamingChatResponseHandler onPartialResponseAndError(final Consumer<String> onPartialResponseLambda, final Consumer<Throwable> onErrorLambda) {
        return new StreamingChatResponseHandler(){

            @Override
            public void onPartialResponse(String partialResponse) {
                onPartialResponseLambda.accept(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
            }

            @Override
            public void onError(Throwable error) {
                onErrorLambda.accept(error);
            }
        };
    }

    public static void onPartialResponseBlocking(StreamingChatModel model, String message, Consumer<String> onPartialResponse) throws InterruptedException {
        LambdaStreamingResponseHandler.onPartialResponseAndErrorBlocking(model, message, onPartialResponse, Throwable::printStackTrace);
    }

    public static void onPartialResponseAndErrorBlocking(StreamingChatModel model, String message, final Consumer<String> onPartialResponse, final Consumer<Throwable> onError) throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        StreamingChatResponseHandler handler = new StreamingChatResponseHandler(){

            @Override
            public void onPartialResponse(String partialResponse) {
                onPartialResponse.accept(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                completionLatch.countDown();
            }

            @Override
            public void onError(Throwable error) {
                onError.accept(error);
                completionLatch.countDown();
            }
        };
        model.chat(message, handler);
        completionLatch.await();
    }
}

