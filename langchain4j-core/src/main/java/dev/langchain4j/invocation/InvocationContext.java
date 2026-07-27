/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NonNull
 */
package dev.langchain4j.invocation;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.invocation.DefaultInvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.NonNull;

public interface InvocationContext {
    public UUID invocationId();

    public String interfaceName();

    public String methodName();

    public List<Object> methodArguments();

    default public UserMessage userMessage() {
        return null;
    }

    public Object chatMemoryId();

    default public ChatRequestParameters defaultRequestParameters() {
        return null;
    }

    default public ModelProvider modelProvider() {
        return null;
    }

    public InvocationParameters invocationParameters();

    default public Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> managedParameters() {
        return Collections.emptyMap();
    }

    public Instant timestamp();

    default public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID invocationId;
        private String interfaceName;
        private String methodName;
        private List<@NonNull Object> methodArguments = new ArrayList<Object>();
        private UserMessage userMessage;
        private Object chatMemoryId;
        private ChatRequestParameters defaultRequestParameters;
        private ModelProvider modelProvider;
        private InvocationParameters invocationParameters;
        private Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> managedParameters;
        private Instant timestamp;

        protected Builder() {
        }

        protected Builder(InvocationContext invocationContext) {
            this.invocationId(invocationContext.invocationId());
            this.interfaceName(invocationContext.interfaceName());
            this.methodName(invocationContext.methodName());
            this.methodArguments(invocationContext.methodArguments());
            this.userMessage(invocationContext.userMessage());
            this.chatMemoryId(invocationContext.chatMemoryId());
            this.defaultRequestParameters(invocationContext.defaultRequestParameters());
            this.modelProvider(invocationContext.modelProvider());
            this.invocationParameters(invocationContext.invocationParameters());
            this.managedParameters(invocationContext.managedParameters());
            this.timestamp(invocationContext.timestamp());
        }

        public Builder invocationId(UUID invocationId) {
            this.invocationId = invocationId;
            return this;
        }

        public Builder interfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
            return this;
        }

        public Builder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder methodArguments(List<Object> methodArguments) {
            if (methodArguments != null) {
                this.methodArguments.addAll(methodArguments);
            }
            return this;
        }

        public Builder methodArgument(Object methodArgument) {
            if (methodArgument != null) {
                this.methodArguments.add(methodArgument);
            }
            return this;
        }

        public Builder userMessage(UserMessage userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        public Builder chatMemoryId(Object memoryId) {
            this.chatMemoryId = memoryId;
            return this;
        }

        public Builder defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this;
        }

        public Builder modelProvider(ModelProvider modelProvider) {
            this.modelProvider = modelProvider;
            return this;
        }

        public Builder invocationParameters(InvocationParameters invocationParameters) {
            this.invocationParameters = invocationParameters;
            return this;
        }

        public Builder managedParameters(Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> managedParameters) {
            this.managedParameters = managedParameters;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder timestampNow() {
            return this.timestamp(Instant.now());
        }

        public <T extends InvocationContext> T build() {
            return (T)new DefaultInvocationContext(this);
        }

        public UUID invocationId() {
            return this.invocationId;
        }

        public String interfaceName() {
            return this.interfaceName;
        }

        public String methodName() {
            return this.methodName;
        }

        public List<@NonNull Object> methodArguments() {
            return this.methodArguments;
        }

        public UserMessage userMessage() {
            return this.userMessage;
        }

        public Object chatMemoryId() {
            return this.chatMemoryId;
        }

        public ChatRequestParameters defaultRequestParameters() {
            return this.defaultRequestParameters;
        }

        public ModelProvider modelProvider() {
            return this.modelProvider;
        }

        public InvocationParameters invocationParameters() {
            return this.invocationParameters;
        }

        public Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> managedParameters() {
            return this.managedParameters;
        }

        public Instant timestamp() {
            return this.timestamp;
        }
    }
}

