/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.guardrail.OutputGuardrail
 *  dev.langchain4j.guardrail.OutputGuardrailResult
 *  dev.langchain4j.internal.JsonParsingUtils
 *  dev.langchain4j.internal.JsonParsingUtils$ParsedJson
 *  dev.langchain4j.internal.ValidationUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.guardrails;

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

public class JsonExtractorOutputGuardrail<T>
implements OutputGuardrail {
    public static final String DEFAULT_REPROMPT_MESSAGE = "Invalid JSON";
    public static final String DEFAULT_REPROMPT_PROMPT = "Make sure you return a valid JSON object following the specified format";
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonExtractorOutputGuardrail.class);
    private final ObjectMapper objectMapper;
    private Class<T> outputClass;
    private TypeReference<T> outputType;

    public JsonExtractorOutputGuardrail(ObjectMapper objectMapper, Class<T> outputClass) {
        this.objectMapper = (ObjectMapper)ValidationUtils.ensureNotNull((Object)objectMapper, (String)"objectMapper");
        this.outputClass = (Class)ValidationUtils.ensureNotNull(outputClass, (String)"outputClass");
    }

    public JsonExtractorOutputGuardrail(ObjectMapper objectMapper, TypeReference<T> outputType) {
        this.objectMapper = (ObjectMapper)ValidationUtils.ensureNotNull((Object)objectMapper, (String)"objectMapper");
        this.outputType = (TypeReference)ValidationUtils.ensureNotNull(outputType, (String)"outputType");
    }

    public JsonExtractorOutputGuardrail(Class<T> outputClass) {
        this(new ObjectMapper(), outputClass);
    }

    public JsonExtractorOutputGuardrail(TypeReference<T> outputType) {
        this(new ObjectMapper(), outputType);
    }

    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        String llmResponse = ((AiMessage)ValidationUtils.ensureNotNull((Object)responseFromLLM, (String)"responseFromLLM")).text();
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
            return Optional.of(this.outputClass != null ? JsonParsingUtils.extractAndParseJson((String)llmResponse, text -> this.objectMapper.readValue(text, this.outputClass)) : JsonParsingUtils.extractAndParseJson((String)llmResponse, text -> this.objectMapper.readValue(text, this.outputType)));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}

