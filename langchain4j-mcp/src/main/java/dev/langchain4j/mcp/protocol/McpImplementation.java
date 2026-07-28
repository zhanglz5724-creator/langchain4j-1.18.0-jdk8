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

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@Internal
public class McpImplementation {
    private String name;
    private String version;
    private String title;

    public McpImplementation() {
    }

    public McpImplementation(String name, String version) {
        this(name, version, null);
    }

    public McpImplementation(String name, String version, String title) {
        this.name = name;
        this.version = version;
        this.title = title;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

