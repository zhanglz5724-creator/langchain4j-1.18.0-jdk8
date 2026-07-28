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
class ShortOutputParser
implements OutputParser<Short> {
    ShortOutputParser() {
    }

    @Override
    public Short parse(String text) {
        return ParsingUtils.parseAsStringOrJson(text, ShortOutputParser::parseShort, Short.class);
    }

    private static Short parseShort(String text) {
        try {
            return Short.parseShort(text);
        }
        catch (NumberFormatException nfe) {
            return (short)DefaultToolExecutor.getBoundedLongValue(text, "short", Short.class, -32768L, 32767L);
        }
    }

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonSchema jsonSchema = JsonSchema.builder().name("integer").rootElement((JsonSchemaElement)JsonObjectSchema.builder().addIntegerProperty("value").required(new String[]{"value"}).build()).build();
        return Optional.of(jsonSchema);
    }

    @Override
    public String formatInstructions() {
        return "integer number in range [-32768, 32767]";
    }
}

