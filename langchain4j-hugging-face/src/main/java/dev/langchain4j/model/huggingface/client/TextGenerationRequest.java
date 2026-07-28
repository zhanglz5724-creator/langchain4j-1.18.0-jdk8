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
package dev.langchain4j.model.huggingface.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.huggingface.client.Options;
import dev.langchain4j.model.huggingface.client.Parameters;
import java.util.Objects;

@Deprecated
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TextGenerationRequest {
    private final String inputs;
    private final Parameters parameters;
    private final Options options;

    TextGenerationRequest(Builder builder) {
        this.inputs = builder.inputs;
        this.parameters = builder.parameters;
        this.options = builder.options;
    }

    public String getInputs() {
        return this.inputs;
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public Options getOptions() {
        return this.options;
    }

    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof TextGenerationRequest && this.equalTo((TextGenerationRequest)another);
    }

    private boolean equalTo(TextGenerationRequest another) {
        return Objects.equals(this.inputs, another.inputs) && Objects.equals(this.parameters, another.parameters) && Objects.equals(this.options, another.options);
    }

    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(this.inputs);
        h += (h << 5) + Objects.hashCode(this.parameters);
        h += (h << 5) + Objects.hashCode(this.options);
        return h;
    }

    public String toString() {
        return "TextGenerationRequest { inputs = " + Utils.quoted((Object)this.inputs) + ", parameters = " + this.parameters + ", options = " + this.options + " }";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String inputs;
        private Parameters parameters;
        private Options options;

        public Builder inputs(String inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder parameters(Parameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder options(Options options) {
            this.options = options;
            return this;
        }

        public TextGenerationRequest build() {
            return new TextGenerationRequest(this);
        }
    }
}

