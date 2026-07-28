/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.Response
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.TypeUtils;
import dev.langchain4j.service.output.DefaultOutputParserFactory;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.OutputParserFactory;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

@Internal
public class ServiceOutputParser {
    private final OutputParserFactory outputParserFactory;

    public ServiceOutputParser() {
        this(new DefaultOutputParserFactory());
    }

    ServiceOutputParser(OutputParserFactory outputParserFactory) {
        this.outputParserFactory = (OutputParserFactory)ValidationUtils.ensureNotNull((Object)outputParserFactory, (String)"outputParserFactory");
    }

    public Object parse(ChatResponse chatResponse, Type returnType) {
        Class<?> rawClass;
        if (TypeUtils.typeHasRawClass(returnType, Result.class)) {
            returnType = TypeUtils.resolveFirstGenericParameterType(returnType);
        }
        if ((rawClass = TypeUtils.getRawClass(returnType)) == Response.class) {
            return Response.from((Object)chatResponse.aiMessage(), (TokenUsage)chatResponse.tokenUsage(), (FinishReason)chatResponse.finishReason());
        }
        if (rawClass == Void.TYPE || rawClass == Void.class) {
            return null;
        }
        AiMessage aiMessage = chatResponse.aiMessage();
        if (rawClass == AiMessage.class) {
            return aiMessage;
        }
        return this.parseText(returnType, rawClass, aiMessage.text());
    }

    public Object parseText(Type returnType, String text) {
        return this.parseText(returnType, TypeUtils.getRawClass(returnType), text);
    }

    private Object parseText(Type returnType, Class<?> rawClass, String text) {
        if (rawClass == String.class) {
            return text;
        }
        Class<?> typeArgumentClass = TypeUtils.resolveFirstGenericParameterClass(returnType);
        OutputParser<?> outputParser = this.outputParserFactory.get(rawClass, typeArgumentClass);
        return outputParser.parse(text);
    }

    public Optional<JsonSchema> jsonSchema(Type returnType) {
        if (TypeUtils.typeHasRawClass(returnType, Result.class)) {
            returnType = TypeUtils.resolveFirstGenericParameterType(returnType);
        }
        Class<?> rawClass = TypeUtils.getRawClass(returnType);
        Class<?> typeArgumentClass = TypeUtils.resolveFirstGenericParameterClass(returnType);
        if (ServiceOutputParser.schemaNotRequired(rawClass)) {
            return Optional.empty();
        }
        OutputParser<?> outputParser = this.outputParserFactory.get(rawClass, typeArgumentClass);
        return outputParser.jsonSchema();
    }

    public String outputFormatInstructions(Type returnType) {
        if (TypeUtils.typeHasRawClass(returnType, Result.class)) {
            returnType = TypeUtils.resolveFirstGenericParameterType(returnType);
        }
        Class<?> rawClass = TypeUtils.getRawClass(returnType);
        Class<?> typeArgumentClass = TypeUtils.resolveFirstGenericParameterClass(returnType);
        if (ServiceOutputParser.schemaNotRequired(rawClass)) {
            return "";
        }
        OutputParser<?> outputParser = this.outputParserFactory.get(rawClass, typeArgumentClass);
        String formatInstructions = outputParser.formatInstructions();
        if (!formatInstructions.startsWith("\nYou must")) {
            formatInstructions = "\nYou must answer strictly in the following format: " + formatInstructions;
        }
        return formatInstructions;
    }

    private static boolean schemaNotRequired(Class<?> type) {
        return type == String.class || type == AiMessage.class || type == TokenStream.class || type == Response.class || type == Map.class || type == Void.TYPE || type == Void.class;
    }
}

