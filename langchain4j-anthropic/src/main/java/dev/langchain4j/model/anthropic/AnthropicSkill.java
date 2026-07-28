/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 */
package dev.langchain4j.model.anthropic;

import dev.langchain4j.Experimental;

@Experimental
public enum AnthropicSkill {
    XLSX("xlsx"),
    PPTX("pptx"),
    DOCX("docx"),
    PDF("pdf");

    private final String skillId;

    private AnthropicSkill(String skillId) {
        this.skillId = skillId;
    }

    public String skillId() {
        return this.skillId;
    }
}

