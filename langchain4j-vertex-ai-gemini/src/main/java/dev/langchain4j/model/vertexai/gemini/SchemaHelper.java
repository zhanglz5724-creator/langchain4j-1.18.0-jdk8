/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.Schema
 *  com.google.cloud.vertexai.api.Schema$Builder
 *  com.google.cloud.vertexai.api.Type
 *  com.google.protobuf.InvalidProtocolBufferException
 *  com.google.protobuf.Message$Builder
 *  com.google.protobuf.util.JsonFormat
 *  dev.langchain4j.model.chat.request.json.JsonAnyOfSchema
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema
 *  dev.langchain4j.model.chat.request.json.JsonBooleanSchema
 *  dev.langchain4j.model.chat.request.json.JsonEnumSchema
 *  dev.langchain4j.model.chat.request.json.JsonIntegerSchema
 *  dev.langchain4j.model.chat.request.json.JsonNullSchema
 *  dev.langchain4j.model.chat.request.json.JsonNumberSchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.request.json.JsonStringSchema
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.Schema;
import com.google.cloud.vertexai.api.Type;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import dev.langchain4j.model.chat.request.json.JsonAnyOfSchema;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNullSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaHelper {
    public static Schema fromJsonSchema(String jsonSchemaString) {
        try {
            String schemaJsonString = jsonSchemaString.replace("\"object\"", "\"OBJECT\"").replace("\"integer\"", "\"INTEGER\"").replace("\"string\"", "\"STRING\"").replace("\"number\"", "\"NUMBER\"").replace("\"array\"", "\"ARRAY\"").replace("\"boolean\"", "\"BOOLEAN\"");
            Schema.Builder builder = Schema.newBuilder();
            JsonFormat.parser().merge(schemaJsonString, (Message.Builder)builder);
            return builder.build();
        }
        catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    public static Schema fromClass(Class<?> theClass) {
        if (CharSequence.class.isAssignableFrom(theClass) || Character.class.isAssignableFrom(theClass) || Character.TYPE.isAssignableFrom(theClass)) {
            return Schema.newBuilder().setType(Type.STRING).build();
        }
        if (Boolean.class.isAssignableFrom(theClass) || Boolean.TYPE.isAssignableFrom(theClass)) {
            return Schema.newBuilder().setType(Type.BOOLEAN).build();
        }
        if (Integer.class.isAssignableFrom(theClass) || Integer.TYPE.isAssignableFrom(theClass) || Long.class.isAssignableFrom(theClass) || Long.TYPE.isAssignableFrom(theClass)) {
            return Schema.newBuilder().setType(Type.INTEGER).build();
        }
        if (Double.class.isAssignableFrom(theClass) || Double.TYPE.isAssignableFrom(theClass) || Float.class.isAssignableFrom(theClass) || Float.TYPE.isAssignableFrom(theClass)) {
            return Schema.newBuilder().setType(Type.NUMBER).build();
        }
        if (theClass.isArray()) {
            Class<?> componentType = theClass.getComponentType();
            return Schema.newBuilder().setType(Type.ARRAY).setItems(SchemaHelper.fromClass(componentType)).build();
        }
        if (Collection.class.isAssignableFrom(theClass)) {
            return Schema.newBuilder().setType(Type.ARRAY).build();
        }
        if (theClass.isEnum()) {
            List enumConstantNames = Arrays.stream(theClass.getEnumConstants()).map(e -> ((Enum)e).name()).collect(Collectors.toList());
            return Schema.newBuilder().setType(Type.STRING).addAllEnum(enumConstantNames).build();
        }
        Schema.Builder schemaBuilder = Schema.newBuilder().setType(Type.OBJECT);
        ArrayList propertyNames = new ArrayList();
        Stream.concat(Arrays.stream(theClass.getDeclaredFields()), Arrays.stream(theClass.getFields())).filter(field -> !field.getName().startsWith("this$")).collect(Collectors.toSet()).forEach(field -> {
            schemaBuilder.putProperties(field.getName(), SchemaHelper.fromClass(field.getType())).build();
            propertyNames.add(field.getName());
        });
        schemaBuilder.addAllRequired(propertyNames);
        return schemaBuilder.build();
    }

    public static Schema from(JsonSchemaElement jsonSchemaElement) {
        if (jsonSchemaElement instanceof JsonStringSchema) {
            JsonStringSchema jsonStringSchema = (JsonStringSchema)jsonSchemaElement;
            Schema.Builder builder = Schema.newBuilder().setType(Type.STRING);
            if (jsonStringSchema.description() != null) {
                builder.setDescription(jsonStringSchema.description());
            }
            return builder.build();
        }
        if (jsonSchemaElement instanceof JsonBooleanSchema) {
            JsonBooleanSchema jsonBooleanSchema = (JsonBooleanSchema)jsonSchemaElement;
            Schema.Builder builder = Schema.newBuilder().setType(Type.BOOLEAN);
            if (jsonBooleanSchema.description() != null) {
                builder.setDescription(jsonBooleanSchema.description());
            }
            return builder.build();
        }
        if (jsonSchemaElement instanceof JsonIntegerSchema) {
            JsonIntegerSchema jsonIntegerSchema = (JsonIntegerSchema)jsonSchemaElement;
            Schema.Builder builder = Schema.newBuilder().setType(Type.INTEGER);
            if (jsonIntegerSchema.description() != null) {
                builder.setDescription(jsonIntegerSchema.description());
            }
            return builder.build();
        }
        if (jsonSchemaElement instanceof JsonNumberSchema) {
            JsonNumberSchema jsonNumberSchema = (JsonNumberSchema)jsonSchemaElement;
            Schema.Builder builder = Schema.newBuilder().setType(Type.NUMBER);
            if (jsonNumberSchema.description() != null) {
                builder.setDescription(jsonNumberSchema.description());
            }
            return builder.build();
        }
        if (jsonSchemaElement instanceof JsonEnumSchema) {
            JsonEnumSchema jsonEnumSchema = (JsonEnumSchema)jsonSchemaElement;
            Schema.Builder builder = Schema.newBuilder().setType(Type.STRING).addAllEnum((Iterable)jsonEnumSchema.enumValues());
            if (jsonEnumSchema.description() != null) {
                builder.setDescription(jsonEnumSchema.description());
            }
            return builder.build();
        }
        if (jsonSchemaElement instanceof JsonArraySchema) {
            JsonArraySchema jsonArraySchema = (JsonArraySchema)jsonSchemaElement;
            Schema.Builder builder = Schema.newBuilder().setType(Type.ARRAY).setItems(SchemaHelper.from(jsonArraySchema.items()));
            if (jsonArraySchema.description() != null) {
                builder.setDescription(jsonArraySchema.description());
            }
            return builder.build();
        }
        if (jsonSchemaElement instanceof JsonAnyOfSchema) {
            JsonAnyOfSchema jsonAnyOfSchema = (JsonAnyOfSchema)jsonSchemaElement;
            Schema.Builder builder = Schema.newBuilder().addAllAnyOf((Iterable)jsonAnyOfSchema.anyOf().stream().map(SchemaHelper::from).collect(Collectors.toList()));
            if (jsonAnyOfSchema.description() != null) {
                builder.setDescription(jsonAnyOfSchema.description());
            }
            return builder.build();
        }
        if (jsonSchemaElement instanceof JsonNullSchema) {
            return Schema.newBuilder().setNullable(true).build();
        }
        if (jsonSchemaElement instanceof JsonObjectSchema) {
            JsonObjectSchema jsonObjectSchema = (JsonObjectSchema)jsonSchemaElement;
            LinkedHashMap properties = new LinkedHashMap();
            jsonObjectSchema.properties().forEach((property, value) -> properties.put(property, SchemaHelper.from(value)));
            Schema.Builder builder = Schema.newBuilder().setType(Type.OBJECT).putAllProperties(properties).addAllRequired((Iterable)jsonObjectSchema.required());
            if (jsonObjectSchema.description() != null) {
                builder.setDescription(jsonObjectSchema.description());
            }
            return builder.build();
        }
        throw new RuntimeException("Unknown type: " + jsonSchemaElement.getClass());
    }
}

