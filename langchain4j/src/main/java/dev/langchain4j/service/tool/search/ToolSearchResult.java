/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.service.tool.search;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Objects;

@Experimental
public class ToolSearchResult {
    private final List<String> foundToolNames;
    private final String toolResultMessageText;

    public ToolSearchResult(List<String> foundToolNames, String toolResultMessageText) {
        this.foundToolNames = Utils.copy(foundToolNames);
        this.toolResultMessageText = (String)ValidationUtils.ensureNotNull((Object)toolResultMessageText, (String)"toolResultMessageText");
    }

    public List<String> foundToolNames() {
        return this.foundToolNames;
    }

    public String toolResultMessageText() {
        return this.toolResultMessageText;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ToolSearchResult that = (ToolSearchResult)o;
        return Objects.equals(this.foundToolNames, that.foundToolNames) && Objects.equals(this.toolResultMessageText, that.toolResultMessageText);
    }

    public int hashCode() {
        return Objects.hash(this.foundToolNames, this.toolResultMessageText);
    }

    public String toString() {
        return "ToolSearchResult{foundToolNames=" + this.foundToolNames + ", toolResultMessageText='" + this.toolResultMessageText + '\'' + '}';
    }
}

