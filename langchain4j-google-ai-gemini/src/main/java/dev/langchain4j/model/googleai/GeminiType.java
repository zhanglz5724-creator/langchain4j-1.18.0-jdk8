/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.googleai;

enum GeminiType {
    STRING,
    NUMBER,
    INTEGER,
    BOOLEAN,
    ARRAY,
    OBJECT,
    NULL;


    public String toString() {
        return this.name().toLowerCase();
    }
}

