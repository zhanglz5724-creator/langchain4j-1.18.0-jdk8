/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.message;

import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.VideoContent;

public enum ContentType {
    TEXT(TextContent.class),
    IMAGE(ImageContent.class),
    AUDIO(AudioContent.class),
    VIDEO(VideoContent.class),
    PDF(PdfFileContent.class);

    private final Class<? extends Content> contentClass;

    private ContentType(Class<? extends Content> contentClass) {
        this.contentClass = contentClass;
    }

    public Class<? extends Content> getContentClass() {
        return this.contentClass;
    }
}

