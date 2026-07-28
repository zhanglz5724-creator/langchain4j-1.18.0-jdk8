/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.spi.services;

import dev.langchain4j.Internal;
import dev.langchain4j.service.AiServiceContext;
import dev.langchain4j.service.AiServices;

@Internal
public interface AiServicesFactory {
    public <T> AiServices<T> create(AiServiceContext var1);
}

