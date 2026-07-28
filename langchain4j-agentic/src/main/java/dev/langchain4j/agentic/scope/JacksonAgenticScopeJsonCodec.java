/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.json.JsonMapper$Builder
 *  dev.langchain4j.Internal
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.JacksonChatMessageJsonCodec
 */
package dev.langchain4j.agentic.scope;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.langchain4j.Internal;
import dev.langchain4j.agentic.scope.AgentInvocation;
import dev.langchain4j.agentic.scope.AgenticScopeJsonCodec;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.JacksonChatMessageJsonCodec;
import java.util.Map;

@Internal
class JacksonAgenticScopeJsonCodec
implements AgenticScopeJsonCodec {
    private static final ObjectMapper MAPPER = JacksonAgenticScopeJsonCodec.agenticScopeJsonSerializer();

    JacksonAgenticScopeJsonCodec() {
    }

    static JsonMapper.Builder agenticScopeJsonMapperBuilder() {
        return (JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)JacksonChatMessageJsonCodec.chatMessageJsonMapperBuilder().addMixIn(DefaultAgenticScope.class, AgenticScopeMixin.class)).addMixIn(DefaultAgenticScope.AgentMessage.class, AgentMessageMixin.class)).addMixIn(AgentInvocation.class, AgentInvocationMixin.class);
    }

    static ObjectMapper agenticScopeJsonSerializer() {
        ObjectMapper mapper = JacksonAgenticScopeJsonCodec.agenticScopeJsonMapperBuilder().build();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator());
        return mapper;
    }

    @Override
    public DefaultAgenticScope fromJson(String json) {
        try {
            return (DefaultAgenticScope)MAPPER.readValue(json, DefaultAgenticScope.class);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize AgenticScope from JSON", e);
        }
    }

    @Override
    public String toJson(DefaultAgenticScope agenticScope) {
        try {
            return MAPPER.writeValueAsString((Object)agenticScope.serializableCopy());
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize AgenticScope to JSON", e);
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class AgentInvocationMixin {
        @JsonCreator
        public AgentInvocationMixin(@JsonProperty(value="agentType") Class<?> agentType, @JsonProperty(value="agentName") String agentName, @JsonProperty(value="agentId") String agentId, @JsonProperty(value="input") Map<String, Object> input, @JsonProperty(value="output") Object output) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class AgentMessageMixin {
        @JsonCreator
        public AgentMessageMixin(@JsonProperty(value="agentName") String agentName, @JsonProperty(value="agentId") String agentId, @JsonProperty(value="message") ChatMessage message) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class AgenticScopeMixin {
        @JsonCreator
        public AgenticScopeMixin(@JsonProperty(value="memoryId") Object memoryId, @JsonProperty(value="kind") DefaultAgenticScope.Kind kind) {
        }
    }
}

