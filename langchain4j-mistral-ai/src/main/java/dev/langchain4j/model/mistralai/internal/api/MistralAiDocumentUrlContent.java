/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.mistralai.internal.api.MistralAiMessageContent;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MistralAiDocumentUrlContent
extends MistralAiMessageContent {
    public String documentUrl;

    public MistralAiDocumentUrlContent(String documentUrl) {
        super("document_url");
        this.documentUrl = documentUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MistralAiDocumentUrlContent that = (MistralAiDocumentUrlContent)o;
        return Objects.equals(this.documentUrl, that.documentUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.documentUrl);
    }

    public String toString() {
        return "MistralAiDocumentUrlContent{documentUrl=" + this.documentUrl + ", type=" + Utils.quoted((Object)this.type) + '}';
    }
}

