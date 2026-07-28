/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonSetter
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.moderation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import java.util.Objects;

@JsonDeserialize(builder=Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class Categories {
    @JsonProperty
    private final Boolean hate;
    @JsonProperty(value="hate/threatening")
    private final Boolean hateThreatening;
    @JsonProperty(value="self-harm")
    private final Boolean selfHarm;
    @JsonProperty
    private final Boolean sexual;
    @JsonProperty(value="sexual/minors")
    private final Boolean sexualMinors;
    @JsonProperty
    private final Boolean violence;
    @JsonProperty(value="violence/graphic")
    private final Boolean violenceGraphic;

    public Categories(Builder builder) {
        this.hate = builder.hate;
        this.hateThreatening = builder.hateThreatening;
        this.selfHarm = builder.selfHarm;
        this.sexual = builder.sexual;
        this.sexualMinors = builder.sexualMinors;
        this.violence = builder.violence;
        this.violenceGraphic = builder.violenceGraphic;
    }

    public Boolean hate() {
        return this.hate;
    }

    public Boolean hateThreatening() {
        return this.hateThreatening;
    }

    public Boolean selfHarm() {
        return this.selfHarm;
    }

    public Boolean sexual() {
        return this.sexual;
    }

    public Boolean sexualMinors() {
        return this.sexualMinors;
    }

    public Boolean violence() {
        return this.violence;
    }

    public Boolean violenceGraphic() {
        return this.violenceGraphic;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof Categories && this.equalTo((Categories)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(Categories another) {
        return Objects.equals(this.hate, another.hate) && Objects.equals(this.hateThreatening, another.hateThreatening) && Objects.equals(this.selfHarm, another.selfHarm) && Objects.equals(this.sexual, another.sexual) && Objects.equals(this.sexualMinors, another.sexualMinors) && Objects.equals(this.violence, another.violence) && Objects.equals(this.violenceGraphic, another.violenceGraphic);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.hate);
        h += (h << 5) + Objects.hashCode(this.hateThreatening);
        h += (h << 5) + Objects.hashCode(this.selfHarm);
        h += (h << 5) + Objects.hashCode(this.sexual);
        h += (h << 5) + Objects.hashCode(this.sexualMinors);
        h += (h << 5) + Objects.hashCode(this.violence);
        h += (h << 5) + Objects.hashCode(this.violenceGraphic);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "Categories{hate=" + this.hate + ", hateThreatening=" + this.hateThreatening + ", selfHarm=" + this.selfHarm + ", sexual=" + this.sexual + ", sexualMinors=" + this.sexualMinors + ", violence=" + this.violence + ", violenceGraphic=" + this.violenceGraphic + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private Boolean hate;
        private Boolean hateThreatening;
        private Boolean selfHarm;
        private Boolean sexual;
        private Boolean sexualMinors;
        private Boolean violence;
        private Boolean violenceGraphic;

        public Builder hate(Boolean hate) {
            this.hate = hate;
            return this;
        }

        @JsonSetter(value="hate/threatening")
        public Builder hateThreatening(Boolean hateThreatening) {
            this.hateThreatening = hateThreatening;
            return this;
        }

        @JsonSetter(value="self-harm")
        public Builder selfHarm(Boolean selfHarm) {
            this.selfHarm = selfHarm;
            return this;
        }

        public Builder sexual(Boolean sexual) {
            this.sexual = sexual;
            return this;
        }

        @JsonSetter(value="sexual/minors")
        public Builder sexualMinors(Boolean sexualMinors) {
            this.sexualMinors = sexualMinors;
            return this;
        }

        public Builder violence(Boolean violence) {
            this.violence = violence;
            return this;
        }

        @JsonSetter(value="violence/graphic")
        public Builder violenceGraphic(Boolean violenceGraphic) {
            this.violenceGraphic = violenceGraphic;
            return this;
        }

        public Categories build() {
            return new Categories(this);
        }
    }
}

