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
import java.math.BigDecimal;
import java.util.Optional;

@Internal
class BigDecimalOutputParser
implements OutputParser<BigDecimal> {
    BigDecimalOutputParser() {
    }

    @Override
    public BigDecimal parse(String text) {
        return ParsingUtils.parseAsStringOrJson(text, BigDecimalOutputParser::parseBigDecimal, BigDecimal.class);
    }

    private static BigDecimal parseBigDecimal(String text) {
        return new BigDecimal(text.trim());
    }

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonSchema jsonSchema = JsonSchema.builder().name("number").rootElement((JsonSchemaElement)JsonObjectSchema.builder().addNumberProperty("value").required(new String[]{"value"}).build()).build();
        return Optional.of(jsonSchema);
    }

    @Override
    public String formatInstructions() {
        return "floating point number";
    }
}

