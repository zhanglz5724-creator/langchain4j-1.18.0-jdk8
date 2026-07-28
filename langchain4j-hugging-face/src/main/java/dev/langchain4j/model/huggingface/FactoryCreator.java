/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.spi.ServiceHelper
 */
package dev.langchain4j.model.huggingface;

import dev.langchain4j.model.huggingface.DefaultHuggingFaceClient;
import dev.langchain4j.model.huggingface.client.HuggingFaceClient;
import dev.langchain4j.model.huggingface.spi.HuggingFaceClientFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.util.Iterator;

class FactoryCreator {
    static final HuggingFaceClientFactory FACTORY = FactoryCreator.factory();

    FactoryCreator() {
    }

    private static HuggingFaceClientFactory factory() {
        Iterator iterator = ServiceHelper.loadFactories(HuggingFaceClientFactory.class).iterator();
        if (iterator.hasNext()) {
            HuggingFaceClientFactory factory = (HuggingFaceClientFactory)iterator.next();
            return factory;
        }
        return new DefaultHuggingFaceClientFactory();
    }

    static class DefaultHuggingFaceClientFactory
    implements HuggingFaceClientFactory {
        DefaultHuggingFaceClientFactory() {
        }

        @Override
        public HuggingFaceClient create(HuggingFaceClientFactory.Input input) {
            return new DefaultHuggingFaceClient(input.httpClientBuilder(), input.baseUrl(), input.apiKey(), input.modelId(), input.timeout());
        }
    }
}

