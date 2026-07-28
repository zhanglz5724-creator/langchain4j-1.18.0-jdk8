/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.chain;

@FunctionalInterface
public interface Chain<Input, Output> {
    public Output execute(Input var1);
}

