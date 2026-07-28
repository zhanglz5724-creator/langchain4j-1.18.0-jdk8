/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.workersai;

public enum WorkersAiImageModelName {
    STABLE_DIFFUSION_XL("@cf/stabilityai/stable-diffusion-xl-base-1.0"),
    DREAM_SHAPER_8_LCM("@cf/lykon/dreamshaper-8-lcm"),
    STABLE_DIFFUSION_V1_5_IMG2IMG("@cf/runwayml/stable-diffusion-v1-5-img2img"),
    STABLE_DIFFUSION_V1_5_IN_PAINTING("@cf/runwayml/stable-diffusion-v1-5-inpainting"),
    STABLE_DIFFUSION_XL_LIGHTNING("@cf/bytedance/stable-diffusion-xl-lightning");

    private final String stringValue;

    private WorkersAiImageModelName(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}

