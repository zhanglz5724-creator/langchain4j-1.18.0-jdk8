/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.invocation.InvocationContext
 */
package dev.langchain4j.service.tool.search;

import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.service.tool.search.ToolSearchRequest;
import dev.langchain4j.service.tool.search.ToolSearchResult;
import java.util.List;

@Experimental
public interface ToolSearchStrategy {
    public List<ToolSpecification> getToolSearchTools(InvocationContext var1);

    public ToolSearchResult search(ToolSearchRequest var1);
}

