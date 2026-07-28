/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic;

import dev.langchain4j.agentic.declarative.TypedKey;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Agent {
    public String name() default "";

    public String value() default "";

    public String description() default "";

    public String outputKey() default "";

    public Class<? extends TypedKey<?>> typedOutputKey() default NoTypedKey.class;

    public boolean async() default false;

    public boolean optional() default false;

    public String[] summarizedContext() default {};

    public static class NoTypedKey
    implements TypedKey<Void> {
    }
}

