package dev.langchain4j.model.chat.common;

import dev.langchain4j.model.chat.response.ChatResponse;

public class ChatResponseAndStreamingMetadata {
    private final ChatResponse chatResponse;
    private final StreamingMetadata streamingMetadata;

    public ChatResponseAndStreamingMetadata(ChatResponse chatResponse, StreamingMetadata streamingMetadata) {
        this.chatResponse = chatResponse;
        this.streamingMetadata = streamingMetadata;
    }

    public ChatResponse chatResponse() {
        return chatResponse;
    }

    public StreamingMetadata streamingMetadata() {
        return streamingMetadata;
    }
}
