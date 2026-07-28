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
import java.util.Objects;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAiModelInfo {
    @JsonProperty(value="id")
    private String id;
    @JsonProperty(value="object")
    private String object;
    @JsonProperty(value="created")
    private Long created;
    @JsonProperty(value="owned_by")
    private String ownedBy;

    public OpenAiModelInfo() {
    }

    public OpenAiModelInfo(String id, String object, Long created, String ownedBy) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.ownedBy = ownedBy;
    }

    public String id() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String object() {
        return this.object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long created() {
        return this.created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String ownedBy() {
        return this.ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenAiModelInfo)) {
            return false;
        }
        OpenAiModelInfo that = (OpenAiModelInfo)o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.object, that.object) && Objects.equals(this.created, that.created) && Objects.equals(this.ownedBy, that.ownedBy);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        return Objects.hash(this.id, this.object, this.created, this.ownedBy);
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "OpenAiModelInfo{id='" + this.id + '\'' + ", object='" + this.object + '\'' + ", created=" + this.created + ", ownedBy='" + this.ownedBy + '\'' + '}';
    }
}

