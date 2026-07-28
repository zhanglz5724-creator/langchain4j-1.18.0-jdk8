/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.http.client.HttpClientBuilder
 *  dev.langchain4j.internal.ChatRequestValidationUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  org.slf4j.Logger
 */
package dev.langchain4j.model.ollama;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.internal.ChatRequestValidationUtils;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.ollama.InternalOllamaHelper;
import dev.langchain4j.model.ollama.OllamaChatRequestParameters;
import dev.langchain4j.model.ollama.OllamaClient;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.slf4j.Logger;

abstract class OllamaBaseChatModel {
    protected OllamaClient client;
    protected OllamaChatRequestParameters defaultRequestParameters;
    protected boolean returnThinking;
    protected List<ChatModelListener> listeners;
    protected Set<Capability> supportedCapabilities;

    OllamaBaseChatModel() {
    }

    void init(Builder<? extends OllamaBaseChatModel, ? extends Builder<?, ?>> builder) {
        ChatRequestParameters commonParameters;
        this.client = OllamaClient.builder().httpClientBuilder(builder.httpClientBuilder).baseUrl(builder.baseUrl).timeout(builder.timeout).customHeaders(builder.customHeadersSupplier).logRequests(builder.logRequests).logResponses(builder.logResponses).logger(builder.logger).build();
        if (builder.defaultRequestParameters != null) {
            this.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        OllamaChatRequestParameters ollamaParameters = builder.defaultRequestParameters instanceof OllamaChatRequestParameters ? (OllamaChatRequestParameters)builder.defaultRequestParameters : OllamaChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((OllamaChatRequestParameters.Builder)((OllamaChatRequestParameters.Builder)((OllamaChatRequestParameters.Builder)((OllamaChatRequestParameters.Builder)((OllamaChatRequestParameters.Builder)((OllamaChatRequestParameters.Builder)((OllamaChatRequestParameters.Builder)((OllamaChatRequestParameters.Builder)OllamaChatRequestParameters.builder().modelName((String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName()))).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).topK((Integer)Utils.getOrDefault((Object)builder.topK, (Object)commonParameters.topK()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.numPredict, (Object)commonParameters.maxOutputTokens()))).stopSequences(Utils.getOrDefault(builder.stop, (List)commonParameters.stopSequences()))).toolSpecifications(commonParameters.toolSpecifications())).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).mirostat((Integer)Utils.getOrDefault((Object)builder.mirostat, (Object)ollamaParameters.mirostat())).mirostatEta((Double)Utils.getOrDefault((Object)builder.mirostatEta, (Object)ollamaParameters.mirostatEta())).mirostatTau((Double)Utils.getOrDefault((Object)builder.mirostatTau, (Object)ollamaParameters.mirostatTau())).numCtx((Integer)Utils.getOrDefault((Object)builder.numCtx, (Object)ollamaParameters.numCtx())).repeatLastN((Integer)Utils.getOrDefault((Object)builder.repeatLastN, (Object)ollamaParameters.repeatLastN())).repeatPenalty((Double)Utils.getOrDefault((Object)builder.repeatPenalty, (Object)ollamaParameters.repeatPenalty())).seed((Integer)Utils.getOrDefault((Object)builder.seed, (Object)ollamaParameters.seed())).minP((Double)Utils.getOrDefault((Object)builder.minP, (Object)ollamaParameters.minP())).keepAlive(ollamaParameters.keepAlive()).think((Boolean)Utils.getOrDefault((Object)builder.think, (Object)ollamaParameters.think())).numThread(ollamaParameters.numThread()).numKeep(ollamaParameters.numKeep()).typicalP(ollamaParameters.typicalP()).numBatch(ollamaParameters.numBatch()).numGPU(ollamaParameters.numGPU()).mainGPU(ollamaParameters.mainGPU()).useMmap(ollamaParameters.useMmap()).build();
        this.returnThinking = (Boolean)Utils.getOrDefault((Object)builder.returnThinking, (Object)false);
        this.listeners = Utils.copy(builder.listeners);
        this.supportedCapabilities = Utils.copy(builder.supportedCapabilities);
    }

    protected void validate(ChatRequestParameters chatRequestParameters) {
        InternalOllamaHelper.validate(chatRequestParameters);
        ChatRequestValidationUtils.validate((ToolChoice)chatRequestParameters.toolChoice());
    }

    protected static abstract class Builder<C extends OllamaBaseChatModel, B extends Builder<C, B>> {
        protected HttpClientBuilder httpClientBuilder;
        protected String baseUrl;
        protected ChatRequestParameters defaultRequestParameters;
        protected String modelName;
        protected Double temperature;
        protected Integer topK;
        protected Double topP;
        protected Integer mirostat;
        protected Double mirostatEta;
        protected Double mirostatTau;
        protected Integer numCtx;
        protected Integer repeatLastN;
        protected Double repeatPenalty;
        protected Integer seed;
        protected Integer numPredict;
        protected List<String> stop;
        protected Double minP;
        protected ResponseFormat responseFormat;
        protected Boolean think;
        protected Boolean returnThinking;
        protected Duration timeout;
        protected Supplier<Map<String, String>> customHeadersSupplier;
        protected Boolean logRequests;
        protected Boolean logResponses;
        protected Logger logger;
        protected List<ChatModelListener> listeners;
        protected Set<Capability> supportedCapabilities;

        protected Builder() {
        }

        protected B self() {
            return (B)this;
        }

        public B httpClientBuilder(HttpClientBuilder httpClientBuilder) {
            this.httpClientBuilder = httpClientBuilder;
            return this.self();
        }

        public B baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this.self();
        }

        public B defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this.self();
        }

        public B modelName(String modelName) {
            this.modelName = modelName;
            return this.self();
        }

        public B temperature(Double temperature) {
            this.temperature = temperature;
            return this.self();
        }

        public B topK(Integer topK) {
            this.topK = topK;
            return this.self();
        }

        public B topP(Double topP) {
            this.topP = topP;
            return this.self();
        }

        public B mirostat(Integer mirostat) {
            this.mirostat = mirostat;
            return this.self();
        }

        public B mirostatEta(Double mirostatEta) {
            this.mirostatEta = mirostatEta;
            return this.self();
        }

        public B mirostatTau(Double mirostatTau) {
            this.mirostatTau = mirostatTau;
            return this.self();
        }

        public B repeatLastN(Integer repeatLastN) {
            this.repeatLastN = repeatLastN;
            return this.self();
        }

        public B repeatPenalty(Double repeatPenalty) {
            this.repeatPenalty = repeatPenalty;
            return this.self();
        }

        public B seed(Integer seed) {
            this.seed = seed;
            return this.self();
        }

        public B numPredict(Integer numPredict) {
            this.numPredict = numPredict;
            return this.self();
        }

        public B numCtx(Integer numCtx) {
            this.numCtx = numCtx;
            return this.self();
        }

        public B stop(List<String> stop) {
            this.stop = stop;
            return this.self();
        }

        public B minP(Double minP) {
            this.minP = minP;
            return this.self();
        }

        public B responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return this.self();
        }

        public B think(Boolean think) {
            this.think = think;
            return this.self();
        }

        public B returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this.self();
        }

        public B timeout(Duration timeout) {
            this.timeout = timeout;
            return this.self();
        }

        public B customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this.self();
        }

        public B customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this.self();
        }

        public B logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this.self();
        }

        public B logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this.self();
        }

        public B logger(Logger logger) {
            this.logger = logger;
            return this.self();
        }

        public B listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this.self();
        }

        public B supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
            return this.self();
        }

        public B supportedCapabilities(Capability ... supportedCapabilities) {
            return this.supportedCapabilities(new HashSet<Capability>(Arrays.asList(supportedCapabilities)));
        }

        public abstract C build();
    }
}

