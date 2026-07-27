/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agent.tool;

import dev.langchain4j.Experimental;

@Experimental
public enum ReturnBehavior {
    TO_LLM,
    IMMEDIATE,
    IMMEDIATE_IF_LAST;

}

