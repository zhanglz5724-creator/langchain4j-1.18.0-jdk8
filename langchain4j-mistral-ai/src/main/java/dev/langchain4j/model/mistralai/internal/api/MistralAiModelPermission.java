/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiModelPermissionBuilder.class)
public class MistralAiModelPermission {
    private String id;
    private String object;
    private Integer created;
    private Boolean allowCreateEngine;
    private Boolean allowSampling;
    private Boolean allowLogprobs;
    private Boolean allowSearchIndices;
    private Boolean allowView;
    private Boolean allowFineTuning;
    private String organization;
    private String group;
    private Boolean isBlocking;

    public static MistralAiModelPermissionBuilder builder() {
        return new MistralAiModelPermissionBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getObject() {
        return this.object;
    }

    public Integer getCreated() {
        return this.created;
    }

    public Boolean getAllowCreateEngine() {
        return this.allowCreateEngine;
    }

    public Boolean getAllowSampling() {
        return this.allowSampling;
    }

    public Boolean getAllowLogprobs() {
        return this.allowLogprobs;
    }

    public Boolean getAllowSearchIndices() {
        return this.allowSearchIndices;
    }

    public Boolean getAllowView() {
        return this.allowView;
    }

    public Boolean getAllowFineTuning() {
        return this.allowFineTuning;
    }

    public String getOrganization() {
        return this.organization;
    }

    public String getGroup() {
        return this.group;
    }

    public Boolean getIsBlocking() {
        return this.isBlocking;
    }

    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.object);
        hash = 61 * hash + Objects.hashCode(this.created);
        hash = 61 * hash + Objects.hashCode(this.allowCreateEngine);
        hash = 61 * hash + Objects.hashCode(this.allowSampling);
        hash = 61 * hash + Objects.hashCode(this.allowLogprobs);
        hash = 61 * hash + Objects.hashCode(this.allowSearchIndices);
        hash = 61 * hash + Objects.hashCode(this.allowView);
        hash = 61 * hash + Objects.hashCode(this.allowFineTuning);
        hash = 61 * hash + Objects.hashCode(this.organization);
        hash = 61 * hash + Objects.hashCode(this.group);
        hash = 61 * hash + Objects.hashCode(this.isBlocking);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiModelPermission other = (MistralAiModelPermission)obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.object, other.object) && Objects.equals(this.organization, other.organization) && Objects.equals(this.group, other.group) && Objects.equals(this.created, other.created) && Objects.equals(this.allowCreateEngine, other.allowCreateEngine) && Objects.equals(this.allowSampling, other.allowSampling) && Objects.equals(this.allowLogprobs, other.allowLogprobs) && Objects.equals(this.allowSearchIndices, other.allowSearchIndices) && Objects.equals(this.allowView, other.allowView) && Objects.equals(this.allowFineTuning, other.allowFineTuning) && Objects.equals(this.isBlocking, other.isBlocking);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiModelPermission [", "]").add("id=" + this.getId()).add("object=" + this.getObject()).add("created=" + this.getCreated()).add("allowCreateEngine=" + this.getAllowCreateEngine()).add("allowSampling=" + this.getAllowSampling()).add("allowLogprobs=" + this.getAllowLogprobs()).add("allowSearchIndices=" + this.getAllowSearchIndices()).add("allowView=" + this.getAllowView()).add("allowFineTuning=" + this.getAllowFineTuning()).add("organization=" + this.getOrganization()).add("group=" + this.getGroup()).add("isBlocking=" + this.getIsBlocking()).toString();
    }

    public MistralAiModelPermission(MistralAiModelPermissionBuilder builder) {
        this.id = builder.id;
        this.object = builder.object;
        this.created = builder.created;
        this.allowCreateEngine = builder.allowCreateEngine;
        this.allowSampling = builder.allowSampling;
        this.allowLogprobs = builder.allowLogprobs;
        this.allowSearchIndices = builder.allowSearchIndices;
        this.allowView = builder.allowView;
        this.allowFineTuning = builder.allowFineTuning;
        this.organization = builder.organization;
        this.group = builder.group;
        this.isBlocking = builder.isBlocking;
    }

    public static class MistralAiModelPermissionBuilder {
        private String id;
        private String object;
        private Integer created;
        private Boolean allowCreateEngine;
        private Boolean allowSampling;
        private Boolean allowLogprobs;
        private Boolean allowSearchIndices;
        private Boolean allowView;
        private Boolean allowFineTuning;
        private String organization;
        private String group;
        private Boolean isBlocking;

        MistralAiModelPermissionBuilder() {
        }

        public MistralAiModelPermissionBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MistralAiModelPermissionBuilder object(String object) {
            this.object = object;
            return this;
        }

        public MistralAiModelPermissionBuilder created(Integer created) {
            this.created = created;
            return this;
        }

        public MistralAiModelPermissionBuilder allowCreateEngine(Boolean allowCreateEngine) {
            this.allowCreateEngine = allowCreateEngine;
            return this;
        }

        public MistralAiModelPermissionBuilder allowSampling(Boolean allowSampling) {
            this.allowSampling = allowSampling;
            return this;
        }

        public MistralAiModelPermissionBuilder allowLogprobs(Boolean allowLogprobs) {
            this.allowLogprobs = allowLogprobs;
            return this;
        }

        public MistralAiModelPermissionBuilder allowSearchIndices(Boolean allowSearchIndices) {
            this.allowSearchIndices = allowSearchIndices;
            return this;
        }

        public MistralAiModelPermissionBuilder allowView(Boolean allowView) {
            this.allowView = allowView;
            return this;
        }

        public MistralAiModelPermissionBuilder allowFineTuning(Boolean allowFineTuning) {
            this.allowFineTuning = allowFineTuning;
            return this;
        }

        public MistralAiModelPermissionBuilder organization(String organization) {
            this.organization = organization;
            return this;
        }

        public MistralAiModelPermissionBuilder group(String group) {
            this.group = group;
            return this;
        }

        public MistralAiModelPermissionBuilder isBlocking(Boolean isBlocking) {
            this.isBlocking = isBlocking;
            return this;
        }

        public MistralAiModelPermission build() {
            return new MistralAiModelPermission(this);
        }
    }
}

