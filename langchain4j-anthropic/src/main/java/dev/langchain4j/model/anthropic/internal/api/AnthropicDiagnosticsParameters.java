/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.anthropic.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicDiagnosticsParameters {
    public String previousMessageId;

    public AnthropicDiagnosticsParameters() {
    }

    public AnthropicDiagnosticsParameters(String previousMessageId) {
        this.previousMessageId = previousMessageId;
    }

    public String getPreviousMessageId() {
        return this.previousMessageId;
    }

    public void setPreviousMessageId(String previousMessageId) {
        this.previousMessageId = previousMessageId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnthropicDiagnosticsParameters)) {
            return false;
        }
        AnthropicDiagnosticsParameters that = (AnthropicDiagnosticsParameters)o;
        return Objects.equals(this.previousMessageId, that.previousMessageId);
    }

    public int hashCode() {
        return Objects.hash(this.previousMessageId);
    }

    public String toString() {
        return "AnthropicDiagnosticsParameters{previousMessageId='" + this.previousMessageId + '\'' + '}';
    }
}

