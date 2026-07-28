/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.voyageai;

import java.util.List;

class EmbeddingRequest {
    private List<String> input;
    private String model;
    private String inputType;
    private Boolean truncation;
    private String encodingFormat;

    EmbeddingRequest() {
    }

    EmbeddingRequest(List<String> input, String model, String inputType, Boolean truncation, String encodingFormat) {
        this.input = input;
        this.model = model;
        this.inputType = inputType;
        this.truncation = truncation;
        this.encodingFormat = encodingFormat;
    }

    public List<String> getInput() {
        return this.input;
    }

    public String getModel() {
        return this.model;
    }

    public String getInputType() {
        return this.inputType;
    }

    public Boolean getTruncation() {
        return this.truncation;
    }

    public String getEncodingFormat() {
        return this.encodingFormat;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private List<String> input;
        private String model;
        private String inputType;
        private Boolean truncation;
        private String encodingFormat;

        Builder() {
        }

        Builder input(List<String> input) {
            this.input = input;
            return this;
        }

        Builder model(String model) {
            this.model = model;
            return this;
        }

        Builder inputType(String inputType) {
            this.inputType = inputType;
            return this;
        }

        Builder truncation(Boolean truncation) {
            this.truncation = truncation;
            return this;
        }

        Builder encodingFormat(String encodingFormat) {
            this.encodingFormat = encodingFormat;
            return this;
        }

        EmbeddingRequest build() {
            return new EmbeddingRequest(this.input, this.model, this.inputType, this.truncation, this.encodingFormat);
        }
    }
}

