/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockDelta
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockDelta$Type
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStart$Type
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStartEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStopEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseMetrics
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseOutput
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse$Builder
 *  software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamMetadataEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.Message
 *  software.amazon.awssdk.services.bedrockruntime.model.Message$Builder
 *  software.amazon.awssdk.services.bedrockruntime.model.MessageStartEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.MessageStopEvent
 *  software.amazon.awssdk.services.bedrockruntime.model.ReasoningContentBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ReasoningContentBlockDelta
 *  software.amazon.awssdk.services.bedrockruntime.model.ReasoningTextBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolUseBlock
 *  software.amazon.awssdk.services.bedrockruntime.model.ToolUseBlock$Builder
 */
package dev.langchain4j.model.bedrock;

import dev.langchain4j.Internal;
import dev.langchain4j.model.bedrock.AwsDocumentConverter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockDelta;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStart;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStartEvent;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlockStopEvent;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseMetrics;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseOutput;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseResponse;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamMetadataEvent;
import software.amazon.awssdk.services.bedrockruntime.model.Message;
import software.amazon.awssdk.services.bedrockruntime.model.MessageStartEvent;
import software.amazon.awssdk.services.bedrockruntime.model.MessageStopEvent;
import software.amazon.awssdk.services.bedrockruntime.model.ReasoningContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ReasoningContentBlockDelta;
import software.amazon.awssdk.services.bedrockruntime.model.ReasoningTextBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ToolUseBlock;

@Internal
class ConverseResponseFromStreamBuilder {
    private final ConverseResponse.Builder converseResponseBuilder = ConverseResponse.builder();
    private Message.Builder messageBuilder = Message.builder();
    private final StringBuilder stringBuilder = new StringBuilder();
    private final boolean returnThinking;
    private final StringBuilder thinkingBuilder = new StringBuilder();
    private final StringBuilder thinkingSignatureBuilder = new StringBuilder();
    private ToolUseBlock.Builder toolUseBlockBuilder = null;
    private StringBuilder toolUseInputBuilder = new StringBuilder();
    private final List<ToolUseBlock> toolUseBlocks = new ArrayList<ToolUseBlock>();

    ConverseResponseFromStreamBuilder(boolean returnThinking) {
        this.returnThinking = returnThinking;
    }

    public ConverseResponseFromStreamBuilder append(ContentBlockStartEvent contentBlockStartEvent) {
        if (contentBlockStartEvent.start().type().equals((Object)ContentBlockStart.Type.TOOL_USE)) {
            this.toolUseBlockBuilder = ToolUseBlock.builder().toolUseId(contentBlockStartEvent.start().toolUse().toolUseId()).name(contentBlockStartEvent.start().toolUse().name());
        }
        return this;
    }

    public ConverseResponseFromStreamBuilder append(ContentBlockDelta delta) {
        if (delta.type().equals((Object)ContentBlockDelta.Type.TEXT)) {
            if (delta.text() != null) {
                this.stringBuilder.append(delta.text());
            }
        } else if (delta.type().equals((Object)ContentBlockDelta.Type.REASONING_CONTENT)) {
            ReasoningContentBlockDelta reasoningContent = delta.reasoningContent();
            if (reasoningContent.text() != null) {
                this.thinkingBuilder.append(reasoningContent.text());
            }
            if (reasoningContent.signature() != null) {
                this.thinkingSignatureBuilder.append(reasoningContent.signature());
            }
        } else if (delta.type().equals((Object)ContentBlockDelta.Type.TOOL_USE) && delta.toolUse().input() != null) {
            this.toolUseInputBuilder.append(delta.toolUse().input());
        }
        return this;
    }

    public ConverseResponseFromStreamBuilder append(ContentBlockStopEvent contentBlockStopEvent) {
        if (Objects.nonNull(this.toolUseBlockBuilder)) {
            if (this.toolUseInputBuilder.length() > 0) {
                this.toolUseBlockBuilder.input(AwsDocumentConverter.documentFromJson(this.toolUseInputBuilder.toString()));
            }
            this.toolUseBlocks.add((ToolUseBlock)this.toolUseBlockBuilder.build());
            this.toolUseInputBuilder = new StringBuilder();
            this.toolUseBlockBuilder = null;
        }
        return this;
    }

    public ConverseResponseFromStreamBuilder append(ConverseStreamMetadataEvent metadataEvent) {
        this.converseResponseBuilder.usage(metadataEvent.usage());
        this.converseResponseBuilder.metrics(builder -> {
            ConverseMetrics cfr_ignored_0 = (ConverseMetrics)builder.latencyMs(metadataEvent.metrics().latencyMs()).build();
        });
        if (metadataEvent.trace() != null) {
            this.converseResponseBuilder.trace(builder -> {
                builder.guardrail(metadataEvent.trace().guardrail()).build();
                builder.promptRouter(metadataEvent.trace().promptRouter()).build();
            });
        }
        return this;
    }

    public ConverseResponseFromStreamBuilder append(MessageStartEvent messageStartEvent) {
        this.messageBuilder = Message.builder();
        this.messageBuilder.role(messageStartEvent.role());
        return this;
    }

    public ConverseResponseFromStreamBuilder append(MessageStopEvent messageStopEvent) {
        this.converseResponseBuilder.stopReason(messageStopEvent.stopReason());
        this.converseResponseBuilder.additionalModelResponseFields(messageStopEvent.additionalModelResponseFields());
        if (Objects.nonNull(this.messageBuilder)) {
            ArrayList<Object> contents = new ArrayList<Object>();
            if (this.returnThinking && this.thinkingBuilder.length() > 0) {
                ReasoningContentBlock reasoningContent = (ReasoningContentBlock)ReasoningContentBlock.builder().reasoningText((ReasoningTextBlock)ReasoningTextBlock.builder().text(this.thinkingBuilder.toString()).signature(this.thinkingSignatureBuilder.toString()).build()).build();
                contents.add(ContentBlock.builder().reasoningContent(reasoningContent).build());
            }
            contents.add(ContentBlock.builder().text(this.stringBuilder.toString()).build());
            contents.addAll(this.toolUseBlocks.stream().map(ContentBlock::fromToolUse).collect(Collectors.toList()));
            this.converseResponseBuilder.output(builder -> {
                ConverseOutput cfr_ignored_0 = (ConverseOutput)builder.message((Message)this.messageBuilder.content((Collection)contents).build()).build();
            });
            this.messageBuilder = null;
        }
        return this;
    }

    public ConverseResponse build() {
        return (ConverseResponse)this.converseResponseBuilder.build();
    }
}

