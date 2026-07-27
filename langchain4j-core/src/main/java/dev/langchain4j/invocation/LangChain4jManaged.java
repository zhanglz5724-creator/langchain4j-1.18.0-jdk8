/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.invocation;

import dev.langchain4j.Internal;
import java.util.Map;

@Internal
public interface LangChain4jManaged {
    public static final ThreadLocal<Map<Class<? extends LangChain4jManaged>, LangChain4jManaged>> CURRENT = new ThreadLocal();

    public static void setCurrent(Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> current) {
        CURRENT.set(current);
    }

    public static Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> current() {
        return CURRENT.get();
    }

    public static <T extends LangChain4jManaged> T current(Class<T> clazz) {
        Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> current = CURRENT.get();
        return (T)(current != null ? (LangChain4jManaged)clazz.cast(current.get(clazz)) : null);
    }

    public static void removeCurrent() {
        CURRENT.remove();
    }
}

