/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Json;
import java.util.Objects;

@Internal
public class JsonParsingUtils {
    public static <T> ParsedJson<T> extractAndParseJson(String text, Class<T> type) throws Exception {
        return JsonParsingUtils.extractAndParseJson(text, (String s) -> Json.fromJson(s, type));
    }

    public static <T> ParsedJson<T> extractAndParseJson(String text, ThrowingFunction<String, T> parser) throws Exception {
        Exception parseException = null;
        try {
            return new ParsedJson<T>(parser.apply(text), text);
        }
        catch (Exception ex) {
            parseException = ex;
            int index = text.length();
            while (true) {
                int jsonEnd;
                if ((jsonEnd = JsonParsingUtils.findJsonEnd(text, index)) < 0) {
                    throw parseException;
                }
                int jsonStart = JsonParsingUtils.findJsonStart(text, jsonEnd, text.charAt(jsonEnd));
                if (jsonStart < 0) {
                    throw parseException;
                }
                try {
                    String tentativeJson = text.substring(jsonStart, jsonEnd + 1);
                    return new ParsedJson<T>(parser.apply(tentativeJson), tentativeJson);
                }
                catch (Exception exception) {
                    index = jsonStart;
                    continue;
                }
            }
        }
    }

    private static int findJsonEnd(String text, int fromIndex) {
        int jsonMapEnd = text.lastIndexOf(125, fromIndex);
        int jsonListEnd = text.lastIndexOf(93, fromIndex);
        return Math.max(jsonMapEnd, jsonListEnd);
    }

    private static int findJsonStart(String text, int jsonEnd, char closingBrace) {
        char openingBrace = closingBrace == '}' ? (char)'{' : '[';
        int braceCount = 0;
        for (int i = jsonEnd; i >= 0; --i) {
            char c = text.charAt(i);
            if (c == '\"' && !JsonParsingUtils.isEscaped(text, i)) {
                --i;
                while (i >= 0 && (text.charAt(i) != '\"' || JsonParsingUtils.isEscaped(text, i))) {
                    --i;
                }
                continue;
            }
            if (c == openingBrace) {
                if (++braceCount != 0) continue;
                return i == 0 || text.charAt(i - 1) != openingBrace ? i : -1;
            }
            if (c != closingBrace) continue;
            --braceCount;
        }
        return -1;
    }

    private static boolean isEscaped(String text, int index) {
        int backslashCount = 0;
        for (int i = index - 1; i >= 0 && text.charAt(i) == '\\'; --i) {
            ++backslashCount;
        }
        return backslashCount % 2 != 0;
    }

    public static class ParsedJson<T> {
        private final T value;
        private final String json;

        public ParsedJson(T value, String json) {
            this.value = value;
            this.json = json;
        }

        public T value() {
            return this.value;
        }

        public String json() {
            return this.json;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            ParsedJson that = (ParsedJson)o;
            return Objects.equals(this.value, that.value) && Objects.equals(this.json, that.json);
        }

        public int hashCode() {
            return Objects.hash(this.value, this.json);
        }

        public String toString() {
            return "ParsedJson[value=" + this.value + ", json=" + this.json + "]";
        }
    }

    @FunctionalInterface
    public static interface ThrowingFunction<T, R> {
        public R apply(T var1) throws Exception;
    }
}

