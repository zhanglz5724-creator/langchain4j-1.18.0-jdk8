/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.auth.oauth2.GoogleCredentials
 *  com.google.genai.Client
 *  com.google.genai.Client$Builder
 *  com.google.genai.types.HttpOptions
 *  com.google.genai.types.HttpOptions$Builder
 */
package dev.langchain4j.model.google.genai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.genai.Client;
import com.google.genai.types.HttpOptions;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

class GoogleGenAiClientFactory {
    static Client createClient(String apiKey, GoogleCredentials googleCredentials, String projectId, String location, Duration timeout, Map<String, String> customHeaders, String apiEndpoint) {
        boolean isVertex;
        HttpOptions.Builder httpOptions = HttpOptions.builder();
        HashMap<String, String> headers = new HashMap<String, String>();
        if (customHeaders != null) {
            headers.putAll(customHeaders);
        }
        headers.put("User-Agent", "LangChain4j");
        httpOptions.headers(headers);
        if (timeout != null) {
            httpOptions.timeout(Integer.valueOf((int)timeout.toMillis()));
        }
        if (apiEndpoint != null && !apiEndpoint.isEmpty()) {
            httpOptions.baseUrl(apiEndpoint);
        }
        Client.Builder clientBuilder = Client.builder().httpOptions(httpOptions.build());
        boolean bl = isVertex = googleCredentials != null || projectId != null && location != null;
        if (isVertex) {
            clientBuilder.vertexAI(true);
            if (googleCredentials != null) {
                clientBuilder.credentials(googleCredentials);
            }
            if (projectId != null) {
                clientBuilder.project(projectId);
            }
            if (location != null) {
                clientBuilder.location(location);
            }
        } else if (apiKey != null) {
            clientBuilder.apiKey(apiKey);
        }
        return clientBuilder.build();
    }

    private GoogleGenAiClientFactory() {
    }
}

