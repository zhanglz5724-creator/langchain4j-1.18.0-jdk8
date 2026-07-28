/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  okhttp3.Headers
 *  okhttp3.Interceptor
 *  okhttp3.Interceptor$Chain
 *  okhttp3.Request
 *  okhttp3.Response
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.vespa;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VespaResponseLoggingInterceptor
implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(VespaResponseLoggingInterceptor.class);

    VespaResponseLoggingInterceptor() {
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        this.log(response);
        return response;
    }

    private void log(Response response) {
        try {
            log.debug("Response:\n- status code: {}\n- headers: {}\n- body: {}", new Object[]{response.code(), VespaResponseLoggingInterceptor.getHeaders(response.headers()), VespaResponseLoggingInterceptor.getBody(response)});
        }
        catch (Exception e) {
            log.warn("Error while logging response: {}", (Object)e.getMessage());
        }
    }

    private static String getBody(Response response) throws IOException {
        return response.peekBody(Long.MAX_VALUE).string();
    }

    static String getHeaders(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false).map(header -> {
            String headerKey = (String)header.component1();
            String headerValue = (String)header.component2();
            return String.format("[%s: %s]", headerKey, headerValue);
        }).collect(Collectors.joining(", "));
    }
}

