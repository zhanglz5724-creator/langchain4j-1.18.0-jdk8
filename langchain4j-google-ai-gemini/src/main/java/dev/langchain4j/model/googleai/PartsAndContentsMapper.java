/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.image.Image
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.AudioContent
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.ImageContent$DetailLevel
 *  dev.langchain4j.data.message.PdfFileContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.data.message.VideoContent
 *  dev.langchain4j.data.pdf.PdfFile
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.CustomMimeTypesFileTypeDetector
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.model.googleai;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
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
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.CustomMimeTypesFileTypeDetector;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.googleai.FunctionMapper;
import dev.langchain4j.model.googleai.GeminiContent;
import dev.langchain4j.model.googleai.GeminiMediaResolution;
import dev.langchain4j.model.googleai.GeminiMediaResolutionLevel;
import dev.langchain4j.model.googleai.GeminiRole;
import dev.langchain4j.model.googleai.Json;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class PartsAndContentsMapper {
    static final String THINKING_SIGNATURE_KEY = "thinking_signature";
    private static final CustomMimeTypesFileTypeDetector mimeTypeDetector = new CustomMimeTypesFileTypeDetector();
    private static final Pattern DATA_URI_PATTERN = Pattern.compile("^data:([^;,]+)(?:;[^,]*)?,(.*)$");

    private PartsAndContentsMapper() {
    }

    static GeminiContent.GeminiPart fromContentToGPart(Content content) {
        return PartsAndContentsMapper.fromContentToGPart(content, false);
    }

    static GeminiContent.GeminiPart fromContentToGPart(Content content, boolean mediaResolutionPerPartEnabled) {
        if (content instanceof TextContent) {
            TextContent textContent = (TextContent)content;
            return GeminiContent.GeminiPart.builder().text(textContent.text()).build();
        }
        if (content instanceof ImageContent) {
            GeminiMediaResolution mediaResolution;
            ImageContent imageContent = (ImageContent)content;
            Image image = imageContent.image();
            GeminiMediaResolution geminiMediaResolution = mediaResolution = mediaResolutionPerPartEnabled ? PartsAndContentsMapper.toGeminiMediaResolution(imageContent.detailLevel()) : null;
            if (!Utils.isNullOrBlank((String)image.base64Data())) {
                return GeminiContent.GeminiPart.builder().inlineData(new GeminiContent.GeminiPart.GeminiBlob(image.mimeType(), image.base64Data())).mediaResolution(mediaResolution).build();
            }
            if (image.url() != null) {
                URI url = image.url();
                if (url.getScheme() != null && url.getScheme().equals("data")) {
                    return GeminiContent.GeminiPart.builder().inlineData(PartsAndContentsMapper.parseDataUri(url)).mediaResolution(mediaResolution).build();
                }
                if (url.getScheme() != null && url.getScheme().startsWith("http")) {
                    byte[] imageBytes = Utils.readBytes((String)url.toString());
                    String base64Data = Base64.getEncoder().encodeToString(imageBytes);
                    return GeminiContent.GeminiPart.builder().inlineData(new GeminiContent.GeminiPart.GeminiBlob((String)Utils.getOrDefault((Object)image.mimeType(), (Object)mimeTypeDetector.probeContentType(url)), base64Data)).mediaResolution(mediaResolution).build();
                }
                return GeminiContent.GeminiPart.builder().fileData(new GeminiContent.GeminiPart.GeminiFileData((String)Utils.getOrDefault((Object)image.mimeType(), (Object)mimeTypeDetector.probeContentType(url)), url.toString())).mediaResolution(mediaResolution).build();
            }
            throw new IllegalArgumentException("Image should contain either base64 data or url");
        }
        if (content instanceof AudioContent) {
            AudioContent audioContent = (AudioContent)content;
            URI uri = audioContent.audio().url();
            if (uri != null) {
                if (uri.getScheme() != null && uri.getScheme().equals("data")) {
                    return GeminiContent.GeminiPart.builder().inlineData(PartsAndContentsMapper.parseDataUri(uri)).build();
                }
                return GeminiContent.GeminiPart.builder().fileData(new GeminiContent.GeminiPart.GeminiFileData(mimeTypeDetector.probeContentType(uri), uri.toString())).build();
            }
            return GeminiContent.GeminiPart.builder().inlineData(new GeminiContent.GeminiPart.GeminiBlob(audioContent.audio().mimeType(), audioContent.audio().base64Data())).build();
        }
        if (content instanceof VideoContent) {
            VideoContent videoContent = (VideoContent)content;
            URI uri = videoContent.video().url();
            if (uri != null) {
                if (uri.getScheme() != null && uri.getScheme().equals("data")) {
                    return GeminiContent.GeminiPart.builder().inlineData(PartsAndContentsMapper.parseDataUri(uri)).build();
                }
                return GeminiContent.GeminiPart.builder().fileData(new GeminiContent.GeminiPart.GeminiFileData(mimeTypeDetector.probeContentType(uri), uri.toString())).build();
            }
            return GeminiContent.GeminiPart.builder().inlineData(new GeminiContent.GeminiPart.GeminiBlob(videoContent.video().mimeType(), videoContent.video().base64Data())).build();
        }
        if (content instanceof PdfFileContent) {
            PdfFileContent pdfFileContent = (PdfFileContent)content;
            PdfFile pdfFile = pdfFileContent.pdfFile();
            URI uri = pdfFile.url();
            if (uri != null) {
                if (uri.getScheme() != null && uri.getScheme().equals("data")) {
                    return GeminiContent.GeminiPart.builder().inlineData(PartsAndContentsMapper.parseDataUri(uri)).build();
                }
                return GeminiContent.GeminiPart.builder().fileData(new GeminiContent.GeminiPart.GeminiFileData(mimeTypeDetector.probeContentType(uri), uri.toString())).build();
            }
            return GeminiContent.GeminiPart.builder().inlineData(new GeminiContent.GeminiPart.GeminiBlob(pdfFile.mimeType(), pdfFile.base64Data())).build();
        }
        throw new UnsupportedFeatureException("Unsupported content type: " + content.type());
    }

    private static GeminiMediaResolution toGeminiMediaResolution(ImageContent.DetailLevel detailLevel) {
        if (detailLevel == null) {
            return null;
        }
        switch (detailLevel) {
            case LOW: {
                return GeminiMediaResolution.of(GeminiMediaResolutionLevel.MEDIA_RESOLUTION_LOW);
            }
            case MEDIUM: {
                return GeminiMediaResolution.of(GeminiMediaResolutionLevel.MEDIA_RESOLUTION_MEDIUM);
            }
            case HIGH: {
                return GeminiMediaResolution.of(GeminiMediaResolutionLevel.MEDIA_RESOLUTION_HIGH);
            }
            case ULTRA_HIGH: {
                return GeminiMediaResolution.of(GeminiMediaResolutionLevel.MEDIA_RESOLUTION_ULTRA_HIGH);
            }
            case AUTO: {
                return GeminiMediaResolution.of(GeminiMediaResolutionLevel.MEDIA_RESOLUTION_UNSPECIFIED);
            }
        }
        throw new IllegalArgumentException("Unknown detail level: " + detailLevel);
    }

    static AiMessage fromGPartsToAiMessage(List<GeminiContent.GeminiPart> parts, boolean includeCodeExecutionOutput, Boolean returnThinking) {
        List<GeminiContent.GeminiPart> safeParts = parts != null ? parts : Collections.emptyList();
        StringBuilder fullText = new StringBuilder();
        ArrayList<String> thoughts = new ArrayList<String>();
        ArrayList<String> thoughtSignatures = new ArrayList<String>();
        ArrayList<GeminiContent.GeminiPart.GeminiFunctionCall> functionCalls = new ArrayList<GeminiContent.GeminiPart.GeminiFunctionCall>();
        ArrayList<Image> generatedImages = new ArrayList<Image>();
        for (GeminiContent.GeminiPart part : safeParts) {
            GeminiContent.GeminiPart.GeminiBlob inlineData;
            String text;
            GeminiContent.GeminiPart.GeminiCodeExecutionResult codeExecutionResult;
            GeminiContent.GeminiPart.GeminiExecutableCode executableCode = part.executableCode();
            if (executableCode != null && includeCodeExecutionOutput) {
                fullText.append("Code executed:\n").append("```python\n").append(executableCode.code()).append("\n```\n");
            }
            if ((codeExecutionResult = part.codeExecutionResult()) != null && includeCodeExecutionOutput) {
                GeminiContent.GeminiPart.GeminiCodeExecutionResult.GeminiOutcome outcome = codeExecutionResult.outcome();
                if (outcome != GeminiContent.GeminiPart.GeminiCodeExecutionResult.GeminiOutcome.OUTCOME_OK) {
                    fullText.append("Code execution failed: **").append(outcome.name()).append("**\n").append(part.text() != null ? part.text() : "");
                } else {
                    fullText.append("Output:\n").append("```\n").append(codeExecutionResult.output()).append("```\n");
                }
            }
            if (Utils.isNotNullOrEmpty((String)(text = part.text()))) {
                if (Boolean.TRUE.equals(part.isThought())) {
                    if (Boolean.TRUE.equals(returnThinking)) {
                        thoughts.add(text);
                    } else if (returnThinking == null) {
                        if (fullText.length() > 0) {
                            fullText.append("\n\n");
                        }
                        fullText.append(text);
                    }
                } else {
                    if (fullText.length() > 0) {
                        fullText.append("\n\n");
                    }
                    fullText.append(text);
                }
            }
            String thoughtSignature = part.thoughtSignature();
            if (Boolean.TRUE.equals(returnThinking) && Utils.isNotNullOrEmpty((String)thoughtSignature)) {
                thoughtSignatures.add(thoughtSignature);
            }
            if (part.functionCall() != null) {
                functionCalls.add(part.functionCall());
            }
            if ((inlineData = part.inlineData()) == null || inlineData.mimeType() == null || !inlineData.mimeType().startsWith("image/") || inlineData.data() == null) continue;
            Image generatedImage = Image.builder().base64Data(inlineData.data()).mimeType(inlineData.mimeType()).build();
            generatedImages.add(generatedImage);
        }
        String text = fullText.toString();
        String thinking = thoughts.stream().collect(Collectors.joining("\n\n"));
        String thinkingSignature = thoughtSignatures.stream().collect(Collectors.joining("\n\n"));
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        if (Utils.isNotNullOrEmpty((String)thinkingSignature)) {
            attributes.put(THINKING_SIGNATURE_KEY, thinkingSignature);
        }
        if (!generatedImages.isEmpty()) {
            attributes.put("generated_images", generatedImages);
        }
        return AiMessage.builder().text(Utils.isNullOrEmpty((String)text) ? null : text).thinking(Utils.isNullOrEmpty((String)thinking) ? null : thinking).toolExecutionRequests(FunctionMapper.toToolExecutionRequests(functionCalls)).attributes(attributes.isEmpty() ? Collections.emptyMap() : attributes).build();
    }

    static List<GeminiContent> fromMessageToGContent(List<ChatMessage> messages, GeminiContent systemInstruction, boolean sendThinking) {
        return PartsAndContentsMapper.fromMessageToGContent(messages, systemInstruction, sendThinking, false);
    }

    static List<GeminiContent> fromMessageToGContent(List<ChatMessage> messages, GeminiContent systemInstruction, boolean sendThinking, boolean mediaResolutionPerPartEnabled) {
        return messages.stream().map(msg -> {
            switch (msg.type()) {
                case SYSTEM: {
                    SystemMessage systemMessage = (SystemMessage)msg;
                    if (systemInstruction != null) {
                        systemInstruction.addPart(GeminiContent.GeminiPart.builder().text(systemMessage.text()).build());
                        return null;
                    }
                    if (Utils.isNotNullOrEmpty((String)systemMessage.text())) {
                        return new GeminiContent(Collections.singletonList(GeminiContent.GeminiPart.builder().text(systemMessage.text()).build()), GeminiRole.MODEL.toString());
                    }
                    return null;
                }
                case AI: {
                    AiMessage aiMessage = (AiMessage)msg;
                    ArrayList<GeminiContent.GeminiPart> parts = new ArrayList<GeminiContent.GeminiPart>();
                    if (sendThinking && Utils.isNotNullOrEmpty((String)aiMessage.thinking())) {
                        parts.add(GeminiContent.GeminiPart.builder().text(aiMessage.thinking()).thought(true).build());
                    }
                    if (Utils.isNotNullOrEmpty((String)aiMessage.text())) {
                        parts.add(GeminiContent.GeminiPart.builder().text(aiMessage.text()).build());
                    }
                    if (aiMessage.hasToolExecutionRequests()) {
                        String thoughtSignature = null;
                        if (sendThinking) {
                            thoughtSignature = (String)aiMessage.attribute(THINKING_SIGNATURE_KEY, String.class);
                        }
                        parts.addAll(PartsAndContentsMapper.toGeminiParts(aiMessage.toolExecutionRequests(), thoughtSignature));
                    }
                    return new GeminiContent(parts, GeminiRole.MODEL.toString());
                }
                case USER: {
                    UserMessage userMessage = (UserMessage)msg;
                    return new GeminiContent(userMessage.contents().stream().map(content -> PartsAndContentsMapper.fromContentToGPart(content, mediaResolutionPerPartEnabled)).collect(Collectors.toList()), GeminiRole.USER.toString());
                }
                case TOOL_EXECUTION_RESULT: {
                    ToolExecutionResultMessage toolResultMessage = (ToolExecutionResultMessage)msg;
                    if (!toolResultMessage.hasSingleText()) {
                        ArrayList<GeminiContent.GeminiPart> toolParts = new ArrayList<GeminiContent.GeminiPart>();
                        HashMap<String, String> responseMap = new HashMap<String, String>();
                        for (Content content2 : toolResultMessage.contents()) {
                            if (content2 instanceof TextContent) {
                                TextContent textContent = (TextContent)content2;
                                responseMap.put("response", textContent.text());
                                continue;
                            }
                            if (content2 instanceof ImageContent) {
                                ImageContent imageContent = (ImageContent)content2;
                                toolParts.add(PartsAndContentsMapper.fromContentToGPart((Content)imageContent, mediaResolutionPerPartEnabled));
                                continue;
                            }
                            throw new UnsupportedFeatureException("Google AI Gemini does not support content type '" + content2.type() + "' in tool results.");
                        }
                        if (responseMap.isEmpty()) {
                            responseMap.put("response", "");
                        }
                        toolParts.add(0, GeminiContent.GeminiPart.builder().functionResponse(new GeminiContent.GeminiPart.GeminiFunctionResponse(toolResultMessage.id(), toolResultMessage.toolName(), responseMap)).build());
                        return new GeminiContent(toolParts, GeminiRole.USER.toString());
                    }
                    return new GeminiContent(Collections.singletonList(GeminiContent.GeminiPart.builder().functionResponse(new GeminiContent.GeminiPart.GeminiFunctionResponse(toolResultMessage.id(), toolResultMessage.toolName(), Collections.singletonMap("response", toolResultMessage.text()))).build()), GeminiRole.USER.toString());
                }
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static GeminiContent.GeminiPart.GeminiBlob parseDataUri(URI uri) {
        String urlString = uri.toString();
        Matcher matcher = DATA_URI_PATTERN.matcher(urlString);
        if (matcher.matches()) {
            String mimeType = matcher.group(1);
            String base64Data = matcher.group(2);
            return new GeminiContent.GeminiPart.GeminiBlob(mimeType, base64Data);
        }
        throw new IllegalArgumentException("Invalid data URI format: " + urlString);
    }

    private static List<GeminiContent.GeminiPart> toGeminiParts(List<ToolExecutionRequest> toolExecutionRequests, String thoughtSignature) {
        ArrayList<GeminiContent.GeminiPart> geminiParts = new ArrayList<GeminiContent.GeminiPart>();
        for (int i = 0; i < toolExecutionRequests.size(); ++i) {
            ToolExecutionRequest toolExecutionRequest = toolExecutionRequests.get(i);
            boolean shouldAddThoughtSignature = i == 0 && Utils.isNotNullOrEmpty((String)thoughtSignature);
            GeminiContent.GeminiPart geminiPart = GeminiContent.GeminiPart.builder().functionCall(new GeminiContent.GeminiPart.GeminiFunctionCall(toolExecutionRequest.id(), toolExecutionRequest.name(), Json.fromJson(toolExecutionRequest.arguments(), Map.class))).thoughtSignature(shouldAddThoughtSignature ? thoughtSignature : null).build();
            geminiParts.add(geminiPart);
        }
        return geminiParts;
    }
}

