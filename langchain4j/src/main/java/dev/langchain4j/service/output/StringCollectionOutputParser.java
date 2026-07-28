/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.request.json.JsonStringSchema
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.ParsingUtils;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

@Internal
abstract class StringCollectionOutputParser<CT extends Collection<String>>
implements OutputParser<CT> {
    StringCollectionOutputParser() {
    }

    @Override
    public CT parse(String text) {
        return ParsingUtils.parseAsStringOrJson(text, s -> s, this.emptyCollectionSupplier(), this.type());
    }

    abstract Supplier<CT> emptyCollectionSupplier();

    private String type() {
        return this.collectionType().getName() + "<java.lang.String>";
    }

    abstract Class<?> collectionType();

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonSchema jsonSchema = JsonSchema.builder().name(this.collectionType().getSimpleName() + "_of_String").rootElement((JsonSchemaElement)JsonObjectSchema.builder().addProperty("values", (JsonSchemaElement)JsonArraySchema.builder().items((JsonSchemaElement)new JsonStringSchema()).build()).required(new String[]{"values"}).build()).build();
        return Optional.of(jsonSchema);
    }

    @Override
    public String formatInstructions() {
        return "\nYou must put every item on a separate line.";
    }
}

