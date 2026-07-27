/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.embedding.request;

import dev.langchain4j.Experimental;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Experimental
public class EmbeddingInput {
    private final List<Content> contents;

    protected EmbeddingInput(List<Content> contents) {
        this.contents = Utils.copy(ValidationUtils.ensureNotEmpty(contents, "contents"));
    }

    public List<Content> contents() {
        return this.contents;
    }

    public Set<ContentType> contentTypes() {
        return this.contents.stream().map(Content::type).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public String text() {
        return this.contents.stream().filter(content -> content instanceof TextContent).map(content -> ((TextContent)content).text()).collect(Collectors.joining("\n"));
    }

    public static EmbeddingInput from(String text) {
        return new EmbeddingInput(Collections.singletonList(TextContent.from(text)));
    }

    public static EmbeddingInput from(TextSegment segment) {
        return EmbeddingInput.from(segment.text());
    }

    public static EmbeddingInput from(Content ... contents) {
        return new EmbeddingInput(Arrays.asList(contents));
    }

    public static EmbeddingInput from(List<Content> contents) {
        return new EmbeddingInput(contents);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        EmbeddingInput that = (EmbeddingInput)o;
        return Objects.equals(this.contents, that.contents);
    }

    public int hashCode() {
        return Objects.hash(this.contents);
    }

    public String toString() {
        return "EmbeddingInput{contents=" + this.contents + '}';
    }
}

