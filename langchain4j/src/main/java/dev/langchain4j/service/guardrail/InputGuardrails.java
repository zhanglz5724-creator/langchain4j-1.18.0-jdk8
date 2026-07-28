/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.guardrail.InputGuardrail
 */
package dev.langchain4j.service.guardrail;

import dev.langchain4j.guardrail.InputGuardrail;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Documented
@Target(value={ElementType.TYPE, ElementType.METHOD})
public @interface InputGuardrails {
    public Class<? extends InputGuardrail>[] value();
}

