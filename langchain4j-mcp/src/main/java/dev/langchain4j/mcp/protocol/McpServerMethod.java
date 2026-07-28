/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonValue
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.langchain4j.Internal;

@Internal
public enum McpServerMethod {
    PING("ping"),
    ROOTS_LIST("roots/list"),
    NOTIFICATION_MESSAGE("notifications/message"),
    NOTIFICATION_TOOLS_LIST_CHANGED("notifications/tools/list_changed"),
    NOTIFICATION_RESOURCES_LIST_CHANGED("notifications/resources/list_changed"),
    NOTIFICATION_PROMPTS_LIST_CHANGED("notifications/prompts/list_changed"),
    NOTIFICATION_RESOURCES_UPDATED("notifications/resources/updated"),
    NOTIFICATION_PROGRESS("notifications/progress"),
    NOTIFICATION_CANCELLED("notifications/cancelled");

    private final String value;

    private McpServerMethod(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static McpServerMethod from(String value) {
        for (McpServerMethod method : McpServerMethod.values()) {
            if (!method.value.equals(value)) continue;
            return method;
        }
        return null;
    }
}

