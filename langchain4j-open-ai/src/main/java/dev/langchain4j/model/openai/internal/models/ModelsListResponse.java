/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.models.OpenAiModelInfo;
import java.util.List;
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelsListResponse {
    @JsonProperty(value="object")
    private String object;
    @JsonProperty(value="data")
    private List<OpenAiModelInfo> data;

    public ModelsListResponse() {
    }

    public ModelsListResponse(String object, List<OpenAiModelInfo> data) {
        this.object = object;
        this.data = data;
    }

    public String getObject() {
        return this.object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<OpenAiModelInfo> getData() {
        return this.data;
    }

    public void setData(List<OpenAiModelInfo> data) {
        this.data = data;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelsListResponse)) {
            return false;
        }
        ModelsListResponse that = (ModelsListResponse)o;
        return Objects.equals(this.object, that.object) && Objects.equals(this.data, that.data);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(this.object, this.data);
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "ModelsListResponse{object='" + this.object + '\'' + ", data=" + this.data + '}';
    }
}

