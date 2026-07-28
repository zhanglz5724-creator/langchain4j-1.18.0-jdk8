/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 */
package dev.langchain4j.model.vertexai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Json {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    Json() {
    }

    static String toJson(Object o) {
        return GSON.toJson(o);
    }
}

