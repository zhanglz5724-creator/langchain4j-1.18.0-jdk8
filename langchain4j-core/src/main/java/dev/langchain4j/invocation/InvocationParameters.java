/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.invocation;

import dev.langchain4j.internal.ValidationUtils;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InvocationParameters {
    private final ConcurrentHashMap<String, Object> map;

    public InvocationParameters() {
        this.map = new ConcurrentHashMap();
    }

    public InvocationParameters(Map<String, Object> map) {
        ValidationUtils.ensureNotNull(map, "map");
        this.map = new ConcurrentHashMap<String, Object>(map);
    }

    public Map<String, Object> asMap() {
        return this.map;
    }

    public <T> T get(String key) {
        return (T)this.map.get(key);
    }

    public <T> T getOrDefault(String key, T defaultValue) {
        return (T)this.map.getOrDefault(key, defaultValue);
    }

    public <T> void put(String key, T value) {
        this.map.put(key, value);
    }

    public boolean containsKey(String key) {
        return this.map.containsKey(key);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        InvocationParameters that = (InvocationParameters)object;
        return Objects.equals(this.map, that.map);
    }

    public int hashCode() {
        return Objects.hashCode(this.map);
    }

    public String toString() {
        return "InvocationParameters{map=" + this.map + '}';
    }

    public static InvocationParameters from(String key, Object value) {
        return new InvocationParameters(Collections.singletonMap(key, value));
    }

    public static InvocationParameters from(Map<String, Object> map) {
        return new InvocationParameters(map);
    }
}

