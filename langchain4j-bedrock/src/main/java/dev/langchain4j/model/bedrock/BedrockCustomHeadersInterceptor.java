/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  software.amazon.awssdk.core.interceptor.Context$ModifyHttpRequest
 *  software.amazon.awssdk.core.interceptor.ExecutionAttributes
 *  software.amazon.awssdk.core.interceptor.ExecutionInterceptor
 *  software.amazon.awssdk.http.SdkHttpRequest
 *  software.amazon.awssdk.http.SdkHttpRequest$Builder
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.Internal;
import java.util.Map;
import java.util.function.Supplier;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.http.SdkHttpRequest;

@Internal
class BedrockCustomHeadersInterceptor
implements ExecutionInterceptor {
    private final Supplier<Map<String, String>> customHeadersSupplier;

    BedrockCustomHeadersInterceptor(Supplier<Map<String, String>> customHeadersSupplier) {
        this.customHeadersSupplier = customHeadersSupplier;
    }

    public SdkHttpRequest modifyHttpRequest(Context.ModifyHttpRequest context, ExecutionAttributes executionAttributes) {
        Map<String, String> headers = this.customHeadersSupplier.get();
        if (headers == null || headers.isEmpty()) {
            return context.httpRequest();
        }
        SdkHttpRequest.Builder builder = (SdkHttpRequest.Builder)context.httpRequest().toBuilder();
        headers.forEach((arg_0, arg_1) -> ((SdkHttpRequest.Builder)builder).appendHeader(arg_0, arg_1));
        return (SdkHttpRequest)builder.build();
    }
}

