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
import java.util.List;

@Internal
public class McpCallToolResult
extends McpJsonRpcMessage {
    private final Result result;

    public McpCallToolResult(Long id, Result result) {
        super(id);
        this.result = result;
    }

    public Result getResult() {
        return this.result;
    }

    public static class Content {
        private final String type;
        private final String text;

        public Content(String type, String text) {
            this.type = type;
            this.text = text;
        }

        public String getType() {
            return this.type;
        }

        public String getText() {
            return this.text;
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    public static class Result {
        private final List<Content> content;
        private final Object structuredContent;
        private final Boolean isError;

        public Result(List<Content> content, Object structuredContent, Boolean isError) {
            this.content = content;
            this.structuredContent = structuredContent;
            this.isError = isError;
        }

        public List<Content> getContent() {
            return this.content;
        }

        public Object getStructuredContent() {
            return this.structuredContent;
        }

        public Boolean getIsError() {
            return this.isError;
        }
    }
}

