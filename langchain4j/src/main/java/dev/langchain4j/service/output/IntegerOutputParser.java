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
import dev.langchain4j.service.tool.DefaultToolExecutor;
import java.util.Optional;

@Internal
class IntegerOutputParser
implements OutputParser<Integer> {
    IntegerOutputParser() {
    }

    @Override
    public Integer parse(String text) {
        return ParsingUtils.parseAsStringOrJson(text, IntegerOutputParser::parseInteger, Integer.class);
    }

    private static Integer parseInteger(String text) {
        try {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException nfe) {
            return (int)DefaultToolExecutor.getBoundedLongValue(text, "int", Integer.class, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonSchema jsonSchema = JsonSchema.builder().name("integer").rootElement((JsonSchemaElement)JsonObjectSchema.builder().addIntegerProperty("value").required(new String[]{"value"}).build()).build();
        return Optional.of(jsonSchema);
    }

    @Override
    public String formatInstructions() {
        return "integer number";
    }
}

