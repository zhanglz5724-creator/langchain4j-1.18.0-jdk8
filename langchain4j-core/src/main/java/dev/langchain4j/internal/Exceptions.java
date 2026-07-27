/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import java.util.concurrent.Callable;

@Internal
public class Exceptions {
    private Exceptions() {
    }

    public static IllegalArgumentException illegalArgument(String format, Object ... args) {
        return new IllegalArgumentException(String.format(format, args));
    }

    public static RuntimeException runtime(String format, Object ... args) {
        return new RuntimeException(String.format(format, args));
    }

    public static Throwable unwrapRuntimeException(Exception e) {
        if (e.getClass() == RuntimeException.class && e.getCause() != null) {
            return e.getCause();
        }
        return e;
    }

    public static <T> T unchecked(Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            }
            throw new RuntimeException(e);
        }
    }
}

