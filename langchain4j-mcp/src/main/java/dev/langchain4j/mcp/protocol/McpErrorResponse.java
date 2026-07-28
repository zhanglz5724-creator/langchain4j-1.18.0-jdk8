/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpJsonRpcMessage;

@Internal
public class McpErrorResponse
extends McpJsonRpcMessage {
    private final Error error;

    public McpErrorResponse(Long id, Error error) {
        super(id);
        this.error = error;
    }

    public Error getError() {
        return this.error;
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    public static class Error {
        private final int code;
        private final String message;
        private final Object data;

        public Error(int code, String message, Object data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public int getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }

        public Object getData() {
            return this.data;
        }
    }
}

