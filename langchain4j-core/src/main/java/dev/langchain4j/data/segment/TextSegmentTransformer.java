/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.segment;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface TextSegmentTransformer {
    public TextSegment transform(TextSegment var1);

    default public List<TextSegment> transformAll(List<TextSegment> segments) {
        return segments.stream().map(this::transform).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default public List<TextSegment> transformAll(TextSegment ... textSegments) {
        if (Utils.isNullOrEmpty(textSegments)) {
            return Collections.emptyList();
        }
        return this.transformAll(Arrays.asList(textSegments));
    }
}

