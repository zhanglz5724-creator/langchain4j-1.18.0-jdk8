/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.listener.ChatModelErrorContext
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.listener.ChatModelRequestContext
 *  dev.langchain4j.model.chat.listener.ChatModelResponseContext
 *  io.micrometer.core.instrument.DistributionSummary
 *  io.micrometer.core.instrument.MeterRegistry
 */
package dev.langchain4j.micrometer.metrics.listeners;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.micrometer.metrics.conventions.OTelGenAiAttributes;
import dev.langchain4j.micrometer.metrics.conventions.OTelGenAiMetricName;
import dev.langchain4j.micrometer.metrics.conventions.OTelGenAiOperationName;
import dev.langchain4j.micrometer.metrics.conventions.OTelGenAiProviderName;
import dev.langchain4j.micrometer.metrics.conventions.OTelGenAiTokenType;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;

@Experimental
public class MicrometerMetricsChatModelListener
implements ChatModelListener {
    private final MeterRegistry meterRegistry;

    public MicrometerMetricsChatModelListener(MeterRegistry meterRegistry) {
        this.meterRegistry = (MeterRegistry)ValidationUtils.ensureNotNull((Object)meterRegistry, (String)"meterRegistry");
    }

    public void onRequest(ChatModelRequestContext requestContext) {
    }

    public void onResponse(ChatModelResponseContext responseContext) {
        this.recordTokenUsageMetrics(responseContext);
    }

    public void onError(ChatModelErrorContext errorContext) {
    }

    private void recordTokenUsageMetrics(ChatModelResponseContext responseContext) {
        if (responseContext == null || responseContext.chatResponse().tokenUsage() == null) {
            return;
        }
        this.addTokenMetric(responseContext, OTelGenAiTokenType.INPUT, responseContext.chatResponse().tokenUsage().inputTokenCount());
        this.addTokenMetric(responseContext, OTelGenAiTokenType.OUTPUT, responseContext.chatResponse().tokenUsage().outputTokenCount());
    }

    private void addTokenMetric(ChatModelResponseContext responseContext, OTelGenAiTokenType tokenType, Integer tokenCount) {
        if (tokenCount == null) {
            return;
        }
        DistributionSummary.builder((String)OTelGenAiMetricName.TOKEN_USAGE.value()).baseUnit("tokens").tag(OTelGenAiAttributes.OPERATION_NAME.value(), OTelGenAiOperationName.CHAT.value()).tag(OTelGenAiAttributes.PROVIDER_NAME.value(), MicrometerMetricsChatModelListener.getProviderName(responseContext)).tag(OTelGenAiAttributes.REQUEST_MODEL.value(), MicrometerMetricsChatModelListener.getRequestModelName(responseContext)).tag(OTelGenAiAttributes.RESPONSE_MODEL.value(), MicrometerMetricsChatModelListener.getResponseModelName(responseContext)).tag(OTelGenAiAttributes.TOKEN_TYPE.value(), tokenType.value()).description("Measures token usage").register(this.meterRegistry).record((double)tokenCount.intValue());
    }

    private static String getProviderName(ChatModelResponseContext responseContext) {
        return OTelGenAiProviderName.fromModelProvider(responseContext.modelProvider());
    }

    private static String getRequestModelName(ChatModelResponseContext responseContext) {
        String modelName = responseContext.chatRequest().parameters().modelName();
        return (String)Utils.getOrDefault((Object)modelName, (Object)"unknown");
    }

    private static String getResponseModelName(ChatModelResponseContext responseContext) {
        String modelName = responseContext.chatResponse().metadata().modelName();
        return (String)Utils.getOrDefault((Object)modelName, (Object)"unknown");
    }
}

