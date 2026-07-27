/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agent.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.PARAMETER})
public @interface P {
    public static final String NO_DEFAULT = "\u0000__LANGCHAIN4J_NO_DEFAULT__\u0000";

    public String name() default "";

    public String description() default "";

    public String value() default "";

    public boolean required() default true;

    public String defaultValue() default "\u0000__LANGCHAIN4J_NO_DEFAULT__\u0000";
}

