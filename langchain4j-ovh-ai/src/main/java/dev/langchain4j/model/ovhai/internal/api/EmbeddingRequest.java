/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonValue
 */
package dev.langchain4j.model.ovhai.internal.api;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;

@Deprecated
public class EmbeddingRequest {
    @JsonValue
    private List<String> input;

    public EmbeddingRequest(List<String> input) {
        this.input = input;
    }

    public EmbeddingRequest() {
    }

    public static EmbeddingRequestBuilder builder() {
        return new EmbeddingRequestBuilder();
    }

    public List<String> getInput() {
        return this.input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EmbeddingRequest)) {
            return false;
        }
        EmbeddingRequest other = (EmbeddingRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<String> this$input = this.getInput();
        List<String> other$input = other.getInput();
        return !(this$input == null ? other$input != null : !((Object)this$input).equals(other$input));
    }

    protected boolean canEqual(Object other) {
        return other instanceof EmbeddingRequest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<String> $input = this.getInput();
        result = result * 59 + ($input == null ? 43 : ((Object)$input).hashCode());
        return result;
    }

    public String toString() {
        return "EmbeddingRequest(input=" + this.getInput() + ")";
    }

    public static class EmbeddingRequestBuilder {
        private List<String> input;

        EmbeddingRequestBuilder() {
        }

        public EmbeddingRequestBuilder input(List<String> input) {
            this.input = input;
            return this;
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(this.input);
        }

        public String toString() {
            return "EmbeddingRequest.EmbeddingRequestBuilder(input=" + this.input + ")";
        }
    }
}

