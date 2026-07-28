/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.store.embedding.tablestore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.langchain4j.internal.ValidationUtils;

class TablestoreUtils {
    private static final int MAX_DEBUG_LOG_LENGTH = 100;
    private static final Gson GSON = new GsonBuilder().create();

    TablestoreUtils() {
    }

    protected static float[] parseEmbeddingString(String embeddingString) {
        ValidationUtils.ensureNotBlank((String)embeddingString, (String)"embeddingString");
        return (float[])GSON.fromJson(embeddingString, float[].class);
    }

    protected static String embeddingToString(float[] embedding) {
        ValidationUtils.ensureNotNull((Object)embedding, (String)"embedding");
        return GSON.toJson((Object)embedding);
    }

    protected static String maxLogOrNull(String str) {
        if (str == null) {
            return null;
        }
        int max = 100;
        if (str.length() <= max) {
            return str;
        }
        return str.substring(0, max) + "......";
    }
}

