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
import dev.langchain4j.model.anthropic.internal.api.AnthropicCacheMissReason;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicDiagnostics {
    public AnthropicCacheMissReason cacheMissReason;

    public AnthropicCacheMissReason getCacheMissReason() {
        return this.cacheMissReason;
    }

    public void setCacheMissReason(AnthropicCacheMissReason cacheMissReason) {
        this.cacheMissReason = cacheMissReason;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnthropicDiagnostics)) {
            return false;
        }
        AnthropicDiagnostics that = (AnthropicDiagnostics)o;
        return Objects.equals(this.cacheMissReason, that.cacheMissReason);
    }

    public int hashCode() {
        return Objects.hash(this.cacheMissReason);
    }

    public String toString() {
        return "AnthropicDiagnostics{cacheMissReason=" + this.cacheMissReason + '}';
    }
}

