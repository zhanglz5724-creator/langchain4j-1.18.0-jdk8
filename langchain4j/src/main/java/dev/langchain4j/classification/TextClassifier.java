/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Document
 *  dev.langchain4j.data.segment.TextSegment
 */
package dev.langchain4j.classification;

import dev.langchain4j.classification.ClassificationResult;
import dev.langchain4j.classification.ScoredLabel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import java.util.List;
import java.util.stream.Collectors;

public interface TextClassifier<L> {
    default public List<L> classify(String text) {
        return this.classifyWithScores(text).scoredLabels().stream().map(ScoredLabel::label).collect(Collectors.toList());
    }

    default public List<L> classify(TextSegment textSegment) {
        return this.classify(textSegment.text());
    }

    default public List<L> classify(Document document) {
        return this.classify(document.text());
    }

    public ClassificationResult<L> classifyWithScores(String var1);

    default public ClassificationResult<L> classifyWithScores(TextSegment textSegment) {
        return this.classifyWithScores(textSegment.text());
    }

    default public ClassificationResult<L> classifyWithScores(Document document) {
        return this.classifyWithScores(document.text());
    }
}

