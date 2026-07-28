/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.cloud.vertexai.api.FunctionResponse
 *  com.google.cloud.vertexai.api.Part
 *  com.google.cloud.vertexai.generativeai.PartMaker
 *  com.google.protobuf.InvalidProtocolBufferException
 *  com.google.protobuf.Message$Builder
 *  com.google.protobuf.Struct
 *  com.google.protobuf.Struct$Builder
 *  com.google.protobuf.Value
 *  com.google.protobuf.util.JsonFormat
 *  dev.langchain4j.data.audio.Audio
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AudioContent
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.PdfFileContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.data.message.VideoContent
 *  dev.langchain4j.data.pdf.PdfFile
 *  dev.langchain4j.data.video.Video
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.vertexai.gemini;

import com.google.cloud.vertexai.api.FunctionResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.VideoContent;
import dev.langchain4j.data.pdf.PdfFile;
import dev.langchain4j.data.video.Video;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.vertexai.gemini.FunctionCallHelper;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PartsMapper {
    private static final Map<String, String> EXTENSION_TO_MIME_TYPE = new HashMap<String, String>();

    PartsMapper() {
    }

    static List<Part> map(ChatMessage message) {
        if (message instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage)message;
            ArrayList<Part> parts = new ArrayList<Part>();
            if (aiMessage.text() != null && !aiMessage.text().isEmpty()) {
                parts.add(Part.newBuilder().setText(aiMessage.text()).build());
            }
            if (aiMessage.hasToolExecutionRequests()) {
                List fnCallReqParts = aiMessage.toolExecutionRequests().stream().map(FunctionCallHelper::fromToolExecutionRequest).map(fnCall -> Part.newBuilder().setFunctionCall(fnCall).build()).collect(Collectors.toList());
                parts.addAll(fnCallReqParts);
            }
            return parts;
        }
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)message;
            return userMessage.contents().stream().map(PartsMapper::map).collect(Collectors.toList());
        }
        if (message instanceof SystemMessage) {
            SystemMessage systemMessage = (SystemMessage)message;
            return Collections.singletonList(Part.newBuilder().setText(systemMessage.text()).build());
        }
        if (message instanceof ToolExecutionResultMessage) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage)message;
            if (!toolExecutionResultMessage.hasSingleText()) {
                throw new UnsupportedFeatureException("Vertex AI Gemini does not support non-text content in tool results. Only text content is supported.");
            }
            String functionResponseText = toolExecutionResultMessage.text();
            Struct responseStruct = PartsMapper.parseFunctionResponseToStruct(functionResponseText);
            return Collections.singletonList(Part.newBuilder().setFunctionResponse(FunctionResponse.newBuilder().setName(toolExecutionResultMessage.toolName()).setResponse(responseStruct).build()).build());
        }
        throw Exceptions.illegalArgument((String)(message.type() + " message is not supported by Gemini"), (Object[])new Object[0]);
    }

    private static Part map(Content content) {
        if (content instanceof TextContent) {
            TextContent text = (TextContent)content;
            return PartsMapper.map(text);
        }
        if (content instanceof ImageContent) {
            ImageContent image = (ImageContent)content;
            return PartsMapper.map(image);
        }
        if (content instanceof AudioContent) {
            AudioContent audio = (AudioContent)content;
            return PartsMapper.map(audio);
        }
        if (content instanceof VideoContent) {
            VideoContent video = (VideoContent)content;
            return PartsMapper.map(video);
        }
        if (content instanceof PdfFileContent) {
            PdfFileContent pdf = (PdfFileContent)content;
            return PartsMapper.map(pdf);
        }
        throw Exceptions.illegalArgument((String)("Unknown content type: " + content), (Object[])new Object[0]);
    }

    private static Part map(TextContent content) {
        return Part.newBuilder().setText(content.text()).build();
    }

    static Part map(ImageContent content) {
        Image image = content.image();
        return PartsMapper.getPart(image.url(), image.mimeType(), image.base64Data());
    }

    static Part map(AudioContent content) {
        Audio audio = content.audio();
        return PartsMapper.getPart(audio.url(), audio.mimeType(), audio.base64Data());
    }

    static Part map(VideoContent content) {
        Video video = content.video();
        return PartsMapper.getPart(video.url(), video.mimeType(), video.base64Data());
    }

    static Part map(PdfFileContent content) {
        PdfFile pdfFile = content.pdfFile();
        return PartsMapper.getPart(pdfFile.url(), pdfFile.mimeType(), pdfFile.base64Data());
    }

    private static Part getPart(URI url, String mimeType, String base64data) {
        if (url != null) {
            String effectiveMimeType = (String)Utils.getOrDefault((Object)mimeType, () -> PartsMapper.detectMimeType(url));
            if (url.getScheme().equals("gs")) {
                return PartMaker.fromMimeTypeAndData((String)effectiveMimeType, (Object)url);
            }
            return PartMaker.fromMimeTypeAndData((String)effectiveMimeType, (Object)Utils.readBytes((String)url.toString()));
        }
        return PartMaker.fromMimeTypeAndData((String)mimeType, (Object)Base64.getDecoder().decode(base64data));
    }

    static String detectMimeType(URI url) {
        String extension;
        String mimeType;
        String[] pathParts = url.getPath().split("\\.");
        if (pathParts.length > 1 && (mimeType = EXTENSION_TO_MIME_TYPE.get(extension = pathParts[pathParts.length - 1].toLowerCase())) != null) {
            return mimeType;
        }
        throw Exceptions.illegalArgument((String)"Unable to detect the MIME type of '%s'. Please provide it explicitly.", (Object[])new Object[]{url});
    }

    private static Struct parseFunctionResponseToStruct(String functionResponseText) {
        try {
            Struct.Builder structBuilder = Struct.newBuilder();
            JsonFormat.parser().merge(functionResponseText, (Message.Builder)structBuilder);
            return structBuilder.build();
        }
        catch (InvalidProtocolBufferException structBuilder) {
            try {
                String wrappedJson = "{\"result\":" + functionResponseText + "}";
                Struct.Builder structBuilder2 = Struct.newBuilder();
                JsonFormat.parser().merge(wrappedJson, (Message.Builder)structBuilder2);
                return structBuilder2.build();
            }
            catch (InvalidProtocolBufferException invalidProtocolBufferException) {
                return Struct.newBuilder().putFields("result", Value.newBuilder().setStringValue(functionResponseText).build()).build();
            }
        }
    }

    static {
        EXTENSION_TO_MIME_TYPE.put("avif", "image/avif");
        EXTENSION_TO_MIME_TYPE.put("bmp", "image/bmp");
        EXTENSION_TO_MIME_TYPE.put("gif", "image/gif");
        EXTENSION_TO_MIME_TYPE.put("jpe", "image/jpeg");
        EXTENSION_TO_MIME_TYPE.put("jpeg", "image/jpeg");
        EXTENSION_TO_MIME_TYPE.put("jpg", "image/jpeg");
        EXTENSION_TO_MIME_TYPE.put("png", "image/png");
        EXTENSION_TO_MIME_TYPE.put("svg", "image/svg+xml");
        EXTENSION_TO_MIME_TYPE.put("tif", "image/tiff");
        EXTENSION_TO_MIME_TYPE.put("tiff", "image/tiff");
        EXTENSION_TO_MIME_TYPE.put("webp", "image/webp");
        EXTENSION_TO_MIME_TYPE.put("mp3", "audio/mp3");
        EXTENSION_TO_MIME_TYPE.put("wav", "audio/wav");
        EXTENSION_TO_MIME_TYPE.put("aac", "audio/aac");
        EXTENSION_TO_MIME_TYPE.put("flac", "audio/flac");
        EXTENSION_TO_MIME_TYPE.put("mpa", "audio/m4a");
        EXTENSION_TO_MIME_TYPE.put("mpga", "audio/mpga");
        EXTENSION_TO_MIME_TYPE.put("opus", "audio/opus");
        EXTENSION_TO_MIME_TYPE.put("pcm", "audio/pcm");
        EXTENSION_TO_MIME_TYPE.put("mp4", "video/mp4");
        EXTENSION_TO_MIME_TYPE.put("mpeg", "video/mpeg");
        EXTENSION_TO_MIME_TYPE.put("mpg", "video/mpg");
        EXTENSION_TO_MIME_TYPE.put("mpegps", "video/mpegps");
        EXTENSION_TO_MIME_TYPE.put("mov", "video/mov");
        EXTENSION_TO_MIME_TYPE.put("avi", "video/avi");
        EXTENSION_TO_MIME_TYPE.put("flv", "video/x-flv");
        EXTENSION_TO_MIME_TYPE.put("webm", "video/webm");
        EXTENSION_TO_MIME_TYPE.put("mmv", "video/wmv");
        EXTENSION_TO_MIME_TYPE.put("3gpp", "video/3gpp");
        EXTENSION_TO_MIME_TYPE.put("pdf", "application/pdf");
    }
}

