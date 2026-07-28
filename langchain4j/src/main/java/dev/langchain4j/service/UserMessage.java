/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.PARAMETER})
public @interface UserMessage {
    public String[] value() default {""};

    public String delimiter() default "\n";

    public String fromResource() default "";
}

