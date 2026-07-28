/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai.internal.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ServiceLoader;

public class ServiceHelper {
    public static <T> Collection<T> loadFactories(Class<T> clazz) {
        return ServiceHelper.loadFactories(clazz, null);
    }

    public static <T> Collection<T> loadFactories(Class<T> clazz, ClassLoader classLoader) {
        ArrayList list = new ArrayList();
        ServiceLoader<T> factories = classLoader != null ? ServiceLoader.load(clazz, classLoader) : ServiceLoader.load(clazz);
        if (factories.iterator().hasNext()) {
            factories.iterator().forEachRemaining(list::add);
            return list;
        }
        factories = ServiceLoader.load(clazz, ServiceHelper.class.getClassLoader());
        if (factories.iterator().hasNext()) {
            factories.iterator().forEachRemaining(list::add);
            return list;
        }
        return Collections.emptyList();
    }
}

