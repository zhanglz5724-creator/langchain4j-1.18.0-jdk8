/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.PolymorphicTypes
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.internal.PolymorphicTypes;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.ParsingUtils;
import dev.langchain4j.service.output.PojoOutputParser;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Supplier;

@Internal
abstract class PojoCollectionOutputParser<T, CT extends Collection<T>>
implements OutputParser<CT> {
    private final Class<T> type;
    private final PojoOutputParser<T> parser;

    PojoCollectionOutputParser(Class<T> type) {
        this.type = (Class)ValidationUtils.ensureNotNull(type, (String)"type");
        this.parser = new PojoOutputParser<T>(type);
    }

    @Override
    public CT parse(String text) {
        return ParsingUtils.parseAsStringOrJson(text, this.parser::parse, this.emptyCollectionSupplier(), this.type());
    }

    abstract Supplier<CT> emptyCollectionSupplier();

    private String type() {
        return this.collectionType().getName() + "<" + this.type.getName() + ">";
    }

    abstract Class<?> collectionType();

    @Override
    public Optional<JsonSchema> jsonSchema() {
        boolean polymorphic = PolymorphicTypes.isPolymorphic(this.type);
        LinkedHashMap visited = new LinkedHashMap();
        JsonSchemaElement itemSchema = polymorphic ? JsonSchemaElementUtils.referenceIfRecursive((JsonSchemaElement)JsonSchemaElementUtils.polymorphicSchemaFrom(this.type, null, (boolean)false, visited), this.type, visited) : JsonSchemaElementUtils.jsonObjectOrReferenceSchemaFrom(this.type, null, (boolean)false, visited, (boolean)true);
        JsonArraySchema valuesArray = JsonArraySchema.builder().items(itemSchema).build();
        JsonObjectSchema rootElement = polymorphic ? JsonSchemaElementUtils.wrapPolymorphic((String)"values", (JsonSchemaElement)valuesArray, visited) : JsonObjectSchema.builder().addProperty("values", (JsonSchemaElement)valuesArray).required(new String[]{"values"}).build();
        return Optional.of(JsonSchema.builder().name(this.collectionType().getSimpleName() + "_of_" + this.type.getSimpleName()).rootElement((JsonSchemaElement)rootElement).build());
    }

    @Override
    public String formatInstructions() {
        throw new IllegalStateException();
    }
}

