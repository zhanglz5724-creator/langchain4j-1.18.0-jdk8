/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.agent.tool.ToolExecutionRequest
 *  dev.langchain4j.data.message.AiMessage
 *  dev.langchain4j.data.message.ChatMessage
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.ImageContent
 *  dev.langchain4j.data.message.PdfFileContent
 *  dev.langchain4j.data.message.SystemMessage
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.data.message.ToolExecutionResultMessage
 *  dev.langchain4j.data.message.UserMessage
 *  dev.langchain4j.exception.UnsupportedFeatureException
 *  dev.langchain4j.internal.Json
 *  dev.langchain4j.internal.JsonSchemaElementUtils
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.model.chat.Capability
 *  dev.langchain4j.model.chat.listener.ChatModelListener
 *  dev.langchain4j.model.chat.request.ChatRequest
 *  dev.langchain4j.model.chat.request.ChatRequestParameters
 *  dev.langchain4j.model.chat.request.DefaultChatRequestParameters
 *  dev.langchain4j.model.chat.request.ResponseFormat
 *  dev.langchain4j.model.chat.request.ResponseFormatType
 *  dev.langchain4j.model.chat.request.ToolChoice
 *  dev.langchain4j.model.chat.request.json.JsonRawSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchema
 *  dev.langchain4j.model.chat.request.json.JsonSchemaElement
 *  dev.langchain4j.model.output.FinishReason
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  software.amazon.awssdk.core.SdkBytes
 *  software.amazon.awssdk.core.document.Document
 *  software.amazon.awssdk.regions.Region
 *  software.amazon.awssdk.services.bedrockruntime.model.AnyToolChoice
 *  software.amazon.awssdk.services.bedrockruntime.model.CachePointBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.CachePointType
 *  software.amazon.awssdk.services.bedrockruntime.model.CacheTTL
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlock$Type
 *  software.amazon.awssdk.services.bedrockruntime.model.ConversationRole
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseTrace
 *  software.amazon.awssdk.services.bedrockruntime.model.DocumentBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.DocumentFormat
 *  software.amazon.awssdk.services.bedrockruntime.model.DocumentSource
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailAssessment
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailConfiguration
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailContentFilter
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailContentPolicyAssessment
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailContextualGroundingFilter
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailContextualGroundingPolicyAssessment
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailPiiEntityFilter
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailRegexFilter
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailSensitiveInformationPolicyAssessment
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailStreamConfiguration
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailStreamProcessingMode
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailTopic
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailTopicPolicyAssessment
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailTrace
 *  software.amazon.awssdk.services.bedrockruntime.model.GuardrailWordPolicyAssessment
 *  software.amazon.awssdk.services.bedrockruntime.model.ImageBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ImageSource
 *  software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration
 *  software.amazon.awssdk.services.bedrockruntime.model.JsonSchemaDefinition
 *  software.amazon.awssdk.services.bedrockruntime.model.Message
 *  software.amazon.awssdk.services.bedrockruntime.model.OutputConfig
 *  software.amazon.awssdk.services.bedrockruntime.model.OutputFormat
 *  software.amazon.awssdk.services.bedrockruntime.model.OutputFormatStructure
 *  software.amazon.awssdk.services.bedrockruntime.model.OutputFormatType
 *  software.amazon.awssdk.services.bedrockruntime.model.ReasoningContentBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ReasoningTextBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ServiceTier
 *  software.amazon.awssdk.services.bedrockruntime.model.ServiceTierType
 *  software.amazon.awssdk.services.bedrockruntime.model.StopReason
 *  software.amazon.awssdk.services.bedrockruntime.model.SystemContentBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.TokenUsage
 *  software.amazon.awssdk.services.bedrockruntime.model.Tool
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolChoice
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolConfiguration
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolConfiguration$Builder
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolInputSchema
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolResultBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolResultContentBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolSpecification
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolUseBlock
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.Internal;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.exception.UnsupportedFeatureException;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.JsonSchemaElementUtils;
import dev.langchain4j.model.bedrock.AwsDocumentConverter;
import dev.langchain4j.model.bedrock.BedrockCachePointPlacement;
import dev.langchain4j.model.bedrock.BedrockChatRequestParameters;
import dev.langchain4j.model.bedrock.BedrockGuardrailConfiguration;
import dev.langchain4j.model.bedrock.BedrockServiceTier;
import dev.langchain4j.model.bedrock.BedrockSystemContent;
import dev.langchain4j.model.bedrock.BedrockSystemMessage;
import dev.langchain4j.model.bedrock.BedrockSystemTextContent;
import dev.langchain4j.model.bedrock.BedrockTokenUsage;
import dev.langchain4j.model.bedrock.GuardrailAssessment;
import dev.langchain4j.model.bedrock.GuardrailAssessmentSummary;
import dev.langchain4j.model.bedrock.InputGuardrailAssessment;
import dev.langchain4j.model.bedrock.OutputGuardrailAssessment;
import dev.langchain4j.model.bedrock.Utils;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.ToolChoice;
import dev.langchain4j.model.chat.request.json.JsonRawSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.output.FinishReason;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.document.Document;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.model.AnyToolChoice;
import software.amazon.awssdk.services.bedrockruntime.model.CachePointBlock;
import software.amazon.awssdk.services.bedrockruntime.model.CachePointType;
import software.amazon.awssdk.services.bedrockruntime.model.CacheTTL;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseTrace;
import software.amazon.awssdk.services.bedrockruntime.model.DocumentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.DocumentFormat;
import software.amazon.awssdk.services.bedrockruntime.model.DocumentSource;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailContentFilter;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailContentPolicyAssessment;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailContextualGroundingFilter;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailContextualGroundingPolicyAssessment;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailPiiEntityFilter;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailRegexFilter;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailSensitiveInformationPolicyAssessment;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailStreamConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailStreamProcessingMode;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailTopic;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailTopicPolicyAssessment;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailTrace;
import software.amazon.awssdk.services.bedrockruntime.model.GuardrailWordPolicyAssessment;
import software.amazon.awssdk.services.bedrockruntime.model.ImageBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ImageSource;
import software.amazon.awssdk.services.bedrockruntime.model.InferenceConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.JsonSchemaDefinition;
import software.amazon.awssdk.services.bedrockruntime.model.Message;
import software.amazon.awssdk.services.bedrockruntime.model.OutputConfig;
import software.amazon.awssdk.services.bedrockruntime.model.OutputFormat;
import software.amazon.awssdk.services.bedrockruntime.model.OutputFormatStructure;
import software.amazon.awssdk.services.bedrockruntime.model.OutputFormatType;
import software.amazon.awssdk.services.bedrockruntime.model.ReasoningContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ReasoningTextBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ServiceTier;
import software.amazon.awssdk.services.bedrockruntime.model.ServiceTierType;
import software.amazon.awssdk.services.bedrockruntime.model.StopReason;
import software.amazon.awssdk.services.bedrockruntime.model.SystemContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.TokenUsage;
import software.amazon.awssdk.services.bedrockruntime.model.Tool;
import software.amazon.awssdk.services.bedrockruntime.model.ToolConfiguration;
import software.amazon.awssdk.services.bedrockruntime.model.ToolInputSchema;
import software.amazon.awssdk.services.bedrockruntime.model.ToolResultBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ToolResultContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ToolSpecification;
import software.amazon.awssdk.services.bedrockruntime.model.ToolUseBlock;

