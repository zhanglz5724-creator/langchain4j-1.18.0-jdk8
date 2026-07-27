/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.data.document;

import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.ValidationUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public class Metadata {
    private static final Set<Class<?>> SUPPORTED_VALUE_TYPES = new LinkedHashSet();
    private final Map<String, Object> metadata;

    public Metadata() {
        this.metadata = new HashMap<String, Object>();
    }

    public Metadata(Map<String, ?> metadata) {
        Metadata.validate(metadata);
        this.metadata = new HashMap(metadata);
    }

    private static void validate(Map<String, ?> metadata) {
        ValidationUtils.ensureNotNull(metadata, "metadata").forEach((key, value) -> {
            Metadata.validate(key, value);
            if (!SUPPORTED_VALUE_TYPES.contains(value.getClass())) {
                throw Exceptions.illegalArgument("The metadata key '%s' has the value '%s', which is of the unsupported type '%s'. Currently, the supported types are: %s", key, value, value.getClass().getName(), SUPPORTED_VALUE_TYPES);
            }
        });
    }

    private static void validate(String key, Object value) {
        ValidationUtils.ensureNotBlank(key, "The metadata key with the value '" + value + "'");
        ValidationUtils.ensureNotNull(value, "The metadata value for the key '" + key + "'");
    }

    public @Nullable String getString(String key) {
        if (!this.containsKey(key)) {
            return null;
        }
        Object value = this.metadata.get(key);
        if (value instanceof String) {
            return (String)value;
        }
        throw Exceptions.runtime("Metadata entry with the key '%s' has a value of '%s' and type '%s'. It cannot be returned as a String.", key, value, value.getClass().getName());
    }

    public @Nullable UUID getUUID(String key) {
        if (!this.containsKey(key)) {
            return null;
        }
        Object value = this.metadata.get(key);
        if (value instanceof UUID) {
            return (UUID)value;
        }
        if (value instanceof String) {
            return UUID.fromString((String)value);
        }
        throw Exceptions.runtime("Metadata entry with the key '%s' has a value of '%s' and type '%s'. It cannot be returned as a UUID.", key, value, value.getClass().getName());
    }

    public @Nullable Integer getInteger(String key) {
        if (!this.containsKey(key)) {
            return null;
        }
        Object value = this.metadata.get(key);
        if (value instanceof String) {
            return Integer.parseInt(value.toString());
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        throw Exceptions.runtime("Metadata entry with the key '%s' has a value of '%s' and type '%s'. It cannot be returned as an Integer.", key, value, value.getClass().getName());
    }

    public @Nullable Long getLong(String key) {
        if (!this.containsKey(key)) {
            return null;
        }
        Object value = this.metadata.get(key);
        if (value instanceof String) {
            return Long.parseLong(value.toString());
        }
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        throw Exceptions.runtime("Metadata entry with the key '%s' has a value of '%s' and type '%s'. It cannot be returned as a Long.", key, value, value.getClass().getName());
    }

    public @Nullable Float getFloat(String key) {
        if (!this.containsKey(key)) {
            return null;
        }
        Object value = this.metadata.get(key);
        if (value instanceof String) {
            return Float.valueOf(Float.parseFloat(value.toString()));
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number)value).floatValue());
        }
        throw Exceptions.runtime("Metadata entry with the key '%s' has a value of '%s' and type '%s'. It cannot be returned as a Float.", key, value, value.getClass().getName());
    }

    public @Nullable Double getDouble(String key) {
        if (!this.containsKey(key)) {
            return null;
        }
        Object value = this.metadata.get(key);
        if (value instanceof String) {
            return Double.parseDouble(value.toString());
        }
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        throw Exceptions.runtime("Metadata entry with the key '%s' has a value of '%s' and type '%s'. It cannot be returned as a Double.", key, value, value.getClass().getName());
    }

    public boolean containsKey(String key) {
        return this.metadata.containsKey(key);
    }

    public Metadata put(String key, String value) {
        Metadata.validate(key, value);
        this.metadata.put(key, value);
        return this;
    }

    public Metadata put(String key, UUID value) {
        Metadata.validate(key, value);
        this.metadata.put(key, value);
        return this;
    }

    public Metadata put(String key, int value) {
        Metadata.validate(key, value);
        this.metadata.put(key, value);
        return this;
    }

    public Metadata put(String key, long value) {
        Metadata.validate(key, value);
        this.metadata.put(key, value);
        return this;
    }

    public Metadata put(String key, float value) {
        Metadata.validate(key, Float.valueOf(value));
        this.metadata.put(key, Float.valueOf(value));
        return this;
    }

    public Metadata put(String key, double value) {
        Metadata.validate(key, value);
        this.metadata.put(key, value);
        return this;
    }

    public Metadata putAll(Map<String, Object> metadata) {
        Metadata.validate(metadata);
        this.metadata.putAll(metadata);
        return this;
    }

    public Metadata remove(String key) {
        this.metadata.remove(key);
        return this;
    }

    public Metadata copy() {
        return new Metadata(this.metadata);
    }

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>(this.metadata);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Metadata that = (Metadata)o;
        return Objects.equals(this.metadata, that.metadata);
    }

    public int hashCode() {
        return Objects.hash(this.metadata);
    }

    public String toString() {
        return "Metadata { metadata = " + this.metadata + " }";
    }

    public static Metadata from(String key, String value) {
        return new Metadata().put(key, value);
    }

    public static Metadata from(Map<String, ?> metadata) {
        return new Metadata(metadata);
    }

    public static Metadata metadata(String key, String value) {
        return Metadata.from(key, value);
    }

    public Metadata merge(@Nullable Metadata another) {
        if (another == null || another.metadata.isEmpty()) {
            return this.copy();
        }
        Map<String, Object> thisMap = this.toMap();
        Map<String, Object> anotherMap = another.toMap();
        HashSet<String> commonKeys = new HashSet<String>(thisMap.keySet());
        commonKeys.retainAll(anotherMap.keySet());
        if (!commonKeys.isEmpty()) {
            throw Exceptions.illegalArgument("Metadata keys are not unique. Common keys: %s", commonKeys);
        }
        HashMap<String, Object> mergedMap = new HashMap<String, Object>(thisMap);
        mergedMap.putAll(anotherMap);
        return Metadata.from(mergedMap);
    }

    static {
        SUPPORTED_VALUE_TYPES.add(String.class);
        SUPPORTED_VALUE_TYPES.add(UUID.class);
        SUPPORTED_VALUE_TYPES.add(Integer.TYPE);
        SUPPORTED_VALUE_TYPES.add(Integer.class);
        SUPPORTED_VALUE_TYPES.add(Long.TYPE);
        SUPPORTED_VALUE_TYPES.add(Long.class);
        SUPPORTED_VALUE_TYPES.add(Float.TYPE);
        SUPPORTED_VALUE_TYPES.add(Float.class);
        SUPPORTED_VALUE_TYPES.add(Double.TYPE);
        SUPPORTED_VALUE_TYPES.add(Double.class);
    }
}

