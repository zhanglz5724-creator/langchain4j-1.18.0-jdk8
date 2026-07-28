/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.api.gax.rpc.ApiException
 *  com.google.api.gax.rpc.StatusCode$Code
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.LangChain4jException
 *  dev.langchain4j.exception.TimeoutException
 *  dev.langchain4j.internal.ExceptionMapper$DefaultExceptionMapper
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import dev.langchain4j.Internal;
import dev.langchain4j.exception.LangChain4jException;
import dev.langchain4j.exception.TimeoutException;
import dev.langchain4j.internal.ExceptionMapper;

@Internal
class VertexAiGeminiExceptionMapper
extends ExceptionMapper.DefaultExceptionMapper {
    static final VertexAiGeminiExceptionMapper INSTANCE = new VertexAiGeminiExceptionMapper();

    private VertexAiGeminiExceptionMapper() {
    }

    public RuntimeException mapException(Throwable t) {
        Throwable cause = t;
        while (cause != null) {
            if (cause instanceof ApiException) {
                ApiException apiException = (ApiException)cause;
                if (apiException.getStatusCode().getCode() == StatusCode.Code.DEADLINE_EXCEEDED) {
                    return new TimeoutException((Throwable)apiException);
                }
                return this.mapHttpStatusCode((Throwable)apiException, apiException.getStatusCode().getCode().getHttpStatusCode());
            }
            cause = cause.getCause() == cause ? null : cause.getCause();
        }
        return t instanceof RuntimeException ? (RuntimeException)t : new LangChain4jException(t);
    }
}

