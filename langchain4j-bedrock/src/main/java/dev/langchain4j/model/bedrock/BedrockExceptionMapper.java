/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.LangChain4jException
 *  dev.langchain4j.exception.TimeoutException
 *  dev.langchain4j.internal.ExceptionMapper$DefaultExceptionMapper
 *  software.amazon.awssdk.core.exception.ApiCallTimeoutException
 *  software.amazon.awssdk.core.exception.SdkServiceException
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.Internal;
import dev.langchain4j.exception.LangChain4jException;
import dev.langchain4j.exception.TimeoutException;
import dev.langchain4j.internal.ExceptionMapper;
import software.amazon.awssdk.core.exception.ApiCallTimeoutException;
import software.amazon.awssdk.core.exception.SdkServiceException;

@Internal
class BedrockExceptionMapper
extends ExceptionMapper.DefaultExceptionMapper {
    static final BedrockExceptionMapper INSTANCE = new BedrockExceptionMapper();

    BedrockExceptionMapper() {
    }

    public RuntimeException mapException(Throwable t) {
        if (t instanceof SdkServiceException) {
            SdkServiceException sdkServiceException = (SdkServiceException)t;
            return this.mapHttpStatusCode((Throwable)sdkServiceException, sdkServiceException.statusCode());
        }
        if (t.getCause() instanceof SdkServiceException) {
            SdkServiceException sdkServiceException = (SdkServiceException)t.getCause();
            return this.mapHttpStatusCode((Throwable)sdkServiceException, sdkServiceException.statusCode());
        }
        if (t instanceof ApiCallTimeoutException) {
            return new TimeoutException(t);
        }
        if (t.getCause() instanceof ApiCallTimeoutException) {
            return new TimeoutException(t.getCause());
        }
        if (t instanceof RuntimeException) {
            return (RuntimeException)t;
        }
        return new LangChain4jException(t);
    }
}

