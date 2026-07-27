/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility
 *  com.fasterxml.jackson.annotation.PropertyAccessor
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.json.JsonMapper
 *  com.fasterxml.jackson.databind.json.JsonMapper$Builder
 */
package dev.langchain4j.model.input.structured;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.langchain4j.Internal;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.spi.prompt.structured.StructuredPromptFactory;
import java.util.Map;

@Internal
class DefaultStructuredPromptFactory
implements StructuredPromptFactory {
    private static final ObjectMapper OBJECT_MAPPER = ((JsonMapper.Builder)JsonMapper.builder().visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)).build();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<Map<String, Object>>(){};

    @Override
    public Prompt toPrompt(Object structuredPrompt) {
        StructuredPrompt annotation = StructuredPrompt.Util.validateStructuredPrompt(structuredPrompt);
        String promptTemplateString = StructuredPrompt.Util.join(annotation);
        PromptTemplate promptTemplate = PromptTemplate.from(promptTemplateString);
        Map<String, Object> variables = DefaultStructuredPromptFactory.extractVariables(structuredPrompt);
        return promptTemplate.apply(variables);
    }

    private static Map<String, Object> extractVariables(Object structuredPrompt) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(structuredPrompt);
            return (Map)OBJECT_MAPPER.readValue(json, MAP_TYPE);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

