/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Internal
public class ToolCallBuilder {
    private volatile int index;
    private volatile String id;
    private volatile String name;
    private final StringBuffer arguments = new StringBuffer();
    private final Queue<ToolExecutionRequest> toolExecutionRequests = new ConcurrentLinkedQueue<ToolExecutionRequest>();

    public ToolCallBuilder() {
        this(0);
    }

    public ToolCallBuilder(int index) {
        this.index = index;
    }

    public int index() {
        return this.index;
    }

    public void updateIndex(Integer index) {
        if (index != null) {
            this.index = index;
        }
    }

    public String id() {
        return this.id;
    }

    public String updateId(String id) {
        if (Utils.isNotNullOrBlank(id)) {
            this.id = id;
        }
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public String updateName(String name) {
        if (Utils.isNotNullOrBlank(name)) {
            this.name = name;
        }
        return this.name;
    }

    public void appendArguments(String partialArguments) {
        if (Utils.isNotNullOrEmpty(partialArguments)) {
            this.arguments.append(partialArguments);
        }
    }

    public CompleteToolCall buildAndReset() {
        String arguments = this.arguments.toString();
        if (arguments.isEmpty()) {
            arguments = "{}";
        }
        ToolExecutionRequest toolExecutionRequest = ToolExecutionRequest.builder().id(this.id).name(this.name).arguments(arguments).build();
        this.toolExecutionRequests.add(toolExecutionRequest);
        this.reset();
        return new CompleteToolCall(this.index, toolExecutionRequest);
    }

    private void reset() {
        this.id = null;
        this.name = null;
        this.arguments.setLength(0);
    }

    public boolean hasRequests() {
        return !this.toolExecutionRequests.isEmpty() || this.name != null;
    }

    public List<ToolExecutionRequest> allRequests() {
        return new ArrayList<ToolExecutionRequest>(this.toolExecutionRequests);
    }
}

