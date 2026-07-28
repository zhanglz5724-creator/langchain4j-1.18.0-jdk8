/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.store.embedding.milvus;

import com.google.gson.JsonObject;
import dev.langchain4j.internal.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Generator {
    Generator() {
    }

    static List<String> generateRandomIds(int size) {
        ArrayList<String> ids = new ArrayList<String>();
        for (int i = 0; i < size; ++i) {
            ids.add(Utils.randomUUID());
        }
        return ids;
    }

    static List<String> generateEmptyScalars(int size) {
        Object[] arr = new String[size];
        Arrays.fill(arr, "");
        return Arrays.asList(arr);
    }

    static List<JsonObject> generateEmptyJsons(int size) {
        ArrayList<JsonObject> list = new ArrayList<JsonObject>();
        for (int i = 0; i < size; ++i) {
            list.add(new JsonObject());
        }
        return list;
    }
}

