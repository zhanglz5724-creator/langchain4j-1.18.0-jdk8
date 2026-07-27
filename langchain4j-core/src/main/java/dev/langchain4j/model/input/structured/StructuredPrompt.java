/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.input.structured;

import dev.langchain4j.internal.ValidationUtils;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface StructuredPrompt {
    public String[] value();

    public String delimiter() default "\n";

    public static class Util {
        private Util() {
        }

        public static StructuredPrompt validateStructuredPrompt(Object structuredPrompt) {
            ValidationUtils.ensureNotNull(structuredPrompt, "structuredPrompt");
            Class<?> cls = structuredPrompt.getClass();
            return ValidationUtils.ensureNotNull(cls.getAnnotation(StructuredPrompt.class), "%s should be annotated with @StructuredPrompt to be used as a structured prompt", cls.getName());
        }

        public static String join(StructuredPrompt structuredPrompt) {
            return String.join((CharSequence)structuredPrompt.delimiter(), structuredPrompt.value());
        }
    }
}

