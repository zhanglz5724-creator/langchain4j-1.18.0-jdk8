/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.node.ArrayNode
 *  com.fasterxml.jackson.databind.node.JsonNodeType
 *  com.fasterxml.jackson.databind.node.ObjectNode
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.agent.tool.ToolSpecification$Builder
 *  dev.langchain4j.model.chat.request.json.JsonAnyOfSchema
 *  dev.langchain4j.model.chat.request.json.JsonAnyOfSchema$Builder
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema
 *  dev.langchain4j.model.chat.request.json.JsonArraySchema$Builder
 *  dev.langchain4j.model.chat.request.json.JsonBooleanSchema
 *  dev.langchain4j.model.chat.request.json.JsonBooleanSchema$Builder
 *  dev.langchain4j.model.chat.request.json.JsonEnumSchema
 *  dev.langchain4j.model.chat.request.json.JsonEnumSchema$Builder
 *  dev.langchain4j.model.chat.request.json.JsonIntegerSchema
 *  dev.langchain4j.model.chat.request.json.JsonIntegerSchema$Builder
 *  dev.langchain4j.model.chat.request.json.JsonNullSchema
 *  dev.langchain4j.model.chat.request.json.JsonNumberSchema
 *  dev.langchain4j.model.chat.request.json.JsonNumberSchema$Builder
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema$Builder
 *  dev.langchain4j.model.chat.request.json.JsonReferenceSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.chat.request.json.JsonStringSchema
 *  dev.langchain4j.model.chat.request.json.JsonStringSchema$Builder
 */
package dev.langchain4j.mcp.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.client.McpIcon;
import dev.langchain4j.model.chat.request.json.JsonAnyOfSchema;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonBooleanSchema;
import dev.langchain4j.model.chat.request.json.JsonEnumSchema;
import dev.langchain4j.model.chat.request.json.JsonIntegerSchema;
import dev.langchain4j.model.chat.request.json.JsonNullSchema;
import dev.langchain4j.model.chat.request.json.JsonNumberSchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonReferenceSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

