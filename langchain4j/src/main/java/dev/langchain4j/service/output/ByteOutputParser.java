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
class ByteOutputParser
implements OutputParser<Byte> {
    ByteOutputParser() {
    }

    @Override
    public Byte parse(String text) {
        return ParsingUtils.parseAsStringOrJson(text, ByteOutputParser::parseByte, Byte.class);
    }

    private static Byte parseByte(String text) {
        try {
            return Byte.parseByte(text);
        }
        catch (NumberFormatException nfe) {
            return (byte)DefaultToolExecutor.getBoundedLongValue(text, "byte", Byte.class, -128L, 127L);
        }
    }

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonSchema jsonSchema = JsonSchema.builder().name("integer").rootElement((JsonSchemaElement)JsonObjectSchema.builder().addIntegerProperty("value").required(new String[]{"value"}).build()).build();
        return Optional.of(jsonSchema);
    }

    @Override
    public String formatInstructions() {
        return "integer number in range [-128, 127]";
    }
}

