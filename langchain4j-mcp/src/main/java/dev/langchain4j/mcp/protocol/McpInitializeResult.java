/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  dev.langchain4j.Internal
 *  org.jspecify.annotations.Nullable
 */
package dev.langchain4j.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.langchain4j.Internal;
import dev.langchain4j.mcp.protocol.McpImplementation;
import dev.langchain4j.mcp.protocol.McpJsonRpcMessage;
import org.jspecify.annotations.Nullable;

@Internal
public class McpInitializeResult
extends McpJsonRpcMessage {
    private final Result result;

    public McpInitializeResult(Long id, Result result) {
        super(id);
        this.result = result;
    }

    public Result getResult() {
        return this.result;
    }

    public static class Capabilities {
        private final Tools tools;

        public Capabilities(Tools tools) {
            this.tools = tools;
        }

        public Tools getTools() {
            return this.tools;
        }

        @JsonInclude(value=JsonInclude.Include.NON_NULL)
        public static class Tools {
            private final Boolean listChanged;

            public Tools(Boolean listChanged) {
                this.listChanged = listChanged;
            }

            public Boolean getListChanged() {
                return this.listChanged;
            }
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    public static class Result {
        private final String protocolVersion;
        private final Capabilities capabilities;
        private final McpImplementation serverInfo;
        private final @Nullable String instructions;

        public Result(String protocolVersion, Capabilities capabilities, McpImplementation serverInfo) {
            this(protocolVersion, capabilities, serverInfo, null);
        }

        public Result(String protocolVersion, Capabilities capabilities, McpImplementation serverInfo, @Nullable String instructions) {
            this.protocolVersion = protocolVersion;
            this.capabilities = capabilities;
            this.serverInfo = serverInfo;
            this.instructions = instructions;
        }

        public String getProtocolVersion() {
            return this.protocolVersion;
        }

        public Capabilities getCapabilities() {
            return this.capabilities;
        }

        public McpImplementation getServerInfo() {
            return this.serverInfo;
        }

        public @Nullable String getInstructions() {
            return this.instructions;
        }
    }
}

