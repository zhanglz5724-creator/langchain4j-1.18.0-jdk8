/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.output.Response
 */
package dev.langchain4j.service.output;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.TypeUtils;
import dev.langchain4j.service.output.DefaultOutputParserFactory;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.PojoOutputParser;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Optional;

public class JsonSchemas {
    public static Optional<JsonSchema> jsonSchemaFrom(Type returnType) {
        if (!JsonSchemas.isPojo(returnType) || returnType == Void.TYPE) {
            return Optional.empty();
        }
        Class<?> rawClass = TypeUtils.getRawClass(returnType);
        JsonSchema jsonSchema = JsonSchema.builder().name(rawClass.getSimpleName()).rootElement(JsonSchemaElementUtils.jsonObjectOrReferenceSchemaFrom(rawClass, null, (boolean)false, new LinkedHashMap(), (boolean)true)).build();
        return Optional.of(jsonSchema);
    }

    private static boolean isPojo(Type returnType) {
        if (returnType == String.class || returnType == AiMessage.class || returnType == TokenStream.class || returnType == Response.class) {
            return false;
        }
        Class<?> rawClass = TypeUtils.getRawClass(returnType);
        Class<?> typeArgumentClass = TypeUtils.resolveFirstGenericParameterClass(returnType);
        OutputParser<?> outputParser = new DefaultOutputParserFactory().get(rawClass, typeArgumentClass);
        return outputParser instanceof PojoOutputParser;
    }
}

