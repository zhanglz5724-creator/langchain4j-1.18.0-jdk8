/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.data.document.Document
 *  dev.langchain4j.data.document.DocumentSplitter
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.TokenCountEstimator
 */
package dev.langchain4j.data.document.splitter;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.document.splitter.SegmentBuilder;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.TokenCountEstimator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class HierarchicalDocumentSplitter
implements DocumentSplitter {
    private HierarchicalDocumentSplitter overlapSentenceSplitter;
    private static final String INDEX = "index";
    protected final int maxSegmentSize;
    protected final int maxOverlapSize;
    protected final TokenCountEstimator tokenCountEstimator;
    protected final DocumentSplitter subSplitter;

    private HierarchicalDocumentSplitter getOverlapSentenceSplitter() {
        if (this.overlapSentenceSplitter == null) {
            this.overlapSentenceSplitter = new DocumentBySentenceSplitter(1, 0, null, null);
        }
        return this.overlapSentenceSplitter;
    }

    protected HierarchicalDocumentSplitter(int maxSegmentSizeInChars, int maxOverlapSizeInChars) {
        this(maxSegmentSizeInChars, maxOverlapSizeInChars, null, null);
    }

    protected HierarchicalDocumentSplitter(int maxSegmentSizeInChars, int maxOverlapSizeInChars, HierarchicalDocumentSplitter subSplitter) {
        this(maxSegmentSizeInChars, maxOverlapSizeInChars, null, subSplitter);
    }

    protected HierarchicalDocumentSplitter(int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator) {
        this(maxSegmentSizeInTokens, maxOverlapSizeInTokens, tokenCountEstimator, null);
    }

    protected HierarchicalDocumentSplitter(int maxSegmentSizeInTokens, int maxOverlapSizeInTokens, TokenCountEstimator tokenCountEstimator, DocumentSplitter subSplitter) {
        this.maxSegmentSize = ValidationUtils.ensureGreaterThanZero((Integer)maxSegmentSizeInTokens, (String)"maxSegmentSize");
        this.maxOverlapSize = ValidationUtils.ensureBetween((Integer)maxOverlapSizeInTokens, (int)0, (int)this.maxSegmentSize, (String)"maxOverlapSize");
        this.tokenCountEstimator = tokenCountEstimator;
        this.subSplitter = subSplitter == null ? this.defaultSubSplitter() : subSplitter;
    }

    protected abstract String[] split(String var1);

    protected abstract String joinDelimiter();

    protected abstract DocumentSplitter defaultSubSplitter();

    public List<TextSegment> split(Document document) {
        ValidationUtils.ensureNotNull((Object)document, (String)"document");
        ArrayList<TextSegment> segments = new ArrayList<TextSegment>();
        SegmentBuilder segmentBuilder = new SegmentBuilder(this.maxSegmentSize, this::estimateSize, this.joinDelimiter());
        AtomicInteger index = new AtomicInteger(0);
        String[] parts = this.split(document.text());
        String overlap = null;
        for (String part : parts) {
            Object segmentText;
            int partSize = segmentBuilder.sizeOf(part);
            if (segmentBuilder.hasSpaceFor(partSize)) {
                segmentBuilder.append(part);
                continue;
            }
            if (segmentBuilder.isNotEmpty() && !((String)(segmentText = segmentBuilder.toString())).equals(overlap)) {
                segments.add(HierarchicalDocumentSplitter.createSegment((String)segmentText, document, index.getAndIncrement()));
                overlap = this.overlapFrom((String)segmentText);
                segmentBuilder.reset();
                segmentBuilder.append(overlap);
                if (segmentBuilder.hasSpaceFor(partSize)) {
                    segmentBuilder.append(part);
                    continue;
                }
            }
            if (this.subSplitter == null) {
                throw new RuntimeException(String.format("The text \"%s...\" (%s %s long) doesn't fit into the maximum segment size (%s %s), and there is no subSplitter defined to split it further.", Utils.firstChars((String)part, (int)30), this.estimateSize(part), this.tokenCountEstimator == null ? "characters" : "tokens", this.maxSegmentSize, this.tokenCountEstimator == null ? "characters" : "tokens"));
            }
            segmentBuilder.append(part);
            segmentText = this.subSplitter.split(Document.from((String)segmentBuilder.toString())).iterator();
            while (segmentText.hasNext()) {
                TextSegment segment = (TextSegment)segmentText.next();
                segments.add(HierarchicalDocumentSplitter.createSegment(segment.text(), document, index.getAndIncrement()));
            }
            TextSegment lastSegment = (TextSegment)segments.get(segments.size() - 1);
            overlap = this.overlapFrom(lastSegment.text());
            segmentBuilder.reset();
            segmentBuilder.append(overlap);
        }
        if (segmentBuilder.isNotEmpty() && !segmentBuilder.toString().equals(overlap)) {
            segments.add(HierarchicalDocumentSplitter.createSegment(segmentBuilder.toString(), document, index.getAndIncrement()));
        }
        return segments;
    }

    String overlapFrom(String segmentText) {
        if (this.maxOverlapSize == 0) {
            return "";
        }
        List<String> sentences = Arrays.asList(this.getOverlapSentenceSplitter().split(segmentText));
        Collections.reverse(sentences);
        SegmentBuilder overlapBuilder = new SegmentBuilder(this.maxOverlapSize, this::estimateSize, this.joinDelimiter());
        for (String sentence : sentences) {
            if (!overlapBuilder.hasSpaceFor(sentence)) break;
            overlapBuilder.prepend(sentence);
        }
        return overlapBuilder.toString();
    }

    int estimateSize(String text) {
        if (this.tokenCountEstimator != null && !Utils.isNullOrBlank((String)text)) {
            return this.tokenCountEstimator.estimateTokenCountInText(text);
        }
        return text == null ? 0 : text.length();
    }

    static TextSegment createSegment(String text, Document document, int index) {
        Metadata metadata = document.metadata().copy().put(INDEX, String.valueOf(index));
        return TextSegment.from((String)text, (Metadata)metadata);
    }
}

