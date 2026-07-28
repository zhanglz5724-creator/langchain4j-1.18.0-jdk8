/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClient
 *  dev.langchain4j.http.client.HttpRequest
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.http.client.sse.CancellationUnsupportedHandle
 *  dev.langchain4j.http.client.sse.ServerSentEvent
 *  dev.langchain4j.http.client.sse.ServerSentEventContext
 *  dev.langchain4j.http.client.sse.ServerSentEventListener
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandle
 *  dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils
 *  dev.langchain4j.model.chat.response.StreamingHandle
 */
package dev.langchain4j.model.openai.internal;

import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.sse.CancellationUnsupportedHandle;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventContext;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandle;
import dev.langchain4j.http.client.sse.ServerSentEventParsingHandleUtils;
import dev.langchain4j.model.chat.response.StreamingHandle;
import dev.langchain4j.model.openai.internal.ErrorHandling;
import dev.langchain4j.model.openai.internal.Json;
import dev.langchain4j.model.openai.internal.ParsedAndRawResponse;
import dev.langchain4j.model.openai.internal.ResponseHandle;
import dev.langchain4j.model.openai.internal.StreamingCompletionHandling;
import dev.langchain4j.model.openai.internal.StreamingResponseHandling;
import java.util.function.Consumer;

class StreamingRequestExecutor<Response> {
    private final HttpClient httpClient;
    private final HttpRequest streamingHttpRequest;
    private final Class<Response> responseClass;

    StreamingRequestExecutor(HttpClient httpClient, HttpRequest streamingHttpRequest, Class<Response> responseClass) {
        this.httpClient = httpClient;
        this.streamingHttpRequest = streamingHttpRequest;
        this.responseClass = responseClass;
    }

    StreamingResponseHandling onPartialResponse(final Consumer<ParsedAndRawResponse<Response>> partialResponseHandler) {
        return new StreamingResponseHandling(){

            @Override
            public StreamingCompletionHandling onComplete(final Runnable streamingCompletionCallback) {
                return new StreamingCompletionHandling(){

                    @Override
                    public ErrorHandling onError(final Consumer<Throwable> errorHandler) {
                        return new ErrorHandling(){

                            @Override
                            public ResponseHandle execute() {
                                return StreamingRequestExecutor.this.stream(partialResponseHandler, streamingCompletionCallback, errorHandler);
                            }
                        };
                    }

                    @Override
                    public ErrorHandling ignoreErrors() {
                        return new ErrorHandling(){

                            @Override
                            public ResponseHandle execute() {
                                return StreamingRequestExecutor.this.stream(partialResponseHandler, streamingCompletionCallback, e -> {});
                            }
                        };
                    }
                };
            }

            @Override
            public ErrorHandling onError(final Consumer<Throwable> errorHandler) {
                return new ErrorHandling(){

                    @Override
                    public ResponseHandle execute() {
                        return StreamingRequestExecutor.this.stream(partialResponseHandler, () -> {}, errorHandler);
                    }
                };
            }

            @Override
            public ErrorHandling ignoreErrors() {
                return new ErrorHandling(){

                    @Override
                    public ResponseHandle execute() {
                        return StreamingRequestExecutor.this.stream(partialResponseHandler, () -> {}, e -> {});
                    }
                };
            }
        };
    }

    private ResponseHandle stream(final Consumer<ParsedAndRawResponse<Response>> partialResponseHandler, final Runnable streamingCompletionCallback, final Consumer<Throwable> errorHandler) {
        ServerSentEventListener listener = new ServerSentEventListener(){
            volatile SuccessfulHttpResponse response;
            volatile StreamingHandle streamingHandle;

            public void onOpen(SuccessfulHttpResponse response) {
                this.response = response;
            }

            public void onEvent(ServerSentEvent event) {
                this.onEvent(event, new ServerSentEventContext((ServerSentEventParsingHandle)new CancellationUnsupportedHandle()));
            }

            public void onEvent(ServerSentEvent event, ServerSentEventContext context) {
                if (this.streamingHandle == null) {
                    this.streamingHandle = ServerSentEventParsingHandleUtils.toStreamingHandle((ServerSentEventParsingHandle)context.parsingHandle());
                }
                if ("[DONE]".equals(event.data())) {
                    return;
                }
                try {
                    if ("error".equals(event.event())) {
                        errorHandler.accept(new RuntimeException(event.data()));
                        return;
                    }
                    Object parsedResponse = Json.fromJson(event.data(), StreamingRequestExecutor.this.responseClass);
                    if (parsedResponse != null) {
                        ParsedAndRawResponse parsedAndRawResponse = ParsedAndRawResponse.builder().parsedResponse(parsedResponse).rawHttpResponse(this.response).rawServerSentEvent(event).streamingHandle(this.streamingHandle).build();
                        partialResponseHandler.accept(parsedAndRawResponse);
                    }
                }
                catch (Exception e) {
                    errorHandler.accept(e);
                }
            }

            public void onClose() {
                if (this.streamingHandle == null || !this.streamingHandle.isCancelled()) {
                    streamingCompletionCallback.run();
                }
            }

            public void onError(Throwable t) {
                errorHandler.accept(t);
            }
        };
        this.httpClient.execute(this.streamingHttpRequest, listener);
        return new ResponseHandle();
    }
}