@Internal
abstract class AbstractBedrockChatModel {
    private static final Logger log = LoggerFactory.getLogger(AbstractBedrockChatModel.class);
    private static final String THINKING_SIGNATURE_KEY = "thinking_signature";
    private static final int MAX_CACHE_POINTS_PER_REQUEST = 4;
    private static final CachePointBlock DEFAULT_CACHE_POINT = (CachePointBlock)CachePointBlock.builder().type(CachePointType.DEFAULT).build();
    protected final Region region;
    protected final Duration timeout;
    protected final boolean returnThinking;
    protected final boolean sendThinking;
    protected final BedrockChatRequestParameters defaultRequestParameters;
    protected final List<ChatModelListener> listeners;
    protected final Set<Capability> supportedCapabilities;
    protected final Supplier<Map<String, String>> customHeadersSupplier;

    private static CachePointBlock buildCachePoint(CacheTTL cacheTtl) {
        if (cacheTtl == null) {
            return DEFAULT_CACHE_POINT;
        }
        return (CachePointBlock)CachePointBlock.builder().type(CachePointType.DEFAULT).ttl(cacheTtl).build();
    }

    protected AbstractBedrockChatModel(AbstractBuilder<?> builder) {
        ChatRequestParameters commonParameters;
        this.region = (Region)dev.langchain4j.internal.Utils.getOrDefault((Object)builder.region, (Object)Region.US_EAST_1);
        this.timeout = (Duration)dev.langchain4j.internal.Utils.getOrDefault((Object)builder.timeout, (Object)Duration.ofMinutes(1L));
        this.returnThinking = (Boolean)dev.langchain4j.internal.Utils.getOrDefault((Object)builder.returnThinking, (Object)false);
        this.sendThinking = (Boolean)dev.langchain4j.internal.Utils.getOrDefault((Object)builder.sendThinking, (Object)true);
        this.listeners = dev.langchain4j.internal.Utils.copy(builder.listeners);
        this.supportedCapabilities = dev.langchain4j.internal.Utils.copy(builder.supportedCapabilities);
        this.customHeadersSupplier = builder.customHeadersSupplier;
        if (builder.defaultRequestParameters != null) {
            AbstractBedrockChatModel.validate(builder.defaultRequestParameters);
            commonParameters = builder.defaultRequestParameters;
        } else {
            commonParameters = DefaultChatRequestParameters.EMPTY;
        }
        BedrockChatRequestParameters bedrockParameters = builder.defaultRequestParameters instanceof BedrockChatRequestParameters ? (BedrockChatRequestParameters)builder.defaultRequestParameters : BedrockChatRequestParameters.EMPTY;
        this.defaultRequestParameters = ((BedrockChatRequestParameters.Builder)((BedrockChatRequestParameters.Builder)((BedrockChatRequestParameters.Builder)((BedrockChatRequestParameters.Builder)((BedrockChatRequestParameters.Builder)((BedrockChatRequestParameters.Builder)((BedrockChatRequestParameters.Builder)BedrockChatRequestParameters.builder().modelName((String)dev.langchain4j.internal.Utils.getOrDefault((Object)builder.modelId, (Object)commonParameters.modelName()))).temperature(commonParameters.temperature())).topP(commonParameters.topP())).maxOutputTokens(commonParameters.maxOutputTokens())).stopSequences(commonParameters.stopSequences())).toolSpecifications(commonParameters.toolSpecifications())).toolChoice(commonParameters.toolChoice())).additionalModelRequestFields(bedrockParameters.additionalModelRequestFields()).promptCaching(bedrockParameters.cachePointPlacement(), bedrockParameters.cacheTtl()).guardrailConfiguration(bedrockParameters.bedrockGuardrailConfiguration()).build();
    }

    protected List<SystemContentBlock> extractSystemMessages(List<ChatMessage> messages) {
        return this.extractSystemMessages(messages, null, null);
    }

    protected List<SystemContentBlock> extractSystemMessages(List<ChatMessage> messages, BedrockCachePointPlacement cachePointPlacement) {
        return this.extractSystemMessages(messages, cachePointPlacement, null);
    }

