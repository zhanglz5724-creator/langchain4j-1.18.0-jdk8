/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.HttpException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.http.client.log;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.HttpException;
import dev.langchain4j.http.client.HttpClient;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.HttpRequestLogger;
import dev.langchain4j.http.client.log.HttpResponseLogger;
import dev.langchain4j.http.client.sse.ServerSentEvent;
import dev.langchain4j.http.client.sse.ServerSentEventContext;
import dev.langchain4j.http.client.sse.ServerSentEventListener;
import dev.langchain4j.http.client.sse.ServerSentEventParser;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
public class LoggingHttpClient
implements HttpClient {
    private static final Logger DEFAULT_LOG = LoggerFactory.getLogger(LoggingHttpClient.class);
    private final HttpClient delegateHttpClient;
    private final boolean logRequests;
    private final boolean logResponses;
    private final Logger log;

    public LoggingHttpClient(HttpClient delegateHttpClient, Boolean logRequests, Boolean logResponses) {
        this.delegateHttpClient = (HttpClient)ValidationUtils.ensureNotNull((Object)delegateHttpClient, (String)"delegateHttpClient");
        this.logRequests = (Boolean)Utils.getOrDefault((Object)logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)logResponses, (Object)false);
        this.log = DEFAULT_LOG;
    }

    public LoggingHttpClient(HttpClient delegateHttpClient, Boolean logRequests, Boolean logResponses, Logger logger) {
        this.delegateHttpClient = (HttpClient)ValidationUtils.ensureNotNull((Object)delegateHttpClient, (String)"delegateHttpClient");
        this.logRequests = (Boolean)Utils.getOrDefault((Object)logRequests, (Object)false);
        this.logResponses = (Boolean)Utils.getOrDefault((Object)logResponses, (Object)false);
        this.log = (Logger)Utils.getOrDefault((Object)logger, (Object)DEFAULT_LOG);
    }

    @Override
    public SuccessfulHttpResponse execute(HttpRequest request) throws HttpException {
        if (this.logRequests) {
            HttpRequestLogger.log(this.log, request);
        }
        SuccessfulHttpResponse response = this.delegateHttpClient.execute(request);
        if (this.logResponses) {
            HttpResponseLogger.log(this.log, response);
        }
        return response;
    }

    @Override
    public void execute(HttpRequest request, final ServerSentEventListener delegateListener) {
        if (this.logRequests) {
            HttpRequestLogger.log(this.log, request);
        }
        this.delegateHttpClient.execute(request, new ServerSentEventListener(){

            @Override
            public void onOpen(SuccessfulHttpResponse response) {
                if (LoggingHttpClient.this.logResponses) {
                    HttpResponseLogger.log(LoggingHttpClient.this.log, response);
                }
                delegateListener.onOpen(response);
            }

            @Override
            public void onEvent(ServerSentEvent event) {
                if (LoggingHttpClient.this.logResponses) {
                    LoggingHttpClient.this.log.debug("{}", (Object)event);
                }
                delegateListener.onEvent(event);
            }

            @Override
            public void onEvent(ServerSentEvent event, ServerSentEventContext context) {
                if (LoggingHttpClient.this.logResponses) {
                    LoggingHttpClient.this.log.debug("{}", (Object)event);
                }
                delegateListener.onEvent(event, context);
            }

            @Override
            public void onError(Throwable throwable) {
                delegateListener.onError(throwable);
            }

            @Override
            public void onClose() {
                delegateListener.onClose();
            }
        });
    }

    @Override
    public void execute(HttpRequest request, ServerSentEventParser parser, final ServerSentEventListener delegateListener) {
        if (this.logRequests) {
            HttpRequestLogger.log(this.log, request);
        }
        this.delegateHttpClient.execute(request, parser, new ServerSentEventListener(){

            @Override
            public void onOpen(SuccessfulHttpResponse response) {
                if (LoggingHttpClient.this.logResponses) {
                    HttpResponseLogger.log(LoggingHttpClient.this.log, response);
                }
                delegateListener.onOpen(response);
            }

            @Override
            public void onEvent(ServerSentEvent event) {
                if (LoggingHttpClient.this.logResponses) {
                    LoggingHttpClient.this.log.debug("{}", (Object)event);
                }
                delegateListener.onEvent(event);
            }

            @Override
            public void onEvent(ServerSentEvent event, ServerSentEventContext context) {
                if (LoggingHttpClient.this.logResponses) {
                    LoggingHttpClient.this.log.debug("{}", (Object)event);
                }
                delegateListener.onEvent(event, context);
            }

            @Override
            public void onError(Throwable throwable) {
                delegateListener.onError(throwable);
            }

            @Override
            public void onClose() {
                delegateListener.onClose();
            }
        });
    }
}

