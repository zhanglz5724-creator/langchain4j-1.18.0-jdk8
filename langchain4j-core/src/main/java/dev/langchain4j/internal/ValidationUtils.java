/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Internal
public class ValidationUtils {
    private ValidationUtils() {
    }

    public static void ensureEq(Object lhs, Object rhs, String format, Object ... args) {
        if (!Objects.equals(lhs, rhs)) {
            throw Exceptions.illegalArgument(format, args);
        }
    }

    public static <T> T ensureNotNull(T object, String name) {
        return ValidationUtils.ensureNotNull(object, "%s cannot be null", name);
    }

    public static <T> T ensureNotNull(T object, String format, Object ... args) {
        if (object == null) {
            throw Exceptions.illegalArgument(format, args);
        }
        return object;
    }

    public static <T extends Collection<?>> T ensureNotEmpty(T collection, String name) {
        if (Utils.isNullOrEmpty(collection)) {
            throw Exceptions.illegalArgument("%s cannot be null or empty", name);
        }
        return collection;
    }

    public static <T> T[] ensureNotEmpty(T[] array, String name) {
        return ValidationUtils.ensureNotEmpty(array, "%s cannot be null or empty", name);
    }

    public static <T> T[] ensureNotEmpty(T[] array, String format, Object ... args) {
        if (array == null || array.length == 0) {
            throw Exceptions.illegalArgument(format, args);
        }
        return array;
    }

    public static <K, V> Map<K, V> ensureNotEmpty(Map<K, V> map, String name) {
        if (Utils.isNullOrEmpty(map)) {
            throw Exceptions.illegalArgument("%s cannot be null or empty", name);
        }
        return map;
    }

    public static String ensureNotEmpty(String string, String name) {
        return ValidationUtils.ensureNotEmpty(string, "%s cannot be null or empty", name);
    }

    public static String ensureNotEmpty(String string, String format, Object ... args) {
        if (Utils.isNullOrEmpty(string)) {
            throw Exceptions.illegalArgument(format, args);
        }
        return string;
    }

    public static String ensureNotBlank(String string, String name) {
        return ValidationUtils.ensureNotBlank(string, "%s cannot be null or blank", name);
    }

    public static String ensureNotBlank(String string, String format, Object ... args) {
        if (Utils.isNullOrBlank(string)) {
            throw Exceptions.illegalArgument(format, args);
        }
        return string;
    }

    public static void ensureTrue(boolean expression, String msg) {
        if (!expression) {
            throw Exceptions.illegalArgument(msg, new Object[0]);
        }
    }

    public static int ensureNotNegative(Integer i, String name) {
        if (i == null || i < 0) {
            throw Exceptions.illegalArgument("%s must not be negative, but is: %s", name, i);
        }
        return i;
    }

    public static int ensureGreaterThanZero(Integer i, String name) {
        if (i == null || i <= 0) {
            throw Exceptions.illegalArgument("%s must be greater than zero, but is: %s", name, i);
        }
        return i;
    }

    public static double ensureGreaterThanZero(Double i, String name) {
        if (i == null || i <= 0.0) {
            throw Exceptions.illegalArgument("%s must be greater than zero, but is: %s", name, i);
        }
        return i;
    }

    public static double ensureBetween(Double d, double min, double max, String name) {
        if (d == null || d < min || d > max) {
            throw Exceptions.illegalArgument("%s must be between %s and %s, but is: %s", name, min, max, d);
        }
        return d;
    }

    public static int ensureBetween(Integer i, int min, int max, String name) {
        if (i == null || i < min || i > max) {
            throw Exceptions.illegalArgument("%s must be between %s and %s, but is: %s", name, min, max, i);
        }
        return i;
    }

    public static long ensureBetween(Long i, long min, long max, String name) {
        if (i == null || i < min || i > max) {
            throw Exceptions.illegalArgument("%s must be between %s and %s, but is: %s", name, min, max, i);
        }
        return i;
    }

    public static Integer ensureGreaterThanZeroIfNotNull(Integer i, String name) {
        if (i != null && i <= 0) {
            throw Exceptions.illegalArgument("%s must be greater than zero, but is: %s", name, i);
        }
        return i;
    }
}

