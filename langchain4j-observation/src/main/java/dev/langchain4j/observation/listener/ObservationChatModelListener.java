/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.model.chat.listener.ChatModelErrorContext
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.listener.ChatModelRequestContext
 *  dev.langchain4j.model.chat.listener.ChatModelResponseContext
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.output.TokenUsage
 *  io.micrometer.core.instrument.DistributionSummary
 *  io.micrometer.core.instrument.Meter$MeterProvider
 *  io.micrometer.core.instrument.MeterRegistry
 *  io.micrometer.observation.Observation
 *  io.micrometer.observation.Observation$Scope
 *  io.micrometer.observation.ObservationRegistry
 */
package dev.langchain4j.observation.listener;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.observation.context.ChatModelObservationContext;
import dev.langchain4j.observation.convention.ChatModelConvention;
import dev.langchain4j.observation.convention.ChatModelDocumentation;
import dev.langchain4j.observation.convention.DefaultChatModelConvention;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import java.util.Optional;

@Experimental
public class ObservationChatModelListener
implements ChatModelListener {
    private static final String OBSERVATION_SCOPE_KEY = "micrometer.observation.scope";
    static final String UNKNOWN = "unknown";
    static final String TOKEN_USAGE = "gen_ai.client.token.usage";
    private final ObservationRegistry observationRegistry;
    private final Meter.MeterProvider<DistributionSummary> tokenDistribution;
    private final ChatModelConvention chatModelConvention;

    public ObservationChatModelListener(ObservationRegistry observationRegistry, MeterRegistry meterRegistry) {
        this.observationRegistry = observationRegistry;
        this.tokenDistribution = DistributionSummary.builder((String)TOKEN_USAGE).description("Measures the quantity of used tokens").baseUnit("tokens").tag(ChatModelDocumentation.LowCardinalityValues.OPERATION_NAME.asString(), "chat").withRegistry(meterRegistry);
        this.chatModelConvention = null;
    }

    public void onRequest(ChatModelRequestContext requestContext) {
        Observation onRequest = ChatModelDocumentation.INSTANCE.start(this.chatModelConvention, new DefaultChatModelConvention(), () -> new ChatModelObservationContext(requestContext, null, null), this.observationRegistry);
        Observation.Scope scope = onRequest.openScope();
        requestContext.attributes().put(OBSERVATION_SCOPE_KEY, scope);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void onResponse(ChatModelResponseContext responseContext) {
        Observation.Scope currentScope = (Observation.Scope)responseContext.attributes().remove(OBSERVATION_SCOPE_KEY);
        if (currentScope == null) {
            return;
        }
        Observation observation = currentScope.getCurrentObservation();
        String providerName = null;
        String requestModel = null;
        try {
            if (observation.getContext() instanceof ChatModelObservationContext) {
                ChatModelObservationContext chatModelObservationContext = (ChatModelObservationContext)observation.getContext();
                chatModelObservationContext.setResponseContext(responseContext);
                ChatModelRequestContext requestContext = chatModelObservationContext.getRequestContext();
                providerName = Optional.ofNullable(requestContext).map(ChatModelRequestContext::modelProvider).map(Enum::name).orElse(UNKNOWN);
                requestModel = Optional.ofNullable(requestContext).map(ChatModelRequestContext::chatRequest).map(ChatRequest::modelName).orElse(UNKNOWN);
            }
            String responseModelName = Optional.ofNullable(responseContext).map(ChatModelResponseContext::chatResponse).map(ChatResponse::modelName).orElse(UNKNOWN);
            Optional<Integer> outputTokens = Optional.ofNullable(responseContext).map(ChatModelResponseContext::chatResponse).map(ChatResponse::tokenUsage).map(TokenUsage::outputTokenCount);
            Optional<Integer> inputTokens = Optional.ofNullable(responseContext).map(ChatModelResponseContext::chatResponse).map(ChatResponse::tokenUsage).map(TokenUsage::inputTokenCount);
            if (inputTokens.isPresent()) {
                ((DistributionSummary)this.tokenDistribution.withTags(new String[]{ChatModelDocumentation.LowCardinalityValues.PROVIDER_NAME.asString(), providerName, ChatModelDocumentation.LowCardinalityValues.REQUEST_MODEL.asString(), requestModel, ChatModelDocumentation.LowCardinalityValues.RESPONSE_MODEL.asString(), responseModelName, ChatModelDocumentation.LowCardinalityValues.TOKEN_TYPE.asString(), "input"})).record((double)inputTokens.get().intValue());
            }
            if (outputTokens.isPresent()) {
                ((DistributionSummary)this.tokenDistribution.withTags(new String[]{ChatModelDocumentation.LowCardinalityValues.PROVIDER_NAME.asString(), providerName, ChatModelDocumentation.LowCardinalityValues.REQUEST_MODEL.asString(), requestModel, ChatModelDocumentation.LowCardinalityValues.RESPONSE_MODEL.asString(), responseModelName, ChatModelDocumentation.LowCardinalityValues.TOKEN_TYPE.asString(), "output"})).record((double)outputTokens.get().intValue());
            }
        }
        finally {
            currentScope.close();
            observation.stop();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void onError(ChatModelErrorContext errorContext) {
        Observation.Scope currentScope = (Observation.Scope)errorContext.attributes().remove(OBSERVATION_SCOPE_KEY);
        if (currentScope != null) {
            Observation observation = currentScope.getCurrentObservation();
            try {
                if (observation.getContext() instanceof ChatModelObservationContext) {
                    ChatModelObservationContext chatModelObservationContext = (ChatModelObservationContext)observation.getContext();
                    chatModelObservationContext.setErrorContext(errorContext);
                }
                observation.error(errorContext.error());
            }
            finally {
                currentScope.close();
                observation.stop();
            }
        }
    }
}

