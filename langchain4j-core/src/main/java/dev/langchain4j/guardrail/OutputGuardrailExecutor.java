/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.guardrail;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.AbstractGuardrailExecutor;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailException;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.observability.api.event.OutputGuardrailExecutedEvent;
import dev.langchain4j.spi.guardrail.OutputGuardrailExecutorBuilderFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class OutputGuardrailExecutor
extends AbstractGuardrailExecutor<OutputGuardrailsConfig, OutputGuardrailRequest, OutputGuardrailResult, OutputGuardrail, OutputGuardrailExecutedEvent, OutputGuardrailResult.Failure> {
    public static final String MAX_RETRIES_MESSAGE_TEMPLATE = "Output validation failed. The guardrails have reached the maximum number of retries.\nGuardrail messages:\n\n%s\n";

    protected OutputGuardrailExecutor(OutputGuardrailsConfig config, List<OutputGuardrail> guardrails) {
        super(config, guardrails);
    }

    @Override
    public OutputGuardrailResult execute(OutputGuardrailRequest request) {
        OutputGuardrailResult result = null;
        OutputGuardrailRequest accumulatedRequest = request;
        int attempt = 0;
        int maxAttempts = ((OutputGuardrailsConfig)this.config()).maxRetries();
        if (maxAttempts == 0) {
            maxAttempts = 1;
        } else if (maxAttempts < 0) {
            maxAttempts = 2;
        }
        while (attempt < maxAttempts) {
            result = this.rewriteResult(request, accumulatedRequest, (OutputGuardrailResult)this.executeGuardrails(accumulatedRequest));
            if (result.isSuccess()) {
                return result;
            }
            if (!result.isRetry()) {
                this.removeViolatingMessageIfRequested(result, request);
                throw new OutputGuardrailException(result.toString(), result.getFirstFailureException(), result);
            }
            if (++attempt >= maxAttempts) continue;
            List chatMessages = Optional.ofNullable(accumulatedRequest.requestParams().chatMemory()).map(ChatMemory::messages).orElseGet(ArrayList::new);
            result.getReprompt().map(UserMessage::from).ifPresent(chatMessages::add);
            ChatResponse response = accumulatedRequest.chatExecutor().execute(chatMessages);
            accumulatedRequest = OutputGuardrailRequest.builder().responseFromLLM(response).chatExecutor(accumulatedRequest.chatExecutor()).requestParams(accumulatedRequest.requestParams()).build();
        }
        if (attempt == maxAttempts) {
            String failureMessages = result.failures().stream().map(GuardrailResult.Failure::message).collect(Collectors.joining(System.lineSeparator()));
            this.removeViolatingMessageIfRequested(result, request);
            throw new OutputGuardrailException(String.format(MAX_RETRIES_MESSAGE_TEMPLATE, failureMessages), null, result);
        }
        return result;
    }

    private void removeViolatingMessageIfRequested(OutputGuardrailResult result, OutputGuardrailRequest request) {
        if (!result.shouldRemoveViolatingMessage()) {
            return;
        }
        ChatMemory memory = request.requestParams().chatMemory();
        if (memory == null) {
            return;
        }
        ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>(memory.messages());
        ListIterator it = messages.listIterator(messages.size());
        while (it.hasPrevious()) {
            ChatMessage msg = (ChatMessage)it.previous();
            if (!(msg instanceof AiMessage)) continue;
            it.remove();
            break;
        }
        memory.clear();
        messages.forEach(memory::add);
    }

    private OutputGuardrailResult rewriteResult(OutputGuardrailRequest originalRequest, OutputGuardrailRequest validatedRequest, OutputGuardrailResult result) {
        String validatedText;
        String originalText;
        if (result.isSuccess() && !result.hasRewrittenResult() && !(originalText = originalRequest.responseFromLLM().aiMessage().text()).equals(validatedText = validatedRequest.responseFromLLM().aiMessage().text())) {
            return OutputGuardrailResult.successWith(originalRequest.responseFromLLM().aiMessage().withText(validatedText));
        }
        return result;
    }

    @Override
    protected OutputGuardrailResult createFailure(List<OutputGuardrailResult.Failure> failures) {
        return OutputGuardrailResult.failure(failures);
    }

    @Override
    protected OutputGuardrailResult createSuccess() {
        return OutputGuardrailResult.success();
    }

    @Override
    protected OutputGuardrailException createGuardrailException(String message, Throwable cause) {
        return new OutputGuardrailException(message, cause);
    }

    @Override
    protected OutputGuardrailResult handleFatalResult(OutputGuardrailResult accumulatedResult, OutputGuardrailResult result) {
        return accumulatedResult.hasRewrittenResult() ? result.blockRetry() : result;
    }

    protected OutputGuardrailExecutedEvent.OutputGuardrailExecutedEventBuilder createEmptyObservabilityEventBuilderInstance() {
        return OutputGuardrailExecutedEvent.builder();
    }

    public static OutputGuardrailExecutorBuilder builder() {
        Iterator<OutputGuardrailExecutorBuilderFactory> iterator = ServiceLoader.load(OutputGuardrailExecutorBuilderFactory.class).iterator();
        if (iterator.hasNext()) {
            OutputGuardrailExecutorBuilderFactory factory = iterator.next();
            return (OutputGuardrailExecutorBuilder)factory.getBuilder();
        }
        return new OutputGuardrailExecutorBuilder();
    }

    public static class OutputGuardrailExecutorBuilder
    extends AbstractGuardrailExecutor.GuardrailExecutorBuilder<OutputGuardrailsConfig, OutputGuardrailResult, OutputGuardrailRequest, OutputGuardrail, OutputGuardrailExecutedEvent, OutputGuardrailExecutorBuilder> {
        protected OutputGuardrailExecutorBuilder() {
            super(OutputGuardrailsConfig.builder().build());
        }

        public OutputGuardrailExecutor build() {
            return new OutputGuardrailExecutor((OutputGuardrailsConfig)this.config(), this.guardrails());
        }
    }
}

