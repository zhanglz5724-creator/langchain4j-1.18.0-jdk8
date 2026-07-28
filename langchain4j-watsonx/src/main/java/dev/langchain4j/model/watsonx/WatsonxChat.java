/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.watsonx.ai.chat.ChatProvider
 *  com.ibm.watsonx.ai.chat.ChatService
 *  com.ibm.watsonx.ai.chat.ChatService$Builder
 *  com.ibm.watsonx.ai.chat.model.ExtractionTags
 *  com.ibm.watsonx.ai.chat.model.Thinking
 *  com.ibm.watsonx.ai.chat.model.ThinkingEffort
 *  com.ibm.watsonx.ai.deployment.DeploymentService
 *  com.ibm.watsonx.ai.deployment.DeploymentService$Builder
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ToolChoice
 */
package dev.langchain4j.model.watsonx;

import com.ibm.watsonx.ai.chat.ChatProvider;
import com.ibm.watsonx.ai.chat.ChatService;
import com.ibm.watsonx.ai.chat.model.ExtractionTags;
import com.ibm.watsonx.ai.chat.model.Thinking;
import com.ibm.watsonx.ai.chat.model.ThinkingEffort;
import com.ibm.watsonx.ai.deployment.DeploymentService;
import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.watsonx.WatsonxBuilder;
import dev.langchain4j.model.watsonx.WatsonxChatRequestParameters;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Internal
abstract class WatsonxChat {
    protected final ChatProvider chatProvider;
    protected final List<ChatModelListener> listeners;
    protected final ChatRequestParameters defaultRequestParameters;
    protected final Set<Capability> supportedCapabilities;

