/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.Utils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Internal
class ToolExecutionRequestUtil {
    private static final Pattern TRAILING_COMMA_PATTERN = Pattern.compile(",(\\s*[}\\]])");
    private static final Pattern LEADING_TRAILING_QUOTE_PATTERN = Pattern.compile("^\"|\"$");
    private static final Pattern ESCAPED_QUOTE_PATTERN = Pattern.compile("\\\\\"");
    private static final Type MAP_TYPE = new ParameterizedType(){

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class, Object.class};
        }

        @Override
        public Type getRawType() {
            return Map.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    };

    private ToolExecutionRequestUtil() {
    }

    static Map<String, Object> argumentsAsMap(String arguments) {
        if (Utils.isNullOrBlank((String)arguments)) {
            return Collections.emptyMap();
        }
        try {
            return (Map)Json.fromJson((String)arguments, (Type)MAP_TYPE);
        }
        catch (Exception ignored) {
            String normalizedArguments = ToolExecutionRequestUtil.removeTrailingComma(ToolExecutionRequestUtil.normalizeJsonString(arguments));
            return (Map)Json.fromJson((String)normalizedArguments, (Type)MAP_TYPE);
        }
    }

    static String removeTrailingComma(String json) {
        if (Utils.isNullOrEmpty((String)json)) {
            return json;
        }
        Matcher matcher = TRAILING_COMMA_PATTERN.matcher(json);
        return matcher.replaceAll("$1");
    }

    static String normalizeJsonString(String arguments) {
        if (Utils.isNullOrEmpty((String)arguments)) {
            return arguments;
        }
        Matcher leadingTrailingMatcher = LEADING_TRAILING_QUOTE_PATTERN.matcher(arguments);
        String normalizedJson = leadingTrailingMatcher.replaceAll("");
        Matcher escapedQuoteMatcher = ESCAPED_QUOTE_PATTERN.matcher(normalizedJson);
        return escapedQuoteMatcher.replaceAll("\"");
    }
}

