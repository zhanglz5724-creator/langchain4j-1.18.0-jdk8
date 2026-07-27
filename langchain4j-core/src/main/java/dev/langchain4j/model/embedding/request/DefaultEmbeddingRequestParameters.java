/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.request;

import dev.langchain4j.Experimental;
import dev.langchain4j.model.embedding.request.EmbeddingInputType;
import dev.langchain4j.model.embedding.request.EmbeddingParameter;
import dev.langchain4j.model.embedding.request.EmbeddingRequestParameters;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Experimental
public class DefaultEmbeddingRequestParameters
implements EmbeddingRequestParameters {
    private final Map<EmbeddingParameter<?>, Object> values;

    protected DefaultEmbeddingRequestParameters(Builder<?> builder) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap(builder.values));
    }

    @Override
    public String modelName() {
        return (String)this.parameter(MODEL_NAME);
    }

    @Override
    public Integer dimensions() {
        return (Integer)this.parameter(DIMENSIONS);
    }

    @Override
    public EmbeddingInputType inputType() {
        return (EmbeddingInputType)((Object)this.parameter(INPUT_TYPE));
    }

    @Override
    public <T> T parameter(EmbeddingParameter<T> parameter) {
        return parameter.cast(this.values.get(parameter));
    }

    @Override
    public Set<EmbeddingParameter<?>> presentParameters() {
        return this.values.keySet();
    }

    @Override
    public EmbeddingRequestParameters overrideWith(EmbeddingRequestParameters that) {
        if (that == null || that.presentParameters().isEmpty()) {
            return this;
        }
        return ((Builder)((Builder)DefaultEmbeddingRequestParameters.builder().overrideWith(this)).overrideWith(that)).build();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DefaultEmbeddingRequestParameters that = (DefaultEmbeddingRequestParameters)o;
        return Objects.equals(this.values, that.values);
    }

    public int hashCode() {
        return Objects.hash(this.values);
    }

    public String toString() {
        return "DefaultEmbeddingRequestParameters{values=" + this.values + '}';
    }

    public static Builder<?> builder() {
        return new Builder();
    }

    public static class Builder<B extends Builder<B>> {
        protected final Map<EmbeddingParameter<?>, Object> values = new LinkedHashMap();

        public <T> B set(EmbeddingParameter<T> parameter, T value) {
            if (value == null) {
                this.values.remove(parameter);
            } else {
                this.values.put(parameter, value);
            }
            return this.self();
        }

        public B modelName(String modelName) {
            return this.set(EmbeddingRequestParameters.MODEL_NAME, modelName);
        }

        public B dimensions(Integer dimensions) {
            return this.set(EmbeddingRequestParameters.DIMENSIONS, dimensions);
        }

        public B inputType(EmbeddingInputType inputType) {
            return this.set(EmbeddingRequestParameters.INPUT_TYPE, inputType);
        }

        public B overrideWith(EmbeddingRequestParameters parameters) {
            if (parameters != null) {
                for (EmbeddingParameter<?> parameter : parameters.presentParameters()) {
                    this.values.put(parameter, parameters.parameter(parameter));
                }
            }
            return this.self();
        }

        protected B self() {
            return (B)this;
        }

        public DefaultEmbeddingRequestParameters build() {
            return new DefaultEmbeddingRequestParameters(this);
        }
    }
}

