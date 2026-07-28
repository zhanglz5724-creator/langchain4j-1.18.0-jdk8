/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.http.client;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpClientBuilderLoader {
    public static HttpClientBuilder loadHttpClientBuilder() {
        String selectedClassName = System.getProperty("langchain4j.http.clientBuilderFactory");
        HttpClientBuilderFactory effectiveFactory = null;
        Collection<HttpClientBuilderFactory> factories = ServiceHelper.loadFactories(HttpClientBuilderFactory.class);
        for (HttpClientBuilderFactory factory : factories) {
            if (effectiveFactory != null) {
                throw new IllegalStateException(String.format("Conflict: multiple HTTP clients have been found in the classpath: %s. Please explicitly specify the one you wish to use using the `langchain4j.http.clientBuilderFactory` system property.", HttpClientBuilderLoader.factoryNames(factories)));
            }
            if (selectedClassName == null) {
                effectiveFactory = factory;
                continue;
            }
            if (!selectedClassName.equals(factory.getClass().getName())) continue;
            effectiveFactory = factory;
            break;
        }
        if (effectiveFactory == null) {
            if (selectedClassName == null || factories.isEmpty()) {
                throw new IllegalStateException("No HTTP client has been found in the classpath");
            }
            throw new IllegalStateException(String.format("The value of the `langchain4j.http.clientBuilderFactory` system property does not match any of the available HTTP Clients in the classpath: %s.", HttpClientBuilderLoader.factoryNames(factories)));
        }
        return effectiveFactory.create();
    }

    private static Set<String> factoryNames(Collection<HttpClientBuilderFactory> factories) {
        return factories.stream().map(f -> f.getClass().getName()).collect(Collectors.toSet());
    }
}

