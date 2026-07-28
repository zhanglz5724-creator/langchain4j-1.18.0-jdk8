/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.agentic.UntypedAgent
 *  dev.langchain4j.agentic.internal.AgentInvoker
 *  dev.langchain4j.agentic.internal.InternalAgent
 *  dev.langchain4j.agentic.internal.McpClientBuilder
 *  dev.langchain4j.agentic.observability.AgentListener
 *  dev.langchain4j.agentic.observability.ComposedAgentListener
 *  dev.langchain4j.agentic.planner.AgentArgument
 *  dev.langchain4j.agentic.planner.AgentInstance
 *  dev.langchain4j.agentic.planner.AgenticSystemConfigurationException
 *  dev.langchain4j.agentic.planner.AgenticSystemTopology
 *  dev.langchain4j.agentic.planner.Planner
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.mcp.client.McpClient
 *  dev.langchain4j.model.chat.request.json.JsonObjectSchema
 *  dev.langchain4j.service.output.ServiceOutputParser
 *  dev.langchain4j.service.tool.ToolExecutionResult
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.agentic.mcp;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.internal.AgentInvoker;
import dev.langchain4j.agentic.internal.InternalAgent;
import dev.langchain4j.agentic.internal.McpClientBuilder;
import dev.langchain4j.agentic.mcp.McpClientInstance;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.ComposedAgentListener;
import dev.langchain4j.agentic.planner.AgentArgument;
import dev.langchain4j.agentic.planner.AgentInstance;
import dev.langchain4j.agentic.planner.AgenticSystemConfigurationException;
import dev.langchain4j.agentic.planner.AgenticSystemTopology;
import dev.langchain4j.agentic.planner.Planner;
import dev.langchain4j.internal.Json;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.output.ServiceOutputParser;
import dev.langchain4j.service.tool.ToolExecutionResult;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMcpClientBuilder<T>
implements McpClientBuilder<T>,
InternalAgent,
InvocationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMcpClientBuilder.class);
    private final ServiceOutputParser serviceOutputParser = new ServiceOutputParser();
    private final McpClient mcpClient;
    private final Class<T> agentServiceClass;
    private String toolName;
    private String name;
    private String agentId;
    private String description;
    private InternalAgent parent;
    private String[] inputKeys;
    private String outputKey;
    private boolean async;
    private AgentListener agentListener;

    DefaultMcpClientBuilder(McpClient mcpClient, Class<T> agentServiceClass) {
        this.mcpClient = mcpClient;
        this.agentServiceClass = agentServiceClass;
    }

    public McpClientBuilder<T> toolName(String toolName) {
        this.toolName = toolName;
        return this;
    }

    public McpClientBuilder<T> inputKeys(String ... inputKeys) {
        this.inputKeys = inputKeys;
        return this;
    }

    public McpClientBuilder<T> outputKey(String outputKey) {
        this.outputKey = outputKey;
        return this;
    }

    public McpClientBuilder<T> async(boolean async) {
        this.async = async;
        return this;
    }

    public McpClientBuilder<T> listener(AgentListener agentListener) {
        this.agentListener = agentListener;
        return this;
    }

    public T build() {
        ToolSpecification toolSpec = this.findTool();
        this.agentId = this.name = toolSpec.name();
        this.description = toolSpec.description();
        if (this.agentServiceClass == UntypedAgent.class && this.inputKeys == null) {
            JsonObjectSchema params = toolSpec.parameters();
            this.inputKeys = params != null && params.properties() != null ? params.properties().keySet().toArray(new String[0]) : new String[0];
        }
        Object agent = Proxy.newProxyInstance(this.agentServiceClass.getClassLoader(), new Class[]{this.agentServiceClass, McpClientInstance.class}, this);
        return (T)agent;
    }

    private ToolSpecification findTool() {
        List tools = this.mcpClient.listTools();
        if (this.toolName == null || this.toolName.isBlank()) {
            if (tools.size() == 1) {
                return (ToolSpecification)tools.get(0);
            }
            throw new AgenticSystemConfigurationException("Tool name is required when there is more than one tool available: " + tools.stream().map(ToolSpecification::name).toList());
        }
        return tools.stream().filter(t -> this.toolName == null || t.name().equals(this.toolName)).findFirst().orElseThrow(() -> new AgenticSystemConfigurationException("Tool '" + this.toolName + "' not found. Available tools: " + tools.stream().map(ToolSpecification::name).toList()));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        if (method.getDeclaringClass() == AgentInstance.class || method.getDeclaringClass() == InternalAgent.class) {
            return method.invoke(Proxy.getInvocationHandler(proxy), args);
        }
        if (method.getDeclaringClass() == McpClientInstance.class) {
            return switch (method.getName()) {
                case "toolName" -> this.name;
                case "toolDescription" -> this.description;
                case "inputKeys" -> this.inputKeys;
                default -> throw new UnsupportedOperationException("Unknown method on McpClientInstance class: " + method.getName());
            };
        }
        return this.invokeTool(method, args);
    }

    private static Type getReturnType(Method method) {
        Type type = method.getGenericReturnType();
        return type == Object.class ? String.class : type;
    }

    private Object invokeTool(Method method, Object[] args) {
        Type returnType = DefaultMcpClientBuilder.getReturnType(method);
        HashMap<String, Object> argsMap = new HashMap<String, Object>();
        if (this.agentServiceClass == UntypedAgent.class) {
            Map params = (Map)args[0];
            for (String inputKey : this.inputKeys) {
                argsMap.put(inputKey, params.get(inputKey));
            }
        } else {
            String[] keys = this.inputKeys;
            if (keys == null) {
                keys = (String[])Stream.of(method.getParameters()).map(AgentInvoker::parameterName).toArray(String[]::new);
            }
            for (int i = 0; i < keys.length && i < args.length; ++i) {
                argsMap.put(keys[i], args[i]);
            }
        }
        String argumentsJson = Json.toJson(argsMap);
        ToolExecutionRequest executionRequest = ToolExecutionRequest.builder().name(this.name).arguments(argumentsJson).build();
        ToolExecutionResult result = this.mcpClient.executeTool(executionRequest);
        if (result.isError()) {
            throw new RuntimeException("MCP tool execution failed: " + result.resultText());
        }
        String responseText = result.resultText();
        LOG.debug("MCP tool '{}' response: {}", (Object)this.name, (Object)responseText);
        return this.serviceOutputParser.parseText(returnType, responseText);
    }

    public void setParent(InternalAgent parent) {
        this.parent = parent;
    }

    public void registerInheritedParentListener(AgentListener parentListener) {
        if (parentListener != null && parentListener.inheritedBySubagents()) {
            this.agentListener = ComposedAgentListener.composeWithInherited((AgentListener)this.listener(), (AgentListener)parentListener);
        }
    }

    public void appendId(String idSuffix) {
        this.agentId = this.agentId + idSuffix;
    }

    public AgentListener listener() {
        return this.agentListener;
    }

    public Class<?> type() {
        return this.agentServiceClass;
    }

    public Class<? extends Planner> plannerType() {
        return null;
    }

    public String name() {
        return this.name;
    }

    public String agentId() {
        return this.agentId;
    }

    public String description() {
        return this.description;
    }

    public Type outputType() {
        return Object.class;
    }

    public String outputKey() {
        return this.outputKey;
    }

    public boolean async() {
        return this.async;
    }

    public List<AgentArgument> arguments() {
        return List.of();
    }

    public AgentInstance parent() {
        return this.parent;
    }

    public List<AgentInstance> subagents() {
        return List.of();
    }

    public AgenticSystemTopology topology() {
        return AgenticSystemTopology.NON_AI_AGENT;
    }
}

