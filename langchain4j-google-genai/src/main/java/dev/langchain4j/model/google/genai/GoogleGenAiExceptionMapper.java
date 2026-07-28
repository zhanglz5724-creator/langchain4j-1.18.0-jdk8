/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.genai.errors.ApiException
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.LangChain4jException
 *  dev.langchain4j.internal.ExceptionMapper$DefaultExceptionMapper
 */
package dev.langchain4j.model.google.genai;

import com.google.genai.errors.ApiException;
import dev.langchain4j.Internal;
import dev.langchain4j.exception.LangChain4jException;
import dev.langchain4j.internal.ExceptionMapper;

@Internal
class GoogleGenAiExceptionMapper
extends ExceptionMapper.DefaultExceptionMapper {
    static final GoogleGenAiExceptionMapper INSTANCE = new GoogleGenAiExceptionMapper();

    private GoogleGenAiExceptionMapper() {
    }

    public RuntimeException mapException(Throwable t) {
        if (t instanceof ApiException) {
            ApiException apiException = (ApiException)t;
            return this.mapHttpStatusCode((Throwable)apiException, apiException.code());
        }
        if (t.getCause() instanceof ApiException) {
            ApiException apiException = (ApiException)t.getCause();
            return this.mapHttpStatusCode((Throwable)apiException, apiException.code());
        }
        return t instanceof RuntimeException ? (RuntimeException)t : new LangChain4jException(t);
    }
}

