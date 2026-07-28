/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.spi.ServiceHelper
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.ovhai.internal.client;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.ovhai.internal.client.DefaultOvhAiClient;
import dev.langchain4j.model.ovhai.internal.client.OvhAiClientBuilderFactory;
import dev.langchain4j.spi.ServiceHelper;
import java.time.Duration;
import java.util.Iterator;
import org.slf4j.Logger;

@Deprecated
public abstract class OvhAiClient {
    public static Builder builder() {
        Iterator iterator = ServiceHelper.loadFactories(OvhAiClientBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OvhAiClientBuilderFactory factory = (OvhAiClientBuilderFactory)iterator.next();
            return (Builder)factory.get();
        }
        return DefaultOvhAiClient.builder();
    }

    public static abstract class Builder<T extends OvhAiClient, B extends Builder<T, B>> {
        public String baseUrl;
        public String apiKey;
        public Duration timeout;
        public Boolean logRequests;
        public Boolean logResponses;
        public Logger logger;

        public abstract T build();

        public B baseUrl(String baseUrl) {
            ValidationUtils.ensureNotBlank((String)baseUrl, (String)"baseUrl");
            this.baseUrl = baseUrl;
            return (B)this;
        }

        public B apiKey(String apiKey) {
            ValidationUtils.ensureNotBlank((String)apiKey, (String)"%s", (Object[])new Object[]{"OVHcloud API key must be defined. It can be generated here: https://endpoints.ai.cloud.ovh.net/"});
            this.apiKey = apiKey;
            return (B)this;
        }

        public B timeout(Duration timeout) {
            ValidationUtils.ensureNotNull((Object)timeout, (String)"timeout");
            this.timeout = timeout;
            return (B)this;
        }

        public B logRequests() {
            return this.logRequests(true);
        }

        public B logRequests(Boolean logRequests) {
            if (logRequests == null) {
                logRequests = false;
            }
            this.logRequests = logRequests;
            return (B)this;
        }

        public B logResponses() {
            return this.logResponses(true);
        }

        public B logResponses(Boolean logResponses) {
            if (logResponses == null) {
                logResponses = false;
            }
            this.logResponses = logResponses;
            return (B)this;
        }

        public B logger(Logger logger) {
            this.logger = logger;
            return (B)this;
        }
    }
}

