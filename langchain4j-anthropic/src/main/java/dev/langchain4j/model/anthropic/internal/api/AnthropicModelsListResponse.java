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
import dev.langchain4j.model.anthropic.internal.api.AnthropicModelInfo;
import java.util.List;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnthropicModelsListResponse {
    public List<AnthropicModelInfo> data;
    public String firstId;
    public String lastId;
    public Boolean hasMore;

    public int hashCode() {
        return Objects.hash(this.data, this.firstId, this.lastId, this.hasMore);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AnthropicModelsListResponse)) {
            return false;
        }
        AnthropicModelsListResponse that = (AnthropicModelsListResponse)obj;
        return Objects.equals(this.data, that.data) && Objects.equals(this.firstId, that.firstId) && Objects.equals(this.lastId, that.lastId) && Objects.equals(this.hasMore, that.hasMore);
    }

    public String toString() {
        return "AnthropicModelsListResponse{data=" + this.data + ", firstId='" + this.firstId + '\'' + ", lastId='" + this.lastId + '\'' + ", hasMore=" + this.hasMore + '}';
    }
}

