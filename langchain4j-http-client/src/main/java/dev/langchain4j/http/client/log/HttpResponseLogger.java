/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  org.slf4j.Logger
 */
package dev.langchain4j.http.client.log;

import dev.langchain4j.Internal;
import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.http.client.log.HttpRequestLogger;
import org.slf4j.Logger;

@Internal
class HttpResponseLogger {
    HttpResponseLogger() {
    }

    static void log(Logger log, SuccessfulHttpResponse response) {
        try {
            log.info("HTTP response:\n- status code: {}\n- headers: {}\n- body: {}\n", new Object[]{response.statusCode(), HttpRequestLogger.format(response.headers()), HttpResponseLogger.formatBody(response)});
        }
        catch (Exception e) {
            log.warn("Exception occurred while logging HTTP response: {}", (Object)e.getMessage());
        }
    }

    private static Object formatBody(SuccessfulHttpResponse response) {
        String contentType = response.contentType();
        if (HttpResponseLogger.isTextual(contentType)) {
            return response.body();
        }
        byte[] bytes = response.bodyBytes();
        int length = bytes == null ? 0 : bytes.length;
        return "[binary body, " + length + " bytes, content-type: " + contentType + "]";
    }

    private static boolean isTextual(String contentType) {
        if (contentType == null) {
            return true;
        }
        String type = contentType.toLowerCase();
        return type.contains("json") || type.contains("text") || type.contains("xml") || type.contains("html") || type.contains("x-www-form-urlencoded") || type.contains("event-stream");
    }
}

