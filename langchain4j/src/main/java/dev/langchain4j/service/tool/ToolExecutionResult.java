/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.data.message.Content
 *  dev.langchain4j.data.message.TextContent
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.service.tool;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.internal.Utils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ToolExecutionResult {
    private final boolean isError;
    private final Object result;
    private final AtomicReference<List<Content>> resultContents;
    private final Supplier<String> resultTextSupplier;
    private final Map<String, Object> attributes;

    public ToolExecutionResult(Builder builder) {
        this.isError = builder.isError;
        this.result = builder.result;
        boolean hasResultText = builder.resultText != null;
        boolean hasResultTextSupplier = builder.resultTextSupplier != null;
        boolean hasResultContents = builder.resultContents != null && !builder.resultContents.isEmpty();
        ToolExecutionResult.validate(hasResultText, hasResultTextSupplier, hasResultContents);
        if (hasResultText) {
            this.resultContents = new AtomicReference<List<Content>>(Collections.<Content>singletonList(TextContent.from((String)builder.resultText)));
            this.resultTextSupplier = null;
        } else if (hasResultTextSupplier) {
            this.resultContents = new AtomicReference<List<Content>>();
            this.resultTextSupplier = builder.resultTextSupplier;
        } else {
            this.resultContents = new AtomicReference<List<Content>>(Utils.copy((List<Content>)builder.resultContents));
            this.resultTextSupplier = null;
        }
        this.attributes = Utils.copy((Map)builder.attributes);
    }

    private static void validate(boolean hasResultText, boolean hasResultTextSupplier, boolean hasResultContents) {
        int setCount = (hasResultText ? 1 : 0) + (hasResultTextSupplier ? 1 : 0) + (hasResultContents ? 1 : 0);
        if (setCount == 0) {
            throw new IllegalArgumentException("One of resultText, resultTextSupplier, or resultContents must be provided");
        }
        if (setCount > 1) {
            throw new IllegalArgumentException("resultText, resultTextSupplier, and resultContents are mutually exclusive");
        }
    }

    public boolean isError() {
        return this.isError;
    }

    public Object result() {
        return this.result;
    }

    public String resultText() {
        List<Content> contents = this.resultContents();
        if (contents.size() == 1 && contents.get(0) instanceof TextContent) {
            TextContent textContent = (TextContent)contents.get(0);
            return textContent.text();
        }
        throw new IllegalStateException("resultText() cannot be called when resultContents contains non-text or multiple content elements. Use resultContents() instead.");
    }

    @Experimental
    public List<Content> resultContents() {
        return this.resultContents.updateAndGet(current -> {
            if (current != null) {
                return current;
            }
            String text = this.resultTextSupplier.get();
            return text == null ? Collections.emptyList() : Collections.singletonList(TextContent.from((String)text));
        });
    }

    public Map<String, Object> attributes() {
        return this.attributes;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        ToolExecutionResult that = (ToolExecutionResult)object;
        return this.isError == that.isError && Objects.equals(this.result, that.result) && Objects.equals(this.resultContents(), that.resultContents()) && Objects.equals(this.attributes, that.attributes);
    }

    public int hashCode() {
        return Objects.hash(this.isError, this.result, this.resultContents(), this.attributes);
    }

    public String toString() {
        return "ToolExecutionResult {isError = " + this.isError + ", result = " + this.result + ", resultContents = " + this.resultContents() + ", attributes = " + this.attributes + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean isError;
        private Object result;
        private String resultText;
        private Supplier<String> resultTextSupplier;
        private List<Content> resultContents;
        private Map<String, Object> attributes;

        public Builder isError(boolean isError) {
            this.isError = isError;
            return this;
        }

        public Builder result(Object result) {
            this.result = result;
            return this;
        }

        public Builder resultText(String resultText) {
            this.resultText = resultText;
            return this;
        }

        public Builder resultTextSupplier(Supplier<String> resultTextSupplier) {
            this.resultTextSupplier = resultTextSupplier;
            return this;
        }

        @Experimental
        public Builder resultContents(List<Content> resultContents) {
            this.resultContents = resultContents;
            return this;
        }

        public Builder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public ToolExecutionResult build() {
            return new ToolExecutionResult(this);
        }
    }
}

