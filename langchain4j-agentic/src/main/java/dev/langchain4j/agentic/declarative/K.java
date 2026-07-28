/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.declarative;

import dev.langchain4j.agentic.declarative.TypedKey;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.PARAMETER})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface K {
    public Class<? extends TypedKey<?>> value();
}

