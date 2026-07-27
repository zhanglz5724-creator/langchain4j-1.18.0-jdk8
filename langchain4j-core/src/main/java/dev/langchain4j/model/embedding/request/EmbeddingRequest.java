/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.request;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.request.DefaultEmbeddingRequestParameters;
import dev.langchain4j.model.embedding.request.EmbeddingInput;
import dev.langchain4j.model.embedding.request.EmbeddingInputType;
import dev.langchain4j.model.embedding.request.EmbeddingRequestParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Experimental
public class EmbeddingRequest {
    private final List<EmbeddingInput> inputs;
    private final EmbeddingRequestParameters parameters;

    protected EmbeddingRequest(Builder builder) {
        this.inputs = Utils.copy(ValidationUtils.ensureNotEmpty(builder.inputs, "inputs"));
        this.parameters = Utils.getOrDefault(builder.parameters(), EmbeddingRequestParameters.EMPTY);
    }

    public List<EmbeddingInput> inputs() {
        return this.inputs;
    }

    public EmbeddingRequestParameters parameters() {
        return this.parameters;
    }

    public String modelName() {
        return this.parameters.modelName();
    }

    public Integer dimensions() {
        return this.parameters.dimensions();
    }

    public EmbeddingInputType inputType() {
        return this.parameters.inputType();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        EmbeddingRequest that = (EmbeddingRequest)o;
        return Objects.equals(this.inputs, that.inputs) && Objects.equals(this.parameters, that.parameters);
    }

    public int hashCode() {
        return Objects.hash(this.inputs, this.parameters);
    }

    public String toString() {
        return "EmbeddingRequest{inputs=" + this.inputs + ", parameters=" + this.parameters + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<EmbeddingInput> inputs = new ArrayList<EmbeddingInput>();
        private EmbeddingRequestParameters parameters;
        private final DefaultEmbeddingRequestParameters.Builder<?> overrides = EmbeddingRequestParameters.builder();
        private boolean overridesUsed = false;

        public Builder input(String text) {
            this.inputs.add(EmbeddingInput.from(text));
            return this;
        }

        public Builder inputs(String ... texts) {
            for (String text : texts) {
                this.inputs.add(EmbeddingInput.from(text));
            }
            return this;
        }

        public Builder textSegment(TextSegment segment) {
            if (segment != null) {
                this.inputs.add(EmbeddingInput.from(segment));
            }
            return this;
        }

        public Builder textSegments(List<TextSegment> segments) {
            if (segments != null) {
                for (TextSegment segment : segments) {
                    this.textSegment(segment);
                }
            }
            return this;
        }

        public Builder input(Content ... contents) {
            this.inputs.add(EmbeddingInput.from(contents));
            return this;
        }

        public Builder input(EmbeddingInput input) {
            if (input != null) {
                this.inputs.add(input);
            }
            return this;
        }

        public Builder inputs(EmbeddingInput ... inputs) {
            for (EmbeddingInput input : inputs) {
                this.input(input);
            }
            return this;
        }

        public Builder inputs(List<EmbeddingInput> inputs) {
            if (inputs != null) {
                this.inputs.addAll(inputs);
            }
            return this;
        }

        public Builder parameters(EmbeddingRequestParameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder modelName(String modelName) {
            this.overrides.modelName(modelName);
            this.overridesUsed = true;
            return this;
        }

        public Builder dimensions(Integer dimensions) {
            this.overrides.dimensions(dimensions);
            this.overridesUsed = true;
            return this;
        }

        public Builder inputType(EmbeddingInputType inputType) {
            this.overrides.inputType(inputType);
            this.overridesUsed = true;
            return this;
        }

        private EmbeddingRequestParameters parameters() {
            EmbeddingRequestParameters base = Utils.getOrDefault(this.parameters, EmbeddingRequestParameters.EMPTY);
            return this.overridesUsed ? base.overrideWith(this.overrides.build()) : base;
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(this);
        }
    }
}

