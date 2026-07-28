/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.databind.PropertyNamingStrategies$SnakeCaseStrategy
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.annotation.JsonNaming
 *  com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder
 *  dev.langchain4j.internal.JacocoIgnoreCoverageGenerated
 */
package dev.langchain4j.model.openai.internal.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.model.openai.internal.chat.Content;
import dev.langchain4j.model.openai.internal.chat.ContentType;
import dev.langchain4j.model.openai.internal.chat.ImageDetail;
import dev.langchain4j.model.openai.internal.chat.ImageUrl;
import dev.langchain4j.model.openai.internal.chat.InputAudio;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.model.openai.internal.chat.PdfFile;
import dev.langchain4j.model.openai.internal.chat.Role;
import dev.langchain4j.model.openai.internal.chat.VideoUrl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder=UserMessage.Builder.class)
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class UserMessage
implements Message {
    @JsonProperty
    private final Role role = Role.USER;
    @JsonProperty
    private final Object content;
    @JsonProperty
    private final String name;

    public UserMessage(Builder builder) {
        this.content = builder.stringContent != null ? builder.stringContent : builder.content;
        this.name = builder.name;
    }

    @Override
    public Role role() {
        return this.role;
    }

    public Object content() {
        return this.content;
    }

    public String name() {
        return this.name;
    }

    @JacocoIgnoreCoverageGenerated
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        return another instanceof UserMessage && this.equalTo((UserMessage)another);
    }

    @JacocoIgnoreCoverageGenerated
    private boolean equalTo(UserMessage another) {
        return Objects.equals((Object)this.role, (Object)another.role) && Objects.equals(this.content, another.content) && Objects.equals(this.name, another.name);
    }

    @JacocoIgnoreCoverageGenerated
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode((Object)this.role);
        h += (h << 5) + Objects.hashCode(this.content);
        h += (h << 5) + Objects.hashCode(this.name);
        return h;
    }

    @JacocoIgnoreCoverageGenerated
    public String toString() {
        return "UserMessage{role=" + (Object)((Object)this.role) + ", content=" + this.content + ", name=" + this.name + "}";
    }

    public static UserMessage from(String text) {
        return UserMessage.builder().content(text).build();
    }

    public static UserMessage from(String text, String ... imageUrls) {
        return UserMessage.builder().addText(text).addImageUrls(imageUrls).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix="")
    @JsonIgnoreProperties(ignoreUnknown=true)
    @JsonNaming(value=PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class Builder {
        private String stringContent;
        private List<Content> content;
        private String name;

        public Builder addText(String text) {
            this.initializeContent();
            Content content = Content.builder().type(ContentType.TEXT).text(text).build();
            this.content.add(content);
            return this;
        }

        public Builder addImageUrl(String imageUrl) {
            return this.addImageUrl(imageUrl, null);
        }

        public Builder addImageUrl(String imageUrl, ImageDetail imageDetail) {
            this.initializeContent();
            Content content = Content.builder().type(ContentType.IMAGE_URL).imageUrl(ImageUrl.builder().url(imageUrl).detail(imageDetail).build()).build();
            this.content.add(content);
            return this;
        }

        public Builder addImageUrls(String ... imageUrls) {
            for (String imageUrl : imageUrls) {
                this.addImageUrl(imageUrl);
            }
            return this;
        }

        public Builder addVideoUrl(String videoUrl) {
            this.initializeContent();
            Content content = Content.builder().type(ContentType.VIDEO_URL).videoUrl(VideoUrl.builder().url(videoUrl).build()).build();
            this.content.add(content);
            return this;
        }

        public Builder addVideoUrls(String ... videoUrls) {
            for (String videoUrl : videoUrls) {
                this.addVideoUrl(videoUrl);
            }
            return this;
        }

        public Builder addInputAudio(InputAudio inputAudio) {
            this.initializeContent();
            this.content.add(Content.builder().type(ContentType.AUDIO).inputAudio(inputAudio).build());
            return this;
        }

        public Builder addPdfFile(PdfFile pdfFile) {
            this.initializeContent();
            this.content.add(Content.builder().type(ContentType.FILE).file(pdfFile).build());
            return this;
        }

        public Builder content(List<Content> content) {
            if (content != null) {
                this.content = Collections.unmodifiableList(content);
            }
            return this;
        }

        public Builder content(String content) {
            this.stringContent = content;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public UserMessage build() {
            return new UserMessage(this);
        }

        private void initializeContent() {
            if (this.content == null) {
                this.content = new ArrayList<Content>();
            }
        }
    }
}

