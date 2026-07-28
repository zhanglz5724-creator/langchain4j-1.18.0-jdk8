/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.guardrail.OutputGuardrail
 */
package dev.langchain4j.service.guardrail;

import dev.langchain4j.guardrail.OutputGuardrail;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.METHOD})
public @interface OutputGuardrails {
    public Class<? extends OutputGuardrail>[] value();

    public int maxRetries() default 2;
}

