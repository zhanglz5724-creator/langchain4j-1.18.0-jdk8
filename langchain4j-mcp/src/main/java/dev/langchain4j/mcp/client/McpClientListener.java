/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.service.tool.ToolExecutionResult
 */
package dev.langchain4j.mcp.client;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.client.McpCallContext;
import dev.langchain4j.mcp.client.McpGetPromptResult;
import dev.langchain4j.mcp.client.McpPrompt;
import dev.langchain4j.mcp.client.McpReadResourceResult;
import dev.langchain4j.mcp.client.McpResource;
import dev.langchain4j.mcp.client.McpResourceTemplate;
import dev.langchain4j.mcp.client.logging.McpLogMessage;
import dev.langchain4j.mcp.client.progress.McpProgressNotification;
import dev.langchain4j.service.tool.ToolExecutionResult;
import java.util.List;
import java.util.Map;

public interface McpClientListener {
    default public void beforeInitialize(McpCallContext context) {
    }

    default public void afterInitialize(McpCallContext context) {
    }

    default public void onInitializeError(McpCallContext context, Throwable error) {
    }

    default public void beforeExecuteTool(McpCallContext context) {
    }

    default public void afterExecuteTool(McpCallContext context, ToolExecutionResult result, Map<String, Object> rawResult) {
    }

    default public void onExecuteToolError(McpCallContext context, Throwable error) {
    }

    default public void beforeToolsList(McpCallContext context) {
    }

    default public void afterToolsList(McpCallContext context, List<ToolSpecification> tools) {
    }

    default public void onToolsListError(McpCallContext context, Throwable error) {
    }

    default public void beforeResourceGet(McpCallContext context) {
    }

    default public void afterResourceGet(McpCallContext context, McpReadResourceResult result, Map<String, Object> rawResult) {
    }

    default public void onResourceGetError(McpCallContext context, Throwable error) {
    }

    default public void beforeResourcesList(McpCallContext context) {
    }

    default public void afterResourcesList(McpCallContext context, List<McpResource> resources) {
    }

    default public void onResourcesListError(McpCallContext context, Throwable error) {
    }

    default public void beforeResourceTemplatesList(McpCallContext context) {
    }

    default public void afterResourceTemplatesList(McpCallContext context, List<McpResourceTemplate> templates) {
    }

    default public void onResourceTemplatesListError(McpCallContext context, Throwable error) {
    }

    default public void beforeResourceSubscribe(McpCallContext context) {
    }

    default public void afterResourceSubscribe(McpCallContext context) {
    }

    default public void onResourceSubscribeError(McpCallContext context, Throwable error) {
    }

    default public void beforeResourceUnsubscribe(McpCallContext context) {
    }

    default public void afterResourceUnsubscribe(McpCallContext context) {
    }

    default public void onResourceUnsubscribeError(McpCallContext context, Throwable error) {
    }

    default public void beforePromptGet(McpCallContext context) {
    }

    default public void afterPromptGet(McpCallContext context, McpGetPromptResult result, Map<String, Object> rawResult) {
    }

    default public void onPromptGetError(McpCallContext context, Throwable error) {
    }

    default public void beforePromptsList(McpCallContext context) {
    }

    default public void afterPromptsList(McpCallContext context, List<McpPrompt> prompts) {
    }

    default public void onPromptsListError(McpCallContext context, Throwable error) {
    }

    default public void beforePing(McpCallContext context) {
    }

    default public void afterPing(McpCallContext context) {
    }

    default public void onPingError(McpCallContext context, Throwable error) {
    }

    default public void onRootsListChanged(McpCallContext context) {
    }

    default public void onNotificationToolsListChanged() {
    }

    default public void onNotificationResourcesListChanged() {
    }

    default public void onNotificationPromptsListChanged() {
    }

    default public void onNotificationResourceUpdated(String uri) {
    }

    default public void onNotificationMessage(McpLogMessage message) {
    }

    default public void onNotificationProgress(McpProgressNotification notification) {
    }

    default public void onNotificationCancelled(long requestId, String reason) {
    }

    default public void onServerPing() {
    }

    default public void onServerRootsList() {
    }
}

