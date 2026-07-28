/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.ParsingUtils;
import java.util.Optional;

@Internal
class BooleanOutputParser
implements OutputParser<Boolean> {
    BooleanOutputParser() {
    }

    @Override
    public Boolean parse(String text) {
        return ParsingUtils.parseAsStringOrJson(text, BooleanOutputParser::parseBoolean, Boolean.class);
    }

    private static boolean parseBoolean(String text) {
        String trimmed = text.trim();
        if (trimmed.equalsIgnoreCase("true") || trimmed.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(trimmed);
        }
        throw ParsingUtils.outputParsingException(text, Boolean.class);
    }

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonSchema jsonSchema = JsonSchema.builder().name("boolean").rootElement((JsonSchemaElement)JsonObjectSchema.builder().addBooleanProperty("value").required(new String[]{"value"}).build()).build();
        return Optional.of(jsonSchema);
    }

    @Override
    public String formatInstructions() {
        return "one of [true, false]";
    }
}

