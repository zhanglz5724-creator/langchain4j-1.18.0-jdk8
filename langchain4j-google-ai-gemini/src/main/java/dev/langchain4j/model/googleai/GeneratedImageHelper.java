/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import java.util.Collections;
import java.util.List;

public class GeneratedImageHelper {
    public static List<Image> getGeneratedImages(AiMessage aiMessage) {
        if (aiMessage == null || aiMessage.attributes() == null) {
            return Collections.emptyList();
        }
        Object images = aiMessage.attributes().get("generated_images");
        if (images instanceof List) {
            try {
                return (List)images;
            }
            catch (ClassCastException e) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    public static boolean hasGeneratedImages(AiMessage aiMessage) {
        return !GeneratedImageHelper.getGeneratedImages(aiMessage).isEmpty();
    }
}

