/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agent.tool;

import dev.langchain4j.Experimental;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Experimental
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface CompensateFor {
    public String value();
}

