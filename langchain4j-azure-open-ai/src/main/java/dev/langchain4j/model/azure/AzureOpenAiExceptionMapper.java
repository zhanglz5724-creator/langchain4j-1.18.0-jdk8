/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.azure.core.exception.HttpResponseException
 *  com.azure.core.http.HttpResponse
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.ContentFilteredException
 *  dev.langchain4j.exception.LangChain4jException
 *  dev.langchain4j.exception.TimeoutException
 *  dev.langchain4j.internal.ExceptionMapper$DefaultExceptionMapper
 *  io.netty.channel.ConnectTimeoutException
 */
package dev.langchain4j.model.azure;

import com.azure.core.exception.HttpResponseException;
import com.azure.core.http.HttpResponse;
import dev.langchain4j.Internal;
import dev.langchain4j.exception.ContentFilteredException;
import dev.langchain4j.exception.LangChain4jException;
import dev.langchain4j.internal.ExceptionMapper;
import io.netty.channel.ConnectTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Internal
class AzureOpenAiExceptionMapper
extends ExceptionMapper.DefaultExceptionMapper {
    static final AzureOpenAiExceptionMapper INSTANCE = new AzureOpenAiExceptionMapper();

    AzureOpenAiExceptionMapper() {
    }

    public RuntimeException mapException(Throwable t) {
        if (t instanceof HttpResponseException) {
            Map errorMap;
            Map map;
            Object errorObj;
            HttpResponseException httpResponseException = (HttpResponseException)t;
            if (httpResponseException.getValue() instanceof Map && ((Map)httpResponseException.getValue()).containsKey("error") && (errorObj = (map = (Map)httpResponseException.getValue()).get("error")) instanceof Map && "content_filter".equals((errorMap = (Map)errorObj).get("code"))) {
                return new ContentFilteredException(t);
            }
            HttpResponse httpResponse = httpResponseException.getResponse();
            if (httpResponse != null) {
                return this.mapHttpStatusCode((Throwable)httpResponseException, httpResponse.getStatusCode());
            }
        }
        if (t instanceof ConnectTimeoutException || t instanceof TimeoutException) {
            return new dev.langchain4j.exception.TimeoutException(t);
        }
        if (t.getCause() instanceof ConnectTimeoutException || t.getCause() instanceof TimeoutException) {
            return new dev.langchain4j.exception.TimeoutException(t.getCause());
        }
        return t instanceof RuntimeException ? (RuntimeException)t : new LangChain4jException(t);
    }
}

