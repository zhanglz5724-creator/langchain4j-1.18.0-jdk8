/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.invocation;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.invocation.LangChain4jManaged;
import dev.langchain4j.model.ModelProvider;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DefaultInvocationContext
implements InvocationContext {
    private final UUID invocationId;
    private final String interfaceName;
    private final String methodName;
    private final List<Object> methodArguments = new ArrayList<Object>();
    private final UserMessage userMessage;
    private final Object chatMemoryId;
    private final ChatRequestParameters defaultRequestParameters;
    private final ModelProvider modelProvider;
    private final InvocationParameters invocationParameters;
    private final Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> managedParameters;
    private final Instant timestamp;

    public DefaultInvocationContext(InvocationContext.Builder builder) {
        this.invocationId = builder.invocationId();
        this.interfaceName = builder.interfaceName();
        this.methodName = builder.methodName();
        this.methodArguments.addAll(builder.methodArguments());
        this.userMessage = builder.userMessage();
        this.chatMemoryId = builder.chatMemoryId();
        this.defaultRequestParameters = builder.defaultRequestParameters();
        this.modelProvider = builder.modelProvider();
        this.invocationParameters = builder.invocationParameters();
        this.managedParameters = builder.managedParameters();
        this.timestamp = builder.timestamp();
    }

    @Override
    public UUID invocationId() {
        return this.invocationId;
    }

    @Override
    public String interfaceName() {
        return this.interfaceName;
    }

    @Override
    public String methodName() {
        return this.methodName;
    }

    @Override
    public List<Object> methodArguments() {
        return this.methodArguments;
    }

    @Override
    public UserMessage userMessage() {
        return this.userMessage;
    }

    @Override
    public Object chatMemoryId() {
        return this.chatMemoryId;
    }

    @Override
    public ChatRequestParameters defaultRequestParameters() {
        return this.defaultRequestParameters;
    }

    @Override
    public ModelProvider modelProvider() {
        return this.modelProvider;
    }

    @Override
    public InvocationParameters invocationParameters() {
        return this.invocationParameters;
    }

    @Override
    public Map<Class<? extends LangChain4jManaged>, LangChain4jManaged> managedParameters() {
        return this.managedParameters;
    }

    @Override
    public Instant timestamp() {
        return this.timestamp;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        DefaultInvocationContext that = (DefaultInvocationContext)object;
        return Objects.equals(this.invocationId, that.invocationId) && Objects.equals(this.interfaceName, that.interfaceName) && Objects.equals(this.methodName, that.methodName) && Objects.equals(this.methodArguments, that.methodArguments) && Objects.equals(this.userMessage, that.userMessage) && Objects.equals(this.chatMemoryId, that.chatMemoryId) && Objects.equals(this.defaultRequestParameters, that.defaultRequestParameters) && Objects.equals((Object)this.modelProvider, (Object)that.modelProvider) && Objects.equals(this.invocationParameters, that.invocationParameters) && Objects.equals(this.managedParameters, that.managedParameters) && Objects.equals(this.timestamp, that.timestamp);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.invocationId, this.interfaceName, this.methodName, this.methodArguments, this.userMessage, this.chatMemoryId, this.defaultRequestParameters, this.modelProvider, this.invocationParameters, this.managedParameters, this.timestamp});
    }

    public String toString() {
        return "DefaultInvocationContext{invocationId=" + this.invocationId + ", interfaceName='" + this.interfaceName + '\'' + ", methodName='" + this.methodName + '\'' + ", methodArguments=" + this.methodArguments + ", userMessage=" + this.userMessage + ", chatMemoryId=" + this.chatMemoryId + ", defaultRequestParameters=" + this.defaultRequestParameters + ", modelProvider=" + (Object)((Object)this.modelProvider) + ", invocationParameters=" + this.invocationParameters + ", managedParameters=" + this.managedParameters + ", timestamp=" + this.timestamp + '}';
    }
}