    protected WatsonxChat(Builder<?> builder) {
        WatsonxChatRequestParameters watsonxChatRequestParameters;
        ChatRequestParameters commonParameters;
        this.listeners = Utils.copy(builder.listeners);
        this.supportedCapabilities = Utils.copy(builder.supportedCapabilities);
        if (builder.defaultRequestParameters != null) {
            this.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        ChatRequestParameters chatRequestParameters = builder.defaultRequestParameters;
        WatsonxChatRequestParameters watsonxParameters = chatRequestParameters instanceof WatsonxChatRequestParameters ? (watsonxChatRequestParameters = (WatsonxChatRequestParameters)chatRequestParameters) : WatsonxChatRequestParameters.EMPTY;
        String modelName = (String)Utils.getOrDefault((Object)builder.modelName, (Object)commonParameters.modelName());
        String projectId = (String)Utils.getOrDefault((Object)builder.projectId, (Object)watsonxParameters.projectId());
        String spaceId = (String)Utils.getOrDefault((Object)builder.spaceId, (Object)watsonxParameters.spaceId());
        Duration timeout = (Duration)Utils.getOrDefault((Object)builder.timeout, (Object)watsonxParameters.timeout());
        Thinking thinking = (Thinking)Utils.getOrDefault((Object)builder.thinking, (Object)watsonxParameters.thinking());
        String deploymentId = (String)Utils.getOrDefault((Object)builder.deploymentId, (Object)watsonxParameters.deploymentId());
        this.defaultRequestParameters = ((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)((WatsonxChatRequestParameters.Builder)WatsonxChatRequestParameters.builder().modelName(modelName)).temperature((Double)Utils.getOrDefault((Object)builder.temperature, (Object)commonParameters.temperature()))).topP((Double)Utils.getOrDefault((Object)builder.topP, (Object)commonParameters.topP()))).frequencyPenalty((Double)Utils.getOrDefault((Object)builder.frequencyPenalty, (Object)commonParameters.frequencyPenalty()))).presencePenalty((Double)Utils.getOrDefault((Object)builder.presencePenalty, (Object)commonParameters.presencePenalty()))).maxOutputTokens((Integer)Utils.getOrDefault((Object)builder.maxOutputTokens, (Object)commonParameters.maxOutputTokens()))).stopSequences(Utils.getOrDefault(builder.stopSequences, (List)commonParameters.stopSequences()))).toolSpecifications(Utils.getOrDefault(builder.toolSpecifications, (List)commonParameters.toolSpecifications()))).toolChoice((ToolChoice)Utils.getOrDefault((Object)builder.toolChoice, (Object)commonParameters.toolChoice()))).responseFormat((ResponseFormat)Utils.getOrDefault((Object)builder.responseFormat, (Object)commonParameters.responseFormat()))).projectId(projectId).spaceId(spaceId).logitBias(Utils.getOrDefault(builder.logitBias, watsonxParameters.logitBias())).logprobs((Boolean)Utils.getOrDefault((Object)builder.logprobs, (Object)watsonxParameters.logprobs())).topLogprobs((Integer)Utils.getOrDefault((Object)builder.topLogprobs, (Object)watsonxParameters.topLogprobs())).seed((Integer)Utils.getOrDefault((Object)builder.seed, (Object)watsonxParameters.seed())).toolChoiceName((String)Utils.getOrDefault((Object)builder.toolChoiceName, (Object)watsonxParameters.toolChoiceName())).timeout(timeout).thinking(thinking).guidedChoice((Set)Utils.getOrDefault(builder.guidedChoice, watsonxParameters.guidedChoice())).guidedGrammar((String)Utils.getOrDefault((Object)builder.guidedGrammar, (Object)watsonxParameters.guidedGrammar())).guidedRegex((String)Utils.getOrDefault((Object)builder.guidedRegex, (Object)watsonxParameters.guidedRegex())).lengthPenalty((Double)Utils.getOrDefault((Object)builder.lengthPenalty, (Object)watsonxParameters.lengthPenalty())).repetitionPenalty((Double)Utils.getOrDefault((Object)builder.repetitionPenalty, (Object)watsonxParameters.repetitionPenalty())).deploymentId(deploymentId).build();
        if (Objects.nonNull(deploymentId)) {
            DeploymentService.Builder deploymentBuilder = Objects.nonNull(builder.authenticator) ? (DeploymentService.Builder)DeploymentService.builder().authenticator(builder.authenticator) : (DeploymentService.Builder)DeploymentService.builder().apiKey(builder.apiKey);
            this.chatProvider = ((DeploymentService.Builder)((DeploymentService.Builder)((DeploymentService.Builder)((DeploymentService.Builder)((DeploymentService.Builder)((DeploymentService.Builder)((DeploymentService.Builder)deploymentBuilder.baseUrl(builder.baseUrl)).version(builder.version)).timeout(timeout)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).httpClient(builder.httpClient)).verifySsl(builder.verifySsl)).build();
        } else {
            ChatService.Builder chatServiceBuilder = Objects.nonNull(builder.authenticator) ? (ChatService.Builder)ChatService.builder().authenticator(builder.authenticator) : (ChatService.Builder)ChatService.builder().apiKey(builder.apiKey);
            this.chatProvider = ((ChatService.Builder)((ChatService.Builder)((ChatService.Builder)((ChatService.Builder)((ChatService.Builder)((ChatService.Builder)((ChatService.Builder)((ChatService.Builder)((ChatService.Builder)((ChatService.Builder)chatServiceBuilder.baseUrl(builder.baseUrl)).modelId(modelName)).version(builder.version)).projectId(projectId)).spaceId(spaceId)).timeout(timeout)).logRequests(builder.logRequests)).logResponses(builder.logResponses)).httpClient(builder.httpClient)).verifySsl(builder.verifySsl)).build();
        }
    }

    final void validate(ChatRequestParameters parameters) {
        if (Objects.nonNull(parameters.topK())) {
            throw new UnsupportedFeatureException("'topK' parameter is not supported by watsonx.ai");
        }
    }

    static abstract class Builder<T extends Builder<T>>
    extends WatsonxBuilder<T> {
        private String modelName;
        private Double temperature;
        private Double topP;
        private Double frequencyPenalty;
        private Double presencePenalty;
        private Integer maxOutputTokens;
        private List<String> stopSequences;
        private ToolChoice toolChoice;
        private ResponseFormat responseFormat;
        private Map<String, Integer> logitBias;
        private Boolean logprobs;
        private Integer topLogprobs;
        private Integer seed;
        private String toolChoiceName;
        private List<ToolSpecification> toolSpecifications;
        private List<ChatModelListener> listeners;
        private ChatRequestParameters defaultRequestParameters;
        private Set<Capability> supportedCapabilities;
        private Set<String> guidedChoice;
        private String guidedRegex;
        private String guidedGrammar;
        private Double repetitionPenalty;
        private Double lengthPenalty;
        private String deploymentId;
        private Thinking thinking;

        Builder() {
        }

        public T modelName(String modelName) {
            this.modelName = modelName;
            return (T)this;
        }

        public T temperature(Double temperature) {
            this.temperature = temperature;
            return (T)this;
        }

        public T topP(Double topP) {
            this.topP = topP;
            return (T)this;
        }

        public T frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return (T)this;
        }

        public T presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return (T)this;
        }

        public T maxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
            return (T)this;
        }

        public T stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return (T)this;
        }

        public T stopSequences(String ... stopSequences) {
            return this.stopSequences(Arrays.asList(stopSequences));
        }

        public T toolChoice(ToolChoice toolChoice) {
            this.toolChoice = toolChoice;
            return (T)this;
        }

        public T responseFormat(ResponseFormat responseFormat) {
            this.responseFormat = responseFormat;
            return (T)this;
        }

        public T logitBias(Map<String, Integer> logitBias) {
            this.logitBias = logitBias;
            return (T)this;
        }

        public T logprobs(Boolean logprobs) {
            this.logprobs = logprobs;
            return (T)this;
        }

        public T topLogprobs(Integer topLogprobs) {
            this.topLogprobs = topLogprobs;
            return (T)this;
        }

        public T seed(Integer seed) {
            this.seed = seed;
            return (T)this;
        }

        public T toolChoiceName(String toolChoiceName) {
            this.toolChoiceName = toolChoiceName;
            return (T)this;
        }

        public T supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
            return (T)this;
        }

        public T supportedCapabilities(Capability ... supportedCapabilities) {
            return this.supportedCapabilities(new HashSet<Capability>(Arrays.asList(supportedCapabilities)));
        }

        public T toolSpecifications(List<ToolSpecification> toolSpecifications) {
            this.toolSpecifications = toolSpecifications;
            return (T)this;
        }

        public T toolSpecifications(ToolSpecification ... toolSpecifications) {
            return this.toolSpecifications(Arrays.asList(toolSpecifications));
        }

        public T listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return (T)this;
        }

        public T defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return (T)this;
        }

        public T deploymentId(String deploymentId) {
            this.deploymentId = deploymentId;
            return (T)this;
        }

        public T thinking(boolean enabled) {
            return this.thinking(Thinking.builder().enabled(Boolean.valueOf(enabled)).build());
        }

        public T thinking(ExtractionTags tags) {
            if (Objects.nonNull(tags)) {
                return this.thinking(Thinking.of((ExtractionTags)tags));
            }
            this.thinking = null;
            return (T)this;
        }

        public T thinking(ThinkingEffort thinkingEffort) {
            if (Objects.nonNull(thinkingEffort)) {
                return this.thinking(Thinking.of((ThinkingEffort)thinkingEffort));
            }
            this.thinking = null;
            return (T)this;
        }

        public T thinking(Thinking thinking) {
            this.thinking = thinking;
            return (T)this;
        }

        public T guidedChoice(String ... guidedChoice) {
            return this.guidedChoice(Set.of((Object[])guidedChoice));
        }

        public T guidedChoice(Set<String> guidedChoices) {
            this.guidedChoice = guidedChoices;
            return (T)this;
        }

        public T guidedRegex(String guidedRegex) {
            this.guidedRegex = guidedRegex;
            return (T)this;
        }

        public T guidedGrammar(String guidedGrammar) {
            this.guidedGrammar = guidedGrammar;
            return (T)this;
        }

        public T repetitionPenalty(Double repetitionPenalty) {
            this.repetitionPenalty = repetitionPenalty;
            return (T)this;
        }

        public T lengthPenalty(Double lengthPenalty) {
            this.lengthPenalty = lengthPenalty;
            return (T)this;
        }
    }
}

