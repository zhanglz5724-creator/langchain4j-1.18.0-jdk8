/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.ChatMessageType
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.bedrock.BedrockSystemContent;
import dev.langchain4j.model.bedrock.BedrockSystemTextContent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BedrockSystemMessage
implements ChatMessage {
    public static final int MAX_CONTENT_BLOCKS = 10;
    public static final int MAX_CACHE_POINTS = 4;
    private final List<BedrockSystemContent> contents;

    private BedrockSystemMessage(Builder builder) {
        ValidationUtils.ensureNotEmpty((Collection)builder.contents, (String)"contents");
        ValidationUtils.ensureBetween((Integer)builder.contents.size(), (int)1, (int)10, (String)"content block count");
        long cachePointCount = builder.contents.stream().filter(BedrockSystemContent::hasCachePoint).count();
        if (cachePointCount > 4L) {
            throw new IllegalArgumentException("Maximum 4 cache points allowed per AWS Bedrock request, but got " + cachePointCount);
        }
        this.contents = Collections.unmodifiableList(new ArrayList(builder.contents));
    }

    public List<BedrockSystemContent> contents() {
        return this.contents;
    }

    public ChatMessageType type() {
        return ChatMessageType.SYSTEM;
    }

    public String text() {
        return this.contents.stream().filter(c -> c instanceof BedrockSystemTextContent).map(c -> ((BedrockSystemTextContent)c).text()).collect(Collectors.joining("\n\n"));
    }

    public boolean hasSingleText() {
        return this.contents.size() == 1 && this.contents.get(0) instanceof BedrockSystemTextContent;
    }

    public String singleText() {
        if (!this.hasSingleText()) {
            throw new IllegalStateException("Expected single text content, but got " + this.contents.size() + " content blocks");
        }
        return ((BedrockSystemTextContent)this.contents.get(0)).text();
    }

    public SystemMessage toSystemMessage() {
        return SystemMessage.from((String)this.text());
    }

    public Builder toBuilder() {
        return new Builder().contents(new ArrayList<BedrockSystemContent>(this.contents));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static BedrockSystemMessage from(String text) {
        return BedrockSystemMessage.builder().addText(text).build();
    }

    public static BedrockSystemMessage from(List<BedrockSystemContent> contents) {
        return BedrockSystemMessage.builder().contents(contents).build();
    }

    public static BedrockSystemMessage from(SystemMessage systemMessage) {
        return BedrockSystemMessage.from(systemMessage.text());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BedrockSystemMessage that = (BedrockSystemMessage)o;
        return Objects.equals(this.contents, that.contents);
    }

    public int hashCode() {
        return Objects.hash(this.contents);
    }

    public boolean hasCachePoints() {
        return this.contents.stream().anyMatch(BedrockSystemContent::hasCachePoint);
    }

    public int cachePointCount() {
        return (int)this.contents.stream().filter(BedrockSystemContent::hasCachePoint).count();
    }

    public String toString() {
        int cachePoints = this.cachePointCount();
        return "BedrockSystemMessage { contents = " + this.contents.size() + " blocks, cachePoints = " + cachePoints + " }";
    }

    public static class Builder {
        private List<BedrockSystemContent> contents = new ArrayList<BedrockSystemContent>();

        public Builder contents(List<BedrockSystemContent> contents) {
            ValidationUtils.ensureNotEmpty(contents, (String)"contents");
            ValidationUtils.ensureBetween((Integer)contents.size(), (int)1, (int)10, (String)"content block count");
            for (int i = 0; i < contents.size(); ++i) {
                ValidationUtils.ensureNotNull((Object)contents.get(i), (String)("contents[" + i + "]"));
            }
            this.contents = new ArrayList<BedrockSystemContent>(contents);
            return this;
        }

        public Builder addContent(BedrockSystemContent content) {
            ValidationUtils.ensureNotNull((Object)content, (String)"content");
            if (this.contents.size() >= 10) {
                throw new IllegalArgumentException("Maximum 10 content blocks allowed");
            }
            this.contents.add(content);
            return this;
        }

        public Builder addText(String text) {
            return this.addContent(BedrockSystemTextContent.from(text));
        }

        public Builder addTextWithCachePoint(String text) {
            return this.addContent(BedrockSystemTextContent.withCachePoint(text));
        }

        public BedrockSystemMessage build() {
            return new BedrockSystemMessage(this);
        }
    }
}

