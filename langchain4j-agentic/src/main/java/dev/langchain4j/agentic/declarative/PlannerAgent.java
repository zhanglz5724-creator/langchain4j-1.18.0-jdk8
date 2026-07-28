/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.declarative;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.declarative.TypedKey;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface PlannerAgent {
    public String name() default "";

    public String description() default "";

    public String outputKey() default "";

    public Class<? extends TypedKey<?>> typedOutputKey() default Agent.NoTypedKey.class;

    public Class<?>[] subAgents();
}

