/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  software.amazon.awssdk.core.document.Document
 *  software.amazon.awssdk.core.document.internal.MapDocument
 */
package dev.langchain4j.model.bedrock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import software.amazon.awssdk.core.document.Document;
import software.amazon.awssdk.core.document.internal.MapDocument;

@Internal
class AwsDocumentConverter {
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private AwsDocumentConverter() {
    }

    public static String documentToJson(Document document) {
        if (document == null) {
            return "{}";
        }
        try {
            HashMap actualValues = new HashMap();
            for (Map.Entry entry : document.asMap().entrySet()) {
                Document doc = (Document)entry.getValue();
                actualValues.put(entry.getKey(), AwsDocumentConverter.documentToObject(doc));
            }
            return OBJECT_MAPPER.writeValueAsString(actualValues);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object documentToObject(Document doc) {
        if (doc.isNumber()) {
            return doc.asNumber();
        }
        if (doc.isBoolean()) {
            return doc.asBoolean();
        }
        if (doc.isList()) {
            return doc.asList().stream().map(AwsDocumentConverter::documentToObject).collect(Collectors.toList());
        }
        if (doc.isMap()) {
            HashMap innerObject = new HashMap();
            doc.asMap().forEach((k, v) -> innerObject.put(k, AwsDocumentConverter.documentToObject(v)));
            return innerObject;
        }
        if (doc.isNull()) {
            return null;
        }
        return doc.asString();
    }

    public static Document documentFromJson(String json) {
        try {
            JsonNode jsonNode = (JsonNode)OBJECT_MAPPER.readValue(json, JsonNode.class);
            Iterator fields = jsonNode.fields();
            return new MapDocument(AwsDocumentConverter.fieldsToDocumentMap(fields));
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Document> fieldsToDocumentMap(Iterator<Map.Entry<String, JsonNode>> fields) {
        HashMap<String, Document> documentMap = new HashMap<String, Document>();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode value = entry.getValue();
            Document doc = AwsDocumentConverter.getDocument(value);
            documentMap.put(entry.getKey(), doc);
        }
        return documentMap;
    }

    private static Document getDocument(JsonNode value) {
        Document doc;
        if (value.isBoolean()) {
            doc = Document.fromBoolean((boolean)value.asBoolean());
        } else if (value.isDouble() || value.isFloat() || value.isBigDecimal()) {
            doc = Document.fromNumber((double)value.asDouble());
        } else if (value.isInt() || value.isLong() || value.isShort() || value.isBigInteger()) {
            doc = Document.fromNumber((BigInteger)value.bigIntegerValue());
        } else if (value.isArray()) {
            ArrayList<Document> list = new ArrayList<Document>();
            for (JsonNode node : value) {
                list.add(AwsDocumentConverter.getDocument(node));
            }
            doc = Document.fromList(list);
        } else {
            doc = value.isObject() || value.isPojo() ? Document.fromMap(AwsDocumentConverter.fieldsToDocumentMap(value.fields())) : Document.fromString((String)value.asText());
        }
        return doc;
    }

    public static Document convertJsonObjectSchemaToDocument(ToolSpecification toolSpecification) {
        HashMap<String, Object> schemaMap = new HashMap<String, Object>();
        schemaMap.put("type", "object");
        if (toolSpecification.parameters() != null) {
            Map propertiesMap = JsonSchemaElementUtils.toMap((Map)toolSpecification.parameters().properties());
            schemaMap.put("properties", propertiesMap);
            ArrayList required = new ArrayList(toolSpecification.parameters().required());
            schemaMap.put("required", required);
        }
        try {
            String jsonSchema = OBJECT_MAPPER.writeValueAsString(schemaMap);
            return AwsDocumentConverter.documentFromJson(jsonSchema);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert schema to Document", e);
        }
    }

    public static Document convertAdditionalModelRequestFields(Map<String, Object> additionalModelRequestFields) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(additionalModelRequestFields);
            return AwsDocumentConverter.documentFromJson(json);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert additionalModelRequestFields to Document", e);
        }
    }
}

