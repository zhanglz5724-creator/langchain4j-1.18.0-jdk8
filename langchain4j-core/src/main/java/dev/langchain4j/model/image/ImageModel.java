/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.image;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import java.util.List;

public interface ImageModel {
    public Response<Image> generate(String var1);

    default public Response<List<Image>> generate(String prompt, int n) {
        throw new IllegalArgumentException("Operation is not supported");
    }

    default public Response<Image> edit(Image image, String prompt) {
        throw new IllegalArgumentException("Operation is not supported");
    }

    default public Response<Image> edit(Image image, Image mask, String prompt) {
        throw new IllegalArgumentException("Operation is not supported");
    }
}

