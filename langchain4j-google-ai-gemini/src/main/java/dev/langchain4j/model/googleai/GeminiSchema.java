/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.model.googleai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.langchain4j.model.googleai.GeminiType;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
class GeminiSchema {
    private GeminiType type;
    private String format;
    private String description;
    private Boolean nullable;
    @JsonProperty(value="enum")
    private List<String> enumeration;
    private String maxItems;
    private Map<String, GeminiSchema> properties;
    private List<String> required;
    private GeminiSchema items;
    private List<GeminiSchema> anyOf;

    GeminiSchema(GeminiType type, String format, String description, Boolean nullable, List<String> enumeration, String maxItems, Map<String, GeminiSchema> properties, List<String> required, GeminiSchema items, List<GeminiSchema> anyOf) {
        this.type = type;
        this.format = format;
        this.description = description;
        this.nullable = nullable;
        this.enumeration = enumeration;
        this.maxItems = maxItems;
        this.properties = properties;
        this.required = required;
        this.items = items;
        this.anyOf = anyOf;
    }

    public static GeminiSchemaBuilder builder() {
        return new GeminiSchemaBuilder();
    }

    public GeminiType getType() {
        return this.type;
    }

    public String getFormat() {
        return this.format;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getNullable() {
        return this.nullable;
    }

    @JsonIgnore
    public List<String> getEnumeration() {
        return this.enumeration;
    }

    public String getMaxItems() {
        return this.maxItems;
    }

    public Map<String, GeminiSchema> getProperties() {
        return this.properties;
    }

    public List<String> getRequired() {
        return this.required;
    }

    public GeminiSchema getItems() {
        return this.items;
    }

    public List<GeminiSchema> getAnyOf() {
        return this.anyOf;
    }

    public void setType(GeminiType type) {
        this.type = type;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public void setEnumeration(List<String> enumeration) {
        this.enumeration = enumeration;
    }

    public void setMaxItems(String maxItems) {
        this.maxItems = maxItems;
    }

    public void setProperties(Map<String, GeminiSchema> properties) {
        this.properties = properties;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public void setItems(GeminiSchema items) {
        this.items = items;
    }

    public void setAnyOf(List<GeminiSchema> anyOf) {
        this.anyOf = anyOf;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GeminiSchema)) {
            return false;
        }
        GeminiSchema other = (GeminiSchema)o;
        if (!other.canEqual(this)) {
            return false;
        }
        GeminiType this$type = this.getType();
        GeminiType other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type)) {
            return false;
        }
        String this$format = this.getFormat();
        String other$format = other.getFormat();
        if (this$format == null ? other$format != null : !this$format.equals(other$format)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        Boolean this$nullable = this.getNullable();
        Boolean other$nullable = other.getNullable();
        if (this$nullable == null ? other$nullable != null : !((Object)this$nullable).equals(other$nullable)) {
            return false;
        }
        List<String> this$enumeration = this.getEnumeration();
        List<String> other$enumeration = other.getEnumeration();
        if (this$enumeration == null ? other$enumeration != null : !((Object)this$enumeration).equals(other$enumeration)) {
            return false;
        }
        String this$maxItems = this.getMaxItems();
        String other$maxItems = other.getMaxItems();
        if (this$maxItems == null ? other$maxItems != null : !this$maxItems.equals(other$maxItems)) {
            return false;
        }
        Map<String, GeminiSchema> this$properties = this.getProperties();
        Map<String, GeminiSchema> other$properties = other.getProperties();
        if (this$properties == null ? other$properties != null : !((Object)this$properties).equals(other$properties)) {
            return false;
        }
        List<String> this$required = this.getRequired();
        List<String> other$required = other.getRequired();
        if (this$required == null ? other$required != null : !((Object)this$required).equals(other$required)) {
            return false;
        }
        GeminiSchema this$items = this.getItems();
        GeminiSchema other$items = other.getItems();
        if (this$items == null ? other$items != null : !((Object)this$items).equals(other$items)) {
            return false;
        }
        List<GeminiSchema> this$anyOf = this.getAnyOf();
        List<GeminiSchema> other$anyOf = other.getAnyOf();
        return !(this$anyOf == null ? other$anyOf != null : !((Object)this$anyOf).equals(other$anyOf));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GeminiSchema;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        GeminiType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        String $format = this.getFormat();
        result = result * 59 + ($format == null ? 43 : $format.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        Boolean $nullable = this.getNullable();
        result = result * 59 + ($nullable == null ? 43 : ((Object)$nullable).hashCode());
        List<String> $enumeration = this.getEnumeration();
        result = result * 59 + ($enumeration == null ? 43 : ((Object)$enumeration).hashCode());
        String $maxItems = this.getMaxItems();
        result = result * 59 + ($maxItems == null ? 43 : $maxItems.hashCode());
        Map<String, GeminiSchema> $properties = this.getProperties();
        result = result * 59 + ($properties == null ? 43 : ((Object)$properties).hashCode());
        List<String> $required = this.getRequired();
        result = result * 59 + ($required == null ? 43 : ((Object)$required).hashCode());
        GeminiSchema $items = this.getItems();
        result = result * 59 + ($items == null ? 43 : ((Object)$items).hashCode());
        List<GeminiSchema> $anyOf = this.getAnyOf();
        result = result * 59 + ($anyOf == null ? 43 : ((Object)$anyOf).hashCode());
        return result;
    }

    public String toString() {
        return "GeminiSchema(type=" + (Object)((Object)this.getType()) + ", format=" + this.getFormat() + ", description=" + this.getDescription() + ", nullable=" + this.getNullable() + ", enumeration=" + this.getEnumeration() + ", maxItems=" + this.getMaxItems() + ", properties=" + this.getProperties() + ", required=" + this.getRequired() + ", items=" + this.getItems() + ", anyOf=" + this.getAnyOf() + ")";
    }

    public static class GeminiSchemaBuilder {
        private GeminiType type;
        private String format;
        private String description;
        private Boolean nullable;
        private List<String> enumeration;
        private String maxItems;
        private Map<String, GeminiSchema> properties;
        private List<String> required;
        private GeminiSchema items;
        private List<GeminiSchema> anyOf;

        GeminiSchemaBuilder() {
        }

        public GeminiSchemaBuilder type(GeminiType type) {
            this.type = type;
            return this;
        }

        public GeminiSchemaBuilder format(String format) {
            this.format = format;
            return this;
        }

        public GeminiSchemaBuilder description(String description) {
            this.description = description;
            return this;
        }

        public GeminiSchemaBuilder nullable(Boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public GeminiSchemaBuilder enumeration(List<String> enumeration) {
            this.enumeration = enumeration;
            return this;
        }

        public GeminiSchemaBuilder maxItems(String maxItems) {
            this.maxItems = maxItems;
            return this;
        }

        public GeminiSchemaBuilder properties(Map<String, GeminiSchema> properties) {
            this.properties = properties;
            return this;
        }

        public GeminiSchemaBuilder required(List<String> required) {
            this.required = required;
            return this;
        }

        public GeminiSchemaBuilder items(GeminiSchema items) {
            this.items = items;
            return this;
        }

        public GeminiSchemaBuilder anyOf(List<GeminiSchema> anyOf) {
            this.anyOf = anyOf;
            return this;
        }

        public GeminiSchema build() {
            return new GeminiSchema(this.type, this.format, this.description, this.nullable, this.enumeration, this.maxItems, this.properties, this.required, this.items, this.anyOf);
        }

        public String toString() {
            return "GeminiSchema.GeminiSchemaBuilder(type=" + (Object)((Object)this.type) + ", format=" + this.format + ", description=" + this.description + ", nullable=" + this.nullable + ", enumeration=" + this.enumeration + ", maxItems=" + this.maxItems + ", properties=" + this.properties + ", required=" + this.required + ", items=" + this.items + ", anyOf=" + this.anyOf + ")";
        }
    }
}

