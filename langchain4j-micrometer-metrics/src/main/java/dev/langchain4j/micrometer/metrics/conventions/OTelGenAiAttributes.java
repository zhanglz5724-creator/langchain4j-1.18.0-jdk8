/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.micrometer.metrics.conventions;

public enum OTelGenAiAttributes {
    OPERATION_NAME("gen_ai.operation.name"),
    PROVIDER_NAME("gen_ai.provider.name"),
    TOKEN_TYPE("gen_ai.token.type"),
    REQUEST_MODEL("gen_ai.request.model"),
    REQUEST_FREQUENCY_PENALTY("gen_ai.request.frequency_penalty"),
    REQUEST_MAX_TOKENS("gen_ai.request.max_tokens"),
    REQUEST_PRESENCE_PENALTY("gen_ai.request.presence_penalty"),
    REQUEST_STOP_SEQUENCES("gen_ai.request.stop_sequences"),
    REQUEST_TEMPERATURE("gen_ai.request.temperature"),
    REQUEST_TOP_K("gen_ai.request.top_k"),
    REQUEST_TOP_P("gen_ai.request.top_p"),
    RESPONSE_FINISH_REASONS("gen_ai.response.finish_reasons"),
    RESPONSE_ID("gen_ai.response.id"),
    RESPONSE_MODEL("gen_ai.response.model"),
    ERROR_TYPE("error.type"),
    SERVER_PORT("server.port"),
    SERVER_ADDRESS("server.address");

    private final String value;

    private OTelGenAiAttributes(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}

