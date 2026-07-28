/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.core.exception.WatsonxException
 *  com.ibm.watsonx.ai.core.exception.model.WatsonxError
 *  com.ibm.watsonx.ai.core.exception.model.WatsonxError$Code
 *  com.ibm.watsonx.ai.core.exception.model.WatsonxError$Error
 *  dev.langchain4j.Internal
 *  dev.langchain4j.exception.AuthenticationException
 *  dev.langchain4j.exception.InvalidRequestException
 *  dev.langchain4j.exception.LangChain4jException
 *  dev.langchain4j.exception.ModelNotFoundException
 *  dev.langchain4j.exception.RateLimitException
 *  dev.langchain4j.exception.TimeoutException
 *  dev.langchain4j.internal.ExceptionMapper$DefaultExceptionMapper
 *  java.net.http.HttpTimeoutException
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.core.exception.WatsonxException;
import com.ibm.watsonx.ai.core.exception.model.WatsonxError;
import dev.langchain4j.Internal;
import dev.langchain4j.exception.AuthenticationException;
import dev.langchain4j.exception.InvalidRequestException;
import dev.langchain4j.exception.LangChain4jException;
import dev.langchain4j.exception.ModelNotFoundException;
import dev.langchain4j.exception.RateLimitException;
import dev.langchain4j.internal.ExceptionMapper;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.TimeoutException;

@Internal
class WatsonxExceptionMapper
extends ExceptionMapper.DefaultExceptionMapper {
    static final WatsonxExceptionMapper INSTANCE = new WatsonxExceptionMapper();

    private WatsonxExceptionMapper() {
    }

    public RuntimeException mapException(Throwable t) {
        Object object;
        if (t instanceof WatsonxException) {
            WatsonxException watsonxException = (WatsonxException)t;
            if (watsonxException.details().isEmpty()) {
                return this.mapHttpStatusCode((Throwable)watsonxException, watsonxException.statusCode());
            }
            WatsonxError details = (WatsonxError)watsonxException.details().get();
            WatsonxError.Error error = (WatsonxError.Error)details.errors().get(0);
            try {
                return switch (WatsonxError.Code.valueOf((String)error.code().toUpperCase())) {
                    case WatsonxError.Code.AUTHENTICATION_TOKEN_EXPIRED, WatsonxError.Code.AUTHORIZATION_REJECTED -> new AuthenticationException(error.message(), (Throwable)watsonxException);
                    case WatsonxError.Code.INVALID_INPUT_ARGUMENT, WatsonxError.Code.INVALID_REQUEST_ENTITY, WatsonxError.Code.JSON_TYPE_ERROR, WatsonxError.Code.JSON_VALIDATION_ERROR -> new InvalidRequestException(error.message(), (Throwable)watsonxException);
                    case WatsonxError.Code.MODEL_NOT_SUPPORTED -> new ModelNotFoundException(error.message(), (Throwable)watsonxException);
                    case WatsonxError.Code.TOKEN_QUOTA_REACHED -> new RateLimitException(error.message(), (Throwable)watsonxException);
                    default -> new LangChain4jException(error.message(), (Throwable)watsonxException);
                };
            }
            catch (IllegalArgumentException e) {
                return new LangChain4jException(error.message(), (Throwable)watsonxException);
            }
        }
        if (t instanceof HttpTimeoutException || t instanceof TimeoutException) {
            return new dev.langchain4j.exception.TimeoutException(t);
        }
        if (t instanceof RuntimeException) {
            RuntimeException re = (RuntimeException)t;
            object = re;
        } else {
            object = new LangChain4jException(t);
        }
        return object;
    }
}

