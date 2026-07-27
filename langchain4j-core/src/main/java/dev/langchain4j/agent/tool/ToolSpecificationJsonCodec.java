/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agent.tool;

public interface ToolSpecificationJsonCodec {
    public String toJson(Object var1);

    public <T> T fromJson(String var1, Class<T> var2);
}

