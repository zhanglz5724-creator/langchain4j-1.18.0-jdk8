/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.exception.ToolArgumentsException
 *  dev.langchain4j.exception.ToolExecutionException
 *  dev.langchain4j.service.tool.ToolExecutionResult
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.exception.ToolArgumentsException;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.mcp.client.McpToolResultExtractor;
import dev.langchain4j.service.tool.ToolExecutionResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class ToolExecutionHelper {
    private static final int ERROR_CODE_INVALID_PARAMETERS = -32602;

    ToolExecutionHelper() {
    }

    static ToolExecutionResult extractResult(JsonNode result, boolean ignoreApplicationLevelErrors, McpToolResultExtractor toolResultExtractor) {
        if (result.has("result")) {
            JsonNode resultNode = result.get("result");
            if (resultNode.has("structuredContent") && !resultNode.get("structuredContent").isNull()) {
                JsonNode content = resultNode.get("structuredContent");
                if (ToolExecutionHelper.isError(resultNode) && !ignoreApplicationLevelErrors) {
                    throw new ToolExecutionException(content.toString());
                }
                return ToolExecutionResult.builder().result(ToolExecutionHelper.toObject(content)).resultText(content.toString()).isError(ToolExecutionHelper.isError(resultNode)).build();
            }
            if (resultNode.has("content")) {
                boolean applicationError = ToolExecutionHelper.isError(resultNode);
                ToolExecutionResult toolExecutionResult = toolResultExtractor.extract(resultNode.get("content"), applicationError);
                if (applicationError && !ignoreApplicationLevelErrors) {
                    throw new ToolExecutionException(ToolExecutionHelper.errorMessage(toolExecutionResult, resultNode.get("content")));
                }
                return toolExecutionResult;
            }
            throw new RuntimeException("Result does not contain 'content' element: " + result);
        }
        if (result.has("error")) {
            String errorMessage = ToolExecutionHelper.extractErrorMessage(result.get("error"));
            Integer errorCode = ToolExecutionHelper.extractErrorCode(result.get("error"));
            if (errorCode != null && errorCode == -32602) {
                throw new ToolArgumentsException(errorMessage, errorCode);
            }
            throw new ToolExecutionException(errorMessage, errorCode);
        }
        throw new RuntimeException("Result contains neither 'result' nor 'error' element: " + result);
    }

    private static String errorMessage(ToolExecutionResult toolExecutionResult, JsonNode content) {
        String contentsText = toolExecutionResult.resultContents().stream().filter(TextContent.class::isInstance).map(TextContent.class::cast).map(TextContent::text).collect(Collectors.joining("\n"));
        if (!contentsText.isEmpty()) {
            return contentsText;
        }
        if (toolExecutionResult.result() != null) {
            return toolExecutionResult.result().toString();
        }
        String rawContentText = StreamSupport.stream(content.spliterator(), false).map(ToolExecutionHelper::textFromContentItem).filter(text -> !text.isEmpty()).collect(Collectors.joining("\n"));
        if (!rawContentText.isEmpty()) {
            return rawContentText;
        }
        return "";
    }

    private static String textFromContentItem(JsonNode contentItem) {
        JsonNode type = contentItem.get("type");
        JsonNode text = contentItem.get("text");
        if (type != null && "text".equals(type.asText()) && text != null) {
            return text.asText();
        }
        return "";
    }

    static Object toObject(JsonNode content) {
        switch (content.getNodeType()) {
            case BOOLEAN: {
                return content.asBoolean();
            }
            case NUMBER: {
                switch (content.numberType()) {
                    case INT: {
                        return content.asInt();
                    }
                    case LONG: {
                        return content.asLong();
                    }
                    case BIG_INTEGER: {
                        return content.bigIntegerValue();
                    }
                    case FLOAT: 
                    case DOUBLE: 
                    case BIG_DECIMAL: {
                        return content.asDouble();
                    }
                }
                throw new IllegalStateException("Unexpected number type: " + content.numberType());
            }
            case STRING: {
                return content.asText();
            }
            case NULL: {
                return null;
            }
            case ARRAY: {
                return StreamSupport.stream(content.spliterator(), true).map(element -> ToolExecutionHelper.toObject(element)).collect(Collectors.toList());
            }
            case OBJECT: {
                HashMap map = new HashMap();
                for (Map.Entry property : content.properties()) {
                    map.put(property.getKey(), ToolExecutionHelper.toObject((JsonNode)property.getValue()));
                }
                return map;
            }
            case BINARY: {
                try {
                    return content.binaryValue();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case POJO: {
                return new Object();
            }
            case MISSING: {
                return new Object();
            }
        }
        throw new IllegalStateException("Unexpected node type: " + content.getNodeType());
    }

    private static boolean isError(JsonNode resultNode) {
        if (resultNode.has("isError")) {
            return resultNode.get("isError").asBoolean();
        }
        return false;
    }

    private static String extractErrorMessage(JsonNode errorNode) {
        if (errorNode.has("message")) {
            return errorNode.get("message").asText("");
        }
        return "";
    }

    private static Integer extractErrorCode(JsonNode errorNode) {
        if (errorNode.has("code")) {
            return errorNode.get("code").asInt();
        }
        return null;
    }
}

