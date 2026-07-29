/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  com.google.genai.types.Candidate
 *  com.google.genai.types.Content
 *  com.google.genai.types.FinishReason
 *  com.google.genai.types.FinishReason$Known
 *  com.google.genai.types.FunctionCall
 *  com.google.genai.types.FunctionCall$Builder
 *  com.google.genai.types.FunctionResponse
 *  com.google.genai.types.FunctionResponse$Builder
 *  com.google.genai.types.GenerateContentResponse
 *  com.google.genai.types.Part
 *  com.google.genai.types.Part$Builder
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.audio.Audio
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AiMessage$Builder
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
 *  dev.langchain4j.model.chat.response.ChatResponse
 *  dev.langchain4j.model.chat.response.ChatResponseMetadata
 *  dev.langchain4j.model.output.FinishReason
 *  dev.langchain4j.model.output.TokenUsage
 */
package dev.langchain4j.model.google.genai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.genai.types.Candidate;
import com.google.genai.types.FinishReason;
import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
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
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.google.genai.GoogleGenAiChatResponseMetadata;
import dev.langchain4j.model.output.TokenUsage;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

class GoogleGenAiContentMapper {
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>(){};
    private static final Map<String, String> EXTENSION_TO_MIME_TYPE = new HashMap<String, String>();
    private static final String SYSTEM_ROLE = "system";
    private static final String USER_ROLE = "user";
    private static final String MODEL_ROLE = "model";
    private static final String FUNCTION_ROLE = "function";

    static com.google.genai.types.Content toSystemInstruction(List<ChatMessage> messages) {
        String systemInstructions = messages.stream().filter(m -> m instanceof SystemMessage).map(m -> ((SystemMessage)m).text()).collect(Collectors.joining("\n"));
        if (systemInstructions.isEmpty()) {
            return null;
        }
        return com.google.genai.types.Content.builder().role(SYSTEM_ROLE).parts(new Part[]{Part.builder().text(systemInstructions).build()}).build();
    }

    static List<com.google.genai.types.Content> toContents(List<ChatMessage> messages) {
        ArrayList<com.google.genai.types.Content> contents = new ArrayList<com.google.genai.types.Content>();
        ArrayList<Part> currentFunctionParts = new ArrayList<Part>();
        for (ChatMessage message : messages) {
            if (message instanceof SystemMessage) continue;
            if (message instanceof ToolExecutionResultMessage) {
                String toolResult;
                ToolExecutionResultMessage toolMsg = (ToolExecutionResultMessage)message;
                try {
                    toolResult = toolMsg.text();
                }
                catch (IllegalStateException e) {
                    throw new UnsupportedFeatureException("Google Gen AI currently does not support non-text content in tool execution results");
                }
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("result", toolResult);
                FunctionResponse.Builder funcRespBuilder = FunctionResponse.builder().name(toolMsg.toolName()).response(responseMap);
                if (toolMsg.id() != null) {
                    funcRespBuilder.id(toolMsg.id());
                }
                currentFunctionParts.add(Part.builder().functionResponse(funcRespBuilder.build()).build());
                continue;
            }
            if (!currentFunctionParts.isEmpty()) {
                contents.add(com.google.genai.types.Content.builder().role(USER_ROLE).parts(currentFunctionParts).build());
                currentFunctionParts = new ArrayList();
            }
            contents.add(GoogleGenAiContentMapper.toContent(message));
        }
        if (!currentFunctionParts.isEmpty()) {
            contents.add(com.google.genai.types.Content.builder().role(USER_ROLE).parts(currentFunctionParts).build());
        }
        return contents;
    }

