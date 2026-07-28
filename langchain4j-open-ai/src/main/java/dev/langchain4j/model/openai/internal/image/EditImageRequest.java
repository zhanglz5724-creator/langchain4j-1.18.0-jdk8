/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.openai.internal.image;

import dev.langchain4j.model.openai.internal.image.ImageFile;

public class EditImageRequest {
    private final ImageFile image;
    private final ImageFile mask;
    private final String model;
    private final String prompt;
    private final int n;
    private final String size;
    private final String quality;
    private final String user;
    private final String background;
    private final String outputFormat;
    private final Integer outputCompression;

    public EditImageRequest(Builder builder) {
        this.image = builder.image;
        this.mask = builder.mask;
        this.model = builder.model;
        this.prompt = builder.prompt;
        this.n = builder.n;
        this.size = builder.size;
        this.quality = builder.quality;
        this.user = builder.user;
        this.background = builder.background;
        this.outputFormat = builder.outputFormat;
        this.outputCompression = builder.outputCompression;
    }

    public ImageFile image() {
        return this.image;
    }

    public ImageFile mask() {
        return this.mask;
    }

    public String model() {
        return this.model;
    }

    public String prompt() {
        return this.prompt;
    }

    public int n() {
        return this.n;
    }

    public String size() {
        return this.size;
    }

    public String quality() {
        return this.quality;
    }

    public String user() {
        return this.user;
    }

    public String background() {
        return this.background;
    }

    public String outputFormat() {
        return this.outputFormat;
    }

    public Integer outputCompression() {
        return this.outputCompression;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ImageFile image;
        private ImageFile mask;
        private String model;
        private String prompt;
        private int n = 1;
        private String size;
        private String quality;
        private String user;
        private String background;
        private String outputFormat;
        private Integer outputCompression;

        public Builder image(ImageFile image) {
            this.image = image;
            return this;
        }

        public Builder mask(ImageFile mask) {
            this.mask = mask;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder n(int n) {
            this.n = n;
            return this;
        }

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder quality(String quality) {
            this.quality = quality;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder background(String background) {
            this.background = background;
            return this;
        }

        public Builder outputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public Builder outputCompression(Integer outputCompression) {
            this.outputCompression = outputCompression;
            return this;
        }

        public EditImageRequest build() {
            return new EditImageRequest(this);
        }
    }
}

