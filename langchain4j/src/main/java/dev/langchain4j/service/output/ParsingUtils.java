/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.service.output.OutputParsingException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Internal
class ParsingUtils {
    ParsingUtils() {
    }

    static <T> T parseAsStringOrJson(String text, Function<String, T> parser, Class<T> type) {
        if (Utils.isNullOrBlank((String)text)) {
            throw ParsingUtils.outputParsingException(text, type);
        }
        if (ParsingUtils.isJson(text)) {
            Map map = (Map)Json.fromJson((String)text, Map.class);
            if (Utils.isNullOrEmpty((Map)map)) {
                throw ParsingUtils.outputParsingException(text, type);
            }
            Object value = map.get("value");
            if (value == null) {
                throw ParsingUtils.outputParsingException(text, type);
            }
            return ParsingUtils.parse(value.toString(), parser, type);
        }
        return ParsingUtils.parse(text, parser, type);
    }

    static <T, CT extends Collection<T>> CT parseAsStringOrJson(String text, Function<String, T> parser, Supplier<CT> emptyCollectionSupplier, String type) {
        if (text == null) {
            throw ParsingUtils.outputParsingException(text, type, null);
        }
        if (ParsingUtils.isJson(text)) {
            Map map = (Map)Json.fromJson((String)text, Map.class);
            if (Utils.isNullOrEmpty((Map)map)) {
                throw ParsingUtils.outputParsingException(text, type, null);
            }
            Object values = map.get("values");
            if (!(values instanceof Collection)) {
                throw ParsingUtils.outputParsingException(text, type, null);
            }
            Collection collection = (Collection)emptyCollectionSupplier.get();
            for (Object value : (Collection)values) {
                String string;
                String stringValue = value instanceof String ? (string = (String)value) : Json.toJson(value);
                collection.add(ParsingUtils.parse(stringValue, parser, type));
            }
            return (CT)collection;
        }
        Collection collection = (Collection)emptyCollectionSupplier.get();
        for (String line : text.split("\n")) {
            if (Utils.isNullOrBlank((String)line)) continue;
            collection.add(ParsingUtils.parse(line.trim(), parser, type));
        }
        return (CT)collection;
    }

    private static boolean isJson(String text) {
        return text.trim().startsWith("{");
    }

    private static <T> T parse(String text, Function<String, T> parser, Type type) {
        return ParsingUtils.parse(text, parser, type.getTypeName());
    }

    private static <T> T parse(String text, Function<String, T> parser, String type) {
        try {
            return parser.apply(text);
        }
        catch (IllegalArgumentException iae) {
            throw ParsingUtils.outputParsingException(text, type, iae);
        }
    }

    static OutputParsingException outputParsingException(String text, Type type) {
        return ParsingUtils.outputParsingException(text, type.getTypeName(), null);
    }

    static OutputParsingException outputParsingException(String text, String type, Throwable cause) {
        return new OutputParsingException(String.format("Failed to parse %s (base64: %s) into %s", Utils.quoted((Object)text), Utils.quoted((Object)Utils.toBase64((String)text)), type), cause);
    }
}

