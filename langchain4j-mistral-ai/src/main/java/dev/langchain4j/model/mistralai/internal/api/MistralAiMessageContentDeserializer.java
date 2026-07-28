/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonParseException
 *  com.fasterxml.jackson.core.JsonParser
 *  com.fasterxml.jackson.core.JsonToken
 *  com.fasterxml.jackson.databind.DeserializationContext
 *  com.fasterxml.jackson.databind.JavaType
 *  com.fasterxml.jackson.databind.JsonDeserializer
 *  com.fasterxml.jackson.databind.type.CollectionType
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import dev.langchain4j.model.mistralai.internal.api.MistralAiTextContent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

class MistralAiMessageContentDeserializer
extends JsonDeserializer<List<MistralAiMessageContent>> {
    MistralAiMessageContentDeserializer() {
    }

    public List<MistralAiMessageContent> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token == JsonToken.VALUE_STRING) {
            return Collections.singletonList(new MistralAiTextContent(p.getText()));
        }
        if (token == JsonToken.START_ARRAY) {
            CollectionType contentListType = ctxt.getTypeFactory().constructCollectionType(List.class, MistralAiMessageContent.class);
            return (List)ctxt.readValue(p, (JavaType)contentListType);
        }
        throw new JsonParseException(p, String.format("Expected string or an array, but got: %s (%s)", token, p.getText()));
    }
}

