/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.injector;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.injector.ContentInjector;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultContentInjector
implements ContentInjector {
    public static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = PromptTemplate.from("{{userMessage}}\n\nAnswer using the following information:\n{{contents}}");
    private final PromptTemplate promptTemplate;
    private final List<String> metadataKeysToInclude;

    public DefaultContentInjector() {
        this(DEFAULT_PROMPT_TEMPLATE, null);
    }

    public DefaultContentInjector(List<String> metadataKeysToInclude) {
        this(DEFAULT_PROMPT_TEMPLATE, ValidationUtils.ensureNotEmpty(metadataKeysToInclude, "metadataKeysToInclude"));
    }

    public DefaultContentInjector(PromptTemplate promptTemplate) {
        this(ValidationUtils.ensureNotNull(promptTemplate, "promptTemplate"), null);
    }

    public DefaultContentInjector(PromptTemplate promptTemplate, List<String> metadataKeysToInclude) {
        this.promptTemplate = Utils.getOrDefault(promptTemplate, DEFAULT_PROMPT_TEMPLATE);
        this.metadataKeysToInclude = Utils.copy(metadataKeysToInclude);
    }

    public static DefaultContentInjectorBuilder builder() {
        return new DefaultContentInjectorBuilder();
    }

    @Override
    public ChatMessage inject(List<Content> contents, ChatMessage chatMessage) {
        if (contents.isEmpty()) {
            return chatMessage;
        }
        Prompt prompt = this.createPrompt(chatMessage, contents);
        if (chatMessage instanceof UserMessage) {
            UserMessage userMessage = (UserMessage)chatMessage;
            return userMessage.toBuilder().contents(Collections.singletonList(TextContent.from(prompt.text()))).build();
        }
        return prompt.toUserMessage();
    }

    protected Prompt createPrompt(ChatMessage chatMessage, List<Content> contents) {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("userMessage", ((UserMessage)chatMessage).singleText());
        variables.put("contents", this.format(contents));
        return this.promptTemplate.apply(variables);
    }

    protected String format(List<Content> contents) {
        return contents.stream().map(this::format).collect(Collectors.joining("\n\n"));
    }

    protected String format(Content content) {
        TextSegment segment = content.textSegment();
        if (this.metadataKeysToInclude.isEmpty()) {
            return segment.text();
        }
        String segmentContent = segment.text();
        String segmentMetadata = this.format(segment.metadata());
        return this.format(segmentContent, segmentMetadata);
    }

    protected String format(Metadata metadata) {
        StringBuilder formattedMetadata = new StringBuilder();
        for (String metadataKey : this.metadataKeysToInclude) {
            String metadataValue = metadata.getString(metadataKey);
            if (metadataValue == null) continue;
            if (formattedMetadata.length() != 0) {
                formattedMetadata.append("\n");
            }
            formattedMetadata.append(metadataKey).append(": ").append(metadataValue);
        }
        return formattedMetadata.toString();
    }

    protected String format(String segmentContent, String segmentMetadata) {
        return segmentMetadata.isEmpty() ? segmentContent : String.format("content: %s\n%s", segmentContent, segmentMetadata);
    }

    public static class DefaultContentInjectorBuilder {
        private PromptTemplate promptTemplate;
        private List<String> metadataKeysToInclude;

        DefaultContentInjectorBuilder() {
        }

        public DefaultContentInjectorBuilder promptTemplate(PromptTemplate promptTemplate) {
            this.promptTemplate = promptTemplate;
            return this;
        }

        public DefaultContentInjectorBuilder metadataKeysToInclude(List<String> metadataKeysToInclude) {
            this.metadataKeysToInclude = metadataKeysToInclude;
            return this;
        }

        public DefaultContentInjector build() {
            return new DefaultContentInjector(this.promptTemplate, this.metadataKeysToInclude);
        }
    }
}

