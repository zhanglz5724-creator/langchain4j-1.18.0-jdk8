/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.guardrail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.internal.JsonParsingUtils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class JsonExtractorOutputGuardrail<T>
implements OutputGuardrail {
    public static final String DEFAULT_REPROMPT_MESSAGE = "Invalid JSON";
    public static final String DEFAULT_REPROMPT_PROMPT = "Make sure you return a valid JSON object following the specified format";
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonExtractorOutputGuardrail.class);
    private final ObjectMapper objectMapper;
    private Class<T> outputClass;
    private TypeReference<T> outputType;

    public JsonExtractorOutputGuardrail(ObjectMapper objectMapper, Class<T> outputClass) {
        this.objectMapper = ValidationUtils.ensureNotNull(objectMapper, "objectMapper");
        this.outputClass = ValidationUtils.ensureNotNull(outputClass, "outputClass");
    }

    public JsonExtractorOutputGuardrail(ObjectMapper objectMapper, TypeReference<T> outputType) {
        this.objectMapper = ValidationUtils.ensureNotNull(objectMapper, "objectMapper");
        this.outputType = ValidationUtils.ensureNotNull(outputType, "outputType");
    }

    public JsonExtractorOutputGuardrail(Class<T> outputClass) {
        this(new ObjectMapper(), outputClass);
    }

    public JsonExtractorOutputGuardrail(TypeReference<T> outputType) {
        this(new ObjectMapper(), outputType);
    }

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        String llmResponse = ValidationUtils.ensureNotNull(responseFromLLM, "responseFromLLM").text();
        LOGGER.debug("LLM output: {}", (Object)llmResponse);
        return this.deserialize(llmResponse).map(r -> this.successWith(r.json(), r.value())).orElseGet(() -> this.invokeInvalidJson(responseFromLLM, llmResponse));
    }

    protected OutputGuardrailResult invokeInvalidJson(AiMessage aiMessage, String json) {
        LOGGER.debug("Found invalid JSON for aiMessage = {} and json = {}", (Object)aiMessage, (Object)json);
        return this.reprompt(this.getInvalidJsonMessage(aiMessage, json), this.getInvalidJsonReprompt(aiMessage, json));
    }

    protected String getInvalidJsonMessage(AiMessage aiMessage, String json) {
        return DEFAULT_REPROMPT_MESSAGE;
    }

    protected String getInvalidJsonReprompt(AiMessage aiMessage, String json) {
        return DEFAULT_REPROMPT_PROMPT;
    }

    protected Optional<JsonParsingUtils.ParsedJson<T>> deserialize(String llmResponse) {
        try {
            return Optional.of(this.outputClass != null ? JsonParsingUtils.extractAndParseJson(llmResponse, text -> this.objectMapper.readValue(text, this.outputClass)) : JsonParsingUtils.extractAndParseJson(llmResponse, text -> this.objectMapper.readValue(text, this.outputType)));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}

