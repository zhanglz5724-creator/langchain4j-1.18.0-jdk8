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
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 */
package dev.langchain4j.model.mistralai.internal.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.model.mistralai.internal.api.MistralAiModelPermission;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder=MistralAiModelCardBuilder.class)
public class MistralAiModelCard {
    private String id;
    private String object;
    private Integer created;
    private String ownerBy;
    private String root;
    private String parent;
    private List<MistralAiModelPermission> permission;

    private MistralAiModelCard(MistralAiModelCardBuilder builder) {
        this.id = builder.id;
        this.object = builder.object;
        this.created = builder.created;
        this.ownerBy = builder.ownerBy;
        this.root = builder.root;
        this.parent = builder.parent;
        this.permission = builder.permission;
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

    public String getOwnerBy() {
        return this.ownerBy;
    }

    public String getRoot() {
        return this.root;
    }

    public String getParent() {
        return this.parent;
    }

    public List<MistralAiModelPermission> getPermission() {
        return this.permission;
    }

    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.object);
        hash = 83 * hash + Objects.hashCode(this.created);
        hash = 83 * hash + Objects.hashCode(this.ownerBy);
        hash = 83 * hash + Objects.hashCode(this.root);
        hash = 83 * hash + Objects.hashCode(this.parent);
        hash = 83 * hash + Objects.hashCode(this.permission);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        MistralAiModelCard other = (MistralAiModelCard)obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.object, other.object) && Objects.equals(this.ownerBy, other.ownerBy) && Objects.equals(this.root, other.root) && Objects.equals(this.parent, other.parent) && Objects.equals(this.created, other.created) && Objects.equals(this.permission, other.permission);
    }

    public String toString() {
        return new StringJoiner(", ", "MistralAiModelCard [", "]").add("id=" + this.getId()).add("object=" + this.getObject()).add("created=" + this.getCreated()).add("ownerBy=" + this.getOwnerBy()).add("root=" + this.getRoot()).add("parent=" + this.getParent()).add("permission=" + this.getPermission()).toString();
    }

    public static MistralAiModelCardBuilder builder() {
        return new MistralAiModelCardBuilder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MistralAiModelCardBuilder {
        private String id;
        private String object;
        private Integer created;
        private String ownerBy;
        private String root;
        private String parent;
        private List<MistralAiModelPermission> permission;

        private MistralAiModelCardBuilder() {
        }

        public MistralAiModelCardBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MistralAiModelCardBuilder object(String object) {
            this.object = object;
            return this;
        }

        public MistralAiModelCardBuilder created(Integer created) {
            this.created = created;
            return this;
        }

        public MistralAiModelCardBuilder ownerBy(String ownerBy) {
            this.ownerBy = ownerBy;
            return this;
        }

        public MistralAiModelCardBuilder root(String root) {
            this.root = root;
            return this;
        }

        public MistralAiModelCardBuilder parent(String parent) {
            this.parent = parent;
            return this;
        }

        public MistralAiModelCardBuilder permission(List<MistralAiModelPermission> permission) {
            this.permission = permission;
            return this;
        }

        public MistralAiModelCard build() {
            return new MistralAiModelCard(this);
        }
    }
}