    protected List<SystemContentBlock> extractSystemMessages(List<ChatMessage> messages, BedrockCachePointPlacement cachePointPlacement, CacheTTL cacheTtl) {
        if (messages == null) {
            return new ArrayList<SystemContentBlock>();
        }
        ArrayList<SystemContentBlock> systemBlocks = new ArrayList<SystemContentBlock>();
        boolean lastWasCoreSystemMessage = false;
        for (ChatMessage message : messages) {
            if (message == null) continue;
            if (message instanceof BedrockSystemMessage) {
                BedrockSystemMessage bedrockMsg = (BedrockSystemMessage)message;
                for (BedrockSystemContent content : bedrockMsg.contents()) {
                    if (content instanceof BedrockSystemTextContent) {
                        BedrockSystemTextContent textContent = (BedrockSystemTextContent)content;
                        systemBlocks.add((SystemContentBlock)SystemContentBlock.builder().text(textContent.text()).build());
                        if (!textContent.hasCachePoint()) continue;
                        systemBlocks.add((SystemContentBlock)SystemContentBlock.builder().cachePoint(AbstractBedrockChatModel.buildCachePoint(cacheTtl)).build());
                        continue;
                    }
                    throw new UnsupportedFeatureException("Unsupported BedrockSystemContent type: " + (Object)((Object)content.type()) + ". Only TEXT content is currently supported.");
                }
                lastWasCoreSystemMessage = false;
                continue;
            }
            if (!(message instanceof SystemMessage)) continue;
            SystemMessage systemMsg = (SystemMessage)message;
            systemBlocks.add((SystemContentBlock)SystemContentBlock.builder().text(systemMsg.text()).build());
            lastWasCoreSystemMessage = true;
        }
        if (cachePointPlacement == BedrockCachePointPlacement.AFTER_SYSTEM && !systemBlocks.isEmpty()) {
            if (lastWasCoreSystemMessage) {
                systemBlocks.add((SystemContentBlock)SystemContentBlock.builder().cachePoint(AbstractBedrockChatModel.buildCachePoint(cacheTtl)).build());
            } else {
                log.warn("BedrockCachePointPlacement.AFTER_SYSTEM is configured but ignored because the last system message is a BedrockSystemMessage with granular cache points. Use granular cache points within BedrockSystemMessage or ensure the last system message is a core SystemMessage.");
            }
        }
        return systemBlocks;
    }

    protected List<Message> extractRegularMessages(List<ChatMessage> messages) {
        return this.extractRegularMessages(messages, null, null);
    }

    protected List<Message> extractRegularMessages(List<ChatMessage> messages, BedrockCachePointPlacement cachePointPlacement) {
        return this.extractRegularMessages(messages, cachePointPlacement, null);
    }

    protected List<Message> extractRegularMessages(List<ChatMessage> messages, BedrockCachePointPlacement cachePointPlacement, CacheTTL cacheTtl) {
        int i;
        if (messages == null) {
            return new ArrayList<Message>();
        }
        ArrayList<Message> bedrockMessages = new ArrayList<Message>();
        ArrayList<ContentBlock> currentBlocks = new ArrayList<ContentBlock>();
        boolean firstUserMessageProcessed = false;
        int lastUserMessageIndex = -1;
        if (cachePointPlacement == BedrockCachePointPlacement.AFTER_LAST_USER_MESSAGE) {
            for (i = messages.size() - 1; i >= 0; --i) {
                if (!(messages.get(i) instanceof UserMessage)) continue;
                lastUserMessageIndex = i;
                break;
            }
        }
        for (i = 0; i < messages.size(); ++i) {
            ChatMessage msg = messages.get(i);
            if (msg == null) continue;
            if (msg instanceof ToolExecutionResultMessage) {
                ToolExecutionResultMessage toolResult = (ToolExecutionResultMessage)msg;
                this.handleToolResult(toolResult, currentBlocks, bedrockMessages, i, messages);
                continue;
            }
            if (!(msg instanceof UserMessage) && !(msg instanceof AiMessage)) continue;
            Message bedrockMessage = this.convertToBedRockMessage(msg);
            boolean shouldAddCachePoint = false;
            if (msg instanceof UserMessage) {
                if (cachePointPlacement == BedrockCachePointPlacement.AFTER_USER_MESSAGE && !firstUserMessageProcessed) {
                    shouldAddCachePoint = true;
                    firstUserMessageProcessed = true;
                } else if (cachePointPlacement == BedrockCachePointPlacement.AFTER_LAST_USER_MESSAGE && i == lastUserMessageIndex) {
                    shouldAddCachePoint = true;
                }
            }
            if (shouldAddCachePoint) {
                ArrayList<Object> contentWithCachePoint = new ArrayList<Object>(bedrockMessage.content());
                contentWithCachePoint.add(ContentBlock.builder().cachePoint(AbstractBedrockChatModel.buildCachePoint(cacheTtl)).build());
                bedrockMessage = (Message)Message.builder().role(bedrockMessage.role()).content(contentWithCachePoint).build();
            }
            bedrockMessages.add(bedrockMessage);
        }
        return bedrockMessages;
    }

    protected void handleToolResult(ToolExecutionResultMessage toolResult, List<ContentBlock> blocks, List<Message> bedrockMessages, int currentIndex, List<ChatMessage> allMessages) {
        boolean isLastOrNextIsNotToolResult;
        blocks.add(this.createToolResultBlock(toolResult));
        boolean bl = isLastOrNextIsNotToolResult = currentIndex + 1 >= allMessages.size() || !(allMessages.get(currentIndex + 1) instanceof ToolExecutionResultMessage);
        if (isLastOrNextIsNotToolResult) {
            bedrockMessages.add((Message)Message.builder().role(ConversationRole.USER).content(blocks).build());
            blocks.clear();
        }
    }

