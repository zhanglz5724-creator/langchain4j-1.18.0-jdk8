/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.internal.AgentUtil;
import java.lang.reflect.Type;
import java.util.Objects;

public class AgentArgument {
    private final Type type;
    private final String name;
    private final Object defaultValue;
    private final boolean isOptional;

    public AgentArgument(Type type, String name, Object defaultValue, boolean isOptional) {
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
        this.isOptional = isOptional;
    }

    public Type type() {
        return this.type;
    }

    public String name() {
        return this.name;
    }

    public Object defaultValue() {
        return this.defaultValue;
    }

    public boolean isOptional() {
        return this.isOptional;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentArgument)) {
            return false;
        }
        AgentArgument other = (AgentArgument)o;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.defaultValue, other.defaultValue)) {
            return false;
        }
        return this.isOptional == other.isOptional;
    }

    public int hashCode() {
        return Objects.hash(this.type, this.name, this.defaultValue, this.isOptional);
    }

    public String toString() {
        return "AgentArgument{type=" + this.type + ", name=" + this.name + ", defaultValue=" + this.defaultValue + ", isOptional=" + this.isOptional + "}";
    }

    public AgentArgument(Type type, String name) {
        this(type, name, null);
    }

    public AgentArgument(Type type, String name, Object defaultValue) {
        this(type, name, defaultValue, false);
    }

    public Class<?> rawType() {
        return AgentUtil.rawType(this.type);
    }
}

