/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.genai.types.FunctionDeclaration
 *  com.google.genai.types.FunctionDeclaration$Builder
 *  com.google.genai.types.Schema
 *  com.google.genai.types.Tool
 *  com.google.genai.types.Type$Known
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema
 *  dev.langchain4j.model.chat.request.json.JsonBooleanSchema
 *  dev.langchain4j.model.chat.request.json.JsonEnumSchema
 *  dev.langchain4j.model.chat.request.json.JsonIntegerSchema
 *  dev.langchain4j.model.chat.request.json.JsonNumberSchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.request.json.JsonStringSchema
 */
package dev.langchain4j.model.google.genai;

import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Tool;
import com.google.genai.types.Type;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class GoogleGenAiToolMapper {
    GoogleGenAiToolMapper() {
    }

    static Tool convertToGoogleTool(ToolSpecification spec) {
        Schema parameterSchema = GoogleGenAiToolMapper.convertToGoogleSchema((JsonSchemaElement)spec.parameters());
        FunctionDeclaration.Builder fdBuilder = FunctionDeclaration.builder().name(spec.name()).description(Utils.isNullOrBlank((String)spec.description()) ? "" : spec.description());
        if (parameterSchema != null) {
            fdBuilder.parameters(parameterSchema);
        }
        FunctionDeclaration functionDeclaration = fdBuilder.build();
        return Tool.builder().functionDeclarations(Collections.singletonList(functionDeclaration)).build();
    }

    static FunctionDeclaration convertToGoogleFunction(ToolSpecification spec) {
        Schema parameterSchema = GoogleGenAiToolMapper.convertToGoogleSchema((JsonSchemaElement)spec.parameters());
        FunctionDeclaration.Builder fdBuilder = FunctionDeclaration.builder().name(spec.name()).description((String)Utils.getOrDefault((Object)spec.description(), (Object)""));
        if (parameterSchema != null) {
            fdBuilder.parameters(parameterSchema);
        }
        return fdBuilder.build();
    }

    static Schema convertToGoogleSchema(JsonSchemaElement element) {
        if (element == null) {
            return null;
        }
        if (element instanceof JsonObjectSchema) {
            JsonObjectSchema objectSchema = (JsonObjectSchema)element;
            HashMap properties = new HashMap();
            if (objectSchema.properties() != null) {
                objectSchema.properties().forEach((key, value) -> properties.put(key, GoogleGenAiToolMapper.convertToGoogleSchema(value)));
            }
            return Schema.builder().type(Type.Known.OBJECT).properties(properties).required(Utils.getOrDefault((List)objectSchema.required(), Collections.emptyList())).description((String)Utils.getOrDefault((Object)objectSchema.description(), (Object)"")).build();
        }
        if (element instanceof JsonStringSchema) {
            JsonStringSchema stringSchema = (JsonStringSchema)element;
            return Schema.builder().type(Type.Known.STRING).description((String)Utils.getOrDefault((Object)stringSchema.description(), (Object)"")).build();
        }
        if (element instanceof JsonEnumSchema) {
            JsonEnumSchema enumSchema = (JsonEnumSchema)element;
            return Schema.builder().type(Type.Known.STRING).format("enum").enum_(enumSchema.enumValues()).description((String)Utils.getOrDefault((Object)enumSchema.description(), (Object)"")).build();
        }
        if (element instanceof JsonIntegerSchema) {
            return Schema.builder().type(Type.Known.INTEGER).description((String)Utils.getOrDefault((Object)element.description(), (Object)"")).build();
        }
        if (element instanceof JsonNumberSchema) {
            return Schema.builder().type(Type.Known.NUMBER).description((String)Utils.getOrDefault((Object)element.description(), (Object)"")).build();
        }
        if (element instanceof JsonBooleanSchema) {
            return Schema.builder().type(Type.Known.BOOLEAN).description((String)Utils.getOrDefault((Object)element.description(), (Object)"")).build();
        }
        if (element instanceof JsonArraySchema) {
            JsonArraySchema arraySchema = (JsonArraySchema)element;
            return Schema.builder().type(Type.Known.ARRAY).items(GoogleGenAiToolMapper.convertToGoogleSchema(arraySchema.items())).description((String)Utils.getOrDefault((Object)arraySchema.description(), (Object)"")).build();
        }
        throw new IllegalArgumentException("Unknown schema type: " + element.getClass());
    }
}

