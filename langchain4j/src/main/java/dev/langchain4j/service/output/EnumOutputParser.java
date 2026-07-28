/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.json.JsonEnumSchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.output.structured.Description
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.ParsingUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Internal
class EnumOutputParser<E extends Enum<E>>
implements OutputParser<E> {
    private final Class<E> enumClass;

    EnumOutputParser(Class<E> enumClass) {
        this.enumClass = (Class)ValidationUtils.ensureNotNull(enumClass, (String)"enumClass");
    }

    @Override
    public E parse(String text) {
        return (E)ParsingUtils.parseAsStringOrJson(text, this::parseEnum, this.enumClass);
    }

    private E parseEnum(String text) {
        text = this.trimAndRemoveBracketsIfPresent(text);
        for (Enum enumConstant : (Enum[])this.enumClass.getEnumConstants()) {
            if (!enumConstant.name().equalsIgnoreCase(text)) continue;
            return (E)enumConstant;
        }
        throw ParsingUtils.outputParsingException(text, this.enumClass);
    }

    @Override
    public Optional<JsonSchema> jsonSchema() {
        JsonSchema jsonSchema = JsonSchema.builder().name(this.enumClass.getSimpleName()).rootElement((JsonSchemaElement)JsonObjectSchema.builder().addProperty("value", (JsonSchemaElement)JsonEnumSchema.builder().enumValues(Arrays.stream(this.enumClass.getEnumConstants()).map(e -> e.name()).collect(Collectors.toList())).build()).required(new String[]{"value"}).build()).build();
        return Optional.of(jsonSchema);
    }

    @Override
    public String formatInstructions() {
        try {
            Object[] enumConstants = (Enum[])this.enumClass.getEnumConstants();
            ValidationUtils.ensureNotEmpty((Object[])enumConstants, (String)"%s", (Object[])new Object[]{"Should be at least one enum constant defined."});
            StringBuilder instruction = new StringBuilder();
            instruction.append("\nYou must answer strictly with one of these enums:");
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

    static Optional<String> getEnumDescription(Class<? extends Enum> enumClass, Enum enumConstant) throws NoSuchFieldException {
        Description annotation;
        Field field = enumClass.getDeclaredField(enumConstant.name());
        if (field.isAnnotationPresent(Description.class) && (annotation = field.getAnnotation(Description.class)) != null) {
            return Optional.of(String.join((CharSequence)" ", annotation.value()));
        }
        return Optional.empty();
    }

    private String trimAndRemoveBracketsIfPresent(String string) {
        if ((string = string.trim()).startsWith("[") && string.endsWith("]")) {
            string = string.substring(1, string.length() - 1);
        }
        return string.trim();
    }
}

