/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.spi;

import dev.langchain4j.Internal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;

@Internal
public class ServiceHelper {
    private ServiceHelper() {
    }

    public static <T> T loadFactory(Class<T> clazz) {
        Collection<T> factories = ServiceHelper.loadFactories(clazz, null);
        return factories.isEmpty() ? null : (T)factories.iterator().next();
    }

    public static <T> Collection<T> loadFactories(Class<T> clazz) {
        return ServiceHelper.loadFactories(clazz, null);
    }

    public static <T> Collection<T> loadFactories(Class<T> clazz, ClassLoader classLoader) {
        List<T> result = classLoader != null ? ServiceHelper.loadAll(ServiceLoader.load(clazz, classLoader)) : ServiceHelper.loadAll(ServiceLoader.load(clazz));
        if (result.isEmpty()) {
            result = ServiceHelper.loadAll(ServiceLoader.load(clazz, ServiceHelper.class.getClassLoader()));
        }
        return result;
    }

    private static <T> List<T> loadAll(ServiceLoader<T> loader) {
        ArrayList list = new ArrayList();
        loader.iterator().forEachRemaining(list::add);
        return list;
    }
}

