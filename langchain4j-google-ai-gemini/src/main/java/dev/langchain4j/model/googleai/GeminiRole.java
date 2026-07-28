/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.googleai;

enum GeminiRole {
    USER,
    MODEL;


    public String toString() {
        return this.name().toLowerCase();
    }
}

