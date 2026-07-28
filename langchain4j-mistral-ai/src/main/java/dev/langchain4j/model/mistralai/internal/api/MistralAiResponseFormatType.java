/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.chat.request.ResponseFormat
 */
package dev.langchain4j.model.mistralai.internal.api;

import dev.langchain4j.model.chat.request.ResponseFormat;

public enum MistralAiResponseFormatType {
    TEXT,
    JSON_OBJECT;


    public ResponseFormat toGenericResponseFormat() {
        switch (this) {
            case TEXT: {
                return ResponseFormat.TEXT;
            }
            case JSON_OBJECT: {
                return ResponseFormat.JSON;
            }
        }
        throw new IllegalStateException("Unexpected value: " + (Object)((Object)this));
    }

    public String toString() {
        return this.name().toLowerCase();
    }
}

