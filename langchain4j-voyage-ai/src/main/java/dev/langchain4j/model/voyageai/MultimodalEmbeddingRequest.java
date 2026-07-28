/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.voyageai;

import java.util.List;

class MultimodalEmbeddingRequest {
    private List<MultimodalInput> inputs;
    private String model;
    private String inputType;
    private Boolean truncation;

    MultimodalEmbeddingRequest(List<MultimodalInput> inputs, String model, String inputType, Boolean truncation) {
        this.inputs = inputs;
        this.model = model;
        this.inputType = inputType;
        this.truncation = truncation;
    }

    public List<MultimodalInput> getInputs() {
        return this.inputs;
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

    static Builder builder() {
        return new Builder();
    }

    static class ContentBlock {
        private final String type;
        private final String text;
        private final String imageUrl;
        private final String imageBase64;

        private ContentBlock(String type, String text, String imageUrl, String imageBase64) {
            this.type = type;
            this.text = text;
            this.imageUrl = imageUrl;
            this.imageBase64 = imageBase64;
        }

        static ContentBlock text(String text) {
            return new ContentBlock("text", text, null, null);
        }

        static ContentBlock imageUrl(String imageUrl) {
            return new ContentBlock("image_url", null, imageUrl, null);
        }

        static ContentBlock imageBase64(String imageBase64) {
            return new ContentBlock("image_base64", null, null, imageBase64);
        }

        public String getType() {
            return this.type;
        }

        public String getText() {
            return this.text;
        }

        public String getImageUrl() {
            return this.imageUrl;
        }

        public String getImageBase64() {
            return this.imageBase64;
        }
    }

    static class MultimodalInput {
        private final List<ContentBlock> content;

        MultimodalInput(List<ContentBlock> content) {
            this.content = content;
        }

        public List<ContentBlock> getContent() {
            return this.content;
        }
    }

    static class Builder {
        private List<MultimodalInput> inputs;
        private String model;
        private String inputType;
        private Boolean truncation;

        Builder() {
        }

        Builder inputs(List<MultimodalInput> inputs) {
            this.inputs = inputs;
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

        MultimodalEmbeddingRequest build() {
            return new MultimodalEmbeddingRequest(this.inputs, this.model, this.inputType, this.truncation);
        }
    }
}

