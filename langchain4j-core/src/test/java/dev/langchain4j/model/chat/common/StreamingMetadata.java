package dev.langchain4j.model.chat.common;

import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import java.util.List;
import java.util.Set;

public class StreamingMetadata {
    private final String concatenatedPartialResponses;
    private final int timesOnPartialResponseWasCalled;
    private final int timesOnPartialThinkingWasCalled;
    private final List<PartialToolCall> partialToolCalls;
    private final List<CompleteToolCall> completeToolCalls;
    private final int timesOnCompleteResponseWasCalled;
    private final Set<Thread> threads;
    private final StreamingChatResponseHandler handler;

    public StreamingMetadata(String concatenatedPartialResponses, int timesOnPartialResponseWasCalled,
            int timesOnPartialThinkingWasCalled, List<PartialToolCall> partialToolCalls,
            List<CompleteToolCall> completeToolCalls, int timesOnCompleteResponseWasCalled,
            Set<Thread> threads, StreamingChatResponseHandler handler) {
        this.concatenatedPartialResponses = concatenatedPartialResponses;
        this.timesOnPartialResponseWasCalled = timesOnPartialResponseWasCalled;
        this.timesOnPartialThinkingWasCalled = timesOnPartialThinkingWasCalled;
        this.partialToolCalls = partialToolCalls;
        this.completeToolCalls = completeToolCalls;
        this.timesOnCompleteResponseWasCalled = timesOnCompleteResponseWasCalled;
        this.threads = threads;
        this.handler = handler;
    }

    public String concatenatedPartialResponses() { return concatenatedPartialResponses; }
    public int timesOnPartialResponseWasCalled() { return timesOnPartialResponseWasCalled; }
    public int timesOnPartialThinkingWasCalled() { return timesOnPartialThinkingWasCalled; }
    public List<PartialToolCall> partialToolCalls() { return partialToolCalls; }
    public List<CompleteToolCall> completeToolCalls() { return completeToolCalls; }
    public int timesOnCompleteResponseWasCalled() { return timesOnCompleteResponseWasCalled; }
    public Set<Thread> threads() { return threads; }
    public StreamingChatResponseHandler handler() { return handler; }
}
