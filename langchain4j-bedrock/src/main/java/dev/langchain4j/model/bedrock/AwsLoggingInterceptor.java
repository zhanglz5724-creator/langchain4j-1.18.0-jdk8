/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.Utils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  software.amazon.awssdk.core.SdkResponse
 *  software.amazon.awssdk.core.interceptor.Context$BeforeExecution
 *  software.amazon.awssdk.core.interceptor.Context$BeforeTransmission
 *  software.amazon.awssdk.core.interceptor.Context$ModifyHttpResponse
 *  software.amazon.awssdk.core.interceptor.Context$ModifyResponse
 *  software.amazon.awssdk.core.interceptor.ExecutionAttributes
 *  software.amazon.awssdk.core.interceptor.ExecutionInterceptor
 *  software.amazon.awssdk.http.ContentStreamProvider
 *  software.amazon.awssdk.http.SdkHttpFullRequest
 *  software.amazon.awssdk.http.SdkHttpMethod
 *  software.amazon.awssdk.http.SdkHttpRequest
 *  software.amazon.awssdk.http.SdkHttpResponse
 *  software.amazon.awssdk.utils.IoUtils
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.utils.IoUtils;

@Internal
class AwsLoggingInterceptor
implements ExecutionInterceptor {
    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(AwsLoggingInterceptor.class);
    private final boolean logRequests;
    private final boolean logResponses;
    private final Logger logger;

    AwsLoggingInterceptor(boolean logRequests, boolean logResponses, Logger logger) {
        this.logRequests = logRequests;
        this.logResponses = logResponses;
        this.logger = (Logger)Utils.getOrDefault((Object)logger, (Object)DEFAULT_LOGGER);
    }

    public void beforeExecution(Context.BeforeExecution context, ExecutionAttributes executionAttributes) {
        if (this.logRequests) {
            this.logger.debug("AWS SDK Operation: {}", (Object)context.request().getClass().getSimpleName());
        }
    }

    public void beforeTransmission(Context.BeforeTransmission context, ExecutionAttributes executionAttributes) {
        SdkHttpRequest request = context.httpRequest();
        String body = null;
        if (this.logRequests) {
            if (request.method() == SdkHttpMethod.POST && request instanceof SdkHttpFullRequest) {
                SdkHttpFullRequest sdkHttpFullRequest = (SdkHttpFullRequest)request;
                try {
                    ContentStreamProvider csp = sdkHttpFullRequest.contentStreamProvider().orElse(null);
                    if (Objects.nonNull(csp)) {
                        body = IoUtils.toUtf8String((InputStream)csp.newStream());
                    }
                }
                catch (IOException e) {
                    this.logger.warn("Unable to obtain request body", (Throwable)e);
                }
            }
            this.logger.debug("Request:\n- method: {}\n- url: {}\n- headers: {}\n- query parameters: {}\n- body: {}", new Object[]{request.method(), request.getUri(), request.headers(), request.rawQueryParameters(), body});
        }
    }

    public SdkResponse modifyResponse(Context.ModifyResponse context, ExecutionAttributes executionAttributes) {
        Optional.ofNullable(context.httpResponse()).ifPresent(response -> this.logResponse((SdkHttpResponse)response, context));
        return context.response();
    }

    private void logResponse(SdkHttpResponse response, Context.ModifyResponse context) {
        if (this.logResponses) {
            this.logger.debug("Response Status: {} \nHeaders: {} \nResponse Body Type: {}", new Object[]{response.statusCode(), response.headers(), context.response().getClass().getSimpleName()});
        }
    }

    public Optional<InputStream> modifyHttpResponseContent(Context.ModifyHttpResponse context, ExecutionAttributes executionAttributes) {
        byte[] content = null;
        if (this.logResponses) {
            try {
                InputStream responseContentStream = context.responseBody().orElse(new ByteArrayInputStream(new byte[0]));
                content = IoUtils.toByteArray((InputStream)responseContentStream);
                this.logger.debug("Response Body: {}", (Object)new String(content, StandardCharsets.UTF_8));
            }
            catch (IOException e) {
                this.logger.warn("Unable to obtain response body", (Throwable)e);
            }
        }
        return Objects.isNull(content) ? Optional.empty() : Optional.of(new ByteArrayInputStream(content));
    }
}

