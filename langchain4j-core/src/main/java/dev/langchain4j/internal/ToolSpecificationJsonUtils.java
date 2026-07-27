/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecificationJsonCodec;
import dev.langchain4j.internal.JacksonToolSpecificationJsonCodec;
import dev.langchain4j.internal.JsonSchemaElementJsonUtils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.agent.tool.ToolSpecificationJsonCodecFactory;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Internal
public class ToolSpecificationJsonUtils {
    private static final ToolSpecificationJsonCodec CODEC = ToolSpecificationJsonUtils.loadCodec();

    private static ToolSpecificationJsonCodec loadCodec() {
        Iterator<ToolSpecificationJsonCodecFactory> iterator = ServiceHelper.loadFactories(ToolSpecificationJsonCodecFactory.class).iterator();
        if (iterator.hasNext()) {
            ToolSpecificationJsonCodecFactory factory = iterator.next();
            return factory.create();
        }
        return new JacksonToolSpecificationJsonCodec();
    }

    private ToolSpecificationJsonUtils() {
    }

    public static String toJson(ToolSpecification toolSpecification) {
        ValidationUtils.ensureNotNull(toolSpecification, "toolSpecification");
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", toolSpecification.name());
        if (toolSpecification.description() != null) {
            map.put("description", toolSpecification.description());
        }
        if (toolSpecification.parameters() != null) {
            map.put("parameters", JsonSchemaElementJsonUtils.toMap(toolSpecification.parameters()));
        }
        if (toolSpecification.metadata() != null && !toolSpecification.metadata().isEmpty()) {
            map.put("metadata", toolSpecification.metadata());
        }
        if (toolSpecification.strict() != null) {
            map.put("strict", toolSpecification.strict());
        }
        return CODEC.toJson(map);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ToolSpecification fromJson(String json) {
        Object metadataObj;
        ValidationUtils.ensureNotNull(json, "json");
        Map map = CODEC.fromJson(json, Map.class);
        ToolSpecification.Builder builder = ToolSpecification.builder().name(ToolSpecificationJsonUtils.optionalString(map, "name")).description(ToolSpecificationJsonUtils.optionalString(map, "description"));
        Object parametersObj = map.get("parameters");
        if (parametersObj instanceof Map) {
            Map parametersMap = (Map)parametersObj;
            JsonSchemaElement element = JsonSchemaElementJsonUtils.fromMap(parametersMap);
            if (!(element instanceof JsonObjectSchema)) throw new IllegalArgumentException("\"parameters\" must be a JSON object schema, but was: " + element.getClass().getSimpleName());
            builder.parameters((JsonObjectSchema)element);
        } else if (parametersObj != null) {
            throw new IllegalArgumentException("\"parameters\" must be a JSON object, but was: " + parametersObj.getClass().getSimpleName());
        }
        if ((metadataObj = map.get("metadata")) instanceof Map) {
            builder.metadata((Map)metadataObj);
        } else if (metadataObj != null) {
            throw new IllegalArgumentException("\"metadata\" must be a JSON object, but was: " + metadataObj.getClass().getSimpleName());
        }
        Object strictObj = map.get("strict");
        if (strictObj instanceof Boolean) {
            builder.strict((Boolean)strictObj);
            return builder.build();
        } else {
            if (strictObj == null) return builder.build();
            throw new IllegalArgumentException("\"strict\" must be a boolean, but was: " + strictObj.getClass().getSimpleName());
        }
    }

    private static String optionalString(Map<String, Object> map, String field) {
        Object value = map.get(field);
        if (value == null) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("\"" + field + "\" must be a string, but was: " + value.getClass().getSimpleName());
        }
        return (String)value;
    }
}

