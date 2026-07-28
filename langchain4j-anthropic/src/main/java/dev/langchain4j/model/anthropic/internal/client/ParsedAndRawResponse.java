/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.SuccessfulHttpResponse
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.anthropic.internal.client;

import dev.langchain4j.http.client.SuccessfulHttpResponse;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.anthropic.internal.api.AnthropicCreateMessageResponse;

public class ParsedAndRawResponse {
    private final AnthropicCreateMessageResponse parsedResponse;
    private final SuccessfulHttpResponse rawResponse;

    public ParsedAndRawResponse(AnthropicCreateMessageResponse parsedResponse, SuccessfulHttpResponse rawResponse) {
        this.parsedResponse = (AnthropicCreateMessageResponse)ValidationUtils.ensureNotNull((Object)parsedResponse, (String)"parsedResponse");
        this.rawResponse = rawResponse;
    }

    public AnthropicCreateMessageResponse parsedResponse() {
        return this.parsedResponse;
    }

    public SuccessfulHttpResponse rawResponse() {
        return this.rawResponse;
    }
}

