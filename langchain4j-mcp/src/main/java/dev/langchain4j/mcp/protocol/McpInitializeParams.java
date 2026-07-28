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
import dev.langchain4j.mcp.protocol.McpClientParams;
import dev.langchain4j.mcp.protocol.McpImplementation;

@Internal
public class McpInitializeParams
extends McpClientParams {
    private String protocolVersion;
    private Capabilities capabilities;
    private McpImplementation clientInfo;

    public String getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Capabilities getCapabilities() {
        return this.capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public McpImplementation getClientInfo() {
        return this.clientInfo;
    }

    public void setClientInfo(McpImplementation clientInfo) {
        this.clientInfo = clientInfo;
    }

    public static class Capabilities {
        private Roots roots;
        @JsonInclude(value=JsonInclude.Include.NON_NULL)
        private Sampling sampling;

        public Roots getRoots() {
            return this.roots;
        }

        public void setRoots(Roots roots) {
            this.roots = roots;
        }

        public Sampling getSampling() {
            return this.sampling;
        }

        public void setSampling(Sampling sampling) {
            this.sampling = sampling;
        }

        public static class Sampling {
        }

        public static class Roots {
            private boolean listChanged;

            public boolean isListChanged() {
                return this.listChanged;
            }

            public void setListChanged(boolean listChanged) {
                this.listChanged = listChanged;
            }
        }
    }
}

