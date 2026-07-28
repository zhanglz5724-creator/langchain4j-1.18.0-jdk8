/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.FunctionCall
 *  com.google.cloud.vertexai.api.FunctionCall$Builder
 *  com.google.cloud.vertexai.api.FunctionDeclaration
 *  com.google.cloud.vertexai.api.FunctionDeclaration$Builder
 *  com.google.cloud.vertexai.api.Tool
 *  com.google.cloud.vertexai.api.Tool$Builder
 *  com.google.gson.Gson
 *  com.google.protobuf.InvalidProtocolBufferException
 *  com.google.protobuf.Message$Builder
 *  com.google.protobuf.Struct
 *  com.google.protobuf.Struct$Builder
 *  com.google.protobuf.Value
 *  com.google.protobuf.util.JsonFormat
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolExecutionRequest$Builder
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.FunctionCall;
import com.google.cloud.vertexai.api.FunctionDeclaration;
import com.google.cloud.vertexai.api.Tool;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.vertexai.gemini.SchemaHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class FunctionCallHelper {
    private static final Gson GSON = new Gson();

    FunctionCallHelper() {
    }

    static FunctionCall fromToolExecutionRequest(ToolExecutionRequest toolExecutionRequest) {
        FunctionCall.Builder fnCallBuilder = FunctionCall.newBuilder().setName(toolExecutionRequest.name());
        Struct.Builder structBuilder = Struct.newBuilder();
        try {
            String toolArguments = toolExecutionRequest.arguments();
            String arguments = Utils.isNullOrBlank((String)toolArguments) ? "{}" : toolArguments;
            JsonFormat.parser().merge(arguments, (Message.Builder)structBuilder);
        }
        catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
        Struct argsStruct = structBuilder.build();
        fnCallBuilder.setArgs(argsStruct);
        return fnCallBuilder.build();
    }

    static List<ToolExecutionRequest> fromFunctionCalls(List<FunctionCall> functionCalls) {
        return IntStream.range(0, functionCalls.size()).mapToObj(index -> FunctionCallHelper.fromFunctionCall(index, (FunctionCall)functionCalls.get(index))).collect(Collectors.toList());
    }

    static ToolExecutionRequest fromFunctionCall(int index, FunctionCall functionCall) {
        ToolExecutionRequest.Builder builder = ToolExecutionRequest.builder().id(String.valueOf(index)).name(functionCall.getName());
        HashMap callArgsMap = new HashMap();
        Struct callArgs = functionCall.getArgs();
        Map callArgsFieldsMap = callArgs.getFieldsMap();
        callArgsFieldsMap.forEach((key, value) -> callArgsMap.put(key, FunctionCallHelper.unwrapProtoValue(value)));
        String serializedArgsMap = GSON.toJson(callArgsMap);
        builder.arguments(serializedArgsMap);
        return builder.build();
    }

    static Object unwrapProtoValue(Value value) {
        Object unwrappedValue;
        switch (value.getKindCase()) {
            case NUMBER_VALUE: {
                unwrappedValue = value.getNumberValue();
                break;
            }
            case STRING_VALUE: {
                unwrappedValue = value.getStringValue();
                break;
            }
            case BOOL_VALUE: {
                unwrappedValue = value.getBoolValue();
                break;
            }
            case STRUCT_VALUE: {
                HashMap mapForStruct = new HashMap();
                value.getStructValue().getFieldsMap().forEach((key, val) -> mapForStruct.put(key, FunctionCallHelper.unwrapProtoValue(val)));
                unwrappedValue = mapForStruct;
                break;
            }
            case LIST_VALUE: {
                unwrappedValue = value.getListValue().getValuesList().stream().map(FunctionCallHelper::unwrapProtoValue).collect(Collectors.toList());
                break;
            }
            default: {
                unwrappedValue = null;
            }
        }
        return unwrappedValue;
    }

    static Tool convertToolSpecifications(List<ToolSpecification> toolSpecifications) {
        Tool.Builder tool = Tool.newBuilder();
        for (ToolSpecification toolSpecification : toolSpecifications) {
            FunctionDeclaration.Builder fnBuilder = FunctionDeclaration.newBuilder().setName(toolSpecification.name());
            if (toolSpecification.description() != null) {
                fnBuilder.setDescription(toolSpecification.description());
            }
            if (toolSpecification.parameters() != null) {
                fnBuilder.setParameters(SchemaHelper.from((JsonSchemaElement)toolSpecification.parameters()));
            }
            tool.addFunctionDeclarations(fnBuilder.build());
        }
        return tool.build();
    }
}

