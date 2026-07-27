/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility
 *  com.fasterxml.jackson.annotation.JsonCreator
 *  com.fasterxml.jackson.annotation.JsonInclude
 *  com.fasterxml.jackson.annotation.JsonInclude$Include
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonSubTypes
 *  com.fasterxml.jackson.annotation.JsonSubTypes$Type
 *  com.fasterxml.jackson.annotation.JsonTypeInfo
 *  com.fasterxml.jackson.annotation.JsonTypeInfo$As
 *  com.fasterxml.jackson.annotation.JsonTypeInfo$Id
 *  com.fasterxml.jackson.annotation.PropertyAccessor
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.annotation.JsonDeserialize
 *  com.fasterxml.jackson.databind.json.JsonMapper
 *  com.fasterxml.jackson.databind.json.JsonMapper$Builder
 */
package dev.langchain4j.data.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageJsonCodec;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.CustomMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.VideoContent;
import dev.langchain4j.data.pdf.PdfFile;
import dev.langchain4j.data.video.Video;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Internal
public class JacksonChatMessageJsonCodec
implements ChatMessageJsonCodec {
    private static final ObjectMapper OBJECT_MAPPER = JacksonChatMessageJsonCodec.chatMessageJsonMapperBuilder().build();
    private static final Type MESSAGE_LIST_TYPE = new TypeReference<List<ChatMessage>>(){}.getType();

    public static JsonMapper.Builder chatMessageJsonMapperBuilder() {
        return (JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)JsonMapper.builder().visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)).addMixIn(ChatMessage.class, ChatMessageMixin.class)).addMixIn(SystemMessage.class, SystemMessageMixin.class)).addMixIn(UserMessage.class, UserMessageMixin.class)).addMixIn(AiMessage.class, AiMessageMixin.class)).addMixIn(ToolExecutionRequest.class, ToolExecutionRequestMixin.class)).addMixIn(ToolExecutionResultMessage.class, ToolExecutionResultMessageMixin.class)).addMixIn(CustomMessage.class, CustomMessageMixin.class)).addMixIn(Content.class, ContentMixin.class)).addMixIn(TextContent.class, TextContentMixin.class)).addMixIn(ImageContent.class, ImageContentMixin.class)).addMixIn(Image.class, ImageMixin.class)).addMixIn(AudioContent.class, AudioContentMixin.class)).addMixIn(Audio.class, AudioMixin.class)).addMixIn(VideoContent.class, VideoContentMixin.class)).addMixIn(Video.class, VideoMixin.class)).addMixIn(PdfFileContent.class, PdfFileContentMixin.class)).addMixIn(PdfFile.class, PdfFileMixin.class);
    }

    @Override
    public ChatMessage messageFromJson(String json) {
        try {
            return (ChatMessage)OBJECT_MAPPER.readValue(json, ChatMessage.class);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ChatMessage> messagesFromJson(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        try {
            List messages = (List)OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.constructType(MESSAGE_LIST_TYPE));
            return messages == null ? Collections.emptyList() : messages;
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String messageToJson(ChatMessage message) {
        try {
            return OBJECT_MAPPER.writeValueAsString((Object)message);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String messagesToJson(List<ChatMessage> messages) {
        try {
            return OBJECT_MAPPER.writeValueAsString(messages);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonDeserialize(builder=PdfFile.Builder.class)
    private static abstract class PdfFileMixin {
        private PdfFileMixin() {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class PdfFileContentMixin {
        @JsonCreator
        public PdfFileContentMixin(@JsonProperty(value="pdfFile") PdfFile pdfFile) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonDeserialize(builder=Video.Builder.class)
    private static abstract class VideoMixin {
        private VideoMixin() {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class VideoContentMixin {
        @JsonCreator
        public VideoContentMixin(@JsonProperty(value="video") Video video) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonDeserialize(builder=Audio.Builder.class)
    private static abstract class AudioMixin {
        private AudioMixin() {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class AudioContentMixin {
        @JsonCreator
        public AudioContentMixin(@JsonProperty(value="audio") Audio audio) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonDeserialize(builder=Image.Builder.class)
    private static abstract class ImageMixin {
        private ImageMixin() {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class ImageContentMixin {
        @JsonCreator
        public ImageContentMixin(@JsonProperty(value="image") Image image, @JsonProperty(value="detailLevel") ImageContent.DetailLevel detailLevel) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class TextContentMixin {
        @JsonCreator
        public TextContentMixin(@JsonProperty(value="text") String text) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="type")
    @JsonSubTypes(value={@JsonSubTypes.Type(value=TextContent.class, name="TEXT"), @JsonSubTypes.Type(value=ImageContent.class, name="IMAGE"), @JsonSubTypes.Type(value=AudioContent.class, name="AUDIO"), @JsonSubTypes.Type(value=VideoContent.class, name="VIDEO"), @JsonSubTypes.Type(value=PdfFileContent.class, name="PDF")})
    private static abstract class ContentMixin {
        private ContentMixin() {
        }

        @JsonProperty
        public abstract ContentType type();
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static class CustomMessageMixin {
        @JsonCreator
        public CustomMessageMixin(@JsonProperty(value="attributes") Map<String, Object> attributes) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonDeserialize(builder=ToolExecutionResultMessage.Builder.class)
    private static abstract class ToolExecutionResultMessageMixin {
        private ToolExecutionResultMessageMixin() {
        }

        @JsonProperty(value="isError")
        abstract Boolean isError();
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonDeserialize(builder=ToolExecutionRequest.Builder.class)
    private static abstract class ToolExecutionRequestMixin {
        private ToolExecutionRequestMixin() {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonDeserialize(builder=AiMessage.Builder.class)
    private static abstract class AiMessageMixin {
        private AiMessageMixin() {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    @JsonDeserialize(builder=UserMessage.Builder.class)
    private static abstract class UserMessageMixin {
        private UserMessageMixin() {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private static abstract class SystemMessageMixin {
        @JsonCreator
        public SystemMessageMixin(@JsonProperty(value="text") String text) {
        }
    }

    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="type")
    @JsonSubTypes(value={@JsonSubTypes.Type(value=SystemMessage.class, name="SYSTEM"), @JsonSubTypes.Type(value=UserMessage.class, name="USER"), @JsonSubTypes.Type(value=AiMessage.class, name="AI"), @JsonSubTypes.Type(value=ToolExecutionResultMessage.class, name="TOOL_EXECUTION_RESULT"), @JsonSubTypes.Type(value=CustomMessage.class, name="CUSTOM")})
    private static abstract class ChatMessageMixin {
        private ChatMessageMixin() {
        }

        @JsonProperty
        public abstract ChatMessageType type();
    }
}

