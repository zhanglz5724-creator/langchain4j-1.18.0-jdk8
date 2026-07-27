/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.web.search;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.WebSearchResults;
import java.util.stream.Collectors;

public class WebSearchTool {
    private final WebSearchEngine searchEngine;

    public WebSearchTool(WebSearchEngine searchEngine) {
        this.searchEngine = ValidationUtils.ensureNotNull(searchEngine, "searchEngine");
    }

    @Tool(value={"This tool can be used to perform web searches using search engines such as Google, particularly when seeking information about recent events."})
    public String searchWeb(@P(value="Web search query") String query) {
        WebSearchResults results = this.searchEngine.search(query);
        return this.format(results);
    }

    private String format(WebSearchResults results) {
        if (Utils.isNullOrEmpty(results.results())) {
            return "No results found.";
        }
        return results.results().stream().map(organicResult -> "Title: " + organicResult.title() + "\nSource: " + organicResult.url().toString() + "\n" + (organicResult.content() != null ? "Content:\n" + organicResult.content() : "Snippet:\n" + organicResult.snippet())).collect(Collectors.joining("\n\n"));
    }

    public static WebSearchTool from(WebSearchEngine searchEngine) {
        return new WebSearchTool(searchEngine);
    }
}

