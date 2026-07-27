/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agent.tool;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.SearchBehavior;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Tool {
    public String name() default "";

    public String[] value() default {""};

    @Experimental
    public ReturnBehavior returnBehavior() default ReturnBehavior.TO_LLM;

    @Experimental
    public SearchBehavior searchBehavior() default SearchBehavior.SEARCHABLE;

    @Experimental
    public String metadata() default "{}";
}

