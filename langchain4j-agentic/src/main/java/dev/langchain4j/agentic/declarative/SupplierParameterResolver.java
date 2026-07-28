/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.declarative;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface SupplierParameterResolver {
    public boolean supports(Context var1);

    public Object resolve(Context var1);

    public static interface Context {
        public Class<?> declaringAgentClass();

        public Method supplierMethod();

        public Parameter parameter();
    }
}