    protected ContentBlock createToolResultBlock(ToolExecutionResultMessage toolResult) {
        if (toolResult.hasSingleText()) {
            return (ContentBlock)ContentBlock.builder().toolResult((ToolResultBlock)ToolResultBlock.builder().toolUseId(toolResult.id()).content(new ToolResultContentBlock[]{(ToolResultContentBlock)ToolResultContentBlock.builder().text(toolResult.text()).build()}).build()).build();
        }
        ArrayList<Object> contentBlocks = new ArrayList<Object>();
        for (Content content : toolResult.contents()) {
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent)content;
                contentBlocks.add(ToolResultContentBlock.builder().text(textContent.text()).build());
                continue;
            }
            if (content instanceof ImageContent) {
                ImageContent imageContent = (ImageContent)content;
                SdkBytes bytes = SdkBytes.fromByteArray((byte[])(Objects.nonNull(imageContent.image().base64Data()) ? Base64.getDecoder().decode(imageContent.image().base64Data()) : dev.langchain4j.internal.Utils.readBytes((String)String.valueOf(imageContent.image().url()))));
                String imgFormat = Utils.extractAndValidateFormat(imageContent.image());
                contentBlocks.add(ToolResultContentBlock.builder().image((ImageBlock)ImageBlock.builder().format(imgFormat).source((ImageSource)ImageSource.builder().bytes(bytes).build()).build()).build());
                continue;
            }
            throw new UnsupportedFeatureException("Bedrock does not support content type '" + content.type() + "' in tool results. Only text and image content are supported.");
        }
        return (ContentBlock)ContentBlock.builder().toolResult((ToolResultBlock)ToolResultBlock.builder().toolUseId(toolResult.id()).content(contentBlocks).build()).build();
    }

    protected Message convertToBedRockMessage(ChatMessage message) {
        if (message instanceof UserMessage) {
            UserMessage userMsg = (UserMessage)message;
            return this.createUserMessage(userMsg);
        }
        if (message instanceof AiMessage) {
            AiMessage aiMsg = (AiMessage)message;
            return this.createAiMessage(aiMsg);
        }
        throw new IllegalArgumentException("Unsupported message type: " + message.getClass());
    }

    protected Message createUserMessage(UserMessage message) {
        return (Message)Message.builder().role(ConversationRole.USER).content(this.convertContents(message.contents())).build();
    }

    protected Message createAiMessage(AiMessage message) {
        ArrayList<Object> blocks = new ArrayList<Object>();
        if (this.sendThinking && message.thinking() != null) {
            ReasoningContentBlock reasoningContentBlock = (ReasoningContentBlock)ReasoningContentBlock.builder().reasoningText((ReasoningTextBlock)ReasoningTextBlock.builder().text(message.thinking()).signature((String)message.attribute(THINKING_SIGNATURE_KEY, String.class)).build()).build();
            blocks.add(ContentBlock.builder().reasoningContent(reasoningContentBlock).build());
        }
        if (message.text() != null) {
            blocks.add(ContentBlock.builder().text(message.text()).build());
        }
        if (message.hasToolExecutionRequests()) {
            blocks.addAll(this.convertToolRequests(message.toolExecutionRequests()));
        }
        return (Message)Message.builder().role(ConversationRole.ASSISTANT).content(blocks).build();
    }

    protected List<ContentBlock> convertToolRequests(List<ToolExecutionRequest> requests) {
        return requests.stream().map(req -> (ContentBlock)ContentBlock.builder().toolUse((ToolUseBlock)ToolUseBlock.builder().name(req.name()).toolUseId(req.id()).input(AwsDocumentConverter.documentFromJson(req.arguments())).build()).build()).collect(Collectors.toList());
    }

    protected List<ContentBlock> convertContents(List<Content> contents) {
        if (dev.langchain4j.internal.Utils.isNullOrEmpty(contents)) {
            return Collections.emptyList();
        }
        return contents.stream().map(this::convertContent).collect(Collectors.toList());
    }

    protected ContentBlock convertContent(Content content) {
        if (content instanceof TextContent) {
            TextContent text = (TextContent)content;
            return (ContentBlock)ContentBlock.builder().text(text.text()).build();
        }
        if (content instanceof PdfFileContent) {
            PdfFileContent pdfFileContent = (PdfFileContent)content;
            SdkBytes bytes = SdkBytes.fromByteArray((byte[])(Objects.nonNull(pdfFileContent.pdfFile().base64Data()) ? Base64.getDecoder().decode(pdfFileContent.pdfFile().base64Data()) : dev.langchain4j.internal.Utils.readBytes((String)String.valueOf(pdfFileContent.pdfFile().url()))));
            return (ContentBlock)ContentBlock.builder().document((DocumentBlock)DocumentBlock.builder().format(DocumentFormat.PDF).source((DocumentSource)DocumentSource.builder().bytes(bytes).build()).name(AbstractBedrockChatModel.extractFilenameWithoutExtensionFromUri(pdfFileContent.pdfFile().url())).build()).build();
        }
        if (content instanceof ImageContent) {
            ImageContent image = (ImageContent)content;
            return this.createImageBlock(image);
        }
        throw new IllegalArgumentException("Unsupported content type: " + content.getClass());
    }

    protected ContentBlock createImageBlock(ImageContent imageContent) {
        SdkBytes bytes = SdkBytes.fromByteArray((byte[])(Objects.nonNull(imageContent.image().base64Data()) ? Base64.getDecoder().decode(imageContent.image().base64Data()) : dev.langchain4j.internal.Utils.readBytes((String)String.valueOf(imageContent.image().url()))));
        String imgFormat = Utils.extractAndValidateFormat(imageContent.image());
        return (ContentBlock)ContentBlock.builder().image((ImageBlock)ImageBlock.builder().format(imgFormat).source((ImageSource)ImageSource.builder().bytes(bytes).build()).build()).build();
    }

    protected ToolConfiguration extractToolConfigurationFrom(ChatRequest chatRequest) {
        return this.extractToolConfigurationFrom(chatRequest, null, null);
    }

    protected ToolConfiguration extractToolConfigurationFrom(ChatRequest chatRequest, BedrockCachePointPlacement cachePointPlacement) {
        return this.extractToolConfigurationFrom(chatRequest, cachePointPlacement, null);
    }

    protected ToolConfiguration extractToolConfigurationFrom(ChatRequest chatRequest, BedrockCachePointPlacement cachePointPlacement, CacheTTL cacheTtl) {
        List toolSpecifications = chatRequest.toolSpecifications();
        ChatRequestParameters parameters = chatRequest.parameters();
        ArrayList<Object> allTools = new ArrayList<Object>();
        ToolConfiguration.Builder toolConfigurationBuilder = ToolConfiguration.builder();
        if (Objects.nonNull(toolSpecifications) && !toolSpecifications.isEmpty()) {
            List tools = toolSpecifications.stream().map(toolSpecification -> {
                ToolInputSchema toolInputSchema = (ToolInputSchema)ToolInputSchema.builder().json(AwsDocumentConverter.convertJsonObjectSchemaToDocument(toolSpecification)).build();
                return (ToolSpecification)ToolSpecification.builder().name(toolSpecification.name()).description(toolSpecification.description()).inputSchema(toolInputSchema).build();
            }).map(toolSpecification -> (Tool)Tool.builder().toolSpec(toolSpecification).build()).collect(Collectors.toList());
            allTools.addAll(tools);
            if (cachePointPlacement == BedrockCachePointPlacement.AFTER_TOOLS) {
                allTools.add(Tool.builder().cachePoint(AbstractBedrockChatModel.buildCachePoint(cacheTtl)).build());
            }
        }
        if (allTools.isEmpty()) {
            return null;
        }
        toolConfigurationBuilder.tools(allTools);
        if (Objects.nonNull(parameters) && ToolChoice.REQUIRED.equals((Object)parameters.toolChoice())) {
            toolConfigurationBuilder.toolChoice(software.amazon.awssdk.services.bedrockruntime.model.ToolChoice.fromAny((AnyToolChoice)((AnyToolChoice)AnyToolChoice.builder().build())));
        }
        return (ToolConfiguration)toolConfigurationBuilder.build();
    }

    protected void validateTotalCachePoints(List<ChatMessage> messages, BedrockCachePointPlacement cachePointPlacement, boolean hasTools) {
        int totalCachePoints = this.countTotalCachePoints(messages, cachePointPlacement, hasTools);
        if (totalCachePoints > 4) {
            throw new IllegalArgumentException("Total cache points (" + totalCachePoints + ") exceeds AWS Bedrock limit of " + 4 + " per request. Reduce cache points in BedrockSystemMessage or adjust BedrockCachePointPlacement settings.");
        }
    }

    private int countTotalCachePoints(List<ChatMessage> messages, BedrockCachePointPlacement cachePointPlacement, boolean hasTools) {
        if (messages == null) {
            return 0;
        }
        int count = 0;
        boolean hasUserMessage = false;
        boolean lastSystemIsCoreMessage = false;
        boolean hasAnySystemMessage = false;
        for (ChatMessage message : messages) {
            if (message == null) continue;
            if (message instanceof BedrockSystemMessage) {
                BedrockSystemMessage bedrockMsg = (BedrockSystemMessage)message;
                count += bedrockMsg.cachePointCount();
                lastSystemIsCoreMessage = false;
                hasAnySystemMessage = true;
                continue;
            }
            if (message instanceof SystemMessage) {
                lastSystemIsCoreMessage = true;
                hasAnySystemMessage = true;
                continue;
            }
            if (!(message instanceof UserMessage)) continue;
            hasUserMessage = true;
        }
        if (cachePointPlacement != null) {
            if (cachePointPlacement == BedrockCachePointPlacement.AFTER_SYSTEM && hasAnySystemMessage && lastSystemIsCoreMessage) {
                ++count;
            }
            if (cachePointPlacement == BedrockCachePointPlacement.AFTER_USER_MESSAGE && hasUserMessage) {
                ++count;
            }
            if (cachePointPlacement == BedrockCachePointPlacement.AFTER_LAST_USER_MESSAGE && hasUserMessage) {
                ++count;
            }
            if (cachePointPlacement == BedrockCachePointPlacement.AFTER_TOOLS && hasTools) {
                ++count;
            }
        }
        return count;
    }

    protected AiMessage aiMessageFrom(ConverseResponse converseResponse) {
        ArrayList<String> texts = new ArrayList<String>();
        String thinking = null;
        HashMap<String, String> attributes = null;
        ArrayList<ToolExecutionRequest> toolExecutionRequests = new ArrayList<ToolExecutionRequest>();
        for (ContentBlock cBlock : converseResponse.output().message().content()) {
            if (cBlock.type() == ContentBlock.Type.TOOL_USE) {
                toolExecutionRequests.add(ToolExecutionRequest.builder().name(cBlock.toolUse().name()).id(cBlock.toolUse().toolUseId()).arguments(AwsDocumentConverter.documentToJson(cBlock.toolUse().input())).build());
                continue;
            }
            if (cBlock.type() == ContentBlock.Type.TEXT) {
                if (!dev.langchain4j.internal.Utils.isNotNullOrEmpty((String)cBlock.text())) continue;
                texts.add(cBlock.text());
                continue;
            }
            if (cBlock.type() == ContentBlock.Type.REASONING_CONTENT) {
                ReasoningTextBlock reasoningTextBlock;
                ReasoningContentBlock reasoningContentBlock;
                if (!this.returnThinking || (reasoningContentBlock = cBlock.reasoningContent()) == null || (reasoningTextBlock = reasoningContentBlock.reasoningText()) == null) continue;
                if (dev.langchain4j.internal.Utils.isNotNullOrEmpty((String)reasoningTextBlock.text())) {
                    thinking = reasoningTextBlock.text();
                }
                if (!dev.langchain4j.internal.Utils.isNotNullOrEmpty((String)reasoningTextBlock.signature())) continue;
                HashMap<String, String> attrs = new HashMap<String, String>();
                attrs.put(THINKING_SIGNATURE_KEY, reasoningTextBlock.signature());
                attributes = attrs;
                continue;
            }
            throw new IllegalArgumentException("Unsupported content in LLM response. Content type: " + cBlock.type());
        }
        String text = texts.stream().collect(Collectors.joining("\n\n"));
        return AiMessage.builder().text(dev.langchain4j.internal.Utils.isNullOrEmpty((String)text) ? null : text).thinking(thinking).attributes(attributes).toolExecutionRequests(toolExecutionRequests).build();
    }

    protected BedrockTokenUsage tokenUsageFrom(TokenUsage tokenUsage) {
        return Optional.ofNullable(tokenUsage).map(usage -> BedrockTokenUsage.builder().inputTokenCount(tokenUsage.inputTokens()).outputTokenCount(tokenUsage.outputTokens()).cacheWriteInputTokens(tokenUsage.cacheWriteInputTokens()).cacheReadInputTokens(tokenUsage.cacheReadInputTokens()).build()).orElseGet(BedrockTokenUsage.builder()::build);
    }

    protected FinishReason finishReasonFrom(StopReason stopReason) {
        if (stopReason == StopReason.END_TURN || stopReason == StopReason.STOP_SEQUENCE) {
            return FinishReason.STOP;
        }
        if (stopReason == StopReason.MAX_TOKENS) {
            return FinishReason.LENGTH;
        }
        if (stopReason == StopReason.TOOL_USE) {
            return FinishReason.TOOL_EXECUTION;
        }
        if (stopReason == StopReason.CONTENT_FILTERED || stopReason == StopReason.GUARDRAIL_INTERVENED) {
            return FinishReason.CONTENT_FILTER;
        }
        throw new IllegalArgumentException("Unknown stop reason: " + stopReason);
    }

    protected InferenceConfiguration inferenceConfigFrom(ChatRequestParameters parameters) {
        return (InferenceConfiguration)InferenceConfiguration.builder().maxTokens(parameters.maxOutputTokens()).temperature(AbstractBedrockChatModel.dblToFloat(parameters.temperature())).topP(AbstractBedrockChatModel.dblToFloat(parameters.topP())).stopSequences((Collection)(dev.langchain4j.internal.Utils.isNullOrEmpty((Collection)parameters.stopSequences()) ? null : parameters.stopSequences())).build();
    }

    protected GuardrailConfiguration guardrailConfigFrom(BedrockGuardrailConfiguration bedrockGuardrailConfiguration) {
        if (bedrockGuardrailConfiguration == null) {
            return null;
        }
        return (GuardrailConfiguration)GuardrailConfiguration.builder().guardrailVersion(bedrockGuardrailConfiguration.guardrailVersion()).guardrailIdentifier(bedrockGuardrailConfiguration.guardrailIdentifier()).trace(GuardrailTrace.ENABLED).build();
    }

    protected GuardrailStreamConfiguration guardrailStreamConfigFrom(BedrockGuardrailConfiguration bedrockGuardrailConfiguration) {
        if (bedrockGuardrailConfiguration == null) {
            return null;
        }
        GuardrailStreamProcessingMode mode = null;
        if (bedrockGuardrailConfiguration.streamProcessingMode() != null) {
            switch (bedrockGuardrailConfiguration.streamProcessingMode()) {
                case SYNC: {
                    mode = GuardrailStreamProcessingMode.SYNC;
                    break;
                }
                case ASYNC: {
                    mode = GuardrailStreamProcessingMode.ASYNC;
                }
            }
        }
        return (GuardrailStreamConfiguration)GuardrailStreamConfiguration.builder().guardrailVersion(bedrockGuardrailConfiguration.guardrailVersion()).guardrailIdentifier(bedrockGuardrailConfiguration.guardrailIdentifier()).trace(GuardrailTrace.ENABLED).streamProcessingMode(mode).build();
    }

    protected ServiceTier serviceTierFor(BedrockServiceTier bedrockServiceTier) {
        ServiceTierType serviceTierType;
        if (bedrockServiceTier == null) {
            return null;
        }
        switch (bedrockServiceTier) {
            case PRIORITY: {
                serviceTierType = ServiceTierType.PRIORITY;
                break;
            }
            case DEFAULT: {
                serviceTierType = ServiceTierType.DEFAULT;
                break;
            }
            case FLEX: {
                serviceTierType = ServiceTierType.FLEX;
                break;
            }
            case RESERVED: {
                serviceTierType = ServiceTierType.RESERVED;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown service tier type: " + (Object)((Object)bedrockServiceTier));
            }
        }
        return (ServiceTier)ServiceTier.builder().type(serviceTierType).build();
    }

    protected Document additionalRequestModelFieldsFrom(ChatRequestParameters chatRequestParameters) {
        HashMap<String, Object> additionalModelRequestFieldsMap = new HashMap<String, Object>(this.defaultRequestParameters.additionalModelRequestFields());
        if (chatRequestParameters instanceof BedrockChatRequestParameters && Objects.nonNull(((BedrockChatRequestParameters)chatRequestParameters).additionalModelRequestFields())) {
            additionalModelRequestFieldsMap.putAll(((BedrockChatRequestParameters)chatRequestParameters).additionalModelRequestFields());
        }
        if (dev.langchain4j.internal.Utils.isNullOrEmpty(additionalModelRequestFieldsMap)) {
            return null;
        }
        return AwsDocumentConverter.convertAdditionalModelRequestFields(additionalModelRequestFieldsMap);
    }

    protected GuardrailAssessmentSummary guardrailAssessmentSummaryFrom(ConverseTrace trace) {
        Object policy6;
        if (trace == null) {
            return null;
        }
        GuardrailAssessmentSummary.Builder builder = GuardrailAssessmentSummary.builder();
        if (trace.guardrail().hasInputAssessment()) {
            ArrayList<GuardrailAssessment> inputAssessments = new ArrayList<GuardrailAssessment>();
            for (software.amazon.awssdk.services.bedrockruntime.model.GuardrailAssessment assessment : trace.guardrail().inputAssessment().values()) {
                Object contextualPolicy;
                GuardrailSensitiveInformationPolicyAssessment sensitivePolicy;
                GuardrailWordPolicyAssessment wordPolicy;
                GuardrailContentPolicyAssessment contentPolicy;
                GuardrailTopicPolicyAssessment topicPolicy = assessment.topicPolicy();
                if (topicPolicy != null && topicPolicy.topics() != null) {
                    for (GuardrailTopic policy2 : topicPolicy.topics()) {
                        inputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)InputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.TOPIC)).name(policy2.name())).action(policy2.actionAsString())).build());
                    }
                }
                if ((contentPolicy = assessment.contentPolicy()) != null && contentPolicy.filters() != null) {
                    GuardrailTopic policy2;
                    policy2 = contentPolicy.filters().iterator();
                    while (policy2.hasNext()) {
                        GuardrailContentFilter policy3 = (GuardrailContentFilter)policy2.next();
                        inputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)InputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.CONTENT)).name(policy3.typeAsString())).action(policy3.actionAsString())).build());
                    }
                }
                if ((wordPolicy = assessment.wordPolicy()) != null) {
                    if (wordPolicy.customWords() != null) {
                        for (Object policy4 : wordPolicy.customWords()) {
                            inputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)InputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.WORD)).name(policy4.match())).action(policy4.actionAsString())).build());
                        }
                    }
                    if (wordPolicy.managedWordLists() != null) {
                        for (Object policy4 : wordPolicy.managedWordLists()) {
                            inputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)InputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.WORD)).name(policy4.typeAsString())).action(policy4.actionAsString())).build());
                        }
                    }
                }
                if ((sensitivePolicy = assessment.sensitiveInformationPolicy()) != null) {
                    Object policy4;
                    if (sensitivePolicy.piiEntities() != null) {
                        policy4 = sensitivePolicy.piiEntities().iterator();
                        while (policy4.hasNext()) {
                            policy6 = (GuardrailPiiEntityFilter)policy4.next();
                            inputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)InputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.SENSITIVE)).name(policy6.typeAsString())).action(policy6.actionAsString())).build());
                        }
                    }
                    if (sensitivePolicy.regexes() != null) {
                        policy4 = sensitivePolicy.regexes().iterator();
                        while (policy4.hasNext()) {
                            policy6 = (GuardrailRegexFilter)policy4.next();
                            inputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)InputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.SENSITIVE)).name(policy6.name())).action(policy6.actionAsString())).build());
                        }
                    }
                }
                if ((contextualPolicy = assessment.contextualGroundingPolicy()) == null || contextualPolicy.filters() == null) continue;
                for (Object policy5 : contextualPolicy.filters()) {
                    inputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)InputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.CONTEXT)).name(policy5.typeAsString())).action(policy5.actionAsString())).build());
                }
            }
            builder.inputAssessments(inputAssessments);
        }
        if (trace.guardrail().hasOutputAssessments()) {
            ArrayList<GuardrailAssessment> outputAssessments = new ArrayList<GuardrailAssessment>();
            Map outputAssessmentValues = trace.guardrail().outputAssessments();
            if (outputAssessmentValues != null) {
                for (List assessments : outputAssessmentValues.values()) {
                    if (assessments == null) continue;
                    for (software.amazon.awssdk.services.bedrockruntime.model.GuardrailAssessment assessment : assessments) {
                        GuardrailContextualGroundingPolicyAssessment contextualPolicy;
                        GuardrailSensitiveInformationPolicyAssessment sensitivePolicy;
                        GuardrailWordPolicyAssessment wordPolicy;
                        GuardrailContentPolicyAssessment contentPolicy;
                        if (assessment == null) continue;
                        GuardrailTopicPolicyAssessment topicPolicy = assessment.topicPolicy();
                        if (topicPolicy != null && topicPolicy.topics() != null) {
                            for (Object policy6 : topicPolicy.topics()) {
                                outputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)OutputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.TOPIC)).name(policy6.name())).action(policy6.actionAsString())).build());
                            }
                        }
                        if ((contentPolicy = assessment.contentPolicy()) != null && contentPolicy.filters() != null) {
                            policy6 = contentPolicy.filters().iterator();
                            while (policy6.hasNext()) {
                                Object policy5;
                                policy5 = (GuardrailContentFilter)policy6.next();
                                outputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)OutputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.CONTENT)).name(policy5.typeAsString())).action(policy5.actionAsString())).build());
                            }
                        }
                        if ((wordPolicy = assessment.wordPolicy()) != null) {
                            if (wordPolicy.customWords() != null) {
                                for (Object policy7 : wordPolicy.customWords()) {
                                    outputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)OutputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.WORD)).name(policy7.match())).action(policy7.actionAsString())).build());
                                }
                            }
                            if (wordPolicy.managedWordLists() != null) {
                                for (Object policy7 : wordPolicy.managedWordLists()) {
                                    outputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)OutputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.WORD)).name(policy7.typeAsString())).action(policy7.actionAsString())).build());
                                }
                            }
                        }
                        if ((sensitivePolicy = assessment.sensitiveInformationPolicy()) != null) {
                            GuardrailPiiEntityFilter policy8;
                            Object policy7;
                            if (sensitivePolicy.piiEntities() != null) {
                                policy7 = sensitivePolicy.piiEntities().iterator();
                                while (policy7.hasNext()) {
                                    policy8 = (GuardrailPiiEntityFilter)policy7.next();
                                    outputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)OutputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.SENSITIVE)).name(policy8.typeAsString())).action(policy8.actionAsString())).build());
                                }
                            }
                            if (sensitivePolicy.regexes() != null) {
                                policy7 = sensitivePolicy.regexes().iterator();
                                while (policy7.hasNext()) {
                                    policy8 = (GuardrailRegexFilter)policy7.next();
                                    outputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)OutputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.SENSITIVE)).name(policy8.name())).action(policy8.actionAsString())).build());
                                }
                            }
                        }
                        if ((contextualPolicy = assessment.contextualGroundingPolicy()) == null || contextualPolicy.filters() == null) continue;
                        for (GuardrailContextualGroundingFilter policy9 : contextualPolicy.filters()) {
                            outputAssessments.add(((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)((GuardrailAssessment.Builder)OutputGuardrailAssessment.builder().policy(GuardrailAssessment.Policy.CONTEXT)).name(policy9.typeAsString())).action(policy9.actionAsString())).build());
                        }
                    }
                }
            }
            builder.outputAssessments(outputAssessments);
        }
        return builder.build();
    }

    protected static void validate(ChatRequestParameters parameters) {
        String errorTemplate = "%s is not supported yet by this model provider";
        if (parameters.topK() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'topK' parameter"));
        }
        if (parameters.frequencyPenalty() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'frequencyPenalty' parameter"));
        }
        if (parameters.presencePenalty() != null) {
            throw new UnsupportedFeatureException(String.format(errorTemplate, "'presencePenalty' parameter"));
        }
    }

    protected static OutputConfig outputConfigFrom(ResponseFormat responseFormat) {
        String jsonSchemaString;
        if (responseFormat == null || responseFormat.type() != ResponseFormatType.JSON) {
            return null;
        }
        JsonSchema jsonSchema = responseFormat.jsonSchema();
        if (jsonSchema == null) {
            throw new UnsupportedFeatureException("JSON response format is not supported without a schema");
        }
        if (jsonSchema.rootElement() instanceof JsonRawSchema) {
            JsonRawSchema rawSchema = (JsonRawSchema)jsonSchema.rootElement();
            jsonSchemaString = rawSchema.schema();
        } else {
            Map jsonSchemaMap = JsonSchemaElementUtils.toMap((JsonSchemaElement)jsonSchema.rootElement(), (boolean)true, (boolean)true, (String)"string");
            jsonSchemaString = Json.toJson((Object)jsonSchemaMap);
        }
        return (OutputConfig)OutputConfig.builder().textFormat((OutputFormat)OutputFormat.builder().type(OutputFormatType.JSON_SCHEMA).structure((OutputFormatStructure)OutputFormatStructure.builder().jsonSchema((JsonSchemaDefinition)JsonSchemaDefinition.builder().schema(jsonSchemaString).name(jsonSchema.name()).build()).build()).build()).build();
    }

    protected static Float dblToFloat(Double d) {
        if (Objects.isNull(d)) {
            return null;
        }
        return Float.valueOf(d.floatValue());
    }

    protected static String extractFilenameWithoutExtensionFromUri(URI uri) {
        String extractedCleanFileName = Utils.extractCleanFileName(uri);
        if (dev.langchain4j.internal.Utils.isNullOrEmpty((String)extractedCleanFileName)) {
            extractedCleanFileName = UUID.randomUUID().toString();
        }
        return extractedCleanFileName;
    }

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {
        protected Region region;
        protected String modelId;
        protected Duration timeout;
        protected Boolean returnThinking;
        protected Boolean sendThinking;
        protected ChatRequestParameters defaultRequestParameters;
        protected Boolean logRequests;
        protected Boolean logResponses;
        protected Logger logger;
        protected List<ChatModelListener> listeners;
        protected Set<Capability> supportedCapabilities;
        protected Supplier<Map<String, String>> customHeadersSupplier;

        public T self() {
            return (T)this;
        }

        public T defaultRequestParameters(ChatRequestParameters defaultRequestParameters) {
            this.defaultRequestParameters = defaultRequestParameters;
            return this.self();
        }

        public T region(Region region) {
            this.region = region;
            return this.self();
        }

        public T modelId(String modelId) {
            this.modelId = modelId;
            return this.self();
        }

        public T returnThinking(Boolean returnThinking) {
            this.returnThinking = returnThinking;
            return this.self();
        }

        public T sendThinking(Boolean sendThinking) {
            this.sendThinking = sendThinking;
            return this.self();
        }

        public T timeout(Duration timeout) {
            this.timeout = timeout;
            return this.self();
        }

        public T logRequests(Boolean logRequests) {
            this.logRequests = logRequests;
            return this.self();
        }

        public T logResponses(Boolean logResponses) {
            this.logResponses = logResponses;
            return this.self();
        }

        public T logger(Logger logger) {
            this.logger = logger;
            return this.self();
        }

        public T listeners(List<ChatModelListener> listeners) {
            this.listeners = listeners;
            return this.self();
        }

        public T listeners(ChatModelListener ... listeners) {
            return this.listeners(Arrays.asList(listeners));
        }

        public T supportedCapabilities(Set<Capability> supportedCapabilities) {
            this.supportedCapabilities = supportedCapabilities;
            return this.self();
        }

        public T supportedCapabilities(Capability ... supportedCapabilities) {
            this.supportedCapabilities = Arrays.stream(supportedCapabilities).collect(Collectors.toSet());
            return this.self();
        }

        public T customHeaders(Map<String, String> customHeaders) {
            this.customHeadersSupplier = () -> customHeaders;
            return this.self();
        }

        public T customHeaders(Supplier<Map<String, String>> customHeadersSupplier) {
            this.customHeadersSupplier = customHeadersSupplier;
            return this.self();
        }
    }
}

