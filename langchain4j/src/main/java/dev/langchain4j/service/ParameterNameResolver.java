/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.service;

import dev.langchain4j.service.V;
import java.lang.reflect.Parameter;
import java.util.Iterator;
import java.util.ServiceLoader;

public interface ParameterNameResolver {
    public boolean hasVariableName(Parameter var1);

    public String getVariableName(Parameter var1);

    public static String name(Parameter parameter) {
        return Holder.RESOLVER.getVariableName(parameter);
    }

    public static boolean hasName(Parameter parameter) {
        return Holder.RESOLVER.hasVariableName(parameter);
    }

    public static class DefaultParameterNameResolver
    implements ParameterNameResolver {
        @Override
        public boolean hasVariableName(Parameter parameter) {
            return parameter.getAnnotation(V.class) != null;
        }

        @Override
        public String getVariableName(Parameter parameter) {
            V annotation = parameter.getAnnotation(V.class);
            if (annotation != null) {
                return annotation.value();
            }
            return parameter.getName();
        }
    }

    public static class Holder {
        public static final ParameterNameResolver RESOLVER = Holder.findResolver();

        private Holder() {
        }

        static ParameterNameResolver findResolver() {
            Iterator<ParameterNameResolver> iterator = ServiceLoader.load(ParameterNameResolver.class).iterator();
            if (iterator.hasNext()) {
                ParameterNameResolver resolver = iterator.next();
                return resolver;
            }
            return new DefaultParameterNameResolver();
        }
    }
}

