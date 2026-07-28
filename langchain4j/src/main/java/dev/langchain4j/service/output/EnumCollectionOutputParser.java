/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema
 *  dev.langchain4j.model.chat.request.json.JsonEnumSchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.service.output.EnumOutputParser;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.ParsingUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Internal
abstract class EnumCollectionOutputParser<E extends Enum<E>, CE extends Collection<E>>
implements OutputParser<CE> {
    protected final Class<E> enumClass;
    protected final EnumOutputParser<E> enumOutputParser;

    EnumCollectionOutputParser(Class<E> enumClass) {
        this.enumClass = (Class)ValidationUtils.ensureNotNull(enumClass, (String)"enumClass");
        this.enumOutputParser = new EnumOutputParser<E>(enumClass);
    }

    @Override
    public CE parse(String text) {
        return ParsingUtils.parseAsStringOrJson(text, this.enumOutputParser::parse, this.emptyCollectionSupplier(), this.type());
    }

    abstract Supplier<CE> emptyCollectionSupplier();

    private String type() {
        return this.collectionType() + "<" + this.enumClass.getName() + ">";
    }

    abstract Class<?> collectionType();

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonSchema jsonSchema = JsonSchema.builder().name(this.collectionType().getSimpleName() + "_of_" + this.enumClass.getSimpleName()).rootElement((JsonSchemaElement)JsonObjectSchema.builder().addProperty("values", (JsonSchemaElement)JsonArraySchema.builder().items((JsonSchemaElement)JsonEnumSchema.builder().enumValues(Arrays.stream(this.enumClass.getEnumConstants()).map(e -> e.name()).collect(Collectors.toList())).build()).build()).required(new String[]{"values"}).build()).build();
        return Optional.of(jsonSchema);
    }

    @Override
    public String formatInstructions() {
        try {
            Object[] enumConstants = (Enum[])this.enumClass.getEnumConstants();
            ValidationUtils.ensureNotEmpty((Object[])enumConstants, (String)"%s", (Object[])new Object[]{"Should be at least one enum constant defined."});
            StringBuilder instruction = new StringBuilder();
            instruction.append("\nYou must answer strictly with zero or more of these enums on a separate line:");
            for (Object enumConstant : enumConstants) {
                instruction.append("\n").append(((Enum)enumConstant).name().toUpperCase(Locale.ROOT));
                Optional<String> optionalEnumDescription = EnumOutputParser.getEnumDescription(this.enumClass, (Enum)enumConstant);
                optionalEnumDescription.ifPresent(description -> instruction.append(" - ").append((String)description));
            }
            return instruction.toString();
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

