/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.Utils
 *  org.slf4j.Logger
 */
package dev.langchain4j.http.client.log;

import dev.langchain4j.Internal;
import dev.langchain4j.http.client.HttpRequest;
import dev.langchain4j.internal.Utils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;

@Internal
class HttpRequestLogger {
    private static final Set<String> COMMON_SECRET_HEADERS = new HashSet<String>(Arrays.asList("authorization", "x-api-key", "x-auth-token"));

    HttpRequestLogger() {
    }

    static void log(Logger log, HttpRequest httpRequest) {
        try {
            log.info("HTTP request:\n- method: {}\n- url: {}\n- headers: {}\n- body: {}\n", new Object[]{httpRequest.method(), httpRequest.url(), HttpRequestLogger.format(httpRequest.headers()), httpRequest.body()});
        }
        catch (Exception e) {
            log.warn("Exception occurred while logging HTTP request: {}", (Object)e.getMessage());
        }
    }

    static String format(Map<String, List<String>> headers) {
        return headers.entrySet().stream().map(header -> HttpRequestLogger.format((String)header.getKey(), (List<String>)header.getValue())).collect(Collectors.joining(", "));
    }

    static String format(String headerKey, List<String> headerValues) {
        if (COMMON_SECRET_HEADERS.contains(headerKey.toLowerCase()) || headerKey.toLowerCase().contains("api-key")) {
            headerValues = headerValues.stream().map(HttpRequestLogger::maskSecretKey).collect(Collectors.toList());
        }
        if (headerValues.size() == 1) {
            return String.format("[%s: %s]", headerKey, headerValues.get(0));
        }
        return String.format("[%s: %s]", headerKey, headerValues);
    }

    static String maskSecretKey(String key) {
        if (Utils.isNullOrBlank((String)key)) {
            return key;
        }
        if (key.length() >= 7) {
            return key.substring(0, 5) + "..." + key.substring(key.length() - 2);
        }
        return "...";
    }
}

