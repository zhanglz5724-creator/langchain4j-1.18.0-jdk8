package dev.langchain4j.agentic.planner;

import dev.langchain4j.agentic.internal.AgentUtil;
import java.lang.reflect.Type;
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

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public boolean getIsOptional() {
        return isOptional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentArgument that = (AgentArgument) o;
        return java.util.Objects.equals(this.type, that.type) && java.util.Objects.equals(this.name, that.name) && java.util.Objects.equals(this.defaultValue, that.defaultValue) && java.util.Objects.equals(this.isOptional, that.isOptional);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, name, defaultValue, isOptional);
    }

    @Override
    public String toString() {
        return "AgentArgument{"type=" + type + , "name=" + name + , "defaultValue=" + defaultValue + , "isOptional=" + isOptional + "}"";
    }


    public AgentArgument(Type type, String name) {
        this(type, name, null);
    }

    public AgentArgument(Type type, String name, Object defaultValue) {
        this(type, name, defaultValue, false);
    }

    public Class<?> rawType() {
        return AgentUtil.rawType(type);
    }
}
