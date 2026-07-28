/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  dev.langchain4j.Internal
 *  org.jspecify.annotations.NonNull
 */
package dev.langchain4j.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpClientParams;
import org.jspecify.annotations.NonNull;

@Internal
public class McpCancellationParams
extends McpClientParams {
    private Long requestId;
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private String reason;

    public McpCancellationParams() {
    }

    public McpCancellationParams(@NonNull Long requestId, String reason) {
        this.requestId = requestId;
        this.reason = reason;
    }

    public Long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