class ToolSpecificationHelper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<McpIcon>> MCP_ICON_LIST_TYPE = new TypeReference<List<McpIcon>>(){};

    ToolSpecificationHelper() {
    }

    static List<ToolSpecification> toolSpecificationListFromMcpResponse(ArrayNode array) {
        ArrayList<ToolSpecification> result = new ArrayList<ToolSpecification>();
        for (JsonNode tool : array) {
            ToolSpecification.Builder builder = ToolSpecification.builder();
            builder.name(tool.get("name").asText());
            if (tool.has("description")) {
                builder.description(tool.get("description").asText());
            }
            builder.parameters((JsonObjectSchema)ToolSpecificationHelper.jsonNodeToJsonSchemaElement(tool.get("inputSchema")));
            if (tool.has("annotations")) {
                ToolSpecificationHelper.processMcpToolAnnotations(tool.get("annotations"), builder);
            }
            if (tool.has("_meta")) {
                ToolSpecificationHelper.processMcpToolMetadata(tool.get("_meta"), builder);
            }
            if (tool.has("title")) {
                builder.addMetadata("title", (Object)tool.get("title").asText());
            }
            if (tool.has("outputSchema")) {
                builder.addMetadata("outputSchema", OBJECT_MAPPER.convertValue((Object)tool.get("outputSchema"), Object.class));
            }
            if (tool.has("icons")) {
                builder.addMetadata("icons", OBJECT_MAPPER.convertValue((Object)tool.get("icons"), MCP_ICON_LIST_TYPE));
            }
            result.add(builder.build());
        }
        return result;
    }

    static JsonSchemaElement jsonNodeToJsonSchemaElement(JsonNode node) {
        if (node.has("anyOf") && !ToolSpecificationHelper.isObjectType(node)) {
            JsonAnyOfSchema.Builder anyOf = JsonAnyOfSchema.builder();
            JsonSchemaElement[] types = (JsonSchemaElement[])StreamSupport.stream(node.get("anyOf").spliterator(), false).map(ToolSpecificationHelper::jsonNodeToJsonSchemaElement).toArray(JsonSchemaElement[]::new);
            anyOf.anyOf(types);
            if (node.has("description")) {
                anyOf.description(node.get("description").asText());
            }
            return anyOf.build();
        }
        if (node.has("$ref")) {
            return JsonReferenceSchema.builder().reference(ToolSpecificationHelper.extractReferenceKey(node.get("$ref").asText())).build();
        }
        JsonNode typeNode = node.get("type");
        if (typeNode == null || node.get("type").getNodeType() == JsonNodeType.STRING && node.get("type").asText().equals("object")) {
            JsonNode defsNode;
            JsonObjectSchema.Builder builder = JsonObjectSchema.builder();
            if (node.has("description")) {
                builder.description(node.get("description").asText());
            }
            if (node.has("properties")) {
                ObjectNode propertiesObject = (ObjectNode)node.get("properties");
                for (Map.Entry property : propertiesObject.properties()) {
                    builder.addProperty((String)property.getKey(), ToolSpecificationHelper.jsonNodeToJsonSchemaElement((JsonNode)property.getValue()));
                }
            }
            if (node.has("required")) {
                builder.required(ToolSpecificationHelper.toStringArray((ArrayNode)node.get("required")));
            }
            if (node.has("additionalProperties")) {
                builder.additionalProperties(Boolean.valueOf(node.get("additionalProperties").asBoolean(false)));
            }
            JsonNode jsonNode = defsNode = node.has("$defs") ? node.get("$defs") : node.get("definitions");
            if (defsNode != null) {
                LinkedHashMap definitions = new LinkedHashMap();
                for (Map.Entry entry : ((ObjectNode)defsNode).properties()) {
                    definitions.put(entry.getKey(), ToolSpecificationHelper.jsonNodeToJsonSchemaElement((JsonNode)entry.getValue()));
                }
                builder.definitions(definitions);
            }
            return builder.build();
        }
        if (node.get("type").getNodeType() == JsonNodeType.STRING) {
            String nodeType = node.get("type").asText();
            if (nodeType.equals("string")) {
                if (node.has("enum")) {
                    JsonEnumSchema.Builder builder = JsonEnumSchema.builder();
                    if (node.has("description")) {
                        builder.description(node.get("description").asText());
                    }
                    builder.enumValues(ToolSpecificationHelper.toStringArray((ArrayNode)node.get("enum")));
                    return builder.build();
                }
                JsonStringSchema.Builder builder = JsonStringSchema.builder();
                if (node.has("description")) {
                    builder.description(node.get("description").asText());
                }
                return builder.build();
            }
            if (nodeType.equals("number")) {
                JsonNumberSchema.Builder builder = JsonNumberSchema.builder();
                if (node.has("description")) {
                    builder.description(node.get("description").asText());
                }
                return builder.build();
            }
            if (nodeType.equals("integer")) {
                JsonIntegerSchema.Builder builder = JsonIntegerSchema.builder();
                if (node.has("description")) {
                    builder.description(node.get("description").asText());
                }
                return builder.build();
            }
            if (nodeType.equals("boolean")) {
                JsonBooleanSchema.Builder builder = JsonBooleanSchema.builder();
                if (node.has("description")) {
                    builder.description(node.get("description").asText());
                }
                return builder.build();
            }
            if (nodeType.equals("array")) {
                JsonArraySchema.Builder builder = JsonArraySchema.builder();
                if (node.has("description")) {
                    builder.description(node.get("description").asText());
                }
                if (node.has("items") && (!node.get("items").isArray() || node.get("items").isArray() && !node.get("items").isEmpty())) {
                    builder.items(ToolSpecificationHelper.jsonNodeToJsonSchemaElement(node.get("items")));
                }
                return builder.build();
            }
            if (nodeType.equals("null")) {
                return new JsonNullSchema();
            }
            throw new IllegalArgumentException("Unknown element type: " + nodeType);
        }
        JsonAnyOfSchema.Builder anyOf = JsonAnyOfSchema.builder();
        JsonSchemaElement[] types = (JsonSchemaElement[])StreamSupport.stream(node.get("type").spliterator(), false).map(ToolSpecificationHelper::toTypeElement).toArray(JsonSchemaElement[]::new);
        anyOf.anyOf(types);
        return anyOf.build();
    }

    private static boolean isObjectType(JsonNode node) {
        JsonNode typeNode = node.get("type");
        return typeNode != null && typeNode.getNodeType() == JsonNodeType.STRING && typeNode.asText().equals("object");
    }

    private static JsonSchemaElement toTypeElement(JsonNode node) {
        if (!node.isTextual()) {
            throw new IllegalArgumentException(node + " is not a string");
        }
        switch (node.textValue()) {
            case "string": {
                return JsonStringSchema.builder().build();
            }
            case "number": {
                return JsonNumberSchema.builder().build();
            }
            case "integer": {
                return JsonIntegerSchema.builder().build();
            }
            case "boolean": {
                return JsonBooleanSchema.builder().build();
            }
            case "array": {
                return JsonArraySchema.builder().build();
            }
            case "object": {
                return JsonObjectSchema.builder().build();
            }
            case "null": {
                return new JsonNullSchema();
            }
        }
        throw new IllegalArgumentException("Unsupported type: " + node.textValue());
    }

    private static String extractReferenceKey(String ref) {
        if (ref.startsWith("#/$defs/")) {
            return ref.substring("#/$defs/".length());
        }
        if (ref.startsWith("#/definitions/")) {
            return ref.substring("#/definitions/".length());
        }
        return ref;
    }

    private static String[] toStringArray(ArrayNode jsonArray) {
        String[] result = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); ++i) {
            result[i] = jsonArray.get(i).asText();
        }
        return result;
    }

    private static void processMcpToolAnnotations(JsonNode annotations, ToolSpecification.Builder builder) {
        if (annotations.has("destructiveHint")) {
            builder.addMetadata("destructiveHint", (Object)annotations.get("destructiveHint").asBoolean());
        }
        if (annotations.has("idempotentHint")) {
            builder.addMetadata("idempotentHint", (Object)annotations.get("idempotentHint").asBoolean());
        }
        if (annotations.has("openWorldHint")) {
            builder.addMetadata("openWorldHint", (Object)annotations.get("openWorldHint").asBoolean());
        }
        if (annotations.has("readOnlyHint")) {
            builder.addMetadata("readOnlyHint", (Object)annotations.get("readOnlyHint").asBoolean());
        }
        if (annotations.has("title")) {
            builder.addMetadata("title-annotation", (Object)annotations.get("title").asText());
        }
    }

    private static void processMcpToolMetadata(JsonNode meta, ToolSpecification.Builder builder) {
        for (Map.Entry property : meta.properties()) {
            builder.addMetadata((String)property.getKey(), OBJECT_MAPPER.convertValue(property.getValue(), Object.class));
        }
    }
}

