/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  okhttp3.Interceptor
 *  okhttp3.Interceptor$Chain
 *  okhttp3.Request
 *  okhttp3.Response
 *  okio.Buffer
 *  okio.BufferedSink
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.vespa;

import dev.langchain4j.store.embedding.vespa.VespaResponseLoggingInterceptor;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VespaRequestLoggingInterceptor
implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(VespaRequestLoggingInterceptor.class);

    VespaRequestLoggingInterceptor() {
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        this.log(request);
        return chain.proceed(request);
    }

    private void log(Request request) {
        try {
            log.debug("Request:\n- method: {}\n- url: {}\n- headers: {}\n- body: {}", new Object[]{request.method(), request.url(), VespaResponseLoggingInterceptor.getHeaders(request.headers()), VespaRequestLoggingInterceptor.getBody(request)});
        }
        catch (Exception e) {
            log.warn("Error while logging request: {}", (Object)e.getMessage());
        }
    }

    private static String getBody(Request request) {
        try {
            Buffer buffer = new Buffer();
            if (request.body() == null) {
                return "";
            }
            request.body().writeTo((BufferedSink)buffer);
            return buffer.readUtf8();
        }
        catch (Exception e) {
            log.warn("Exception while getting body", (Throwable)e);
            return "Exception while getting body: " + e.getMessage();
        }
    }
}