    static com.google.genai.types.Content toContent(ChatMessage message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)message;
            return com.google.genai.types.Content.builder().role(USER_ROLE).parts(GoogleGenAiContentMapper.toParts(userMessage)).build();
        }
        if (message instanceof AiMessage) {
            AiMessage aiMsg = (AiMessage)message;
            ArrayList<Part> parts = new ArrayList<Part>();
            if (aiMsg.text() != null) {
                parts.add(Part.builder().text(aiMsg.text()).build());
            }
            if (aiMsg.toolExecutionRequests() != null) {
                for (ToolExecutionRequest req : aiMsg.toolExecutionRequests()) {
                    String sigBase64;
                    Map args = new HashMap();
                    if (req.arguments() != null && !req.arguments().isEmpty()) {
                        try {
                            args = (Map)OBJECT_MAPPER.readValue(req.arguments(), MAP_TYPE_REFERENCE);
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    FunctionCall.Builder fcBuilder = FunctionCall.builder().name(req.name()).args(args);
                    if (req.id() != null) {
                        fcBuilder.id(req.id());
                    }
                    Part.Builder partBuilder = Part.builder().functionCall(fcBuilder.build());
                    if (req.id() != null && (sigBase64 = (String)aiMsg.attribute("thought_signature_" + req.id(), String.class)) != null) {
                        partBuilder.thoughtSignature(Base64.getDecoder().decode(sigBase64));
                    }
                    parts.add(partBuilder.build());
                }
            }
            return com.google.genai.types.Content.builder().role(MODEL_ROLE).parts(parts).build();
        }
        throw new IllegalArgumentException("Unknown message type: " + message.type());
    }

    static ChatResponse toChatResponse(GenerateContentResponse response, String modelName) {
        List<Candidate> candidates = response.candidates().orElse(Collections.emptyList());
        if (candidates.isEmpty()) {
            GoogleGenAiChatResponseMetadata emptyMetadata = GoogleGenAiChatResponseMetadata.builder()
                    .modelName(modelName)
                    .tokenUsage(new TokenUsage(0, 0))
                    .finishReason(dev.langchain4j.model.output.FinishReason.OTHER)
                    .build();
            return ChatResponse.builder().aiMessage(AiMessage.from("Empty response")).metadata(emptyMetadata).build();
        }
        Candidate candidate = (Candidate)candidates.get(0);
        com.google.genai.types.Content content = candidate.content().orElse(null);
        StringBuilder textBuilder = new StringBuilder();
        ArrayList<ToolExecutionRequest> toolRequests = new ArrayList<ToolExecutionRequest>();
        Map<String, Object> attributes = new HashMap<>();
        if (content != null) {
            List<Part> parts = content.parts().orElse(Collections.emptyList());
            for (Part part : parts) {
                String jsonArgs;
                if (part.text().isPresent()) {
                    textBuilder.append((String)part.text().get());
                }
                if (!part.functionCall().isPresent()) continue;
                FunctionCall fc = (FunctionCall)part.functionCall().get();
                String fnName = (String)fc.name().get();
                Map args = fc.args().orElse(Collections.emptyMap());
                try {
                    jsonArgs = OBJECT_MAPPER.writeValueAsString(args);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String id = fc.id().orElseGet(() -> UUID.randomUUID().toString());
                if (part.thoughtSignature().isPresent()) {
                    byte[] sig = (byte[])part.thoughtSignature().get();
                    attributes.put("thought_signature_" + id, Base64.getEncoder().encodeToString(sig));
                }
                toolRequests.add(ToolExecutionRequest.builder().id(id).name(fnName).arguments(jsonArgs).build());
            }
        }
        String text = textBuilder.toString();
        AiMessage.Builder aiMessageBuilder = AiMessage.builder();
        if (!toolRequests.isEmpty() && !Utils.isNullOrEmpty((String)text)) {
            aiMessageBuilder.text(text);
            aiMessageBuilder.toolExecutionRequests(toolRequests);
        } else if (!toolRequests.isEmpty()) {
            aiMessageBuilder.toolExecutionRequests(toolRequests);
        } else {
            aiMessageBuilder.text(text);
        }
        if (!attributes.isEmpty()) {
            aiMessageBuilder.attributes(attributes);
        }
        AiMessage aiMessage = aiMessageBuilder.build();
        TokenUsage usage = response.usageMetadata().map(meta -> {
            int promptTokenCount = meta.promptTokenCount().isPresent() ? (Integer)meta.promptTokenCount().get() : 0;
            int candidatesTokenCount = meta.candidatesTokenCount().isPresent() ? (Integer)meta.candidatesTokenCount().get() : 0;
            int totalTokenCount = meta.totalTokenCount().isPresent() ? (Integer)meta.totalTokenCount().get() : promptTokenCount + candidatesTokenCount;
            return new TokenUsage(Integer.valueOf(promptTokenCount), Integer.valueOf(candidatesTokenCount), Integer.valueOf(totalTokenCount));
        }).orElse(new TokenUsage(Integer.valueOf(0), Integer.valueOf(0)));
        dev.langchain4j.model.output.FinishReason finishReason = candidate.finishReason().map(GoogleGenAiContentMapper::mapFinishReason).orElseGet(() -> !toolRequests.isEmpty() ? dev.langchain4j.model.output.FinishReason.TOOL_EXECUTION : dev.langchain4j.model.output.FinishReason.STOP);
        GoogleGenAiChatResponseMetadata metadata = GoogleGenAiChatResponseMetadata.builder()
                .modelName(modelName)
                .tokenUsage(usage)
                .finishReason(finishReason)
                .rawResponse(response)
                .build();
        return ChatResponse.builder().aiMessage(aiMessage).metadata((ChatResponseMetadata)metadata).build();
    }

    private static List<Part> toParts(UserMessage userMessage) {
        return userMessage.contents().stream().map(GoogleGenAiContentMapper::map).collect(Collectors.toList());
    }

    private static Part map(Content content) {
        if (content instanceof TextContent) {
            TextContent textContent = (TextContent)content;
            return Part.fromText((String)textContent.text());
        }
        if (content instanceof ImageContent) {
            ImageContent imageContent = (ImageContent)content;
            Image image = imageContent.image();
            String mimeType = (String)Utils.getOrDefault((Object)image.mimeType(), (Object)"image/png");
            if (image.base64Data() != null) {
                return Part.fromBytes((byte[])Base64.getDecoder().decode(image.base64Data()), (String)mimeType);
            }
            if (image.url() != null) {
                return Part.fromUri((String)image.url().toString(), (String)mimeType);
            }
            throw Exceptions.illegalArgument((String)"Image must have either base64 data or URL", (Object[])new Object[0]);
        }
        if (content instanceof AudioContent) {
            AudioContent audioContent = (AudioContent)content;
            Audio audio = audioContent.audio();
            String mimeType = (String)Utils.getOrDefault((Object)audio.mimeType(), (Object)"audio/mp3");
            if (audio.base64Data() != null) {
                return Part.fromBytes((byte[])Base64.getDecoder().decode(audio.base64Data()), (String)mimeType);
            }
            if (audio.url() != null) {
                return Part.fromUri((String)audio.url().toString(), (String)mimeType);
            }
            if (audio.binaryData() != null) {
                return Part.fromBytes((byte[])audio.binaryData(), (String)mimeType);
            }
            throw Exceptions.illegalArgument((String)"Audio must have base64 data, binary data, or URL", (Object[])new Object[0]);
        }
        if (content instanceof VideoContent) {
            VideoContent videoContent = (VideoContent)content;
            Video video = videoContent.video();
            String mimeType = (String)Utils.getOrDefault((Object)video.mimeType(), (Object)"video/mp4");
            if (video.base64Data() != null) {
                return Part.fromBytes((byte[])Base64.getDecoder().decode(video.base64Data()), (String)mimeType);
            }
            if (video.url() != null) {
                return Part.fromUri((String)video.url().toString(), (String)mimeType);
            }
            throw Exceptions.illegalArgument((String)"Video must have either base64 data or URL", (Object[])new Object[0]);
        }
        if (content instanceof PdfFileContent) {
            PdfFileContent pdfFileContent = (PdfFileContent)content;
            PdfFile pdfFile = pdfFileContent.pdfFile();
            return GoogleGenAiContentMapper.getPart(pdfFile.url(), pdfFile.mimeType(), pdfFile.base64Data(), null);
        }
        throw Exceptions.illegalArgument((String)("Unknown content type: " + content), (Object[])new Object[0]);
    }

    private static Part getPart(URI url, String mimeType, String base64data, byte[] binaryData) {
        if (url != null) {
            String effectiveMimeType = (String)Utils.getOrDefault((Object)mimeType, () -> GoogleGenAiContentMapper.detectMimeType(url));
            if (url.getScheme().equals("gs")) {
                return GoogleGenAiContentMapper.fromMimeTypeAndData(effectiveMimeType, url);
            }
            return GoogleGenAiContentMapper.fromMimeTypeAndData(effectiveMimeType, Utils.readBytes((String)url.toString()));
        }
        if (binaryData != null) {
            return GoogleGenAiContentMapper.fromMimeTypeAndData(mimeType, binaryData);
        }
        return GoogleGenAiContentMapper.fromMimeTypeAndData(mimeType, Base64.getDecoder().decode(base64data));
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

    static Part fromMimeTypeAndData(String mimeType, byte[] bytes) {
        return Part.fromBytes((byte[])bytes, (String)mimeType);
    }

    static Part fromMimeTypeAndData(String mimeType, URI uri) {
        return Part.fromUri((String)uri.toString(), (String)mimeType);
    }

    static dev.langchain4j.model.output.FinishReason mapFinishReason(FinishReason finishReason) {
        if (finishReason == null) {
            return dev.langchain4j.model.output.FinishReason.OTHER;
        }
        FinishReason.Known known = finishReason.knownEnum();
        if (known == null) {
            return dev.langchain4j.model.output.FinishReason.OTHER;
        }
        switch (known) {
            case STOP: {
                return dev.langchain4j.model.output.FinishReason.STOP;
            }
            case MAX_TOKENS: {
                return dev.langchain4j.model.output.FinishReason.LENGTH;
            }
            case SAFETY: 
            case RECITATION: 
            case BLOCKLIST: 
            case PROHIBITED_CONTENT: 
            case SPII: 
            case IMAGE_SAFETY: 
            case IMAGE_PROHIBITED_CONTENT: 
            case IMAGE_RECITATION: {
                return dev.langchain4j.model.output.FinishReason.CONTENT_FILTER;
            }
        }
        return dev.langchain4j.model.output.FinishReason.OTHER;
    }

    private GoogleGenAiContentMapper() {
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
        EXTENSION_TO_MIME_TYPE.put("m4a", "audio/m4a");
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
        EXTENSION_TO_MIME_TYPE.put("wmv", "video/wmv");
        EXTENSION_TO_MIME_TYPE.put("3gpp", "video/3gpp");
        EXTENSION_TO_MIME_TYPE.put("pdf", "application/pdf");
        EXTENSION_TO_MIME_TYPE.put("txt", "text/plain");
        EXTENSION_TO_MIME_TYPE.put("log", "text/plain");
        EXTENSION_TO_MIME_TYPE.put("csv", "text/plain");
        EXTENSION_TO_MIME_TYPE.put("tsv", "text/plain");
        EXTENSION_TO_MIME_TYPE.put("xml", "text/plain");
        EXTENSION_TO_MIME_TYPE.put("json", "text/plain");
    }
}

